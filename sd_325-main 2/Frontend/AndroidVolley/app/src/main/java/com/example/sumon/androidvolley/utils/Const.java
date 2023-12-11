package com.example.sumon.androidvolley.utils;

/**
 * Public Const class that holds static final Strings that don't change throughout our program
 * @author Zach
 */
public class Const {
	/**
	 * Given URL_JSON_OBJECT string from tutorial
	 */
	public static final String URL_JSON_OBJECT = "https://jsonplaceholder.typicode.com/users/1";
	/**
	 * Given URL_JSON_ARRAY string from tutorial
	 */
	public static final String URL_JSON_ARRAY = "https://jsonplaceholder.typicode.com/users";
	/**
	 * Given URL_STRING_REQ string from tutorial
	 */
	public static final String URL_STRING_REQ = "https://jsonplaceholder.typicode.com/users/1";
	/**
	 * Given URL_IMAGE string from tutorial
	 */
	public static final String URL_IMAGE = "http://sharding.org/outgoing/temp/testimg3.jpg";
	/**
	 * Our LOCAL_HOST String value for when we are developing our code not yet present in the server, changes
	 * based on who's computer it is
	 */
	public static final String LOCAL_HOST = "http://192.168.56.1:8080/api/";
	/**
	 * Our SERVER String value that is the start of all of our endpoints for server connection
	 */
	public static final String SERVER = "http://coms-309-058.class.las.iastate.edu:8080/api/";
	/**
	 * The string status of an unpaid member
	 */
	public static final String UNPAID_MEMBER = "Unpaid Member";
	/**
	 * The string status of a paid member
	 */
	public static final String PAID_MEMBER = "Paid Member";
}
