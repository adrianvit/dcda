package com.team314.dcda.local.utils;

public class UnauthorizedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException()
	{
		super("User is not authorized!");
	}
}
