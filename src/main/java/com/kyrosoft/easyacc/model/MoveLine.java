package com.kyrosoft.easyacc.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MoveLine extends BaseEntity {

	@JsonBackReference
	@ManyToOne(optional=false) 
	@JoinColumn(name="moveId")
	private Move move;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="accountId")
	private Account account;
	
	@Basic
	@NotNull
	private Double debit;
	
	@Basic
	@NotNull
	private Double credit;
	
	@Basic
	@NotNull
	private Date datePosted;
}
