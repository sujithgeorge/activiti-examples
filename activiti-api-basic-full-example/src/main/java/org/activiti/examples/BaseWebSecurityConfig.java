package org.activiti.examples;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@Conditional(CheckSecurityConfigCondition.class)
@EnableWebSecurity
public class BaseWebSecurityConfig extends WebSecurityConfigurerAdapter {
     

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/swagger-ui.html", 
				"/api.html", 
				"/css/styles.js", 
				"/js/swagger.js", 
				"/js/swagger-ui.js", 
				"/js/swagger-oauth.js", 
				"/images/pacman_logo.svg", 
				"/images/favicon-32x32.png", 
				"/images/favicon-16x16.png", 
				"/images/favicon.ico", 
				"/docs/v1/api.html", 
				"/swagger-resources/**", 
				"/v2/api-docs/**",
				"/v2/swagger.json");
	}

	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/**").permitAll();
    }
}
