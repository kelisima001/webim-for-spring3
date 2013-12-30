/*
 * WebimClient.java
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
import java.util.List;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * WebimClient是与消息服务器通信接口，采用JSON/HTTP协议设计，接口包括:<br>
 * <ul>
 * <li>online: 通知消息服务器用户上线</li>
 * <li>online: 通知消息服务器用户下线</li>
 * <li>publish: 向消息服务器转发Presence、Status、Message</li>
 * <li>members: 向消息服务器获取群组当前在线用户表</li>
 * <li>join: 通知消息服务器有用户加入群组</li>
 * <li>leave: 通知消息服务器有用户离开群组</li>
 * </ul>
 * 
 * @author Ery Lee <ery.lee @ gmail.com>
 * @since 1.0
 */
public class WebimClient {

	/**
	 * 协议版本号
	 */
	public static String APIVSN = "v5";

	/**
	 * 当前端点(用户)
	 */
	private WebimEndpoint ep;
	
	/**
	 * 站点域名
	 */
	private String domain;

	/**
	 * 消息服务器通信APIKEY
	 */
	private String apikey;
	
	/**
	 * 消息服务器内部通信地址
	 */
	private String host;

	/**
	 * 消息服务器端口
	 */
	private int port;

	/**
	 * 通信令牌，每online一次，消息服务器返回一个新令牌
	 */
	private String ticket = "";

	/**
	 * 创建Clieng实例
	 * @param ep 当前端点
	 * @param domain 域名
	 * @param apikey APIKEY
	 * @param host 消息服务器地址
	 * @param port 消息服务器端口
	 */
	public WebimClient(WebimEndpoint ep, String domain, String apikey, String host, int port) {
		this.ep = ep;
		this.domain = domain;
		this.apikey = apikey;
		this.host = host;
		this.port = port;
	}

	/**
	 * 设置与消息服务器通信令牌
	 * 
	 * @param ticket 通信令牌
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	/**
	 * 通知消息服务器用户上线
	 * 
	 * @param buddies 用户好友id列表
	 * @param groups 用户群组id列表
	 * 
	 * @return JSONObject 成功返回JSON对象
	 * @throws WebimException 失败返回异常
	 */
	public JSONObject online(List<String> buddies, List<String> groups)
			throws WebimException {
		Map<String, String> data = newData();
		data.put("groups", this.listJoin(",", groups));
		data.put("buddies", this.listJoin(",", buddies));
		data.put("name", ep.getId());
		data.put("nick", ep.getNick());
		data.put("status", ep.getStatus());
		data.put("show", ep.getShow());
		try {
			String body = httpost("/presences/online", data);
			JSONObject json = new JSONObject(body);
			setTicket(json.getString("ticket"));
			return json;
		} catch (WebimException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebimException(500, e.getMessage());
		}
	}

	/**
	 * 域名
	 * 
	 * @return Domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * 端点
	 * @return Endpoint
	 */
	public WebimEndpoint getEndpoint() {
		return ep;
	}
	
	/**
	 * 通知消息服务器下线
	 * 
	 * @return JSONObject "{'status': 'ok'}" or "{'status': 'error', 'message': 'blabla'}"
	 * @throws WebimException
	 */
	public JSONObject offline() throws WebimException {
		Map<String, String> data = newData();
		data.put("ticket", this.ticket);
		try {
			String body = httpost("/presences/offline", data);
			return new JSONObject(body);
		} catch (WebimException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebimException(500, e.getMessage());
		}
	}

	/**
	 * 向消息服务器转发现场(Presence)
	 *  
	 * @param presence 现场
	 * @return JSONObject "{'status': 'ok'}" or "{'status': 'error', 'message': 'blabla'}"
	 * @throws WebimException
	 */
	public JSONObject publish(WebimPresence presence) throws WebimException {
		Map<String, String> data = newData();
		data.put("nick", ep.getNick());
		presence.feed(data);
		try {
			String body = httpost("/presences/show", data);
			return new JSONObject(body);
		} catch (WebimException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebimException(500, e.getMessage());
		}
	}

	/**
	 * 向消息服务器转发状态(Status)
	 * 
	 * @param status 状态
	 * @return JSONObject "{'status': 'ok'}" or "{'status': 'error', 'message': 'blabla'}"
	 * @throws WebimException
	 */
	public JSONObject publish(WebimStatus status) throws WebimException {
		Map<String, String> data = newData();
		data.put("nick", ep.getNick());
		status.feed(data);
		try {
			String body = httpost("/statuses", data);
			return new JSONObject(body);
		} catch (WebimException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebimException(500, e.getMessage());
		}
	}

