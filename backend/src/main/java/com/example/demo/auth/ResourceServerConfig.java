package com.example.demo.auth;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /*SEGURIDAD*/

    // ASIGNA QUIEN PUEDE VER, CREAR, BORRAR
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/clientes", "/api/uploads/img/**", "/images/**").permitAll()
                .antMatchers("/api/clientes/{id}").permitAll()
                .antMatchers("/api/facturas/**").permitAll()
                .anyRequest().authenticated()
                .and().cors().configurationSource(corsConfigurationSource());
                /*// permisos a todos
                .antMatchers(HttpMethod.GET, "api/clientes/{id}").hasAnyRole("USER", "ADMIN")// ususarios que puede ver clientes concretos
                .antMatchers(HttpMethod.POST, "api/clientes/{id}").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "api/clientes/{id}").hasRole("ADMIN") // solo el admin puede crear
                .antMatchers( "api/clientes/**").hasRole("ADMIN") // solo el admin puede borrar, cualquier ruta hacia delante requiere permisos adminsitrador
                .anyRequest().authenticated()
                ;
                 */

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("*"));  // "GET", "POST", "PUT", "DELETE", "OPTIONS"
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }


}
