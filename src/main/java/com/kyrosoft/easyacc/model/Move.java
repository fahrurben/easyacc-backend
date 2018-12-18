package com.kyrosoft.easyacc.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Move extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name="moveCategoryId")
	private MoveCategory moveCategory;
	
	@Basic
	private Long transId;
	
	@Basic
	@Size(min = 3)
	private String ref;
	
	@Basic
	private String desc;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "move", fetch = FetchType.EAGER)
	private List<MoveLine> lines;
	
	@Basic
	@NotNull
	private Date datePosted;
}
