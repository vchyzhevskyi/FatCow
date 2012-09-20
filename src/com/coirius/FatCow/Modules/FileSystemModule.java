/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow.Modules;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.coirius.FatCow.ServerModule;

public class FileSystemModule extends ServerModule {
	protected String[] cat(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		ArrayList<String> buf = new ArrayList<String>();
		String line = "";
		while ((line = in.readLine()) != null)
			buf.add(line);
		in.close();
		return buf.toArray(new String[buf.size()]);
	}

	protected byte[] catBinary(File file) throws IOException {
		byte[] res = new byte[(int) file.length()];
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		for (int i = 0; i < res.length; i++)
			res[i] = (byte) in.read();
		in.close();
		return res;
	}

	protected boolean cp(File src, File dest, boolean srcDel)
			throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists())
				dest.mkdir();
			else {
				dest = new File(dest, src.getName());
				dest.mkdir();
			}
			String[] files = src.list();
			for (String s : files)
				cp(new File(src, s), new File(dest, s), srcDel);
			if (srcDel)
				src.delete();
		} else {
			FileInputStream in = new FileInputStream(src);
			if (dest.isDirectory())
				dest = new File(dest, src.getName());
			FileOutputStream out = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int l;
			while ((l = in.read(buf)) > 0)
				out.write(buf, 0, l);
			in.close();
			out.close();
			if (srcDel)
				src.delete();
		}
		return true;
	}

	@Override
	public Object doWork(String[] argv) {
		Object res = null;
		try {
			switch (argv[1]) {
			case "cat": {
				res = cat(new File(argv[2]));
				break;
			}
			case "ls": {
				res = ls(new File(argv[2]));
				break;
			}
			case "cp": {
				res = cp(new File(argv[2]), new File(argv[3]), false);
				break;
			}
			case "mv": {
				res = cp(new File(argv[2]), new File(argv[3]), true);
				break;
			}
			case "catBin": {
				res = catBinary(new File(argv[2]));
				break;
			}
			default: {
				res = null;
				break;
			}
			}
		} catch (Exception ex) {
			res = null;
		}
		return res;
	}

	@Override
	public boolean getReqAuth() {
		return true;
	}

	protected String[] ls(File path) {
		if (path.isDirectory())
			return path.list();
		return null;
	}
}
