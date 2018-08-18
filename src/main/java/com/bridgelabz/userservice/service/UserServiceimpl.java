package com.bridgelabz.userservice.service;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.userservice.model.LoginDto;
import com.bridgelabz.userservice.model.MailDto;
import com.bridgelabz.userservice.model.RegisterDto;
import com.bridgelabz.userservice.model.ResetPasswordDto;
import com.bridgelabz.userservice.model.User;
import com.bridgelabz.userservice.repository.IUserDAO;
import com.bridgelabz.userservice.utility.Messages;
import com.bridgelabz.userservice.utility.ModelMapperService;
import com.bridgelabz.userservice.utility.RestPreCondition;
import com.bridgelabz.userservice.utility.Utility;
import com.bridgelabz.userservice.utility.EmailSecurity.UserEmailSecurity;
import com.bridgelabz.userservice.utility.Exception.TodoException;
import com.bridgelabz.userservice.utility.MessagingService.Producer;
import com.bridgelabz.userservice.utility.RedisService.IRedisRepository;

import io.jsonwebtoken.Claims;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>Service implementation class implements UserService interface .</b>
 *        </p>
 */

@Service
public class UserServiceimpl implements IUSerService {
	@Autowired
	private IUserDAO userDao;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserEmailSecurity userEmailSecurity;

	@Autowired
	private Producer produce;
	@Autowired
	private Utility utility;
	@Autowired
	ModelMapperService model;

	@Autowired
	IRedisRepository redisRepo;

	@Autowired
	Messages messages;

	/**
	 * <p>
	 * <b>This method is used to register the user details</b>
	 * </p>
	 * 
	 * @param registerDTO
	 * @param request
	 * @return
	 * @throws TodoException
	 * @throws MessagingException
	 */
	@Override
	public void registerUser(RegisterDto registerDTO, String uri) throws MessagingException, TodoException {
		Optional<User> optionalUser = userDao.findByEmail(registerDTO.getEmail());
		if (optionalUser.isPresent()) {
			throw new TodoException(messages.get("124"));
		}
		Utility.isValidateAllFields(registerDTO);
		User user = model.map(registerDTO, User.class);
		user.setPassword(encoder.encode(registerDTO.getPassword()));
		userDao.save(user);

		Optional<User> optionalUser1 = userDao.findByEmail(registerDTO.getEmail());
		sendEmailMessage(uri, optionalUser1);

	}

	/**
	 * <p>
	 * <b>This method is used to login if the user is registered</b>
	 * </p>
	 * 
	 * @param loginDTO
	 * @param request
	 * @return
	 * @throws TodoException
	 * @throws MessagingException
	 */
	@Override
	public String loginUser(LoginDto loginDTO, String uri) throws TodoException, MessagingException {
		Optional<User> optionalUser = userDao.findByEmail(loginDTO.getEmailId());
		RestPreCondition.checkArgument(userDao.findByEmail(loginDTO.getEmailId()), messages.get("121"));
		if (optionalUser.get().isActivate()) {
			if (!encoder.matches(loginDTO.getPassword(), optionalUser.get().getPassword())) {
				throw new TodoException(messages.get("122"));
			}
			System.out.println(loginDTO.getEmailId());
			Optional<User> user = userDao.findByEmail(loginDTO.getEmailId());
			String jwtToken = utility.createToken(user.get().getEmail());
			sendEmailMessage(uri, optionalUser);
			return jwtToken;
		}
		throw new TodoException(messages.get("123"));

	}

	/**
	 * <p>
	 * <b> This method is used to send link if user forget the password</b>
	 * </p>
	 * 
	 * @param emailId
	 * @param request
	 * @return
	 * @throws TodoException
	 * @throws MessagingException
	 */
	@Override
	public void forgotPassword(String emailId, String uri) throws MessagingException, TodoException {
		System.out.println(emailId);
		Optional<User> optionalUser = userDao.findByEmail(emailId);
		System.out.println(optionalUser.get().getEmail());
		String token = Utility.createToken(optionalUser.get().getId());
		sendEmailMessage(uri, optionalUser);

	}

	/**
	 * <p>
	 * <b>This method is used to activate the account once the user is
	 * registered</b>
	 * </p>
	 * 
	 * @param token
	 * @return
	 * @throws TodoException
	 */
	@Override
	public void setActivationStatus(String token) throws TodoException {
		Claims claim = utility.parseJwt(token);
		redisRepo.setToken(token);
		Optional<User> optionalUser = userDao.findById(claim.getId());

		if (!optionalUser.isPresent()) {
			throw new TodoException(messages.get("125"));
		}

		User user = optionalUser.get();
		user.setActivate(true);
		userDao.save(user);
	}

	/**
	 * <p>
	 * <b> This method is used to change password</b>
	 * </p>
	 * 
	 * @param resetPasswordDTO
	 * @param token
	 * @return
	 * @throws TodoException
	 */
	@Override
	public void resetPassword(ResetPasswordDto resetPasswordDTO, String token) throws TodoException {
		Claims claim = utility.parseJwt(token);
		if (!Utility.validatePassword(resetPasswordDTO.getNewPassword())) {
			throw new TodoException(messages.get("126"));
		}

		if (!Utility.isPasswordMatch(resetPasswordDTO.getNewPassword(), resetPasswordDTO.getConfirmPassword())) {
			throw new TodoException(messages.get("127"));
		}
		Optional<User> optionalUser = userDao.findById(claim.getId());
		if (!optionalUser.isPresent()) {
			throw new TodoException(messages.get("128"));
		}

		User user = optionalUser.get();
		user.setPassword(encoder.encode(resetPasswordDTO.getNewPassword()));
		userDao.save(user);
	}

	/**
	 * @param uri
	 * @param optionalUser
	 * @throws MessagingException
	 */
	public void sendEmailMessage(String uri, Optional<User> optionalUser) throws MessagingException {
		String token = Utility.createToken(optionalUser.get().getId());
		// redisRepo.setToken(token);
		MailDto mailDTO = new MailDto();
		mailDTO.setId(optionalUser.get().getId());
		mailDTO.setToMailAddress(optionalUser.get().getEmail());
		mailDTO.setSubject(messages.get("129"));
		mailDTO.setSalutation("Hi " + optionalUser.get().getFirstName());
		mailDTO.setBody(
				"Activate your account by clicking on this link: http://localhost:8080" + uri + "?token=" + token);
		mailDTO.setMailSign("\nThank you \n Lakshmi G \n Bridge Labz \n Mumbai");
		userEmailSecurity.sendEmail(mailDTO);
		produce.produceMessage(mailDTO);
	}

	/**
	 * @return
	 *         <p>
	 * 		<b>method used to display list of users in note using feign</b>
	 *         </p>
	 */
	@Override
	public List<?> getuser() {
		List<?> list = userDao.findAll();
		return list;
	}

}