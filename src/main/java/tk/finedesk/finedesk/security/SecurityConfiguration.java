package tk.finedesk.finedesk.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tk.finedesk.finedesk.repositories.UserRepository;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


/**
 * @author asukiasyan@universe.dart.spb
 * @since 11/jan/2022
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final List<String> permittedUrls = List.of("/verification/confirm", "/login", "/user/register");
    private final UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
        // ignore swagger
        web.ignoring().mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs");

        web.ignoring().antMatchers("/auth/login");

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/verification/confirm/**", "/auth/login", "/user/register/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access_denied")
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .addFilterAfter(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

//        http.addFilter(new AuthenticationFilter(authenticationManagerBean()));

//        http.csrf().disable()
//                .authorizeRequests().anyRequest().permitAll();
//        http.sessionManagement().sessionCreationPolicy(STATELESS);
//        http.addFilter(new AuthenticationFilter(authenticationManagerBean()));

    }

    @Bean
    public CustomAuthenticationFilter authenticationFilter() {
        return new CustomAuthenticationFilter(userRepository);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
