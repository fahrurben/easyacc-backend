package com.kyrosoft.easyacc.service.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kyrosoft.easyacc.EntityNotFoundException;
import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.BalanceMove;
import com.kyrosoft.easyacc.model.Move;
import com.kyrosoft.easyacc.model.MoveLine;
import com.kyrosoft.easyacc.repository.BalanceMoveRepository;
import com.kyrosoft.easyacc.service.AccountService;
import com.kyrosoft.easyacc.service.MoveService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoveServiceImplUpdateTest extends BaseTest {

	@Autowired
	private MoveService moveService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BalanceMoveRepository balanceMoveRepo;
	
	private Account account;
	
	private Move move;
	
	@Before
	public void before() throws EntityNotFoundException {
		
		account = accountService.get(1L);
		
		move = new Move();
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
	}
	
	@Test
	public void updateMoveLines() throws Exception {
		
		move = new Move();
		move.setId(1L);
		move.setMoveCategory(null);
		move.setDesc("updated");
		move.setRef("updated");
		move.setDatePosted(new Date());
		
		MoveLine line = new MoveLine();
		line.setAccount(account);
		line.setCredit(2.0);
		line.setDebit(0.0);
		line.setDatePosted(new Date());
		line.setMove(move);
		
		List<MoveLine> lines = new ArrayList<>();
		lines.add(line);
		move.setLines(lines);
		
		moveService.update(1L, move);
		
		Move saved = moveService.get(move.getId());
		
		MoveLine savedLine = saved.getLines().get(0);
		assertEquals(account.getId(), savedLine.getAccount().getId());
		assertEquals(2.0, savedLine.getCredit().doubleValue(), delta);
		assertEquals(0.0, savedLine.getDebit().doubleValue(), delta);
		assertEquals(move.getId(), savedLine.getMove().getId());
		
		BalanceMove balMove = balanceMoveRepo.findFirstByMoveLine(savedLine);
		assertEquals(savedLine.getAccount().getId(), balMove.getAccount().getId());
		assertEquals(0.0, balMove.getBalanceBefore(), delta);
		assertEquals(2.0, balMove.getAmount(), delta);
		assertEquals(2.0, balMove.getBalance(), delta);
	}
}
