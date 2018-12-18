package com.kyrosoft.easyacc.service.impl;

import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;

import com.kyrosoft.easyacc.EntityNotFoundException;
import com.kyrosoft.easyacc.model.BaseEntity;
import com.kyrosoft.easyacc.model.criteria.BaseSpec;
import com.kyrosoft.easyacc.repository.BaseRepository;

@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity, S extends BaseSpec>  {
	
	protected Validator validator;
	
	protected BaseRepository<T> repository;
	
	public BaseServiceImpl() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	public T create(T entity) {
		Set<ConstraintViolation<T>> violations = validator.validate(entity);
		
		if(violations.size() > 0) {
			throw new IllegalArgumentException("Entity is not valid");
		}
		entity = repository.save(entity);
		return entity;
	}
	
	public T get(Long id) throws EntityNotFoundException {
		Optional<T> entity = repository.findById(id);
		
		if (!entity.isPresent()) {
			throw new EntityNotFoundException("Entity not found");
		}
		return entity.get();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public T update(Long id, T updatedVal)
			throws Exception {
		Optional<T> entityOpt = repository.findById(id);
		
		if (!entityOpt.isPresent()) {
			throw new EntityNotFoundException("Entity not found");
		}
		
		T entity = entityOpt.get();
		
		setValueFromValueObject(entity, updatedVal);
		
		Set<ConstraintViolation<T>> violations = validator.validate(updatedVal);
		
		if(violations.size() > 0) {
			throw new IllegalArgumentException("Entity is not valid");
		}
		
		entity = repository.save(entity);
		return entity;
	}
	
	/**
	 * Set values of entity from value object
	 */
	protected abstract void setValueFromValueObject(T entity, T entityVal) throws Exception;
	
	public Page<T> find(S criteria,Pageable pageable) {

		Specifications<T> specs = setSearchCriteria(criteria);
		
		Page<T> entityList = repository.findAll(specs, pageable);
		
		return entityList;
	}
	
	/**
	 * Set search criteria
	 */
	protected abstract Specifications<T> setSearchCriteria(S criteria);
}
