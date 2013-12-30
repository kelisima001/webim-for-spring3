/*
 * WebimEndpoint.java
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
 * 路由端点(用户)。即时消息路由端点，标识一个用户、访客、服务。
 * 
 * @author Ery Lee <ery.lee at gmail.com>
 * @since 1.0
 */
public class WebimEndpoint {

	/**
	 * 端点标识，传递给消息服务器时，作为name(历史原因)
	 */
	private String id;

	/**
	 *端点(用户)昵称
	 */
	private String nick;
	
	/**
	 * 现场
	 */
	private String show = "unavailable";
	
	/**
	 * 状态最近更新时间
	 */
	private String status_time = "";
	
	/**
	 * 端点(用户)主页
	 */
	private String url = "";
	
	/**
	 * 端点(用户)图片
	 */
	private String pic_url = "";
	
	/**
	 * 端点状态
	 */
	private String status = "Offline";

	/**
	 * 创建端点对象
	 * @param id 端点标识(用户名)
	 * @param nick 端点昵称(用户昵称)
	 */
	public WebimEndpoint(String id, String nick)
    {
        this.setId(id);
        this.setNick(nick);
        this.setStatus_time("");
        this.setUrl("");
        this.setPic_url("");
    }

	/**
	 * 读取端点标识，id或name
	 * @return id 端点标识
	 */
	public String getId() {
		return id;
	}

	
	/**
	 * 设置端点
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 读取现场
	 * @return 现场状态
	 */
	public String getShow() {
		return show;
	}

	/**
	 * 设置现场
	 * 
	 * @param show
	 */
	public void setShow(String show) {
		this.show = show;
	}

	/**
	 * 读取状态变更时间
	 * @return 状态变更时间
	 */
	public String getStatus_time() {
		return status_time;
	}

	/**
	 * 设置状态变更时间
	 * 
	 * @param status_time
	 */
	public void setStatus_time(String status_time) {
		this.status_time = status_time;
	}

	/**
	 * 端点(用户)主页
	 * 
	 * @return url 端点(用户)首页
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置端点(用户)首页
	 * 
	 * @param url 端点(用户)首页
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 端点(用户)图片
	 * 
	 * @return 端点(用户)图片
	 */
	public String getPic_url() {
		return pic_url;
	}

	/**
	 * 设置端点(用户)图片
	 * @param pic_url 端点(用户)图片
	 */
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	/**
	 * 端点(用户)状态
	 * @return status 端点(用户)状态
	 */
	public String getStatus() {
		return status;
	}
	

	/**
	 * 设置端点(用户)状态
	 * @param status 用户状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 端点(用户)昵称
	 * 
	 * @return 端点(用户)昵称
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * 设置端点(用户)昵称
	 * 
	 * @param nick 端点(用户)昵称
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	public String toString() {
		return String.format("Endpoint(id=%s, nick=%s, show=%s", id, nick, show);
	}

}
