package com.bridgelabz.userservice.utility.EmailSecurity;

import javax.mail.MessagingException;

import com.bridgelabz.userservice.model.MailDto;





public interface UserEmailSecurity {
	public void sendEmail(MailDto mailDTO) throws MessagingException;
}
