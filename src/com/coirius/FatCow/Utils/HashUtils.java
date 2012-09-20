/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow.Utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class HashUtils {
	@SuppressWarnings("resource")
	public static String SHA1(String str) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
		mDigest.reset();
		mDigest.update(str.getBytes("UTF-8"));
		Formatter f = new Formatter();
		for (byte b : mDigest.digest())
			f.format("%02x", b);
		return f.toString();
	}
}
