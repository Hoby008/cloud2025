package com.exemple.Cloud.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exemple.Cloud.Model.User;

@Repository
public interface UserDAO extends JpaRepository<User , Long> {
    Optional<User> findByEmail(String email);
    
}
