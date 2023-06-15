package com.auth.userauthenticationemail.controller;

import com.auth.userauthenticationemail.model.EmailDetails;
import com.auth.userauthenticationemail.model.User;
import com.auth.userauthenticationemail.repository.UserRepository;
import com.auth.userauthenticationemail.service.EmailService;
import com.auth.userauthenticationemail.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String sigUp() {
        return "signUp";
    }

    @PostMapping("/do_signUp")
    public String doSignUp(@ModelAttribute User user, HttpSession session) {
        session.setAttribute("newUser", user);
        return "redirect:/send-otp-new-user";
    }

    //    Sending Otp to User
    @GetMapping("/send-otp-new-user")
    public String sendOTP(HttpSession session) {

        User user = (User) session.getAttribute("newUser");
        String email = user.getEmail();

//        Generating 4-digit New Otp
        Random random = new Random();
        int otp = random.nextInt(999999);

//        code for sending otp to mail
        String subject = "OTP from Cloud Contact";
        String msgBody = "OTP is "+ otp;
        String recipient = email;

//        setting email details
        EmailDetails emailDetails = new EmailDetails(recipient, msgBody, subject);
        String sendMail = this.emailService.sendMail(emailDetails);

        if (sendMail.equals("sent")) {
            session.setAttribute("NewUserOtp", otp);
            session.setAttribute("email", email);
            return "verify_NewUser_otp";
//            otp sent
        } else {
//            failed to send otp
            return "signUp";
        }
    }

    @PostMapping("/verify-otp-new-user")
    public String verifyOtp(@RequestParam("otp") Integer otp, HttpSession session) {
        Integer newUserOtp = (int) session.getAttribute("NewUserOtp");
        String email = (String) session.getAttribute("email");

        if (otp.equals(newUserOtp)) {
            User user = this.userService.getUserByEmail(email);
            if (user == null) {
                User newUser = (User) session.getAttribute("newUser");
                this.userService.saveUser(newUser);
                return "signin";
            } else {
                return "signUp";
            }
        } else {
//            wrong otp
            return "verify_NewUser_otp";
        }
    }

}
