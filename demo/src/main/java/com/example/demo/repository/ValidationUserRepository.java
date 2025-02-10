package com.example.demo.repository;

import com.example.demo.entity.ValidationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidationUserRepository extends JpaRepository<ValidationUser, Long> {
    List<ValidationUser> findByEmail(String email);
    List<ValidationUser> findByRoleId(Integer roleId);
    List<ValidationUser> findByTokenDeblocage(String tokenDeblocage);
}
