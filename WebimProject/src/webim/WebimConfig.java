/*
 * WebimConfig.java
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package webim;

import java.util.Map;

import java.util.HashMap;

/**
 *
 * 测试的配置文件，配置消息服务器的地址、端口、通信DOMAIN、APIKEY。<br>
 * 
 * TODO: 正式项目配置应写在XML文件或者数据库中。
 *
 * @author erylee <ery.lee at gmail.com>
 *
 */
public class WebimConfig {
	
	/**
	 * Webim库版本
	 */
	public static final String VERSION = "5.5";

	/**
	 * 是否开启
	 */
	public static final boolean ISOPEN = true;

	/**
	 * 站点域名
	 */
	public static final String DOMAIN = "localhost";

	/**
	 * 通信APIKEY
	 */
	public static final String APIKEY = "public";

	/**
	 * 消息服务器地址
	 */
	public static final String HOST = "t.nextalk.im";

	/**
	 * 消息服务器端口
	 */
	public static final int PORT = 8000;

	/**
	 * 界面Theme
	 */
	public static final String THEME = "base";

	/**
	 * 本地语言
	 */
	public static final String LOCAL = "zh-CN";

	/**
	 * 表情库
	 */
	public static final String EMOT = "default";

	/**
	 * 工具条透明度
	 */
	public static final int OPACITY = 80;

	/**
	 * 群组聊天
	 */
	public static final boolean ENABLE_ROOM = true;

	/**
	 * 临时讨论组
	 */
	public static final boolean ENABLE_DISCUSSION = true;
	
	/**
	 * 显示通知按钮
	 */
	public static final boolean ENABALE_NOTI = true;

	/**
	 * 支持快捷工具栏
	 */
	public static final boolean ENABLE_SHORTCUT = false;

	/**
	 * 支持聊天链接
	 */
	public static final boolean ENABLE_CHATLINK = true;

	/**
	 * 显示菜单栏
	 */
	public static final boolean ENABLE_MENU = false;

	/**
	 * 显示不在线好友
	 */
	public static final boolean SHOW_UNAVAILABLE = true;

	/**
	 * 支持访客
	 */
	public static final boolean ENABLE_VISITOR = false;

	/**
	 * 支持文件上传
	 */
	public static final boolean ENABLE_UPLOAD = false;
	
	private Map<String, Object> data;

	public WebimConfig() {
		data = new HashMap<String, Object>();
		data.put("version", VERSION);
		data.put("isopen", Boolean.valueOf(ISOPEN));
		data.put("domain", DOMAIN);
		data.put("apikey", APIKEY);
		data.put("host", HOST);
		data.put("port", Integer.valueOf(PORT));
		data.put("theme", THEME);
		data.put("local", LOCAL);
		data.put("emot", EMOT);
		data.put("opacity", Integer.valueOf(OPACITY));
		data.put("enable_room", Boolean.valueOf(ENABLE_ROOM));
		data.put("enable_discussion", Boolean.valueOf(ENABLE_DISCUSSION));
		data.put("enable_noti", Boolean.valueOf(ENABALE_NOTI));
		data.put("enable_shortcut", Boolean.valueOf(ENABLE_SHORTCUT));
		data.put("enable_chatlink", Boolean.valueOf(ENABLE_CHATLINK));
		data.put("enable_menu", Boolean.valueOf(ENABLE_MENU));
		data.put("show_unavailable", Boolean.valueOf(SHOW_UNAVAILABLE));
		data.put("enable_visitor", Boolean.valueOf(ENABLE_VISITOR));
		data.put("enable_upload", Boolean.valueOf(ENABLE_UPLOAD));
	}

	/**
	 * 读取配置
	 * 
	 * @param key
	 * 
	 * @return value
	 */
	public Object get(String key) {
		return data.get(key);
	}
	
	public boolean getBoolean(String key) {
		return ((Boolean)get(key)).booleanValue();
	}
	
	public Map<String, Object> all() {
		return this.data;
	}
	
}
