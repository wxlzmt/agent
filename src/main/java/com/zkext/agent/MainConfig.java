package com.zkext.agent;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.template.Engine;
import com.zkext.agent.controller.MainController;
import com.zkext.agent.core.ext.MeJacksonFactory;
import com.zkext.agent.core.util.Constant;
import com.zkext.agent.web.GlobalHandler;

/**
 * @ClassName: MainConfig
 * @Description: JFinal 核心配置文件
 */
public class MainConfig extends JFinalConfig {

	public static void main(String[] args) {
		JFinal.start("webapp", 8081, "/", 5);
	}

	/**
	 * 配置JFinal常量
	 */
	@Override
	public void configConstant(Constants me) {
		me.setEncoding(Constant.DEFAULT_ENCODING);
		// 设置当前是否为开发模式
		me.setDevMode(Constant.DEV_MODE);
		// 设置默认上传文件保存路径 getFile等使用
		me.setBaseUploadPath(Constant.UPLOAD_PATH);
		// 设置上传最大限制尺寸
		me.setMaxPostSize(1024 * 1024 * 10);

		// 使用jackson, 可以更好的支持反序列化,
		// final默认处理键不用驼峰规则而是用下划线,
		// 当使用jackson之后,处理键用的是驼峰规则;
		me.setJsonFactory(new MeJacksonFactory());

		me.setErrorView(401, Constant.URL_401);
		me.setErrorView(403, Constant.URL_403);
		me.setErrorView(404, Constant.URL_404);
		me.setErrorView(500, Constant.URL_500);
		me.setErrorView(400, Constant.URL_400);
		me.setErrorView(405, Constant.URL_405);
		
		// 获取GroupTemplate ,可以设置共享变量等操作
		// @SuppressWarnings("unused")
		// GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate ;
		// 修改模板文件后,立即生效的意思
		me.setFreeMarkerTemplateUpdateDelay(0);
	}

	/**
	 * 配置JFinal插件 数据库连接池 ORM 缓存等插件 自定义插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		// 不需要数据库
	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		// 不需要拦截器
	}

	/**
	 * 配置全局处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		//me.add(new ContextPathHandler("ctx"));
		me.add(new GlobalHandler());
	}

	/**
	 * 配置JFinal路由映射
	 */
	@Override
	public void configRoute(Routes me) {
		me.setBaseViewPath("/WEB-INF/view");
		me.add("/", MainController.class);
	}

	@Override
	public void configEngine(Engine me) {
		me.setDatePattern(Constant.DEFAULT_DATE_PATTERN);
	}

	@Override
	public void afterJFinalStart() {
		// nothing to do
	}
}
