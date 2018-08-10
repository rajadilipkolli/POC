open module poc.springsecurity.mongodb {

	requires java.validation;
	
	requires spring.beans;
	requires spring.context;
	requires spring.core;
	requires spring.web;
	requires spring.webmvc;
	
	requires spring.security.config;
	requires spring.security.core;
	requires spring.security.web;
	
	requires spring.boot;
    requires spring.boot.autoconfigure;
    
	requires spring.data.commons;
	requires spring.data.mongodb;

	requires tomcat.embed.core;
	
	requires jdk.unsupported;

}