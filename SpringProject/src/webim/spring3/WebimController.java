package webim.spring3;

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
	
	private WebimClient client = null;
	
	private WebimEndpoint endpoint = null;

	public WebimController() {
		this.config = new WebimConfig();
		this.model = new WebimModel();
		this.plugin = new WebimPlugin();
		this.endpoint = this.plugin.endpoint();
	}
	
	private String currentUid() {
		return this.endpoint.getId();
	}

	/**
	 * 返回当前的Webim客户端。
	 * 
	 * @param ticket
	 *            通信令牌
	 * @return 当前用户的Webim客户端
	 */
	private WebimClient client(String ticket) {
		if(this.client == null) {
			this.client = new WebimClient(
				this.endpoint, 
				(String)this.config.get("domain"),
				(String)this.config.get("APIKEY"), 
				(String)this.config.get("HOST"), 
				((Integer)this.config.get("PORT")).intValue()
			);
			this.client.setTicket(ticket);
		}
		return this.client;
	}
	
	@RequestMapping("/boot")	
	public ModelAndView boot() {
		String message = "Webim Booting... ";
		//System.out.println(message);
		return new ModelAndView("Webim/boot", "message", message);
	}

	@RequestMapping(value = "/online", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> online(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("online.....");
		Map<String, Object> data = new HashMap<String, Object>();
		String uid = this.currentUid();
		List<WebimEndpoint> buddies = this.plugin.buddies(uid);
		List<WebimRoom> rooms = this.plugin.rooms(uid);
		rooms.addAll(this.model.rooms(uid));
		// Forward Online to IM Server
		WebimClient client = this.client("");
		List<String> buddyIds = buddyIds(buddies);
		List<String> roomIds = groupIds(rooms);
		try {
			data = client.online(buddyIds, roomIds);
			System.out.println(data.toString());

			// Online Buddies
			Map<String, WebimEndpoint> buddyMap = new HashMap<String, WebimEndpoint>();
			for (WebimEndpoint e : buddies) {
				buddyMap.put(e.getId(), e);
			}

			Map<String,String> presences = (Map<String,String>)data.get("presences");
			Iterator<String> it = presences.keySet().iterator();
			while ( it.hasNext() ) {
				String key = it.next();
				WebimEndpoint buddy = buddyMap.get(key);
				String show = presences.get(key);
				buddy.setPresence("online");
				buddy.setShow(show);
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
			data.remove("presences");
			data.put("buddies", rtBuddies.toArray());
			data.put("rooms", rooms.toArray());
			data.put("server_time", System.currentTimeMillis()); //TODO: / 1000.0
			data.put("user", client.getEndpoint());
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
		String uid = this.currentUid();
		String ticket = request.getParameter("ticket");
		String type = request.getParameter("type");
		if (type == null) type = "chat";
		String offline = request.getParameter("offline");
		String to = request.getParameter("to");
		String body = request.getParameter("body");
		String style = request.getParameter("style");
		if (style == null) style = "";

		WebimClient c = this.client(ticket);
		WebimMessage msg = new WebimMessage(
				to, 
				this.endpoint.getNick(),
				body, 
				style, 
				System.currentTimeMillis()); //TODO: / 1000.0
		msg.setType(type);
		msg.setOffline(offline == "true" ? true : false);
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
		if (status == null) status = "";
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
		if (status == null) status = "";
		WebimClient c = this.client(ticket);
		c.publish(new WebimStatus(to, show, status));
		return SUCCESS;
	}

	@RequestMapping(value = "/buddies", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimEndpoint> buddies(HttpServletRequest request,
			HttpServletResponse response) {
		String ids = request.getParameter("ids");
		return this.plugin.buddiesByIds(this.currentUid(), ids.split(","));
	}

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimHistory> history(HttpServletRequest request,
			HttpServletResponse response) {
		String with = request.getParameter("id");
		String type = request.getParameter("type");
		return this.model.histories(this.currentUid(), with, type, 50);
	}

	@RequestMapping(value = "/history/clear", method = RequestMethod.POST)
	@ResponseBody
	public String clearHistory(HttpServletRequest request,
			HttpServletResponse response) {
		String with = request.getParameter("id");
		this.model.clearHistories(this.currentUid(), with);
		return SUCCESS;
	}

	@RequestMapping("/history/download")
	//@ResponseBody
	public ModelAndView downloadHistory(HttpServletRequest request,
			HttpServletResponse response) {
		String with = request.getParameter("id");
		String type = request.getParameter("type");
		List<WebimHistory> histories = this.model.histories(this.currentUid(), with, type, 1000);
		return new ModelAndView("Webim/download_history", "histories",
				histories);
	}

	@RequestMapping(value = "/room/invite", method = RequestMethod.POST)
	@ResponseBody
	public WebimRoom inviteRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//TODO:
		return null;
	}

	
	@RequestMapping(value = "/room/join", method = RequestMethod.POST)
	@ResponseBody
	public WebimRoom joinGroup(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
//		String uid = this.currentUid();
		String id = request.getParameter("id");
//		String nick = request.getParameter("nick");
		String ticket = request.getParameter("ticket");
		WebimRoom room = this.plugin.findRoom(id);
		if(room == null) {
			room = this.model.findRoom(id);
		}
		if(room != null) {
			this.client(ticket).join(id);
		}
		return room;
	}

	@RequestMapping(value = "/members", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> members(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String roomId = request.getParameter("id");
		String ticket = request.getParameter("ticket");
		WebimRoom room = this.plugin.findRoom(roomId);
		List<WebimMember> members = null;
		if(room != null) {
			members = this.plugin.members(roomId);
		} else {
			room = this.model.findRoom(roomId);
			if(room != null) {
				members = this.model.members(roomId);
			}
		}
		if(room == null) return null;
		JSONObject presences = this.client(ticket).members(roomId);
		
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for(WebimMember member : members) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("id", member.getId());
			m.put("nick", member.getNick());
			if(presences.has(member.getId())) {
				m.put("presence", "online");
				m.put("show", presences.getString(member.getId()));
			} else {
				m.put("presence", "offline");
				m.put("show", "unavailable");
			}
			
		}
		return data;
	}


	@RequestMapping(value = "/room/leave", method = RequestMethod.POST)
	@ResponseBody
	public String leaveRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String room = request.getParameter("id");
		String ticket = request.getParameter("ticket");
		WebimClient c = this.client(ticket);
		c.leave(room);
		return SUCCESS;
	}

	@RequestMapping(value = "/room/block", method = RequestMethod.POST)
	@ResponseBody
	public String blockRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String room = request.getParameter("id");
		this.model.blockRoom(room, this.currentUid());
		return SUCCESS;
	}
	
	@RequestMapping(value = "/room/unblock", method = RequestMethod.POST)
	@ResponseBody
	public String unblockRoom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String room = request.getParameter("id");
		this.model.unblockRoom(room, this.currentUid());
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
		String data = request.getParameter("data");
		this.model.saveSetting(this.currentUid(), data);
		return SUCCESS;
	}


	@RequestMapping(value = "/notifications", method = RequestMethod.GET)
	@ResponseBody
	public List<WebimNotification> notifications(
			HttpServletRequest request, HttpServletResponse response) {
		return this.plugin.notifications(this.currentUid());
	}

	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	@ResponseBody
	public Collection<WebimMenu> menu() {
		return this.plugin.menu(this.currentUid());
	}

	private List<String> buddyIds(List<WebimEndpoint> buddies) {
		List<String> ids = new ArrayList<String>();
		for (WebimEndpoint b : buddies) {
			ids.add(b.getId());
		}
		return ids;
	}

	private List<String> groupIds(List<WebimRoom> groups) {
		List<String> ids = new ArrayList<String>();
		for (WebimRoom g : groups) {
			ids.add(g.getId());
		}
		return ids;
	}

}
