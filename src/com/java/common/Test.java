package com.java.common;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String username = ReadPropertiesFile.getPropValues("username");
		String password = ReadPropertiesFile.getPropValues("passwordaa");
		System.out.println(username);
		System.out.println(password);
	}

}
