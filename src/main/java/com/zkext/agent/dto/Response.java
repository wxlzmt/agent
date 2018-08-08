package com.zkext.agent.dto;

import java.io.Serializable;

public class Response implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success = false;

	private Object data = null;

	private String msg = null;

	public Response() {
		// 由框架构造实体的时候可能需要无参构造方法
	}

	public Response(boolean success, Object data, String msg) {
		this.success = success;
		this.data = data;
		this.msg = msg;
	}

	public Response(boolean success, Object data) {
		this.success = success;
		this.data = data;
	}

	public Response(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public Response(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
