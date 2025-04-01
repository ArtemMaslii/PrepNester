//package com.project.prepnester.util.jwt;
//
//import com.project.prepnester.model.UserDetails;
//import com.project.prepnester.service.UserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Component
//@AllArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//  private final JwtTokenProvider jwtTokenProvider;
//
//  private final UserDetailsService userDetailsService;
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request,
//      HttpServletResponse response,
//      FilterChain filterChain) throws ServletException, IOException {
//
//    String token = getTokenFromRequest(request);
//
//    if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
//      String email = jwtTokenProvider.getEmail(token);
//
//      UserDetails userDetails = userDetailsService.getUserDetails(email);
//
//      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//          userDetails,
//          null,
//          userDetails.
//      );
//
//      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//    }
//
//    filterChain.doFilter(request, response);
//  }
//
//  private String getTokenFromRequest(HttpServletRequest request) {
//    String bearerToken = request.getHeader("Authorization");
//
//    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//      return bearerToken.substring(7, bearerToken.length());
//    }
//
//    return null;
//  }
//}