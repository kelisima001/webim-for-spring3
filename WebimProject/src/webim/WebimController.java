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
package webim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import webim.client.WebimClient;
import webim.client.WebimEndpoint;
import webim.client.WebimMember;
import webim.client.WebimRoom;
import webim.client.WebimHistory;
import webim.client.WebimMenu;
import webim.client.WebimMessage;
import webim.client.WebimNotification;
import webim.client.WebimPresence;
import webim.client.WebimStatus;

@Controller()
@RequestMapping("/Webim")
public class WebimController {

	public static final String SUCCESS = "ok";

	private WebimConfig config = null;

	private WebimModel model = null;

	private WebimPlugin plugin = null;

	public WebimController() {
		this.config = new WebimConfig();
		this.model = new WebimModel();
		this.plugin = new WebimPlugin();
	}

	private WebimEndpoint currentEndpoint() {
		return this.plugin.endpoint();
	}

	/**
	 * 返回当前的Webim客户端。
	 * 
	 * @param ticket
	 *            通信令牌
	 * @return 当前用户的Webim客户端
	 */
	private WebimClient client(String ticket) {
		return client(currentEndpoint(), ticket);
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
	private WebimClient client(WebimEndpoint ep, String ticket) {
		WebimClient client = new WebimClient(currentEndpoint(),
				(String) this.config.get("domain"),
				(String) this.config.get("apikey"),
				(String) this.config.get("host"),
				((Integer) this.config.get("port")).intValue());
		client.setTicket(ticket);
		return client;

	}

	@RequestMapping("/boot")
	public ModelAndView boot(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// System.out.println(message);
		WebimEndpoint endpoint = currentEndpoint();
		response.setHeader("Cache-Control", "no-cache");
		Map<String, Object> data = new HashMap<String, Object>();
		String[] keys = new String[] { "version", "theme", "local", "emot",
				"opacity", "enable_room", "enable_discussion",
				"enable_chatlink", "enable_shortcut", "enable_noti",
				"enable_menu", "show_unavailable", "upload" };
		for (String key : keys) {
			data.put(key, this.config.get(key));
		}
		System.out.println(request.getContextPath());
		data.put("context_path", request.getContextPath());
		data.put("is_login", "1");
		data.put("is_visitor", Boolean.FALSE);
		data.put("login_options", "");
		data.put("jsonp", false);
		data.put("setting", this.model.getSetting(endpoint.getId()));
		return new ModelAndView("Webim/boot", data);
	}

	@RequestMapping(value = "/online", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> online(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("online.....");
		WebimEndpoint endpoint = currentEndpoint();
		String uid = endpoint.getId();
		String show = request.getParameter("show");
		if(show != null) { endpoint.setShow(show); }
		Map<String, Object> data = new HashMap<String, Object>();
		List<WebimEndpoint> buddies = this.plugin.buddies(uid);
		List<WebimRoom> rooms = this.plugin.rooms(uid);
		rooms.addAll(this.model.rooms(uid));
		// Forward Online to IM Server
		WebimClient client = this.client(endpoint, "");
		List<String> buddyIds = buddyIds(buddies);
		List<String> roomIds = roomIds(rooms);
		try {
			data = client.online(buddyIds, roomIds);
			System.out.println(data.toString());

			// Online Buddies
			Map<String, WebimEndpoint> buddyMap = new HashMap<String, WebimEndpoint>();
			for (WebimEndpoint e : buddies) {
				buddyMap.put(e.getId(), e);
			}

			Map<String, String> presences = (Map<String, String>) data
					.get("presences");
			Iterator<String> it = presences.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				WebimEndpoint buddy = buddyMap.get(key);
				show = presences.get(key);
				if(!show.equals("invisible")) {
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
            //need test
            List<WebimHistory> offlineHistories = this.model.offlineHistories(uid, 100);
            this.model.offlineHistoriesReaded(uid);
			data.remove("presences");
			data.put("buddies", rtBuddies.toArray());
			data.put("rooms", rooms.toArray());
            data.put("new_messages", offlineHistories.toArray());
			data.put("server_time", System.currentTimeMillis()); // TODO: /
			data.put("user", endpoint);
		} catch (Exception e) {
			data.put("success", false);
			data.put("error_msg", "IM Server is not found");
		}
		return data;
	}

	@RequestMapping(value = "/offline", method = RequestMethod.POST)
	@ResponseBody
	public String offline(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ticket = request.getParameter("ticket");
		WebimClient c = this.client(ticket);
		c.offline();
		return SUCCESS;
	}

	@RequestMapping("/refresh")
	@ResponseBody
	public String refresh(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ticket = request.getParameter("ticket");
		WebimClient c = this.client(ticket);
		c.offline();
		return SUCCESS;
	}

	@RequestMapping(value = "/message", method = RequestMethod.POST)
	@ResponseBody
	public String message(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint();
		String uid = endpoint.getId();
		String ticket = request.getParameter("ticket");
		String type = request.getParameter("type");
		if (type == null)
			type = "chat";
		String offline = request.getParameter("offline");
		String to = request.getParameter("to");
		String body = request.getParameter("body");
		String style = request.getParameter("style");
		if (style == null) style = "";
		WebimClient c = this.client(endpoint, ticket);
		WebimMessage msg = new WebimMessage(to, c.getEndpoint().getNick(),
				body, style, System.currentTimeMillis()); // TODO: / 1000.0
		msg.setType(type);
		msg.setOffline("true".equals(offline) ? true : false);
		c.publish(msg);
		if (body != null && !body.startsWith("webim-event:")) {
			this.model.insertHistory(uid, msg);
		}
		return SUCCESS;
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
		WebimClient c = this.client(ticket);
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
		WebimClient c = this.client(ticket);
		c.publish(new WebimStatus(to, show, status));
		return SUCCESS;
	}

	@RequestMapping(value = "/buddies", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimEndpoint> buddies(HttpServletRequest request,
			HttpServletResponse response) {
		WebimEndpoint endpoint = currentEndpoint();
		String ids = request.getParameter("ids");
		return this.plugin.buddiesByIds(endpoint.getId(), ids.split(","));
	}

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimHistory> history(HttpServletRequest request,
			HttpServletResponse response) {
		WebimEndpoint endpoint = currentEndpoint();
		String with = request.getParameter("id");
		String type = request.getParameter("type");
		return this.model.histories(endpoint.getId(), with, type, 50);
	}

	@RequestMapping(value = "/history/clear", method = RequestMethod.POST)
	@ResponseBody
	public String clearHistory(HttpServletRequest request,
			HttpServletResponse response) {
		WebimEndpoint endpoint = currentEndpoint();
		String with = request.getParameter("id");
		this.model.clearHistories(endpoint.getId(), with);
		return SUCCESS;
	}

	@RequestMapping("/history/download")
	// @ResponseBody
	public ModelAndView downloadHistory(HttpServletRequest request,
			HttpServletResponse response) {
		WebimEndpoint endpoint = currentEndpoint();
		String with = request.getParameter("id");
		String type = request.getParameter("type");
		List<WebimHistory> histories = this.model.histories(endpoint.getId(),
				with, type, 1000);
		return new ModelAndView("Webim/download_history", "histories",
				histories);
	}

	@RequestMapping(value = "/room/invite", method = RequestMethod.POST)
	@ResponseBody
	public WebimRoom inviteRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint();
		String uid = endpoint.getId();
		String roomId = request.getParameter("id");
		String nick = request.getParameter("nick");
		String ticket = request.getParameter("ticket");
		WebimClient c = this.client(ticket);
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
        //TODO: write database
		if (room != null) {
			this.client(ticket).join(id);
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
		JSONObject presences = this.client(ticket).members(roomId);
		for (WebimMember member : members) {
			String mid = member.getId();
			if (presences.has(mid)) {
				String show = presences.getString(mid);
				if(!"invisible".equals(show)) {
					member.setPresence("online");
					member.setShow(presences.getString(mid));
				}
			}
		}
		return members;
	}

	@RequestMapping(value = "/room/leave", method = RequestMethod.POST)
	@ResponseBody
	public String leaveRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint();
		String room = request.getParameter("id");
		String ticket = request.getParameter("ticket");
        this.model.leaveRoom(room, endpoint.getId());
		WebimClient c = this.client(endpoint, ticket);
		c.leave(room);
		return SUCCESS;
	}

	@RequestMapping(value = "/room/block", method = RequestMethod.POST)
	@ResponseBody
	public String blockRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint();
		String room = request.getParameter("id");
		this.model.blockRoom(room, endpoint.getId());
		return SUCCESS;
	}

	@RequestMapping(value = "/room/unblock", method = RequestMethod.POST)
	@ResponseBody
	public String unblockRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebimEndpoint endpoint = currentEndpoint();
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

	@RequestMapping(value = "/setting", method = RequestMethod.POST)
	@ResponseBody
	public String setting(HttpServletRequest request,
			HttpServletResponse response) {
		WebimEndpoint endpoint = currentEndpoint();
		String data = request.getParameter("data");
		this.model.saveSetting(endpoint.getId(), data);
		return SUCCESS;
	}

	@RequestMapping(value = "/notifications", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimNotification> notifications(HttpServletRequest request,
			HttpServletResponse response) {
		WebimEndpoint endpoint = currentEndpoint();
		return this.plugin.notifications(endpoint.getId());
	}

	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	@ResponseBody
	public Collection<WebimMenu> menu() {
		WebimEndpoint endpoint = currentEndpoint();
		return this.plugin.menu(endpoint.getId());
	}

	private List<String> buddyIds(List<WebimEndpoint> buddies) {
		List<String> ids = new ArrayList<String>();
		for (WebimEndpoint b : buddies) {
			ids.add(b.getId());
		}
		return ids;
	}

	private List<String> roomIds(List<WebimRoom> rooms) {
		List<String> ids = new ArrayList<String>();
		for (WebimRoom g : rooms) {
			ids.add(g.getId());
		}
		return ids;
	}

}
