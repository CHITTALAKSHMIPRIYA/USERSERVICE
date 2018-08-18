
package com.bridgelabz.userservice.utility.RedisService;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.userservice.model.User;
import com.bridgelabz.userservice.utility.Utility;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import io.jsonwebtoken.Claims;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */
@Repository
public class RedisRepository implements IRedisRepository {
	@Autowired
	Utility util;
	

	private RedisTemplate<String, User> redisTemplate;
	private static HashOperations<String, String, String> hashOperations;
	private static String KEY = "lakshmi";

	@Autowired
	public RedisRepository(RedisTemplate<String, User> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	private static Logger logger = LoggerFactory.getLogger(RedisRepository.class);

	/**
	 * To initialize hash operations and this method MUST be invoked before the
	 * class is put into service.
	 */
	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	/**
	 * @param clientId
	 * @param jwtToken
	 */
	@Override
	public void setToken(String token) {
		Claims claim = util.parseJwt(token);
		// String userId = tokenProvider.parseJWT(jwtToken);
		hashOperations.put(KEY, claim.getId(), token);
		logger.info("Token set in redis");
	}

	/**
	 * @param clientId
	 * @return token
	 */
	@Override
	public String getToken(Claims claim) {
		logger.info("Getting token from redis");
		return hashOperations.get(KEY, claim);
	}

	/**
	 * @param clientId
	 */
	@Override
	public void deleteToken(Claims claim) {
		logger.info("Deleting token from redis");
		hashOperations.delete(KEY, claim);
	}
}
