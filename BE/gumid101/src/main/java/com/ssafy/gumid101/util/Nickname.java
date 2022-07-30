package com.ssafy.gumid101.util;

public class Nickname {
	public static boolean nickOk(String nick) {
		if (nick == null) {
			return false;
		}
		int sizeCount = 0;
		for (int i = 0; i < nick.length() && sizeCount <= 16; i++) {
			char c = nick.charAt(i);
			if ('0' <= c && c <= '9') {
				sizeCount++;
			} else if ('a' <= c && c <= 'z') {
				sizeCount++;
			} else if ('A' <= c && c <= 'Z') {
				sizeCount++;
			} else if ('가' <= c && c <= '힣') {
				sizeCount += 2;
			} else if ('ㄱ' <= c && c <= 'ㅎ') {
				sizeCount += 2;
			} else {
				// 허용되지 않는 문자 나온 경우
				return false;
			}
		}
		if (sizeCount < 4 || sizeCount > 16) {
			// 사이즈 조건 만족을 하지 못한 경우
			return false;
		}
		// 모든 조건을 만족한 경우
		return true;
	}
}
