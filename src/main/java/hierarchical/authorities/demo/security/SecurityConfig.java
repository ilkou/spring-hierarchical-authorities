package hierarchical.authorities.demo.security;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import java.util.Set;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
class SecurityConfig {

    private final Enforcer enforcer;

    public SecurityConfig(Enforcer enforcer) {
        this.enforcer = enforcer;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                .addFilterBefore(new CasbinFilter(enforcer), AuthorizationFilter.class)
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        CustomUserDetails user1 = new CustomUserDetails("user",
                "{noop}password",
                Set.of(Authorities.CREATE_USER));
        CustomUserDetails admin = new CustomUserDetails("admin",
                "{noop}password",
                Set.of(Authorities.CREATE_USER, Authorities.READ_GROUPS));

        manager.createUser(user1);
        manager.createUser(admin);

        return manager;
    }

}
