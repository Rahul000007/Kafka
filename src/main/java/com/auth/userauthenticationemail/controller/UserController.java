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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Random;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/home")
    public String home() {
        return "normal/home";
    }

    @GetMapping("/resetpassword")
    public String resetPassword(Principal principal, HttpSession session) {
        String email = principal.getName();

//        Generating 4-digit New Otp
        Random random = new Random();
        int otp = random.nextInt(999999);

//        code for sending otp to mail
        String subject = "OTP from Cloud Contact";
        String msgBody = "OTP is " + otp;
        String recipient = email;

//        setting email details
        EmailDetails emailDetails = new EmailDetails(recipient, msgBody, subject);
        String sendMail = this.emailService.sendMail(emailDetails);
        if (sendMail.equals("sent")) {
            session.setAttribute("otp", otp);
            session.setAttribute("email", email);
            return "normal/verify_otp";
        } else {
            return "normal/home";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") Integer otp, HttpSession session) {
        Integer userOtp = (int) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        if (otp.equals(userOtp)) {
            return "normal/newPassword";
        } else {
            return "normal/verify_otp";
        }
    }

    @PostMapping("/changepassword")
    public String changePassword(@RequestParam("password") String password,HttpSession session){
        String email = (String ) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        this.userService.saveUser(user);
        return "redirect:/user/home";
    }

}
