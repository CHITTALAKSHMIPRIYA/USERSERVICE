package com.bridgelabz.userservice.utility;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bridgelabz.userservice.utility.Exception.TodoException;



/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */
@Component
public class ModelMapperService {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	RestPreCondition restprecondition;
	
	public <D> D map(Object source,Class<D> destinationType) throws TodoException
	{
		restprecondition.checkNotNull(source,"Null Pointer Exception:source cannot be null");
		restprecondition.checkNotNull(source,"Null Pointer Exception:destination cannot be null");
		return modelMapper.map(source, destinationType);
	}

}
