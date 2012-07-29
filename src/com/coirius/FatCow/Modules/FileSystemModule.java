/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow.Modules;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.coirius.FatCow.ServerModule;

public class FileSystemModule extends ServerModule {
	@Override
	public boolean getReqAuth() {
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
					res = mv(new File(argv[2]), new File(argv[3]));
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

	protected String[] cat(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		ArrayList<String> buf = new ArrayList<String>();
		String line = "";
		while ((line = in.readLine()) != null)
			buf.add(line);
		in.close();
		return buf.toArray(new String[buf.size()]);
	}

	protected String[] ls(File path) {
		if(path.isDirectory())
			return path.list();
		return null;
	}

	protected boolean cp(File src, File dest, boolean srcDel) throws IOException {
		if(src.isDirectory()) {
			if(!dest.exists())
				dest.mkdir();
			else {
				dest = new File(dest, src.getName());
				dest.mkdir();
			}
			String[] files = src.list();
			for (String s : files)
				cp(new File(src, s), new File(dest, s), srcDel);
			if(srcDel)
				src.delete();
		} else {
			FileInputStream in = new FileInputStream(src);
			if(dest.isDirectory())
				dest = new File(dest, src.getName());
			FileOutputStream out = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int l;
			while((l = in.read(buf)) > 0)
				out.write(buf, 0, l);
			in.close();
			out.close();
			if(srcDel)
				src.delete();
		}
		return true;
	}

	protected boolean mv(File src, File dest) throws IOException {
		return cp(src, dest, true);
	}
}
