package com.aadim.elibrary.repository;

import com.aadim.elibrary.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    public Users findByName(String userName);
    public Users findByEmail(String email);
}
