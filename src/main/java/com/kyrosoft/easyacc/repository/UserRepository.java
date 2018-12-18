package com.kyrosoft.easyacc.repository;

import com.kyrosoft.easyacc.model.User;

public interface UserRepository extends BaseRepository<User> {
	User findByEmail(String email);
}
