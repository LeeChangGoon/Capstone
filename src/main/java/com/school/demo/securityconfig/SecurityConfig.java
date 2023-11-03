package com.school.demo.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.school.demo.userdetails.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

	@Bean 
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((authorizeRequests) -> { //http 요청 접근 제어규칙 
				authorizeRequests.requestMatchers("/user/**").authenticated(); 
				authorizeRequests.requestMatchers("/admin/**").hasRole("admin"); 
				authorizeRequests.anyRequest().permitAll(); 
			})
	        .formLogin((formLogin) -> formLogin.loginPage("/login").defaultSuccessUrl("/user/home", true))
			.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login"));
		return http.build(); 
	}
}

