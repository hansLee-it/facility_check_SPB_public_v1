package hans.firefighting.check.facility_check.config;

import hans.firefighting.check.facility_check.settings.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import hans.firefighting.check.facility_check.user.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	UserService userService;

	@Autowired
	UserLogService userLogService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/assets/**").permitAll()
	        	.requestMatchers("/login").permitAll()
				.requestMatchers("/error").permitAll()
				.anyRequest().authenticated()
			)
			.rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
					.rememberMeParameter("remember-me")
					.tokenValiditySeconds(60*60*24*7*4)
			)
			.formLogin((form) -> form
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/login")
				.successHandler(getAuthenticationSuccessHandler())
				.failureHandler(getAuthenticationFailureHandler())
				.permitAll()
			)
			.logout((logout) -> logout
			        .logoutUrl("/logout")
					.logoutSuccessHandler(getLogoutSuccessHandler())
			        .logoutSuccessUrl("/login")
					.deleteCookies("JSESSIONID")
					.deleteCookies("loginCookie")
			        .permitAll()
			        .invalidateHttpSession(true));
		http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .invalidSessionUrl("/login");
	      


		return http.build();
	}

	// Process on Authentication Success : UserAuthenticationSuccessHandler
	@Bean
	UserAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return new UserAuthenticationSuccessHandler(userService, userLogService);
	}

	// Process on Authentication Failure : UserAuthenticationFailureHandler
	@Bean
	UserAuthenticationFailureHandler getAuthenticationFailureHandler() {
		return new UserAuthenticationFailureHandler(userService, userLogService);
	}

	// Process on Authentication Failure : UserAuthenticationFailureHandler
	@Bean
	UserLogoutSuccessHandler getLogoutSuccessHandler() {
		return new UserLogoutSuccessHandler(userService, userLogService);
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userService)
				.passwordEncoder(passwordEncoder());
	}

	
	
}