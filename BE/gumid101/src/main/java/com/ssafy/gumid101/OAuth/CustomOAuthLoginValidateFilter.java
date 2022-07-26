package com.ssafy.gumid101.OAuth;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.google.api.client.util.Strings;
import com.ssafy.gumid101.OAuth.custom.validate.GoogleTokenValidate;
import com.ssafy.gumid101.OAuth.custom.validate.KaKaoTokenValiate;
import com.ssafy.gumid101.OAuth.custom.validate.NaverTokenValidate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthLoginValidateFilter extends GenericFilterBean {

	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final GoogleTokenValidate googleTokenValidate = new GoogleTokenValidate();
    private final NaverTokenValidate naverTokenValidate = new NaverTokenValidate();
    private final KaKaoTokenValiate kakaoTokenValidate = new KaKaoTokenValiate();
    
	private final SimpleUrlAuthenticationSuccessHandler successHandler;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		
		try {

			boolean result = doFilter((HttpServletRequest) request, (HttpServletResponse) response);

			if (result ) {
				successHandler.onAuthenticationSuccess((HttpServletRequest) request, (HttpServletResponse) response,
						SecurityContextHolder.getContext().getAuthentication());
			}else {
				chain.doFilter(request, response);
			}
		} catch (Exception e) {

			chain.doFilter(request, response);
		}
		
	}

	private boolean doFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uri = request.getServletPath();
		if (!pathMatcher.match("/login/oauth2/code/*", uri)) {
			return false;
		}


		String idToken = request.getParameter("code");

		if (Strings.isNullOrEmpty(idToken)) {
			// 토큰 검사 요청했으면서 토큰이 없음
			return false;
		}

		log.info("Oauth token 검증 시작");
		String registrationId = uri.substring(uri.lastIndexOf("/") + 1);
		SecurityContextHolder.getContext().getAuthentication();
		
		Map<String, Object> userMap=null;
		try {
			
		
		switch (registrationId) {
		case "google":
			userMap = googleTokenValidate.validate(idToken);

			break;
		case "naver":
			userMap = naverTokenValidate.validate(idToken);
			break;
		case "kakao":
			userMap = kakaoTokenValidate.validate(idToken);
			break;
		default:
			break;
		}
		}catch (Exception e) {
			log.warn("토큰 검증 중 에러 : {}",e.getMessage());
			e.printStackTrace();
		}
		
		if(userMap ==null && userMap.size() == 0) {
			
			
			return false;
			//검증에 실패하였을 경우
			
		}

		OAuth2User oAuth2User = new DefaultOAuth2User(null, userMap, "email");
		OAuth2AuthenticationToken oauthToken = new OAuth2AuthenticationToken(oAuth2User, null, registrationId);
		
		SecurityContextHolder.getContext().setAuthentication(oauthToken);
		
		
		return true;

	}

}
