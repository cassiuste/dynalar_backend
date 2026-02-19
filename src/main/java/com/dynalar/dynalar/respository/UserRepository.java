package com.dynalar.dynalar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dynalar.dynalar.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {	
}	