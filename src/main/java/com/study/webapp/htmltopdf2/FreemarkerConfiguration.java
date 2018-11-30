package com.study.webapp.htmltopdf2;

import freemarker.template.Configuration;

public class FreemarkerConfiguration {

	private static Configuration config = null;

	static {
		config = new Configuration();
		config.setClassForTemplateLoading(FreemarkerConfiguration.class, "/template");
	}

	public static Configuration getConfiguation() {
		return config;
	}

}
