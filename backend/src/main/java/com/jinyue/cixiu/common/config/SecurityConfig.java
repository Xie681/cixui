
package com.jinyue.cixiu.common.config;

import com.jinyue.cixiu.common.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )
            .authorizeHttpRequests(auth -> auth

                // CORS预检请求
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()

                // 健康检查
                .requestMatchers("/api/health", "/health")
                .permitAll()

                // 登录与注册
                .requestMatchers("/api/auth/**")
                .permitAll()

                // 公开的基础商品配置
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/customize/styles",
                    "/api/customize/fabrics",
                    "/api/customize/threads"
                ).permitAll()

                // 公开报价计算
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/customize/calculate-price"
                ).permitAll()

                // 知识库管理仅允许管理员操作
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/knowledge/upload"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/knowledge/documents/**"
                ).hasRole("ADMIN")

                // 预留管理员专用接口
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")

                // 其余接口必须登录
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                    (request, response, e) ->
                        response.sendError(401)
                )
                .accessDeniedHandler(
                    (request, response, e) ->
                        response.sendError(403)
                )
            )
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
