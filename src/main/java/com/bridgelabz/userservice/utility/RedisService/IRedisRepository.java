
package com.bridgelabz.userservice.utility.RedisService;

import io.jsonwebtoken.Claims;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */

public interface IRedisRepository {

	/**
	 * @param jwtToken
	 */
	void setToken(String jwtToken);

	/**
	 * @param userId
	 */
	// void deleteToken(String userId);
	// public String getToken(String userId);

	/**
	 * @param claim
	 * @return
	 */
	String getToken(Claims claim);

	/**
	 * @param claim
	 */
	void deleteToken(Claims claim);
}
