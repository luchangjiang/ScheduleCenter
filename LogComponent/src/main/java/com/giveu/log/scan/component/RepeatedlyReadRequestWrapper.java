package com.giveu.log.scan.component;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

public class RepeatedlyReadRequestWrapper extends HttpServletRequestWrapper {
	private byte[] body;

	public RepeatedlyReadRequestWrapper(HttpServletRequest request) {
		super(request);
//		body = readBytes(request.getReader(), "utf-8");
		try {
			body = readBytes(request.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
		return new ServletInputStream() {

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {

			}

			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}

	/**
	 * 通过BufferedReader和字符编码集转换成byte数组
	 * @param br
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	private byte[] readBytes(BufferedReader br,String encoding) throws IOException{
		String str = null,retStr="";
		while ((str = br.readLine()) != null) {
			retStr += str;
		}
		if (StringUtils.isNotBlank(retStr)) {
			return retStr.getBytes(Charset.forName(encoding));
		}
		return null;
	}
	private byte[] readBytes(InputStream is) throws IOException{

		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();

		byte[] bytes = new byte[2048];
		int len = 0;
		while ((len = is.read(bytes)) != -1) {
			swapStream.write(bytes, 0, len);
		}

		return swapStream.toByteArray();
	}
}