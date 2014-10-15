/*
 * WebimController.java
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
package webim.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import webim.WebimConfig;
import webim.client.WebimClient;
import webim.client.WebimCluster;
import webim.model.WebimEndpoint;
import webim.model.WebimMember;
import webim.model.WebimRoom;
import webim.model.WebimHistory;
import webim.model.WebimMenu;
import webim.model.WebimMessage;
import webim.model.WebimNotification;
import webim.model.WebimPresence;
import webim.model.WebimStatus;
import webim.service.WebimModel;
import webim.service.WebimPlugin;
import webim.service.WebimVisitorManager;

@Controller()
@RequestMapping("/Webim")
public class WebimController {

	public static final String SUCCESS = "ok";

	@Resource(name="webimConfig")
	private WebimConfig config ;

	@Resource(name="webimModel")
	private WebimModel model ;

	@Resource(name="webimPlugin")
	private WebimPlugin plugin;

	@Resource(name="webimCluster")
	private WebimCluster cluster;
	
	@Resource(name="webimVisitorManager")
	private WebimVisitorManager visitorManager;
	
	public WebimController() {
	}

	/**
	 * 返回当前Webim的端点
	 * 
	 * @param request
	 *            HTTP请求
	 * @return Webim端点(用户)
	 */
	private WebimEndpoint currentEndpoint(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint ep = this.plugin.endpoint(request);
		if (ep == null && config.getBoolean("enable_visitor")) {
			ep = this.visitorManager.endpoint(request, response);
		}
		return ep;
	}

	/**
	 * 返回当前的Webim客户端。
	 * 
	 * @param ticket
	 *            通信令牌
	 * @return 当前用户的Webim客户端
	 */
	private WebimClient client(HttpServletRequest request, HttpServletResponse response, String ticket) throws Exception {
		return client(currentEndpoint(request, response), request, ticket);
	}

	/**
	 * 根据当前endpoint、ticket，创建WebimClient实例
	 * 
	 * @param ep
	 *            当前endpoint
	 * @param ticket
	 *            通信ticket
	 * @return WebimClient实例
	 */
	private WebimClient client(WebimEndpoint ep, HttpServletRequest request,
			String ticket) {
		WebimClient client = new WebimClient(ep,
				(String)this.config.get("domain"),
				(String)this.config.get("apikey"),
				cluster);
		client.setTicket(ticket);
		return client;
	}

	@RequestMapping("/boot")
	public ModelAndView boot(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// System.out.println(message);
		WebimEndpoint endpoint = currentEndpoint(request,response);
		response.setHeader("Cache-Control", "no-cache");
		Map<String, Object> data = new HashMap<String, Object>();
		String[] keys = new String[] { 
				"version", 
				"theme", 
				"local", 
				"emot",
				"opacity", 
				"enable_room", 
				"enable_discussion",
				"enable_chatlink", 
				"enable_shortcut", 
				"enable_noti",
				"enable_menu", 
				"show_unavailable", 
				"upload"
		};
		for (String key : keys) {
			data.put(key, this.config.get(key));
		}
//		System.out.println(request.getContextPath());
		data.put("context_path", request.getContextPath());
		data.put("is_login", "1");
		data.put("is_visitor", this.visitorManager.isVid(endpoint.getId()));
		data.put("login_options", "");
		data.put("jsonp", this.config.getBoolean("jsonp"));
		data.put("setting", this.model.getSetting(endpoint.getId()));
		return new ModelAndView("Webim/boot", data);
	}

	@RequestMapping(value = "/online", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> online(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("online.....");
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String uid = endpoint.getId();
		String show = request.getParameter("show");
		if (show != null) {
			endpoint.setShow(show);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		List<WebimEndpoint> buddies = this.plugin.buddies(uid);

		//pending buddies ids that need to read buddy
		Set<String> pendingIds = new HashSet<String>();
		String chatlinkIds = request.getParameter("chatlink_ids");
		if(chatlinkIds != null) {
			for(String id : chatlinkIds.split(",")) { pendingIds.add(id); };
		}
		List<WebimHistory> offlineHistories = this.model.offlineHistories(
		uid, 100);
		for(WebimHistory h : offlineHistories) { pendingIds.add(h.getFrom()); }
		pendingIds.removeAll(buddyIds(buddies));
		
		if(pendingIds.size() > 0) {
			buddies.addAll(this.plugin.buddiesByIds(uid, pendingIds.toArray(new String[pendingIds.size()])));
		}

		List<WebimRoom> rooms = this.plugin.rooms(uid);
		rooms.addAll(this.model.rooms(uid));
		// Forward Online to IM Server
		WebimClient client = this.client(endpoint, request, "");
		Set<String> buddyIds = buddyIds(buddies);
		Set<String> roomIds = roomIds(rooms);
		try {
			data = client.online(buddyIds, roomIds);
			System.out.println(data.toString());

			// Online Buddies
			Map<String, WebimEndpoint> buddyMap = new HashMap<String, WebimEndpoint>();
			for (WebimEndpoint e : buddies) {
				buddyMap.put(e.getId(), e);
			}
			
			@SuppressWarnings("unchecked")
			Map<String, String> presences = (Map<String, String>) data
					.get("presences");
			Iterator<String> it = presences.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				WebimEndpoint buddy = buddyMap.get(key);
				show = presences.get(key);
				if (!show.equals("invisible")) {
					buddy.setPresence("online");
					buddy.setShow(show);
				}
			}

			Collection<WebimEndpoint> rtBuddies;
			if (this.config.getBoolean("show_unavailable")) {
				rtBuddies = buddyMap.values();
			} else {
				rtBuddies = new ArrayList<WebimEndpoint>();
				for (WebimEndpoint e : buddyMap.values()) {
					if (e.getPresence() == "online")
						rtBuddies.add(e);
				}
			}
			// need test
			this.model.offlineHistoriesReaded(uid);
			data.remove("presences");
			data.put("buddies", rtBuddies.toArray());
			data.put("rooms", rooms.toArray());
			data.put("new_messages", offlineHistories.toArray());
			data.put("server_time", System.currentTimeMillis()); // TODO: /
			data.put("user", endpoint);
		} catch (Exception e) {
			e.printStackTrace();
			data.put("success", false);
			data.put("error_msg", "IM Server is not found");
		}
		return data;
	}

	@RequestMapping(value = "/offline", method=RequestMethod.POST)
	@ResponseBody
	public String offline(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ticket = request.getParameter("ticket");
		WebimClient c = this.client(request,response, ticket);
		c.offline();
		return SUCCESS;
	}

	@RequestMapping(value="/refresh", method=RequestMethod.POST)
	@ResponseBody
	public String refresh(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ticket = request.getParameter("ticket");
		WebimClient c = this.client(request,response, ticket);
		c.offline();
		return SUCCESS;
	}

	@RequestMapping(value = "/message", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> message(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		Map<String, String> rtData = new HashMap<String, String>();
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String uid = endpoint.getId();
		String ticket = request.getParameter("ticket");
		String type = request.getParameter("type");
		if (type == null)
			type = "chat";
		String offline = request.getParameter("offline");
		String to = request.getParameter("to");
		String body = request.getParameter("body");
		String style = request.getParameter("style");
		if (style == null)
			style = "";
		
		if(!this.model.isBuddy(uid, to)) {
			this.model.addBuddy(uid, to);
		} 
		
		if(!plugin.checkCensor(body)) {
			rtData.put("status", "error");
			rtData.put("message", "您发送消息有敏感词");
			return rtData;
		}
		if(plugin.isRobotSupport() && plugin.isFromRobot(to)) {
			WebimClient c = this.client(endpoint, request, null);
			
			WebimMessage requestMsg = new WebimMessage(to, 
					c.getEndpoint().getNick(), body, style, System.currentTimeMillis());
			this.model.insertHistory(uid, requestMsg);
			String answer = plugin.getRobot().answer(body);
			WebimMessage answermsg = new WebimMessage(uid, plugin.getRobot().getNick(),
					answer, "", System.currentTimeMillis());
			this.model.insertHistory(to, answermsg);
			c.push(to, answermsg);
		} else {
			WebimClient c = this.client(endpoint, request, ticket);
			
			WebimMessage msg = new WebimMessage(to, c.getEndpoint().getNick(),
				body, style, System.currentTimeMillis()); 
			msg.setType(type);
			msg.setOffline("true".equals(offline) ? true : false);
			if (body != null && !body.startsWith("webim-event:")) {
				this.model.insertHistory(uid, msg);
			}
			c.publish(msg);
		}
		rtData.put("status", "ok");
		return rtData;
	}

	@RequestMapping(value = "/presence", method = RequestMethod.POST)
	@ResponseBody
	public String presence(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ticket = request.getParameter("ticket");
		String show = request.getParameter("show");
		String status = request.getParameter("status");
		if (status == null)
			status = "";
		WebimClient c = this.client(request,response, ticket);
		c.publish(new WebimPresence(show, status));
		return SUCCESS;
	}

	@RequestMapping(value = "/status", method = RequestMethod.POST) 
	@ResponseBody
	public String status(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ticket = request.getParameter("ticket");
		String to = request.getParameter("to");
		String show = request.getParameter("show");
		String status = request.getParameter("status");
		if (status == null)
			status = "";
		WebimClient c = this.client(request,response, ticket);
		c.publish(new WebimStatus(to, show, status));
		return SUCCESS;
	}

	@RequestMapping(value = "/buddies", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimEndpoint> buddies(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String ticket = request.getParameter("ticket");
		String[] ids = request.getParameter("ids").split(",");
		// user id list
		List<String> uids = new ArrayList<String>();
		// visitor id list
		List<String> vids = new ArrayList<String>();
		for (String id : ids) {
			if (this.visitorManager.isVid(id)) {
				vids.add(id);
			} else {
				uids.add(id);
			}
		}
		// read buddies from user service
		List<WebimEndpoint> buddies = plugin.buddiesByIds(endpoint.getId(),
				uids.toArray(new String[uids.size()]));
		//TODO: read visitors from 'webim_visitors' table
		buddies.addAll(model.visitors(vids.toArray(new String[vids.size()])));

		Set<String> buddyIds = buddyIds(buddies);

		// feed presence
		JSONObject presences = this.client(request,response, ticket).presences(buddyIds);
		for (WebimEndpoint buddy : buddies) {
			String id = buddy.getId();
			if (presences.has(id)) {
				String show = presences.getString(id);
				if (!"invisible".equals(show)) {
					buddy.setPresence("online");
					buddy.setShow(show);
				}
			}
		}

		return buddies;
	}

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimHistory> history(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String with = request.getParameter("id");
		String type = request.getParameter("type");
		if(plugin.isRobotSupport() && with.equals(plugin.getRobot().getId())) {
			//String[] askList = plugin.getRobot().getAskList();
			//TODO: 如果没有历史消息,返回问题列表
			//如果有历史消息,问题放到最后
		}
		return this.model.histories(endpoint.getId(), with, type, 50);
	}

	@RequestMapping(value = "/history/clear", method = RequestMethod.POST)
	@ResponseBody
	public String clearHistory(HttpServletRequest request,
			HttpServletResponse response)throws Exception  {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String with = request.getParameter("id");
		this.model.clearHistories(endpoint.getId(), with);
		return SUCCESS;
	}

	@RequestMapping("/history/download")
	// @ResponseBody
	public ModelAndView downloadHistory(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String with = request.getParameter("id");
		String type = request.getParameter("type");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"histories.html\"");
		List<WebimHistory> histories = this.model.histories(endpoint.getId(),
				with, type, 1000);
		return new ModelAndView("Webim/download_history", "histories",
				histories);
	}

	@RequestMapping(value = "/room/invite", method = RequestMethod.POST)
	@ResponseBody
	public WebimRoom inviteRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String uid = endpoint.getId();
		String roomId = request.getParameter("id");
		String nick = request.getParameter("nick");
		String ticket = request.getParameter("ticket");
		WebimClient c = this.client(request,response, ticket);
		WebimRoom room = this.model.findRoom(roomId);
		if (room != null) {
			room = this.model.createRoom(uid, roomId, nick);
		}
		this.model.joinRoom(roomId, uid, c.getEndpoint().getNick());
		String[] memberIds = request.getParameter("members").split(",");
		List<WebimEndpoint> members = this.plugin.buddiesByIds(uid, memberIds);
		this.model.inviteRoom(roomId, members);

		// send invite message to members
		for (WebimEndpoint m : members) {
			String body = "webim-event:invite|,|" + roomId + "|,|" + nick;
			c.publish(new WebimMessage(m.getId(), c.getEndpoint().getNick(),
					body, "", System.currentTimeMillis()));

		}
		c.join(roomId);
		return room;
	}

	@RequestMapping(value = "/room/join", method = RequestMethod.POST)
	@ResponseBody
	public WebimRoom joinRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// String uid = this.currentUid();
		String id = request.getParameter("id");
		// String nick = request.getParameter("nick");
		String ticket = request.getParameter("ticket");
		WebimRoom room = this.plugin.findRoom(id);
		if (room == null) {
			room = this.model.findRoom(id);
		}
		// TODO: write database
		if (room != null) {
			this.client(request,response, ticket).join(id);
		}
		return room;
	}

	@RequestMapping(value = "/room/members", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimMember> roomMembers(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String roomId = request.getParameter("id");
		String ticket = request.getParameter("ticket");
		WebimRoom room = this.plugin.findRoom(roomId);
		List<WebimMember> members = null;
		if (room != null) {
			members = this.plugin.members(roomId);
		} else {
			room = this.model.findRoom(roomId);
			if (room != null) {
				members = this.model.members(roomId);
			}
		}
		if (room == null)
			return null;
		JSONObject presences = this.client(request,response, ticket).members(roomId);
		for (WebimMember member : members) {
			String mid = member.getId();
			if (presences.has(mid)) {
				String show = presences.getString(mid);
				if (!"invisible".equals(show)) {
					member.setPresence("online");
					member.setShow(presences.getString(mid));
				}
			}
		}
		return members;
	}

	@RequestMapping(value = "/room/leave" , method = RequestMethod.POST)
	@ResponseBody
	public String leaveRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String room = request.getParameter("id");
		String ticket = request.getParameter("ticket");
		this.model.leaveRoom(room, endpoint.getId());
		WebimClient c = this.client(endpoint, request, ticket);
		c.leave(room);
		return SUCCESS;
	}

	@RequestMapping(value = "/room/block" , method = RequestMethod.POST) 
	@ResponseBody
	public String blockRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String room = request.getParameter("id");
		this.model.blockRoom(room, endpoint.getId());
		return SUCCESS;
	}

	@RequestMapping(value = "/room/unblock" , method = RequestMethod.POST)
	@ResponseBody
	public String unblockRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request, response);
		String room = request.getParameter("id");
		this.model.unblockRoom(room, endpoint.getId());
		return SUCCESS;
	}

	@RequestMapping("/upload")
	@ResponseBody
	public String upload(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO:
		return SUCCESS;
	}

	@RequestMapping(value = "/setting" , method = RequestMethod.POST)
	@ResponseBody
	public String setting(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		String data = request.getParameter("data");
		this.model.saveSetting(endpoint.getId(), data);
		return SUCCESS;
	}

	@RequestMapping(value = "/notifications", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimNotification> notifications(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request,response);
		return this.plugin.notifications(endpoint.getId());
	}

	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	@ResponseBody
	public Collection<WebimMenu> menu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint(request, response);
		return this.plugin.menu(endpoint.getId());
	}

	private Set<String> buddyIds(List<WebimEndpoint> buddies) {
		Set<String> ids = new HashSet<String>();
		for (WebimEndpoint b : buddies) {
			ids.add(b.getId());
		}
		return ids;
	}

	private Set<String> roomIds(List<WebimRoom> rooms) {
		Set<String> ids = new HashSet<String>();
		for (WebimRoom g : rooms) {
			ids.add(g.getId());
		}
		return ids;
	}

	
}
