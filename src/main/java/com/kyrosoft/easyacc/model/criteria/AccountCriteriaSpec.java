package com.kyrosoft.easyacc.model.criteria;

import org.springframework.data.jpa.domain.Specification;

import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.AccountType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountCriteriaSpec extends BaseSpec {
	
	private String name;
	
	private String code;
	
	private AccountType type;
	
	private Boolean isActive;
	
	public static Specification<Account> haveId() {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.gt(entity.get("id"), 0);
	}
	
	public static Specification<Account> nameLike(String name) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(entity.get("name"), "%" + name + "%");
	}
	
	public static Specification<Account> codeLike(String code) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(entity.get("code"), "%" + code );
	}
	
	public static Specification<Account> typeEqual(AccountType type) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(entity.get("type"), type);
	}
	
	public static Specification<Account> isActiveEqual(Boolean isActive) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(entity.get("isActive"), isActive);
	}
}
