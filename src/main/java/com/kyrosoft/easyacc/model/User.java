package com.kyrosoft.easyacc.model;

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
public class User extends BaseEntity {
	
	@Size(min = 4)
	private String email;
	
	@Size(min = 3)
	private String password;
	
	@Size(min = 1)
	private String firstName;
	
	@Size(min=1)
	private String lastName;
	
	@Size(min=3)
	private String fullname;
	
	@NotNull
	private Boolean isActive;
}
