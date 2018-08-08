package com.zkext.agent.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


/**
 * 简单Javabean , 2个属性: 键,值;
 * 
 * @ClassName: KVBean
 * @Description: key-value bean
 *
 */
public class KVBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String val;

	public KVBean() {
		//由框架构造实体的时候可能需要无参构造方法
	}

	public KVBean(String key, String val) {
		this.key = key;
		this.val = val;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KVBean) {
			KVBean kv = (KVBean)obj;
			if(StringUtils.equals(kv.getKey(), this.key) && StringUtils.equals(kv.getVal(), this.val)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected KVBean clone() throws CloneNotSupportedException {
		return new KVBean(this.key, this.val);
	}

}
