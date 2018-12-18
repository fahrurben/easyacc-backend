package com.kyrosoft.easyacc.service.impl;

import static com.kyrosoft.easyacc.model.criteria.MoveCriteriaSpec.haveId;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;

import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.AccountType;
import com.kyrosoft.easyacc.model.BalanceMove;
import com.kyrosoft.easyacc.model.Move;
import com.kyrosoft.easyacc.model.MoveLine;
import com.kyrosoft.easyacc.model.NormalBalance;
import com.kyrosoft.easyacc.model.criteria.MoveCriteriaSpec;
import com.kyrosoft.easyacc.repository.BalanceMoveRepository;
import com.kyrosoft.easyacc.repository.MoveLineRepository;
import com.kyrosoft.easyacc.repository.MoveRepository;
import com.kyrosoft.easyacc.service.MoveService;

@Service
public class MoveServiceImpl extends BaseServiceImpl<Move, MoveCriteriaSpec> 
	implements MoveService {
	
	private MoveLineRepository moveLineRepository;
	
	private BalanceMoveRepository balanceMoveRepository;
	
	private EntityManager em;
	
	public MoveServiceImpl(
			MoveRepository moveRepository, 
			MoveLineRepository moveLineRepository,
			BalanceMoveRepository balanceMoveRepository,
			EntityManager em) {
		this.repository = moveRepository;
		this.moveLineRepository = moveLineRepository;
		this.balanceMoveRepository = balanceMoveRepository;
		this.em = em;
	}

	@Override
	@Transactional
	public Move create(Move entity) {
		Set<ConstraintViolation<Move>> violations = validator.validate(entity);
		
		if(violations.size() > 0) {
			String errMessage = "";
			for (ConstraintViolation<Move> violation: violations) {
				errMessage += violation.getPropertyPath() + " " + violation.getMessage() + "\n";
			}
			throw new IllegalArgumentException("Entity is not valid: \n" +errMessage);
		}
		
		List<MoveLine> updatedLines = entity.getLines();
		
		entity = repository.save(entity);
		if (updatedLines != null) {
			for (MoveLine line: updatedLines) {
				moveLineRepository.save(line);
				createBalanceMove(entity, line);
			}
		}
		return entity;
	}
	
	@Override
	@Transactional
	protected void setValueFromValueObject(Move entity, Move entityVal) throws Exception {
		
		entity.setTransId(entityVal.getTransId());
		entity.setRef(entityVal.getRef());
		entity.setDesc(entityVal.getDesc());
		entity.setDatePosted(entityVal.getDatePosted());
		
		List<MoveLine> existingLines = entity.getLines();
		List<MoveLine> updatedLines = entityVal.getLines();
		List<MoveLine> removeLines = new ArrayList<>();
		
		if (updatedLines != null) {
			for (MoveLine line: updatedLines) {
				if (line.getId() == null) {
					moveLineRepository.save(line);
					createBalanceMove(entity, line);
				} else {
					moveLineRepository.save(line);
					updateBalanceMove(entity, line);
				}
			}
		}
		
		// Put deleted move line to removeLine list
		if (existingLines != null) {
			for (MoveLine line: existingLines) {
				if (!isLineExist(line, updatedLines)) {
					removeLines.add(line);
				}
			}
		}
		
		if (removeLines != null) {
			for (MoveLine line: removeLines) {
				deleteBalanceMove(entity, line);
				entity.getLines().remove(line);
				moveLineRepository.delete(line);
			}
		}
	}
	
	@Transactional
	public BalanceMove createBalanceMove(Move move, MoveLine line) {
		
		BalanceMove lastBalMove = balanceMoveRepository.findFirstByAccountOrderByIdDesc(line.getAccount());
		
		Double amount = 0.0;
		
		if (!line.getCredit().equals(0.0)) {
			amount = line.getAccount().getNormalBalance() == NormalBalance.CREDIT ? line.getCredit() : (-1.0 * line.getCredit());
		} else {
			amount = line.getAccount().getNormalBalance() == NormalBalance.DEBIT ? line.getDebit() : (-1.0 * line.getDebit());
		}
		
		BalanceMove balanceMove = new BalanceMove(line.getAccount(), move, line, 0.0, amount, amount, line.getDatePosted());
		
		if (lastBalMove != null) {
			balanceMove.setBalanceBefore(lastBalMove.getAmount());
			balanceMove.setBalance(lastBalMove.getAmount() + amount);
		}
		
		balanceMove = balanceMoveRepository.save(balanceMove);
		
		return balanceMove;
	}
	
	@Transactional
	public void updateBalanceMove(Move move, MoveLine line) {
		
		MoveLine existingLine = moveLineRepository.findById(line.getId()).get();
		
		Double amount = 0.0;
		
		if (!line.getCredit().equals(0.0)) {
			amount = line.getAccount().getNormalBalance() == NormalBalance.CREDIT ? line.getCredit() : (-1.0 * line.getCredit());
		} else {
			amount = line.getAccount().getNormalBalance() == NormalBalance.DEBIT ? line.getDebit() : (-1.0 * line.getDebit());
		}
			
		if (existingLine.getAccount().getId() == line.getAccount().getId()) {
			
			BalanceMove balMove = balanceMoveRepository.findFirstByMoveLine(line);
			
			Double diff = amount - balMove.getAmount();
			
			balMove.set(line.getAccount(), move, line, 0.0, amount, balMove.getAmount() + diff, line.getDatePosted());
			
			balanceMoveRepository.save(balMove);
			
			// Update all next balMove balanceBefore, and balance
			Query qUpdateBal = em.createNamedQuery("BalanceMove.IncreaseBalanceThatNewer")
					.setParameter("diff", diff)
					.setParameter("balId", balMove.getId())
					.setParameter("accountId", balMove.getAccount().getId());
			
			qUpdateBal.executeUpdate();
		} else {
			deleteBalanceMove(move, existingLine);
			createBalanceMove(move, existingLine);
		}
	}
	
	@Transactional
	public void deleteBalanceMove(Move move, MoveLine line) {

		BalanceMove balMove = balanceMoveRepository.findFirstByMoveLine(line);
		
		if (balMove != null) {
			// Update all next balMove balanceBefore, and balance
			Query qUpdateBal = em.createNamedQuery("BalanceMove.DecreaseBalanceThatNewer")
					.setParameter("diff", balMove.getAmount())
					.setParameter("balId", balMove.getId())
					.setParameter("accountId", balMove.getAccount().getId());
			
			qUpdateBal.executeUpdate();
			balanceMoveRepository.delete(balMove);
		}
	}
	
	private Boolean isLineExist(MoveLine line, List<MoveLine> lines) {
		
		if (lines != null) {
			
			for (MoveLine existLine:lines) {
				if (line.getId() == existLine.getId()) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	protected Specifications<Move> setSearchCriteria(MoveCriteriaSpec criteria) {
		// TODO Move Service: setSearchCriteria
		Specifications<Move> specs = where(haveId());
		
		return specs;
	}

}
