/*
 * WebimService.java
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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import webim.client.WebimClient;
import webim.config.WebimConfig;
import webim.client.WebimEndpoint;
import webim.client.WebimGroup;
import webim.client.WebimHistory;
import webim.client.WebimMenu;
import webim.client.WebimMessage;
import webim.client.WebimNotification;
import webim.dao.WebimDao;
import webim.dao.WebimHistoryDao;
import webim.dao.WebimSettingDao;

/**
 * WebimService封装Webim Actions与Webim Dao之间依赖调用。
 * 
 * @author Ery Lee <ery.lee at gamil.com>
 * @since 1.0
 */
public class WebimService {

	static WebimService instance = null;

	private WebimDao webimDao;
	private WebimSettingDao settingDao;
	private WebimHistoryDao historyDao;

	public WebimService() {
		webimDao = new WebimDao();
		settingDao = new WebimSettingDao();
		historyDao = new WebimHistoryDao();
	}

	/**
	 * 单例
	 * 
	 * @return WebimService单例
	 */
	public synchronized static WebimService instance() {
		if (instance == null) {
			instance = new WebimService();
		}
		return instance;
	}

	/**
	 * 返回当前Webim用户的Uid，一般应从HTTP Session中读取。
	 * 
	 * @return 当前Webim用户的UID
	 */
	public long currentUid() {
		// TODO: 应该替换该代码，读取当前登陆用户的ID。
		return 1;
	}

	/**
	 * 返回当前的Webim端点(用户)
	 * 
	 * @return 当前Webim端点(用户)
	 */
	public WebimEndpoint currentEndpoint() {
		// TODO: 应替换该代码，返回集成系统的当前用户。
		WebimEndpoint ep = new WebimEndpoint("1", "user1");
		ep.setPic_url("https://1.gravatar.com/avatar/136e370cbf1cf500cbbf791e56dac614?d=https%3A%2F%2Fidenticons.github.com%2F577292a0aa8cb84aa3e6f06fee6f711c.png&s=70"); // �û�ͷ��
		ep.setShow("available");
		ep.setUrl(""); // 用户空间
		ep.setStatus(""); // 用户状态
		return ep;
	}

	/**
	 * 返回当前的Webim客户端。
	 * 
	 * @param ticket
	 *            通信令牌
	 * @return 当前用户的Webim客户端
	 */
	public WebimClient currentClient(String ticket) {
		WebimClient c = new WebimClient(currentEndpoint(), WebimConfig.DOMAIN,
				WebimConfig.APIKEY, WebimConfig.HOST, WebimConfig.PORT);
		c.setTicket(ticket);
		return c;
	}

	/**
	 * 根据当前用户id，返回该用户好友，从WebimDao中读取。
	 * 
	 * @param uid
	 *            当前用户id
	 * @return 该用户好友。
	 */
	public List<WebimEndpoint> getBuddies(long uid) {
		return webimDao.getBuddiesByUid(uid, 1000);
	}

	/**
	 * 根据好友id列表读取好友列表。
	 * 
	 * @param ids
	 *            好友id列表
	 * @return 好友列表
	 */
	public List<WebimEndpoint> getBuddiesByIds(long[] ids) {
		return webimDao.getBuddiesByIds(ids);
	}

	/**
	 * 根据当前用户id，返回用户所属群组，从WebimDao中读取
	 * 
	 * @param uid
	 *            当前用户id
	 * @return 用户所属群组
	 */
	public List<WebimGroup> getGroups(long uid) {
		return webimDao.getGroups(uid, 100);
	}

