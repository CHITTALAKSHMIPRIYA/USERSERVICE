package com.bridgelabz.userservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LAKSHMI PRIYA
 * @since  DATE:10-07-2018
 *         <p>
 *         <b>IUserDAO extending MongoRepository</b>
 *         </p>
 *
 */

import org.springframework.stereotype.Repository;

import com.bridgelabz.userservice.model.User;



/**
 * <p>
 * <b>Interface IUserDao extending MongoRepository</b>
 * </p>
 *
 */
@Repository
public interface IUserDAO extends MongoRepository<User, String> {

	public Optional<User> findByEmail(String email);

	public void save(Optional<User> user);

}
