package com.giveu.log.scan.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by fox on 2018/8/22.
 */
public class PathUtil {

	private static final String POM_XML = "pom.xml";

	private static final String JAR = "jar";

	/**
	 * 获取项目名称
	 * @return
	 */
	public static String getProjectName() {
		File file = new File(PathUtil.class.getResource("/").getPath());
		if (file == null) {
			return "";
		}

		int index = String.valueOf(file).indexOf(JAR);
		if (index >= 0) {
			String timpStr = file.toString().substring(0, index + JAR.length());
			index = timpStr.lastIndexOf("/");
			timpStr = timpStr.substring(index+1);
			return timpStr;
		}

		String pomPath = getFilePath(file,POM_XML) + "/" + POM_XML;

		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = null;
		try {
			model = reader.read(new FileReader(pomPath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return model.getArtifactId();
	}

	public static String getFilePath(File file, String fileName) {
		File parentFile = file.getParentFile();
		if (parentFile == null) {
			return "";
		}
		String[] strings = parentFile.list();
		if (strings != null) {
			for (String str : strings) {
				if (str.equals(fileName)) {
					return parentFile.toString();
				}
			}
		}
		return getFilePath(parentFile, fileName);
	}

}
