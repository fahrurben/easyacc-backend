package com.kyrosoft.easyacc.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T> extends  CrudRepository<T, Long>,JpaSpecificationExecutor<T> {

}
