/*
 * WebimVisitorManager.java
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
package webim.service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import webim.model.WebimVisitor;

/**
 * Webim访客管理类
 * 
 * @author Feng Lee <feng.lee at nextalk.im>
 *
 */
@Service("webimVisitorManager")
public class WebimVisitorManager {
	
	final static String COOKIE_VID = "_webim_visitor_id";
	
	@Resource(name="webimModel")
	private WebimModel model;
	
	public WebimVisitorManager() {}

	/**
	 * 是否访客ID
	 * 
	 * @return
	 */
	public boolean isVid(String id) {
		return id.startsWith("vid:");
	}

	/**
	 * 获取当前访客对象，如不存在创建。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public WebimVisitor endpoint(HttpServletRequest request,
			HttpServletResponse response) {
		String vid = findVid(request);
		if(vid == null) {
			vid = createVid(request, response);
		}
		WebimVisitor visitor = this.model.findVisitor(vid);
		if(visitor == null) {
			visitor = this.model.createVisitor(vid, request);
		}
		return visitor;
	}

	private String findVid(HttpServletRequest request) {
		Cookie cookie = findCookie(request, COOKIE_VID);
		if(cookie != null) {
			return cookie.getValue();
		}
		return null;
	}

	private Cookie findCookie(HttpServletRequest request, String cookieId) {
		Cookie[] cookies = request.getCookies();
		for(Cookie c : cookies) {
			if(c.getName().equals(cookieId)) {
				return c;
			}
		}
		return null;
	}

	private String createVid(HttpServletRequest request, HttpServletResponse response) {
		//TODO: 
		String vid = String.valueOf(System.currentTimeMillis());
		createCookie(response, vid);
		return vid;
	}

	private void createCookie(HttpServletResponse response, String vid) {
		Cookie cookie = new Cookie(COOKIE_VID, vid);
		response.addCookie(cookie);
	}

}
