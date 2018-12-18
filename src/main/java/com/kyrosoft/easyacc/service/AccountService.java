package com.kyrosoft.easyacc.service;

import java.util.List;

import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.criteria.AccountCriteriaSpec;

public interface AccountService extends BaseService<Account, AccountCriteriaSpec> {

	public List<Account> getAccountsBalance();
}
