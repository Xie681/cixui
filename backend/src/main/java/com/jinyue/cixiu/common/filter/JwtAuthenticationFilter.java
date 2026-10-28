
package com.jinyue.cixiu.common.filter;

import cn.hutool.core.util.StrUtil;
import com.jinyue.cixiu.common.utils.JwtUtil;
import com.jinyue.cixiu.module.user.entity.User;
import com.jinyue.cixiu.module.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (StrUtil.isNotBlank(authHeader)
                && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.getUserIdFromToken(token);

                    if (userId != null &&
                            SecurityContextHolder.getContext()
                                    .getAuthentication() == null) {

                        User user = userService.getUserById(userId);

                        if (Integer.valueOf(1).equals(user.getStatus())
                                && user.getRole() != null) {

                            var authorities = List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + user.getRole().name()
                                    )
                            );

                            var authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userId,
                                            null,
                                            authorities
                                    );

                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource()
                                            .buildDetails(request)
                            );

                            SecurityContextHolder.getContext()
                                    .setAuthentication(authentication);

                            request.setAttribute("userId", userId);
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("JWT认证被拒绝: {}",
                        e.getClass().getSimpleName());
            }
        }

        filterChain.doFilter(request, response);
    }
}
