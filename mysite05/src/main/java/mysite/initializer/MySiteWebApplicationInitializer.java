package mysite.initializer;

import java.util.ResourceBundle;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration.Dynamic;
import mysite.config.AppConfig;
import mysite.config.WebConfig;

public class MySiteWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] {AppConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] {WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		ResourceBundle bundle = ResourceBundle.getBundle("mysite.config.web.fileupload");
		
		long maxFileSize = Long.parseLong(bundle.getString("fileupload.maxFileSize"));
		long maxRequestSize = Long.parseLong(bundle.getString("fileupload.maxRequestSize"));
		int fileSizeThresHold = Integer.parseInt(bundle.getString("fileupload.fileSizeThreshold"));
		
		MultipartConfigElement config = new MultipartConfigElement(null, maxFileSize, maxRequestSize, fileSizeThresHold);
		registration.setMultipartConfig(config);
	}

}
