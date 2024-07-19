package com.aadim.elibrary.controller;

import com.aadim.elibrary.dto.RegisterDto;
import com.aadim.elibrary.entity.Users;
import com.aadim.elibrary.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request)
    {
        System.out.println("register page loaded");
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println("CSRF Token: " + csrfToken.getToken());
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result) {

        System.out.println("register Post form loaded");

        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            result.addError(
                    new FieldError("registerDto", "confirmpassword",
                            "Password And Confirm Password Doesnot Match !")
            );
        }

        Users users = userRepository.findByEmail(registerDto.getEmail());
        if(users != null)
        {
            result.addError(
                    new FieldError("registerDto", "email","Email is already Taken.")
            );
        }

        if(result.hasErrors())
        {
            return "register";
        }

        try {
            //create users
            var passwordEncoder = new BCryptPasswordEncoder();
            Users newUser = new Users();
            newUser.setName(registerDto.getName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPhoneNo(registerDto.getPhone());
            newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//            newUser.setA(registerDto.getAddress());

            userRepository.save(newUser);

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);

        }
        catch (Exception exception)
        {
            result.addError((
                    new FieldError("registerDto", "name",exception.getMessage())
                    ));
        }
        return "register";
    }

    @GetMapping("/")
    public String home()
    {
        return "base_layout";
    }
}
