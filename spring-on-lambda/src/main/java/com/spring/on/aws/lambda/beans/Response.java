package com.spring.on.aws.lambda.beans;

public class Response {

	private int code;
	private String message;
	private Object data;

	public int getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Response [code=" + code + ", message=" + message + ", data=" + data + "]";
	}

}
