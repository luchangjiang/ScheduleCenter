package com.giveu.log.scan.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by fox on 2018/6/30.
 */
public class MD5Util {

	private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "a", "b", "c", "d", "e", "f"};

	/**
	 * 转换字节数组为16进制字串
	 *
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (byte aB : b) {
			resultSb.append(byteToHexString(aB));
		}
		return resultSb.toString();
	}

	/**
	 * 转换byte到16进制
	 *
	 * @param b 要转换的byte
	 * @return 16进制格式
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD5编码
	 *
	 * @param origin 原始字符串
	 * @return 经过MD5加密之后的结果
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * 签名
	 * @param original
	 * @return
	 */
	public static String sign(String original) {
		byte[] bytes = null;
		char[] chars = null;
		try {
			bytes = original.toUpperCase().replace(" ","").getBytes("UTF-8");
			chars = new String(bytes).toCharArray();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Arrays.sort(chars);
		String transform = new String(chars);
		String encrypted = MD5Util.MD5Encode(transform).toUpperCase();
		return encrypted;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
//		System.out.println(MD5Util.MD5Encode("giveu123"));
//		String str = "jobNameGiveuTest2jobCodeSFJLJ23K4JLJ288787SKFXFAAcronExpression*/2 * * * * ?jobDescTestTestTestcallbackUrlhttp://localhost:8080/jobadmin153035406291112345".toUpperCase().replace(" ","");
//		String str = "jobNameGiveuTest4cronExpression*/2 * * * * ?jobCodeSFJLJ23K4JLJ288787SKFXFAAxGiveuTimestamp1530354062911callbackUrlhttp://localhost:8080/jobxGiveuAppKeyadminjobDescTestTestTest12345".toUpperCase().replace(" ","");
//		String add = "jobNameGiveuTest5jobCodeSFJLJ23K4JLJ288787SKFXFAAcronExpression*/2 * * * * ?jobDescTestTestTestcallbackUrlhttp://localhost:8080/jobxGiveuAppKeyadminxGiveuTimestamp153035406291112345".toUpperCase().replace(" ","");
//
////		String del = "jobCodeSFJLJ23K4JLJ288787SKFXFXXxGiveuAppKeyadminxGiveuTimestamp153035406291112345".toUpperCase().replace(" ","");
//		byte[] b  = add.getBytes("utf-8");
//		char[] c = new String(b).toCharArray();
//		Arrays.sort(c);
//		String xx = new String(c);
//		String yy = MD5Util.MD5Encode(xx);
//		System.out.println(new String(c));
//		System.out.println(yy.toUpperCase());
		System.out.println(sign(""));
	}
}
