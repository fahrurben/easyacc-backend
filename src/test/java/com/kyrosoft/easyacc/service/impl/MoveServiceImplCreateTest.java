package com.kyrosoft.easyacc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.BalanceMove;
import com.kyrosoft.easyacc.model.Move;
import com.kyrosoft.easyacc.model.MoveLine;
import com.kyrosoft.easyacc.repository.BalanceMoveRepository;
import com.kyrosoft.easyacc.service.AccountService;
import com.kyrosoft.easyacc.service.MoveService;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoveServiceImplCreateTest extends BaseTest {

	@Autowired
	private MoveService moveService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BalanceMoveRepository balanceMoveRepo;
	
	@Autowired
	private EntityManager em;
	
	@Test
	public void createMove() throws Exception {
		
		Account account = accountService.get(1L);
		
		Move move = new Move();
		move.setMoveCategory(null);
		move.setDesc("test");
		move.setRef("test");
		move.setDatePosted(new Date());
		
		MoveLine line = new MoveLine();
		line.setAccount(account);
		line.setCredit(1.0);
		line.setDebit(0.0);
		line.setDatePosted(new Date());
		line.setMove(move);
		
		List<MoveLine> lines = new ArrayList<>();
		lines.add(line);
		move.setLines(lines);
		
		moveService.create(move);
		
		Move saved = moveService.get(move.getId());
		
		MoveLine savedLine = saved.getLines().get(0);
		assertEquals(account.getId(), savedLine.getAccount().getId());
		assertEquals(1.0, savedLine.getCredit().doubleValue(), delta);
		assertEquals(0.0, savedLine.getDebit().doubleValue(), delta);
		assertEquals(move.getId(), savedLine.getMove().getId());
		
		BalanceMove balMove = balanceMoveRepo.findFirstByMoveLine(savedLine);
		assertEquals(savedLine.getAccount().getId(), balMove.getAccount().getId());
		assertEquals(0.0, balMove.getBalanceBefore(), delta);
		assertEquals(1.0, balMove.getAmount(), delta);
		assertEquals(1.0, balMove.getBalance(), delta);
	}
}
