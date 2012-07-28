/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow.Utils;

import java.security.MessageDigest;
import java.util.Formatter;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class HashUtils {
	public static String SHA1(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
		mDigest.reset();
		mDigest.update(str.getBytes("UTF-8"));
		Formatter f = new Formatter();
		for (byte b : mDigest.digest())
			f.format("%02x", b);
		return f.toString();
	}
}
