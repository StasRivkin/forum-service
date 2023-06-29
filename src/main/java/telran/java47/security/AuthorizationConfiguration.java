package telran.java47.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import telran.java47.accounting.model.UserRole;

@Configuration
public class AuthorizationConfiguration {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf().disable();
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //cookies closed
        http.authorizeRequests(authorize -> authorize
                .mvcMatchers("/account/register", "/forum/posts/**")
                    .permitAll()
                .mvcMatchers("/account/password")
                    .authenticated()
                .mvcMatchers("/account/user/{login}/role/{role}")
                    .hasRole(UserRole.ADMINISTRATOR.name())
                .mvcMatchers(HttpMethod.PUT, "/account/user/{login}")
                    .access("#login == authentication.name")
                .mvcMatchers(HttpMethod.DELETE, "/account/user/{login}")
                    .access("#login == authentication.name or hasRole(T(telran.java47.accounting.model.UserRole).ADMINISTRATOR)")
                .mvcMatchers(HttpMethod.POST, "/forum/post/{author}")
                    .access("#author == authentication.name")
                .mvcMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}")
                    .access("#author == authentication.name")
                .mvcMatchers(HttpMethod.PUT, "/forum/post/{id}")
                    .access("@customWebSecurity.checkPostAuthor(#id, authentication.name)")
                .mvcMatchers(HttpMethod.DELETE, "/forum/post/{id}")
                    .access("@customWebSecurity.checkPostAuthor(#id, authentication.name) or hasRole(T(telran.java47.accounting.model.UserRole).MODERATOR)")
                .mvcMatchers("/account/**")
                    .access("@customWebSecurity.checkPasswordExp(authentication.name)")
                .anyRequest()
                        .authenticated()


        );

        return http.build();
    }
}
