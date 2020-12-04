package org.sample.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author
 * VelocityUtils  Velocity的工具类
 */
public class VelocityUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(VelocityUtils.class);

    private VelocityUtils() {
    }

    private static VelocityEngine ve;

    static {
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }

    /**
     * 获取模板
     *
     * @param templatePath
     * @param params
     * @return
     */
    public static String getTemplate(String templatePath, Map<String, Object> params) {
    	logger.info("vm path:::{}", templatePath);
        Template t = ve.getTemplate(templatePath);
        VelocityContext ctx = new VelocityContext();
        params.forEach((key, value) -> ctx.put(key, value));
        StringWriter writer = new StringWriter();
        t.merge(ctx, writer);
        return writer.toString();
    }


}
