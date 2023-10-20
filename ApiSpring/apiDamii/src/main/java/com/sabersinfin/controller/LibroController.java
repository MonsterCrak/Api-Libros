package com.sabersinfin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.sabersinfin.entity.Genero;
import com.sabersinfin.entity.Libro;
import com.sabersinfin.entity.Usuario;
import com.sabersinfin.servicesImpl.GeneroServices;

import com.sabersinfin.servicesImpl.LibroServices;
import com.sabersinfin.servicesImpl.UsuarioServices;
import com.sabersinfin.utils.NotFoundException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;


import org.springframework.http.MediaType;



@RestController
@RequestMapping("api/libro")
@CrossOrigin(originPatterns = "*")
public class LibroController {

	@Autowired
	private LibroServices serLibro;

	@Autowired
	private GeneroServices serGenero;

	@Autowired
	private UsuarioServices serUsuario;

    private final String RUTA_RECURSOS = "http://localhost:8092/api/libro/archivos";

        

	@GetMapping("/lista")
	public ResponseEntity<List<Libro>> lista() {
		
		List<Libro> libros = serLibro.listarTodos();
		
		 for (Libro libro : libros) {
		        libro.setArchivo(RUTA_RECURSOS + "/pdfs/" + libro.getArchivo());
		        libro.setPortada(RUTA_RECURSOS + "/portadas/"+libro.getPortada());
		    }
		
		return new ResponseEntity<>(serLibro.listarTodos(), HttpStatus.OK);
	}
	
	
	@GetMapping("/archivos/{tipo}/{nombre}")
    public void servirArchivo(@PathVariable String tipo, @PathVariable String nombre,
                              HttpServletResponse response) throws IOException {
        String ruta = "src/main/resources/" + tipo + "/" + nombre;
        File archivo = new File(ruta);

        InputStream inputStream = new FileInputStream(archivo);
        IOUtils.copy(inputStream, response.getOutputStream());

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + nombre);
        response.flushBuffer();
    }
	
	
	@PostMapping("/registrar")
	public ResponseEntity<String> registrar(@RequestParam("archivo") MultipartFile[] archivo,
			@RequestParam("portada") MultipartFile[] portada, @RequestParam("titulo") String titulo,
			@RequestParam("descripcion") String descripcion, @RequestParam("autor") String autor,
			@RequestParam("idUsuario") int idUsuario,
			@RequestParam("idGenero") int idGenero) {

		Libro bean = new Libro();

		bean.setTitulo(titulo);
		bean.setDescripcion(descripcion);
		bean.setAutor(autor);
		bean.setEstado(1);
		bean.setRegistro(LocalDate.now());

		Usuario usuario = serUsuario.buscarPorId(idUsuario);
	    Genero genero = serGenero.buscarPorId(idGenero);
	    bean.setUsuario(usuario);
	    bean.setGenero(genero);

	    String ruta = "src/main/resources";
	    
		for (MultipartFile archivos : archivo) {
			String nombreArchivo = archivos.getOriginalFilename();
			String nuevoNombreArchivo = nombreArchivo.replaceAll("\\s+", "_");
			System.out.println("Nombre del archivo: " + nuevoNombreArchivo);
			bean.setArchivo(nuevoNombreArchivo);
			
			 try {
		            byte[] bytes = archivos.getBytes();
		            Path path = Paths.get(ruta + "/pdfs/" + nuevoNombreArchivo);
		            Files.write(path, bytes);
		            System.out.println("Archivo guardado en: " + path.toString());
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			
		}

		for (MultipartFile portadas : portada) {
			String nombrePortada = portadas.getOriginalFilename();
			String nuevoNombrePortada = nombrePortada.replaceAll("\\s+", "_");
			System.out.println("Nombre de la portada: " + nuevoNombrePortada);
			bean.setPortada(nuevoNombrePortada);
			try {
	            byte[] bytes = portadas.getBytes();
	            Path path = Paths.get(ruta + "/portadas/" + nuevoNombrePortada);
	            Files.write(path, bytes);
	            System.out.println("Portada guardada en: " + path.toString());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}

		
		System.out.println(archivo);
		System.out.println(portada);
		
		

		serLibro.registrar(bean);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Libro " + bean.getTitulo() + " registrado correctamente");
	}

	
	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizar(@RequestBody Libro bean) {
		Libro lib = validarLibro(bean.getId());

		lib = serLibro.actualizar(bean);
		lib.setRegistro(LocalDate.now());
		// dar valor de estado por frontent para modificar
		// 1 visible
		// 2 oculto
		// lib.setEstado(l.getEstado());

		serLibro.actualizar(lib);

		return ResponseEntity.status(HttpStatus.OK).body("Libro " + lib.getTitulo() + " actualizado correctamente");
	}

	@GetMapping("/buscar/{codigo}")
	public ResponseEntity<Libro> buscar(@PathVariable("codigo") Integer codigo) {
		Libro lib = validarLibro(codigo);

		serLibro.buscarPorId(lib.getId());

		return new ResponseEntity<>(lib, HttpStatus.OK);
	}

	@GetMapping("/buscarPorGenero/{codigoGenero}")
	public ResponseEntity<List<Libro>> buscarPorGenero(@PathVariable("codigoGenero") Integer codigoGenero) {
		Genero g = validarGenero(codigoGenero);

		List<Libro> libros = serLibro.buscarPorGenero(g.getId());

		return new ResponseEntity<>(libros, HttpStatus.OK);
	}

	private Libro validarLibro(Integer codigo) {
		Libro lib = serLibro.buscarPorId(codigo);
		if (lib == null) {
			throw new NotFoundException();
		}
		return lib;
	}

	private Genero validarGenero(Integer codigo) {
		Genero g = serGenero.buscarPorId(codigo);
		if (g == null) {
			throw new NotFoundException();
		}
		return g;
	}

	private Usuario validarUsuario(Integer codigo) {
		Usuario u = serUsuario.buscarPorId(codigo);
		if (u == null) {
			throw new NotFoundException();
		}
		return u;
	}
}
