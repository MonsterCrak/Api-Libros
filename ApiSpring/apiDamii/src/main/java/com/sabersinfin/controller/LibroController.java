package com.sabersinfin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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

	private static int contArchivo = 1;
	private static int contPortada = 1;

	@GetMapping("/lista")
	public ResponseEntity<List<Libro>> lista() {

		List<Libro> libros = serLibro.listarTodos();

		for (Libro libro : libros) {
			libro.setArchivo(RUTA_RECURSOS + "/pdfs/" + libro.getArchivo());
			libro.setPortada(RUTA_RECURSOS + "/portadas/" + libro.getPortada());
		}

		return new ResponseEntity<>(serLibro.listarTodos(), HttpStatus.OK);
	}

	@GetMapping("/archivos/{tipo}/{nombre}")
	public void servirArchivo(@PathVariable String tipo, @PathVariable String nombre, HttpServletResponse response)
			throws IOException {
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
			@RequestParam("estado") boolean estado, @RequestParam("idUsuario") int idUsuario,
			@RequestParam("idGenero") int idGenero) {

		Libro bean = new Libro();

		bean.setTitulo(titulo);
		bean.setDescripcion(descripcion);
		bean.setAutor(autor);
		bean.setEstado(estado);
		bean.setRegistro(LocalDate.now());

		Usuario usuario = serUsuario.buscarPorId(idUsuario);
		Genero genero = serGenero.buscarPorId(idGenero);
		bean.setUsuario(usuario);
		bean.setGenero(genero);

		String ruta = "src/main/resources";

		for (MultipartFile archivos : archivo) {
			String nombreArchivoOriginal = archivos.getOriginalFilename();
			String extension = nombreArchivoOriginal.substring(nombreArchivoOriginal.lastIndexOf('.'));
			String nuevoNombreArchivo = contArchivo + "-a" + extension;

			System.out.println("Nombre del archivo: " + nuevoNombreArchivo);
			bean.setArchivo(nuevoNombreArchivo);
			contArchivo++;
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
			String nombrePortadaOriginal = portadas.getOriginalFilename();
			String extension = nombrePortadaOriginal.substring(nombrePortadaOriginal.lastIndexOf('.'));
			String nuevoNombrePortada = contPortada + "-p" + extension;
			System.out.println("Nombre de la portada: " + nuevoNombrePortada);
			bean.setPortada(nuevoNombrePortada);
			contPortada++;
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

		return ResponseEntity.status(HttpStatus.OK).body("{ \"message\": \"Libro registrado\" }");
	}

	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizar(@RequestParam(value = "archivo", required = false) MultipartFile[] archivo,
	    @RequestParam(value = "portada", required = false) MultipartFile[] portada, @RequestParam("titulo") String titulo,
	    @RequestParam("descripcion") String descripcion, @RequestParam("autor") String autor,
	    @RequestParam("estado") boolean estado, @RequestParam("idUsuario") int idUsuario,
	    @RequestParam("idGenero") int idGenero, @RequestParam("idLibro") int idLibro) {

	    Libro bean = serLibro.buscarPorId(idLibro);

	    if (bean == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"message\": \"Libro no encontrado\" }");
	    }

	    String nombreArchivoOriginal = bean.getArchivo();
	    String nombrePortadaOriginal = bean.getPortada();

	    bean.setTitulo(titulo);
	    bean.setDescripcion(descripcion);
	    bean.setAutor(autor);
	    bean.setEstado(estado);
	    bean.setRegistro(LocalDate.now());

	    Usuario usuario = serUsuario.buscarPorId(idUsuario);
	    Genero genero = serGenero.buscarPorId(idGenero);

	    if (bean.getUsuario().getId() == 0 || bean.getGenero().getId() == 0) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body("{ \"message\": \"Usuario o género no encontrado\" }");
	    }

	    bean.setUsuario(usuario);
	    bean.setGenero(genero);

	    String ruta = "src/main/resources";

	    if (archivo != null && archivo.length > 0) {
	        try {
	            byte[] bytes = archivo[0].getBytes();
	            Path path = Paths.get(ruta + "/pdfs/" + nombreArchivoOriginal);
	            Files.write(path, bytes);
	            System.out.println("Archivo actualizado en: " + path.toString());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    if (portada != null && portada.length > 0) {
	        try {
	            byte[] bytes = portada[0].getBytes();
	            Path path = Paths.get(ruta + "/portadas/" + nombrePortadaOriginal);
	            Files.write(path, bytes);
	            System.out.println("Portada actualizada en: " + path.toString());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    serLibro.actualizar(bean);

	    return ResponseEntity.status(HttpStatus.OK).body("{ \"message\": \"Libro actualizado\" }");
	}


	
	@GetMapping("/buscar/{codigo}")
	public ResponseEntity<Libro> buscar(@PathVariable("codigo") Integer codigo) {
		Libro lib = validarLibro(codigo);
		
		Libro libroConEnlaces = serLibro.buscarPorId(lib.getId());
	    libroConEnlaces.setArchivo(RUTA_RECURSOS + "/pdfs/" + libroConEnlaces.getArchivo());
	    libroConEnlaces.setPortada(RUTA_RECURSOS + "/portadas/" + libroConEnlaces.getPortada());

	    return new ResponseEntity<>(libroConEnlaces, HttpStatus.OK);
	}

	@GetMapping("/buscarPorGenero/{codigoGenero}")
	public ResponseEntity<List<Libro>> buscarPorGenero(@PathVariable("codigoGenero") Integer codigoGenero) {
		Genero g = validarGenero(codigoGenero);

		List<Libro> libros = serLibro.buscarPorGenero(g.getId());

		return new ResponseEntity<>(libros, HttpStatus.OK);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<String> eliminarLibro(@PathVariable("id") int id) {
	    Libro libro = serLibro.buscarPorId(id);

	    if (libro == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"message\": \"Libro no encontrado\" }");
	    }

	    try {
	        // Eliminar archivo y portada si existen
	        if (libro.getArchivo() != null) {
	            Files.deleteIfExists(Paths.get("src/main/resources/pdfs/" + libro.getArchivo()));
	        }
	        if (libro.getPortada() != null) {
	            Files.deleteIfExists(Paths.get("src/main/resources/portadas/" + libro.getPortada()));
	        }

	        serLibro.eliminarPorId(id);

	        return ResponseEntity.status(HttpStatus.OK).body("{ \"message\": \"Libro eliminado\" }");
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("{ \"message\": \"Error al eliminar el libro\" }");
	    }
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

}
