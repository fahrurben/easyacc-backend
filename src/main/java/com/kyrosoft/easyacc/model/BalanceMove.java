package com.kyrosoft.easyacc.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedQueries({
    @NamedQuery(name="BalanceMove.IncreaseBalanceThatNewer",
                query="UPDATE BalanceMove SET balanceBefore = ( balanceBefore + :diff ), "+
    					"balance = ( balance + :diff ) WHERE id > :balId AND account.id = :accountId"),
    @NamedQuery(name="BalanceMove.DecreaseBalanceThatNewer",
	    query="UPDATE BalanceMove SET balanceBefore = ( balanceBefore - :diff ), "+
	    		"balance = ( balance - :diff ) WHERE id > :balId AND account.id = :accountId")
}) 
public class BalanceMove extends BaseEntity {

	@ManyToOne(optional=false) 
	@JoinColumn(name="accountId")
	private Account account;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="moveId")
	private Move move;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="moveLineId")
	private MoveLine moveLine;
	
	@Basic
	@NotNull
	private Double balanceBefore;
	
	@Basic
	@NotNull
	private Double amount;
	
	@Basic
	@NotNull
	private Double balance;
	
	@Basic
	@NotNull
	private Date datePosted;
	
	public BalanceMove(Account acc, Move move, MoveLine line,
			Double balBefore, Double amount, Double bal, Date postedOn) {
		this.account = acc;
		this.move = move;
		this.moveLine = line;
		this.balanceBefore = balBefore;
		this.amount = amount;
		this.balance = bal;
		this.datePosted = postedOn;
	}
	
	public void set(Account acc, Move move, MoveLine line,
			Double balBefore, Double amount, Double bal, Date postedOn) {
		this.account = acc;
		this.move = move;
		this.moveLine = line;
		this.balanceBefore = balBefore;
		this.amount = amount;
		this.balance = bal;
		this.datePosted = postedOn;
	}
}
