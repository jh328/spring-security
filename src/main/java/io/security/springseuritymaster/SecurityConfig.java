package io.security.springseuritymaster;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    /**
     * defaultSuccessUrl 옵션 alwaysUse 기본값은 false
     * true 설정을 하게 되면 지정한 경로로 간다.
     * true 아니면 경우에 따라서 처음에 시도한 경로로 간다.
     * 인증을 받기 전에 어딘가 요청ㅇ르 했고,그 요청이 막혀서 인증을 성공을 하면
     * 이전에 요청한 경로로 가게 되는거다.
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .formLogin(form -> form
//                        .loginPage("/loginPage")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/failed")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        /*.
                        어떤 핸들러인지 인터페이스 이름을 알아야 되서 익명 클래스로 했는데 람다로 변경
                        람다로 변경을 하는 단축키 option + enter
                        successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                System.out.println("authentication = " + authentication);
                                response.sendRedirect("/home");
                            }
                        })*/
                        .successHandler((request, response, authentication) -> {
                            System.out.println("authentication = " + authentication);
                            response.sendRedirect("/home");
                        })
                        /*.failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                System.out.println("exception " + exception.getMessage());
                                response.sendRedirect("/login");
                            }
                        })*/
                        /**
                         * 예외가 발생 했을 경우 실패하게 되니깐 예외 정보가 넘어온다.
                         * 실패 했을 경우 일반적으로 로그인 페이지로 이동하는게 일반적이다.
                         * 로그인 페이지, url, faile 인증을 하지 못해도 접근이 가능해야 하기 때문에
                         * .permitAll 옵션 설정을 한다.
                         * */
                        .failureHandler((request, response, exception) -> {
                            System.out.println("exception " + exception.getMessage());
                            response.sendRedirect("/login");
                        })
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }
}
