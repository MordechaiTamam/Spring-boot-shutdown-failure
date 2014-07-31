package demo;

import javax.servlet.MultipartConfigElement;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.AbstractProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class ServletCtxConfig {

	@Autowired
	WebApplicationContext ctx;

	@Autowired
	private ServerProperties server;

	@Autowired(required = false)
	private MultipartConfigElement multipartConfig;

	Integer port=8081;

	@Bean
	EmbeddedServletContainerFactory servletContainerFactory() {
		TomcatEmbeddedServletContainerFactory retVal = new TomcatEmbeddedServletContainerFactory();
		retVal.setPort(port);
		return retVal;
	}

	@Bean
	EmbeddedServletContainerCustomizer myCustomizer() {
		MyCustomizer myCustomizer = new MyCustomizer();
		return myCustomizer;
	}

	@Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);
		return dispatcherServlet;
	}

	@Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)
	public ServletRegistrationBean dispatcherServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet(),
				this.server.getServletPath());
		registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
		if (this.multipartConfig != null) {
			registration.setMultipartConfig(this.multipartConfig);
		}
		return registration;
	}

	@Bean
	public ServerProperties serverProperties() {
		return new ServerProperties();
	}
}

class MyCustomizer implements EmbeddedServletContainerCustomizer {
	
	Integer port =8081;

	@Override
	public void customize(ConfigurableEmbeddedServletContainer factory) {
		if (factory instanceof TomcatEmbeddedServletContainerFactory) {
			customizeTomcat((TomcatEmbeddedServletContainerFactory) factory);
		}
	}

	public void customizeTomcat(TomcatEmbeddedServletContainerFactory factory) {
		factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				AbstractProtocol protocol = (AbstractProtocol) connector.getProtocolHandler();
				protocol.setPort(port);
			}
		});
	}
}
