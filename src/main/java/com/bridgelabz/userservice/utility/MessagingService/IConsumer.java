
package com.bridgelabz.userservice.utility.MessagingService;

import com.bridgelabz.userservice.model.MailDto;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */

public interface IConsumer {
	public void recievedMessage(MailDto mailDTO);
}
