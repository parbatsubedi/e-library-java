package com.aadim.elibrary.repository;

import com.aadim.elibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByName(String userName);
    public User findByEmail(String email);
}
