package com.kyrosoft.easyacc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kyrosoft.easyacc.EntityNotFoundException;
import com.kyrosoft.easyacc.model.BaseEntity;
import com.kyrosoft.easyacc.model.criteria.BaseSpec;

public interface BaseService<T extends BaseEntity, S extends BaseSpec> {

	public T create(T entity);
	
	public T update(Long id, T entity) throws Exception;
	
	public T get(Long id) throws EntityNotFoundException;
	
	public Page<T> find(S criteria,Pageable pageable);
}
