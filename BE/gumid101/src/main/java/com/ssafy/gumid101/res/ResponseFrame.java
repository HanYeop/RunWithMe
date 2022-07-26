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

	private Boolean success;
	private T data;
	private int count;
	private String msg;

	public static ResponseFrame<?> of(boolean success,String message){
		ResponseFrame<?> frame = new ResponseFrame<>();
		frame.setCount(0);
		frame.setData(null);
		frame.setMsg(message);
		frame.setSuccess(success);
		return  frame;
	}
	
	public static <T> ResponseFrame<T> of(T data,int count,String message){
		ResponseFrame<T> frame = new ResponseFrame<>();
		frame.setCount(count);
		frame.setData(data);
		frame.setMsg(message);
		frame.setSuccess(true);
		return  frame;
	}
}
