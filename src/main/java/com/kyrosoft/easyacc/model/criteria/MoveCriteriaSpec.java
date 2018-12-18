package com.kyrosoft.easyacc.model.criteria;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.kyrosoft.easyacc.model.Move;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MoveCriteriaSpec extends BaseSpec {
	
	private Date datePosted;
	
	private Long moveCategoryId;

	public static Specification<Move> haveId() {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.gt(entity.get("id"), 0);
	}
	
	public static Specification<Move> datePostedEqual(Date date) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(entity.get("datePosted"), date);
	}
	
	public static Specification<Move> moveCategoryEqual(Long moveCategoryId) {
		return (entity, criteriaQuery, criteriaBuilder) -> 
			criteriaBuilder.equal(entity.get("moveCategory").get("id"), moveCategoryId);
	}
}
