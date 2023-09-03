package Go.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

 /*  @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");
        return http.build();
    }
    */
}