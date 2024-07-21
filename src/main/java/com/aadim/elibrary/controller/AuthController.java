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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request) {
        System.out.println("register page loaded");
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println("CSRF Token: " + csrfToken.getToken());
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute("registerDto", registerDto);
        model.addAttribute("success", false);
        model.addAttribute("_csrf", csrfToken);
        return "register";
    }
    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result) {

        if (result.hasErrors()) {
            logger.error("Form has errors: {}", result.getAllErrors());
            return "register";
        }

        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            logger.error("Password and Confirm Password do not match");
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
            logger.info("Phone number received is {}", registerDto.getPhone());
            //create users
            var passwordEncoder = new BCryptPasswordEncoder();
            Users newUser = new Users();
            newUser.setName(registerDto.getName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPhoneNo(registerDto.getPhone());
            newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            newUser.setRole(registerDto.getRole());
//            newUser.setA(registerDto.getAddress());

            userRepository.save(newUser);
            logger.info("User saved successfully");

            System.out.println("register Post form SAVED");


            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);

        }
        catch (Exception exception)
        {
            result.addError((
                    new FieldError("registerDto", "name",exception.getMessage())
                    ));
        }
        return "redirect:/register?success";
    }

    @GetMapping("/")
    public String home()
    {
        return "base_layout";
    }
}
