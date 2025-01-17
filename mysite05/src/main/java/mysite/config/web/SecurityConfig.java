package mysite.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import mysite.repository.UserRepository;
import mysite.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {	
    	
    	http
    	.formLogin((formLogin) -> {
    		formLogin.loginPage("/user/login")
    		.loginProcessingUrl("/user/auth")
    		.usernameParameter("email")
    		.passwordParameter("password")
            .defaultSuccessUrl("/")
            .failureUrl("/user/login?result=fail");
    	})
    	.authorizeHttpRequests((authorizeRequests) -> {
    		// Access Control List(ACL)
    		authorizeRequests
    			.requestMatchers(new RegexRequestMatcher("^/user/update$", null))
    			.authenticated()
    			
    			// 나머지는 통과
    			.anyRequest().permitAll();
    	});
    	
    	return http.build();
    }
	
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncode) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        
        authenticationProvider.setPasswordEncoder(passwordEncode);
        authenticationProvider.setUserDetailsService(userDetailsService);
        
        return new ProviderManager(authenticationProvider);
    }

     @Bean
     public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4); // 4 ~ 31
     }

     @Bean
     public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
     }    
}
