package com.jinyue.cixiu.common.filter;

import cn.hutool.core.util.StrUtil;
import com.jinyue.cixiu.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 获取Authorization头
        String authHeader = request.getHeader("Authorization");

        // 解析Token
        if (StrUtil.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // 验证Token
                if (jwtUtil.validateToken(token)) {
                    // 获取用户ID
                    Long userId = jwtUtil.getUserIdFromToken(token);

                    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 创建认证对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 设置到Security上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 将userId设置到request attribute中，方便Controller使用
                        request.setAttribute("userId", userId);
                    }
                }
            } catch (Exception e) {
                log.error("JWT认证失败: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
