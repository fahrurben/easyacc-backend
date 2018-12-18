package com.kyrosoft.easyacc.repository;

import java.util.List;

import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.BalanceMove;
import com.kyrosoft.easyacc.model.MoveLine;

public interface BalanceMoveRepository extends BaseRepository<BalanceMove> {

	List<BalanceMove> findByMoveLine(MoveLine moveLine);
	
	BalanceMove findFirstByMoveLine(MoveLine moveLine);
	
	BalanceMove findFirstByAccountOrderByIdDesc(Account account);
}
