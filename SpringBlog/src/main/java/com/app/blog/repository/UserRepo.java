package com.app.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.blog.model.User;

@Repository
public interface UserRepo extends JpaRepository<User,Long>{
	Optional<User> findUserByUsername(String username);

}
