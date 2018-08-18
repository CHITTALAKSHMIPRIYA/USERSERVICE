package com.bridgelabz.userservice.service;

import java.util.List;

import javax.mail.MessagingException;
import org.springframework.stereotype.Service;

import com.bridgelabz.userservice.model.LoginDto;
import com.bridgelabz.userservice.model.RegisterDto;
import com.bridgelabz.userservice.model.ResetPasswordDto;
import com.bridgelabz.userservice.utility.Exception.TodoException;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>This interface contains methods for registration,login,forgot
 *        password,Reset password.</b>
 *        </p>
 */

@Service
public interface IUSerService {
	/**
	 * @param registerDTO
	 * @param uri
	 * @return
	 * @throws MessagingException
	 * @throws TodoException
	 *             <p>
	 *             <b> This method is used to check if the User is present or not.if
	 *             the user is present it throws an exception else it will register
	 *             the user</b>
	 *             </p>
	 */
	void registerUser(RegisterDto registerDTO, String uri) throws MessagingException, TodoException;

	/**
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>This method is used to set the activation status if the user
	 *             is present</b>
	 *             </p>
	 */
	public void setActivationStatus(String token) throws TodoException;

	/**
	 * @param resetPasswordDTO
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>This method is used to change the password if the user forget
	 *             the password</b>
	 *             </p>
	 */
	public void resetPassword(ResetPasswordDto resetPasswordDTO, String token) throws TodoException;

	/**
	 * @param emailId
	 * @param uri
	 * @throws MessagingException
	 *             <p>
	 *             <b>Token will be generated to a mail to change password</b>
	 *             </p>
	 * @throws TodoException
	 */
	public void forgotPassword(String emailId, String uri) throws MessagingException, TodoException;

	/**
	 * @param loginDTO
	 * @param uri
	 * @param resp
	 * @return
	 * @throws TodoException
	 * @throws MessagingException
	 *             <p>
	 *             <b>Method to login the user if the user is registered</b>
	 *             </p>
	 */
	String loginUser(LoginDto loginDTO, String uri) throws TodoException, MessagingException;

	/**
	 * @return
	 *         <p>
	 *         <b>Method to display the users present in db in note using feign</b>
	 *         </p>
	 */
	List<?> getuser();
}
