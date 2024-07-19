package com.aadim.elibrary.services;


import com.aadim.elibrary.entity.Users;
import com.aadim.elibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
//    private String email;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users =repository.findByEmail(email);

        //return whether user password is valid or not
        if(users !=null)
        {
            var userDetail= User.withUsername(users.getEmail())
                    .password(users.getPassword())
                    .roles(users.getRole())
                    .build();
            return userDetail;
        }
        return  null;
    }
}
