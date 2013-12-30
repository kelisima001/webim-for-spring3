/*
 * WebimGroup.java
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
 * Webim群组对象
 * 
 * @author Ery Lee <ery.lee at gmail.com>
 * @since 1.0 
 */
public class WebimGroup {

	/**
	 * 群组id
	 */
	private String id;
	
	/**
	 * 群组昵称
	 */
	private String nick;
	
	/**
	 * 群组首页
	 */
	private String url = "";
	
	/**
	 * 在线成员统计
	 */
	private int count = 0;
	
	/**
	 * 全部成员统计
	 */
	//private int all_count = 0;
	
	/**
	 * 群组图片
	 */
	private String pic_url = "";

	/**
	 * 是否临时讨论组
	 */
	private boolean temporary = false;
	
	/**
	 * 创建群组实例
	 * 
	 * @param id 群组id
	 * @param nick 群组昵称
	 */
	public WebimGroup(String id, String nick) {
		this.id = id;
		this.nick = nick;
	}

	/**
	 * 群组id
	 * 
	 * @return 群组id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 群组昵称
	 * @return 群组昵称
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * 设置群组昵称
	 * @param nick 群组昵称
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * 群组首页
	 * @return 群组首页
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置群组首页
	 * @param url 群组首页
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 群组成员统计
	 * @return 群组成员统计
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 设置群组成员统计
	 * @param count 群组成员统计
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 群组在线成员统计
	 * @return 在线成员统计
	 */
	//public int getAll_count() {
	//	return all_count;
	//}

	/**
	 * 设置群组在线成员统计
	 * 
	 * @param all_count 在线成员统计
	 */
	//public void setAll_count(int all_count) {
	//	this.all_count = all_count;
	//}

	/**
	 * 群组图片
	 * 
	 * @return 群组图片
	 */
	public String getPic_url() {
		return pic_url;
	}

	/**
	 * 设置群组图片
	 * 
	 * @param pic_url 群组图片
	 */
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public String toString() {
		return String.format("Group(id=%s, nick=%s, count=%d", id, nick, count);
	}

}
