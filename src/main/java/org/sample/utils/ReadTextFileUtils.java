package org.sample.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 文本文件读取工具类
 * @author dw
 *
 */
public class ReadTextFileUtils {

	static Logger logger = LoggerFactory.getLogger(ReadTextFileUtils.class);

	/**
	 * 读取配置文件
	 */
	public static String readFileByInS(InputStream inputStream) throws IOException {

		StringBuilder builder = new StringBuilder();
		InputStreamReader reader = null;
		BufferedReader bfReader = null;
		try {
			reader = new InputStreamReader(inputStream, "UTF-8");
			bfReader = new BufferedReader(reader);
			String tmpContent = null;
			while ((tmpContent = bfReader.readLine()) != null) {
				builder.append(tmpContent.trim()).append("\n");
			}
			bfReader.close();
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// 忽略
		}
		return filter(builder.toString());
	}

	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	public static String filter(String input) {
//		return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
		return input.replaceAll("", "");
	}

	public static String readFileByPath(String path) throws IOException {
		FileInputStream fis = new FileInputStream(path);
		return readFileByInS(fis);
	}

	/**
	 * 读取properties文件
	 * @param path 文件路径
	 * @return map
	 */
	public static Map<String, String> loadProp(String path) {

		InputStream in = null;
		try {

			File file = new File(path);
			//
			if (!file.exists()) {
				logger.info("properties文件不存在");
				return null;
			}

			Map<String, String> prop = new HashMap<>();

			String text = readFileByPath(path);
			Properties properties = new Properties();
			// 使用ClassLoader加载properties配置文件生成对应的输入流
			in = new ByteArrayInputStream(text.getBytes());
			properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));

			Set<String> keys = properties.stringPropertyNames();

			for (String key : keys) {
				prop.put(key, properties.getProperty(key));
			}
			return prop;
		} catch (IOException e) {
			logger.error("load report info error {} ::: ", path, e);
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		return null;
	}

}
