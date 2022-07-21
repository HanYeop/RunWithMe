package com.ssafy.gumid101.jwt;

/**
 * JWT 기본 설정값
 */
public class JwtProperties {
    public static final int EXPIRATION_TIME = 600000; // 10분
    //가져온 코드는 COOKIE_NAME으로 되있었지만 , 우리는 쿠기를 사용하지 않고 ,
    //보낼 때 바디에 실엇 보낸다 . 받을 때는 헤더에서 가져온다. 따라서 이름 바꾸자 . 
    public static final String JWT_ACESS_NAME = "JWT-AUTHENTICATION";
}