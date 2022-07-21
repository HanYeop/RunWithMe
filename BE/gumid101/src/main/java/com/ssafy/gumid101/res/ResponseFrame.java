package com.ssafy.gumid101.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFrame<T> {

	private boolean isSuccess;
	private T data;
	private int count;

}
