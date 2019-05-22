package com.giveu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by fox on 2019/1/17.
 */
public class CommandUtil {

	public static String run(String command) throws IOException {
		Scanner input = null;
		String result = "";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			try {
				//等待命令执行完成
				process.waitFor(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InputStream is = process.getInputStream();
			input = new Scanner(is);

			StringBuilder sb = new StringBuilder();
			sb.append(command);
			sb.append("\n");
			while (input.hasNextLine()) {
				sb.append(input.nextLine());
				sb.append("\n");
			}
			result = sb.toString();
		} finally {
			if (input != null) {
				input.close();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}

	public static List<String> run(String[] command) throws IOException {
		Scanner input = null;
//		String result = "";
		Process process = null;
		List<String> list = new ArrayList<>();
		try {
			process = Runtime.getRuntime().exec(command);
			try {
				//等待命令执行完成
				process.waitFor(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InputStream is = process.getInputStream();
			input = new Scanner(is);

			StringBuilder sb = new StringBuilder();
//			sb.append(command);
//			sb.append("\n");
			while (input.hasNextLine()) {
//				sb.append(input.nextLine());
//				sb.append("\n");
				list.add(input.nextLine());
			}
//			result = sb.toString();
		} finally {
			if (input != null) {
				input.close();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return list;
	}

//	public static void main(String[] args) throws IOException {
//		List<String> list = new ArrayList<>();
//		list.add("/bin/sh");
//		list.add("-c");
//		list.add("ps -ef|grep jar");
//		String str = run(list.toArray(new String[list.size()]));
//		System.out.println(str);
//	}
}
