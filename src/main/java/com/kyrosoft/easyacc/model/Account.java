package com.kyrosoft.easyacc.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity {
	
	@Enumerated
	private NormalBalance normalBalance;
	
	@Enumerated
	private AccountType type;
	
	@Basic
	@Size(min = 3)
	private String name;
	
	@Basic
	@Size(min = 3)
	private String code;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="accountGroupId")
	private AccountGroup accountGroup;
	
	@Basic
	@NotNull
	private Boolean isActive;
	
	@Transient
	private Double balance;
}
