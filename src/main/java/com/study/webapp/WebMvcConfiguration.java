/**
 */
package com.study.webapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 添加servlet filter bean
 * 
 * @author xwb
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	/**
	 * 配置拦截器
	 * 
	 */

	// @Bean
	// public UserSecurityInterceptor userSecurityInterceptor() {
	// return new UserSecurityInterceptor();
	// }

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
		// InterceptorRegistration ir =
		// registry.addInterceptor(userSecurityInterceptor());
		// ir.addPathPatterns("/*");
		// ir.addPathPatterns("/*/**");
		// ir.excludePathPatterns("/user/login/password");
		// ir.excludePathPatterns("/login");
		// ir.excludePathPatterns("/excel2/**");

	}

}
