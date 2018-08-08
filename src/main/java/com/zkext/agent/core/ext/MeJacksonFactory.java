package com.zkext.agent.core.ext;

import com.jfinal.json.IJsonFactory;
import com.jfinal.json.Json;

public class MeJacksonFactory implements IJsonFactory {

	private static final MeJacksonFactory me = new MeJacksonFactory();

	public static MeJacksonFactory me() {
		return me;
	}

	public Json getJson() {
		return new MeJackson();
	}
}
