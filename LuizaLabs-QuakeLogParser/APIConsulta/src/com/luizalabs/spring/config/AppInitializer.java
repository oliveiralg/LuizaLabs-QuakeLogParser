package com.luizalabs.spring.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/*
 * Classe de inicializacao do spring
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Class[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Class[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
