package com.giveu.utils;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * Created by fox on 2018/8/3.
 */
public class EmailUtil {

	private static final String URL = "10.11.11.112:8001/api/Email/Send";

	private static final String MAILFROMADD = "EmailEngine@dafycredit.com";

	private static final String ENGINETYPE = "0";


	public static boolean sendMail(String mailToAdd, String mailSubject, String mailBody) {
		HttpRequest httpRequest = HttpRequest.post(URL).form(
				"MailFromAdd",MAILFROMADD,
				"EngineType",ENGINETYPE,
				"MailToAdd",mailToAdd,
				"MailSubject",mailSubject,
				"MailBody",mailBody
		);
		HttpResponse response = httpRequest.send();
		if (response.statusCode() == 200) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
//		Boolean b = sendMail("hanbin@dafycredit.com;zhangyu6@dafycredit.com;", "test", "tsetbody");
//		Boolean b = sendMail("aunide@163.com", "test", "tsetbody");
		Boolean b = sendMail("hanbin@dafycredit.com;aunide@163.com;", "test11", "tsetbody");
		System.out.println(b);
	}
}
