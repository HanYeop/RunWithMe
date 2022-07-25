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
import com.ssafy.gumid101.res.ResponseFrame;
import com.ssafy.gumid101.user.Role;
import com.ssafy.gumid101.user.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		hRes.setCharacterEncoding("UTF-8");
		hRes.setContentType("application/json;charset=utf-8");

		String userSeq = validateTokenOrOccurFailResponse(hRes, token);

		if (userSeq != null) {

			try {
				Long.parseLong(userSeq);

				UserDto userDto = userRepo.findById(Long.parseLong(userSeq)).map((user) -> {
					return UserDto.of(user);
				}).orElse(null);

				Authentication auth = getAuthentication(userDto);
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (Exception e) {
				log.debug("토큰은 있으나 등록되지 않은 사용자");

				ArrayList<SimpleGrantedAuthority> athorities = new ArrayList<>();
				athorities.add(new SimpleGrantedAuthority(Role.TEMP.getKey()));
				
				UserDto tempUser = UserDto.builder().email(JwtUtils.getUsername(token,"userEmail")).build();
				
				SecurityContextHolder.getContext().
				setAuthentication(new UsernamePasswordAuthenticationToken(tempUser, "", athorities));
			}

		} else {
			log.debug("토큰 부적합");
			return;
		}

		chain.doFilter(request, response);
	}

	private String validateTokenOrOccurFailResponse(HttpServletResponse hRes, String token) throws IOException {
		String userSeq = null;

		try {
			userSeq = JwtUtils.getUsername(token,"userSeq");

		} catch (Exception e) {
			logger.info("토큰 에러:" + e.getMessage());

			hRes.setStatus(401);
			
			hRes.getWriter().write(mapper.writeValueAsString(ResponseFrame.of(false,e.getMessage())));

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