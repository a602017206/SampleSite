package org.sample.config;

import ch.qos.logback.core.PropertyDefinerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 日志分机器保存
 * @author 18123055
 *
 */
public class LogIpConfig extends PropertyDefinerBase {

	private static final Logger logger = LoggerFactory.getLogger(LogIpConfig.class);
	
	@Override
	public String getPropertyValue() {

		try {
			InetAddress address = InetAddress.getLocalHost();
			return address.getHostAddress().replaceAll("\\.", "_");

		} catch (UnknownHostException e) {
			logger.error("get server error :: ", e);
		}

		return "";
	}

}
