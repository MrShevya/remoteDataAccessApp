package edu.shev.myApp.config;

import edu.shev.myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // разобраться че за хуйня
public class WebSecurityConfig {

    @Autowired
    private UserService userService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests // включаем авторизацию
                        .requestMatchers("/", "/registration", "/login").permitAll() // разрешаем полный доступ без авторизации
                        .requestMatchers("/main/download/{file_name}").authenticated()
                        .anyRequest().authenticated() // для всех остальных запросов требуем авторизацию

                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin((form) -> form // включаем логин форму
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }




    @Autowired
    public void userDetailsService(
            AuthenticationManagerBuilder builder
    ) throws Exception {
        builder.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
    }