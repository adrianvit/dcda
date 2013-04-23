package com.team314.dcda.local.utils;

public class ForbiddenException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ForbiddenException()
	{
		super("User is forbidden to access this resource!");
	}

}
