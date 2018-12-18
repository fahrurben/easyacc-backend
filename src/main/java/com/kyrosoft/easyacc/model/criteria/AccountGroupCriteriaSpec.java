package com.kyrosoft.easyacc.model.criteria;

import org.springframework.data.jpa.domain.Specification;
import com.kyrosoft.easyacc.model.AccountGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountGroupCriteriaSpec extends BaseSpec {

	private String name;
	
	private String code;
	
	private Boolean isActive;
	
	public static Specification<AccountGroup> haveId() {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.gt(entity.get("id"), 0);
	}
	
	public static Specification<AccountGroup> nameLike(String name) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(entity.get("name"), "%" + name + "%");
	}
	
	public static Specification<AccountGroup> codeLike(String code) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.like(entity.get("code"), "%" + code );
	}
	
	public static Specification<AccountGroup> isActiveEqual(Boolean isActive) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(entity.get("isActive"), isActive);
	}
}
