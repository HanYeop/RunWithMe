package com.ssafy.gumid101.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.user.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

	private final JwtUtilsService JwtUtils;
	private final UserRepository userRepo;

	private final ObjectMapper mapper;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String token = ((HttpServletRequest) request).getHeader(JwtProperties.JWT_ACESS_NAME);
		HttpServletResponse hRes = (HttpServletResponse) response;

		if (!StringUtils.hasLength(token)) {
			hRes.setCharacterEncoding("UTF-8");
			hRes.setContentType("application/json;charset=utf-8");

			String userSeq = validateTokenOrOccurFailResponse(hRes, token);

			if (userSeq != null) {

				UserDto userDto = userRepo.findById(userSeq).map((user) -> {
					return UserDto.of(user);
				}).orElse(null);

				if (userDto != null) {

					Authentication auth = getAuthentication(userDto);
					SecurityContextHolder.getContext().setAuthentication(auth);
				} else {
					logger.info("토큰은 있으나 등록되지 않은 사용자");
					return;
				}
			}
		}

		chain.doFilter(request, response);
	}

	private String validateTokenOrOccurFailResponse(HttpServletResponse hRes, String token) throws IOException {
		String userSeq = null;

		try {
			userSeq = JwtUtils.getUsername(token);

		} catch (Exception e) {
			logger.info("토큰 에러:" + e.getMessage());

			hRes.setStatus(401);
			Map<String, String> map = new HashMap<>();
			map.put("msg", e.getMessage());
			hRes.getWriter().write(mapper.writeValueAsString(map));

			hRes.flushBuffer();
		}
		// UnsupportedJwtException - if the claimsJws argument does not represent an
		// Claims
		// JWSMalformedJwtException - if the claimsJws string is not a valid JWS
		// SignatureException - if the claimsJws JWS signature validation
		// failsExpiredJwtException - if the specified JWT is a Claims JWT and the
		// Claims has an expiration timebefore the time this method is invoked.
		// IllegalArgumentException - if the claimsJws string is null or empty or only
		// whitespace

		return userSeq;
	}

	public Authentication getAuthentication(UserDto member) {

		ArrayList<SimpleGrantedAuthority> athorities = new ArrayList<>();

		if ("ROLE_USER".equals(member.getRole().getKey())) {
			athorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		} else if ("ROLE_ADMIN".equals(member.getRole().getKey())) {
			athorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			athorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		return new UsernamePasswordAuthenticationToken(member, "", athorities);
	}
}