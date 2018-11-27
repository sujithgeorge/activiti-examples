package org.activiti.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import feign.RequestInterceptor;
import feign.RequestTemplate;


@Configuration("WebSecurityConfig")
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * Constructor disables the default security settings
	 **/
	public SpringSecurityConfig() {
		super(true);
	}
	
	@Bean
	public RequestInterceptor requestTokenBearerInterceptor() {
	    return new RequestInterceptor() {
	        @Override
	        public void apply(RequestTemplate requestTemplate) {
	        	log.info("Is SecurityContextHolder.getContext() null ===========>"+(SecurityContextHolder.getContext() != null));
	        	if(SecurityContextHolder.getContext() != null) {
	        		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	        		log.info("Token Value===========>"+details.getTokenValue());
	        		requestTemplate.header("Authorization", "bearer " + details.getTokenValue());
	        	}
	        }
	    };
	}
	
    /**
     * Allow url encoded slash http firewall.
     *
     * @return the http firewall
     */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
		web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
		web.ignoring().antMatchers("/public/**", "/swagger-ui.html", "/api.html", "/js/swagger-oauth.js", "/images/pacman_logo.svg", "/js/swagger.js", "/js/swagger-ui.js", "/images/favicon-32x32.png", "/images/favicon-16x16.png", "/images/favicon.ico", "/swagger-resources/**", "/v2/api-docs/**", "/webjars/**",
				"/v1/auth/**", "/client-auth/**", "/user/login/**", "/auth/refresh/**", "/user/authorize/**"); 
		web.ignoring().antMatchers("/imgs/**");
		web.ignoring().antMatchers("/css/**"); 
		web.ignoring().antMatchers("/css/font/**");
		web.ignoring().antMatchers("/proxy*/**");
		web.ignoring().antMatchers("/hystrix/monitor/**");
		web.ignoring().antMatchers("/hystrix/**");
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    /* (non-Javadoc)
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.anonymous().and().antMatcher("/user").authorizeRequests().antMatchers("/public/**").permitAll()
		.antMatchers("/secure/**").authenticated();
    }
}
