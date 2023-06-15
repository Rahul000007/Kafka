package com.auth.userauthenticationemail.service;

import com.auth.userauthenticationemail.model.EmailDetails;

public interface EmailService {

    String sendMail(EmailDetails emailDetails);
}
