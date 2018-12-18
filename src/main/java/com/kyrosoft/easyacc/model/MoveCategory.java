package com.kyrosoft.easyacc.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MoveCategory extends BaseEntity {

	@Basic
	@Size(min = 3)
	private String name;
	
	@NotNull
	private Boolean isActive;
}
