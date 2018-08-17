package com.zkext.agent.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.log.Log;

/**
 * 全局Handler，设置一些通用功能
 * 描述：主要是一些全局变量的设置，再就是日志记录开始和结束操作
 */
public class GlobalHandler extends Handler {
	
	private static final Log log = Log.getLog(GlobalHandler.class);

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		if(log.isDebugEnabled()) {
			log.debug("com.zkext.agent.handler.GlobalHandler::target="+target);
			System.out.println("request.getRequestURI()="+request.getRequestURI());
			System.out.println("request.getServletPath()="+request.getServletPath());
		}
		
		String ctx = request.getContextPath();
		request.setAttribute("ctx", ctx);//设置全局上下文

		//允许跨域请求
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
		response.setHeader("Allow", "POST, GET, OPTIONS, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Accept, Accept-Language, Cache-Control, Connection, Host, Pragma, Sec-Metadata, Upgrade-Insecure-Requests, User-Agent");

	    next.handle(target, request, response, isHandled);
	    if(log.isDebugEnabled()) {	    	
	    	log.debug("com.zkext.agent.handler.GlobalHandler请求处理完毕");
	    }
	}
	
}
