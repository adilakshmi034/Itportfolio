package com.techpixe.website.constants;

import java.security.SecureRandom;

public class PasswordGenerator {

	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DIGITS = "0123456789";

	private static final String ALL = LOWER + UPPER + DIGITS;

	public static String generatePassword(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			password.append(ALL.charAt(random.nextInt(ALL.length())));
		}
		return password.toString();
	}
}
