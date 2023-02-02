package com.app.raghu.config;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests((requests) -> requests
				.antMatchers("/", "/home").permitAll()
				.antMatchers("/customer").hasAuthority("CUSTOMER")
				.antMatchers("/admin").hasAuthority("ADMIN")
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout((logout) -> logout.permitAll());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(DataSource datasource) {
		UserDetails user1 = User.builder()
				.username("sam")
				.password("$2a$10$XNoDWrVtlgq8Ghv9sq5KKuh/k4r65PrIGM4IBFrVdB9qwF0vR77V.")
				.authorities("ADMIN")
				.build();
		
		UserDetails user2 = User.builder()
				.username("ram")
				.password("$2a$10$BRM4q3O1NeilPgtreNXQlOp9Ffx/iFl8WOXHWg9MAWcsMuo7wzNFG")
				.authorities("CUSTOMER")
				.build();

		JdbcUserDetailsManager users = new JdbcUserDetailsManager(datasource);
		users.createUser(user1);
		users.createUser(user2);
		return users;
	}
}
