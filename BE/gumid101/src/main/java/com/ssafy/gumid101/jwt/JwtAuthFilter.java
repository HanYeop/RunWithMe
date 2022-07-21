package com.ssafy.gumid101.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

	private final UserRepository userRepo;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String token = ((HttpServletRequest) request).getHeader(JwtProperties.JWT_ACESS_NAME);
		
		if(!StringUtils.hasLength(token)) {
			String userSeq = JwtUtils.getUsername(token);
			
			
			UserDto userDto = userRepo.findById(userSeq).map((user) -> {
				
				return  UserDto.of(user);
			}).orElse(null);

			
			
			if (userDto != null) {

				Authentication auth = getAuthentication(userDto);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		

		chain.doFilter(request, response);
	}

	public Authentication getAuthentication(UserDto member) {
		
		ArrayList<SimpleGrantedAuthority> athorities = new ArrayList<>();
		
		if("ROLE_USER".equals( member.getRole().getKey())) {
			athorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}else if("ROLE_ADMIN".equals(member.getRole().getKey())) {
			athorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			athorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		
		return new UsernamePasswordAuthenticationToken(member, "",
				athorities);
	}
}