	/**
	 * 向消息服务器转发聊天消息(Message)
	 * 
	 * @param message 聊天消息
	 * @return JSONObject "{'status': 'ok'}" or "{'status': 'error', 'message': 'blabla'}"
	 * @throws WebimException
	 */
	public JSONObject publish(WebimMessage message) throws WebimException {
		Map<String, String> data = newData();
		message.feed(data);
		try {
			String body = httpost("/messages", data);
			return new JSONObject(body);
		} catch (WebimException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebimException(500, e.getMessage());
		}
	}

	/**
	 * 向消息服务器请求群组在线成员信息
	 * 
	 * @param grpid 群组id
	 * @return member 成员列表
	 * @throws WebimException
	 */
	public JSONObject members(String grpid) throws WebimException {
		Map<String, String> data = newData();
		data.put("group", grpid);
		try {
			String body = httpget("/group/members", data);
			return new JSONObject(body);
		} catch (WebimException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebimException(500, e.getMessage());
		}
	}

	/**
	 * 通知消息服务器用户加入群组
	 * 
	 * @param grpid 群组id
	 * @return JSONObject "{'id': grpid, 'count': '0'}"
	 * @throws WebimException
	 */
	public JSONObject join(String grpid) throws WebimException {
		Map<String, String> data = newData();
		data.put("nick", ep.getNick());
		data.put("group", grpid);
		try {
			String body = httpost("/group/join", data);
			JSONObject respObj = new JSONObject(body);
			JSONObject rtObj = new JSONObject();
			rtObj.put("id", grpid);
			rtObj.put("count", respObj.getInt(grpid)); 
			return rtObj;
		} catch (WebimException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebimException(500, e.getMessage());
		}
	}

	/**
	 * 通知消息服务器用户离开群组
	 * 
	 * @param grpid 群组id
	 * @return JSONObject "{'status': 'ok'}" or "{'status': 'error', 'message': 'blabla'}"
	 * @throws WebimException
	 */
	public JSONObject leave(String grpid) throws WebimException {
		Map<String, String> data = newData();
		data.put("nick", ep.getNick());
		data.put("group", grpid);
		try {
			String body = httpost("/group/leave", data);
			return new JSONObject(body);
		} catch (WebimException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebimException(500, e.getMessage());
		}
	}
	
	private String httpget(String path, Map<String, String> params)
			throws Exception {
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL(apiurl(path) + "?" + encodeData(params));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			initConn(conn);
			conn.connect();
			return readResonpse(conn);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

	}

	private String httpost(String path, Map<String, String> data)
			throws Exception {
		URL url;
		HttpURLConnection conn = null;
		try {
			// Create connection
			url = new URL(apiurl(path));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			String urlParameters = encodeData(data);
			conn.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));

			initConn(conn);

			// Send request
			DataOutputStream wr = null;
			try {
				wr = new DataOutputStream(conn.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
			} finally {
				if(wr != null) wr.close();
			}
			return readResonpse(conn);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private void initConn(HttpURLConnection conn) {
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
	}

	private String readResonpse(HttpURLConnection conn) throws IOException,
			WebimException {
		// Get Response
		if (conn.getResponseCode() != 200) {
			throw new WebimException(conn.getResponseCode(),
					conn.getResponseMessage());
		}
		BufferedReader rd = null;
		StringBuffer response = new StringBuffer();
		try {
			rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			System.out.println(response.toString());
		} finally {
			if(rd != null) rd.close();
		}
		return response.toString();
	}

	private Map<String, String> newData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("version", APIVSN);
		data.put("domain", domain);
		data.put("apikey", apikey);
		data.put("ticket", ticket);
		return data;
	}

	private String encodeData(Map<String, String> data) throws Exception {
		List<String> list = new ArrayList<String>();
		Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			list.add(pair.getKey() + "="
					+ URLEncoder.encode(pair.getValue(), "utf-8"));
		}
		return listJoin("&", list);
	}

	private String listJoin(String sep, List<String> groups) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < groups.size(); i++) {
			if (first) {
				sb.append(groups.get(i));
				first = false;
			} else {
				sb.append(sep);
				sb.append(groups.get(i));
			}
		}
		return sb.toString();
	}

	private String apiurl(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return String.format("http://%s:%d/%s%s", host, port, APIVSN, path);
	}

}
