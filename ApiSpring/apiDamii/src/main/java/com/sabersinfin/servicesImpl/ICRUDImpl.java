package com.sabersinfin.servicesImpl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sabersinfin.services.ICRUD;

public abstract class ICRUDImpl<T, ID> implements ICRUD<T, ID>{

	public abstract JpaRepository<T, ID> getRepository();
	
	@Override
	public T registrar(T bean) {
		return getRepository().save(bean);
	}

	@Override
	public T actualizar(T bean) {
		return getRepository().save(bean);
	}

	@Override
	public void eliminarPorId(ID cod) {
		getRepository().deleteById(cod);
	}

	@Override
	public T buscarPorId(ID cod) {
		return getRepository().findById(cod).orElse(null);
	}

	@Override
	public List<T> listarTodos() {
		return getRepository().findAll();
	}
	
	
	
}