	/**
	 * 根据当前用户uid，获取临时讨论组。
	 * 
	 * @param uid
	 *            用户uid
	 * @return 临时讨论组
	 * @throws JSONException
	 */
	public List<WebimGroup> getTmpGroups(long uid) throws JSONException {
		List<WebimGroup> groups = new ArrayList<WebimGroup>();
		String json = settingDao.get(uid);
		JSONObject obj = new JSONObject(json);
		if (obj.has("temporary_rooms")) {
			JSONArray array = obj.getJSONArray("temporary_rooms");
			for (int i = 0; i < array.length(); i++) {
				JSONObject o = array.getJSONObject(i);
				WebimGroup g = new WebimGroup(o.getString("id"),
						o.getString("nick"));
				g.setTemporary(true);
				// FIXME Later
				g.setPic_url("/SpringProject/static/images/chat.png");
				groups.add(g);
			}
		}
		return groups;
	}

	/**
	 * 根据群组id，返回一个群组的详细信息。
	 * 
	 * @param id
	 *            群组id
	 * @return 群组详细
	 */
	public WebimGroup getGroup(String id) {
		return webimDao.getGroup(id);
	}

	/**
	 * 根据用户uid，读取该用户离线消息
	 * 
	 * @param uid
	 *            用户uid
	 * @return 离线消息
	 */
	public List<WebimHistory> getOfflineMessages(long uid) {
		return historyDao.getOfflineMessages(uid);
	}

	/**
	 * 清除用户的离线消息
	 * 
	 * @param uid
	 *            用户uid
	 */
	public void offlineMessageToHistory(long uid) {
		historyDao.offlineMessageToHistory(uid);
	}

	/**
	 * 写入聊天纪录
	 * 
	 * @param uid
	 *            用户uid
	 * @param offline
	 *            是否离线
	 * @param msg
	 *            即时消息
	 */
	public void insertHistory(long uid, String offline, WebimMessage msg) {
		WebimHistory h = new WebimHistory();
		h.setFrom(String.valueOf(uid));
		h.setSend(offline == "true" ? 0 : 1);
		h.setNick(msg.getNick());
		h.setType(msg.getType());
		h.setTo(msg.getTo());
		h.setBody(msg.getBody());
		h.setStyle(msg.getStyle());
		h.setTimestamp(msg.getTimestamp());
		historyDao.insert(h);
	}

	/**
	 * 读取用户配置
	 * 
	 * @param uid
	 *            用户uid
	 * @return 用户配置
	 */
	public String getSetting(long uid) {
		return settingDao.get(uid);
	}

	/**
	 * 设置用户配置
	 * 
	 * @param uid
	 *            用户uid
	 * @param data
	 *            配置数据,json格式
	 */
	public void updateSetting(long uid, String data) {
		settingDao.set(uid, data);
	}

	/**
	 * 读取当前用户与好友聊天纪录
	 * 
	 * @param uid
	 *            用户uid
	 * @param with
	 *            好友id(根据需要可转换为long)
	 * @param type
	 *            聊天记录类型(chat|grpchat)
	 * @return 聊天记录
	 */
	public List<WebimHistory> getHistories(long uid, String with, String type) {
		return historyDao.getHistories(uid, with, type);
	}

	/**
	 * 清除与好友聊天记录
	 * 
	 * @param uid
	 *            用户uid
	 * @param with
	 *            好友id
	 */
	public void clearHistories(long uid, String with) {
		historyDao.clearHistories(uid, with);
	}

	/**
	 * 读取当前用户通知
	 * 
	 * @param uid
	 *            用户uid
	 * @return 通知列表
	 */
	public List<WebimNotification> getNotifications(long uid) {
		return webimDao.getNotifications(uid);
	}

	/**
	 * 读取当前用户菜单
	 * 
	 * @param uid
	 *            用户uid
	 * @return 用户菜单
	 */
	public List<WebimMenu> getMenuList(long uid) {
		return webimDao.getMenuList(uid);
	}

	/**
	 * 创建临时群组
	 * 
	 * @param id
	 *            群组id
	 * @param nick
	 *            群组昵称
	 * @return WebimGroup 群组对象
	 */
	public WebimGroup newTmpGroup(String id, String nick) {
		WebimGroup group = new WebimGroup(id, nick);
		group.setTemporary(true);
		//TODO: FIXME Later
		group.setPic_url("/SpringProject/static/images/chat.png");
		return group;
	}

}

