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

/**
 * 测试的配置文件，配置消息服务器的地址、端口、通信domain、apikey。<br>
 * 
 * 正式项目配置应写在XML文件或者数据库中。
 *      
 * @author Ery Lee <ery.lee @ gmail.com>
 * @since 1.0
 */
public class WebimConfig {


	/**
	 * 是否开启
	 */
	public static boolean ISOPEN = true;
	
	
    public static String VERSION = "1.0";
    /**
     * 站点域名
     */
    public static String DOMAIN = "localhost";
    /**
     * 通信APIKEY
     */
    public static String APIKEY = "public";
    
    /**
     * 消息服务器地址
     */
    public static String HOST = "nextalk.im";
    
    /**
     * 消息服务器端口
     */
    public static int PORT = 8000;

    /**
     * 界面Theme
     */
    public static String THEME = "base";
    
    /**
     * 本地语言
     */
    public static String LOCAL = "zh-CN";
    
    /**
     * 表情库
     */
    public static String EMOT = "default";

    /**
     * 工具条透明度
     */
    public static int OPACITY = 80;
    
    /**
     * 群组聊天
     */
    public static boolean ENABLE_ROOM = true;

    /**
     * 显示通知按钮
     */
    public static boolean ENABALE_NOTI = true;

    /**
     * 支持快捷工具栏
     */
    public static boolean ENABLE_SHORTCUT = false;
    
    /**
     * 支持聊天链接
     */
	public static boolean ENABLE_CHATLINK = false;
    
    /**
     * 显示菜单栏
     */
    public static boolean ENABLE_MENU = false;
    
    /**
     * 显示不在线好友
     */
    public static boolean SHOW_UNAVAILABLE = true;
    
    /**
     * 支持访客
     */
    public static boolean VISITOR = true;
}

