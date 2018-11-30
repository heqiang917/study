/**
 */
package com.study.webapp;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;



/**
 * 供 tomcat等服务器部署的入口启动类
 * @author xwb
 * @since 2016年1月13日
 */
public class AppWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }
    
    public AppWebXml() {
        super();
//        setRegisterErrorPageFilter(false);
    }
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }
    
}
