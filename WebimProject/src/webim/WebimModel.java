/*
 * WebimModel.java
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
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import webim.dao.WebimHistoryDao;
import webim.dao.WebimRoomDao;
import webim.dao.WebimSettingDao;
import webim.dao.WebimVisitorDao;
import webim.model.WebimEndpoint;
import webim.model.WebimHistory;
import webim.model.WebimMember;
import webim.model.WebimMessage;
import webim.model.WebimRoom;


/**
 * WebIM数据库接口
 * 
 * @author Feng Lee <feng.lee at nextalk.im>
 * 
 * @since 5.4
 */
@Service("webimModel")
public class WebimModel {
	
	@Resource(name="webimHistoryDao")
	private WebimHistoryDao historyDao;
	
	@Resource(name="webimSettingDao")
	private WebimSettingDao settingDao;

	@Resource(name="webimRoomDao")
	private WebimRoomDao roomDao;
	
	@Resource(name="webimVisitorDao")
	private WebimVisitorDao visitorDao;

	/**
	 * 读取与with用户聊天记录.
	 * 
	 * @param uid
	 *            当前用户id
	 * @param with
	 *            对方id，可根据需要转换为long
	 * @param type
	 *            记录类型：chat | grpchat
	 *            
	 * @param limit
	 * 			  记录条数
	 * @return 聊天记录
	 */
	public List<WebimHistory> histories(String uid, String with, String type, int limit) {
        return historyDao.getHistories(uid, with, type, limit);
		
	}

	/**
	 * 读取用户的离线消息。
	 * 
	 * @param uid 用户uid
	 * @return 返回离线消息
	 */
	public List<WebimHistory> offlineHistories(String uid, int limit) {
        return historyDao.getOfflineHistories(uid, limit);
	}
	
	/**
	 * 插入一条聊天历史纪录
	 * 
	 * @param uid 用户id
	 * @param msg 消息
	 */
	public void insertHistory(String uid, WebimMessage msg) {
		WebimHistory history = new WebimHistory();
		history.setFrom(uid);
		history.setTo(msg.getTo());		
		history.setType(msg.getType());
		history.setNick(msg.getNick());
		history.setBody(msg.getBody());
		history.setStyle(msg.getStyle());
		history.setSend(msg.isOffline() ? 0 : 1); 
		history.setTimestamp(msg.getTimestamp());
		insertHistory(history);
	}
	
	/**
	 * 插入一条聊天记录，参考库表与WebimHistory字段。
	 * 
	 * @param history
	 *            聊天记录
	 */
	public void insertHistory(WebimHistory history) {
		historyDao.insertHistory(history);
	}
	
	/**
	 * 清除与with用户聊天记录
	 * 
	 * @param uid
	 *            用户uid
	 * @param with
	 *            对方id,可根据需要转换为long
	 */
	public void clearHistories(String uid, String with) {
		historyDao.clearHistories(uid, with);
	}

	/**
	 * 离线消息转换为历史消息
	 * 
	 * @param uid
	 *            用户uid
	 */
	public void offlineHistoriesReaded(String uid) {
		historyDao.offlineHistoriesReaded(uid);
	}

	/**
	 * 读取用户配置数据
	 * 
	 * @param uid
	 *            用户uid
	 * @return 配置数据，JSON格式
	 */
	public String getSetting(String uid) {
		return settingDao.get(uid);
	}

	/**
	 * 设置用户配置数据。
	 * 
	 * @param uid
	 *            用户uid
	 * @param data
	 *            配置数据，JSON格式
	 */
	public void saveSetting(String uid, String data) {
		settingDao.set(uid, data);
	}
	
	/**
	 * 根据roomId读取临时讨论组
	 * 
	 * @param roomId
	 * @return 临时讨论组
	 */
	public WebimRoom findRoom(String roomId) {
		return roomDao.getRoom(roomId);
	}

	/**
	 * 读取当前用户的临时讨论组
	 * 
	 * @param uid 用户id
	 * 
	 * @return 群组列表
	 */
	public List<WebimRoom> rooms(String uid) {
		return roomDao.getRoomsOfUser(uid);
	}
	
	/**
	 * 根据临时讨论组id，读取临时讨论组列表
	 * 
	 * @param uid 用户UID
	 * @param ids 临时讨论组Id列表
	 * 
	 * @return 群组列表
	 */
	public List<WebimRoom> roomsByIds(String uid, String[] ids) {
		return roomDao.getRoomsByIds(uid, ids);
	}

	/**
	 * 读取临时讨论组成员列表
	 * 
	 * @param room 临时讨论组ID
	 * @return 成员列表
	 */
	public List<WebimMember> members(String roomId) {
		return roomDao.getMembersOfRoom(roomId);
	}
	
	/**
	 * 创建临时讨论组
	 * 
	 * @param owner
	 * @param name
	 * @param nick
	 */
	public WebimRoom createRoom(String owner, String name, String nick) {
		WebimRoom room = new WebimRoom(name, nick);
		room.setOwner(owner);
		roomDao.insertRoom(room);
		return room;
	}
	
	/**
	 * 邀请成员加入临时讨论组
	 * 
	 * @param roomId 讨论组name
	 * @param members 成员列表
	 */
	public void inviteRoom(String roomId, List<WebimEndpoint> members) {
		roomDao.inviteMembersToRoom(roomId, members);
		//TODO: invite members to room
	}
	
	/**
	 * 加入临时讨论组
	 * 
	 * @param room 讨论组name
	 * @param uid
	 * @param nick
	 */
	public void joinRoom(String roomId, String uid, String nick) {
		roomDao.joinRoom(roomId, new WebimMember(uid, nick));
	}
	
	/**
	 * 离开讨论组
	 * 
	 * @param roomId
	 * @param uid
	 */
	public void leaveRoom(String roomId, String uid) {
		roomDao.leaveRoom(roomId, uid);
	}
	
	/**
	 * 屏蔽讨论组
	 * 
	 * @param room
	 * @param uid
	 */
	public void blockRoom(String roomId, String uid) {
		roomDao.blockRoom(roomId, uid);
	}
	
	/**
	 * 解除屏蔽
	 * 
	 * @param roomId
	 * @param uid
	 */
	public void unblockRoom(String roomId, String uid) {
		roomDao.unblockRoom(roomId, uid);
	}
	
	/**
	 * 讨论组是否屏蔽
	 * 
	 * @param room
	 * @param uid
	 * 
	 * @return is blocked
	 */
	public boolean isRoomBlocked(String roomId, String uid) {
		return roomDao.isRoomBlocked(roomId, uid);
	}

	/**
	 * 查找或创建访客
	 * 
	 * @param request
	 * 
	 * @return
	 */
	public WebimEndpoint findOrCreateVisitor(HttpServletRequest request) {
		//find or create visitor;
		// TODO Auto-generated method stub
		return null;
		/*
        global $_COOKIE, $_SERVER;
        if (isset($_COOKIE['_webim_visitor_id'])) {
            $id = $_COOKIE['_webim_visitor_id'];
        } else {
            $id = substr(uniqid(), 6);
            setcookie('_webim_visitor_id', $id, time() + 3600 * 24 * 30, "/", "");
        }
        $vid = 'vid:'. $id;
        $visitor = $this->where('name', $vid)->find();
        if( !$visitor ) {
            $ipaddr = isset($_SERVER['X-Forwarded-For']) ? $_SERVER['X-Forwarded-For'] : $_SERVER["REMOTE_ADDR"];
            $loc = IP::find($ipaddr);
            if(is_array($loc)) $loc = implode('',  $loc);
            $referer = isset($_SERVER['HTTP_REFERER']) ? $_SERVER['HTTP_REFERER'] : '';
            $this->create(array(
                "name" => $vid,
                "ipaddr" => $ipaddr,
                "url" => $_SERVER['REQUEST_URI'],
                "referer" => $referer,
                "location" => $loc,
            ));
            $this->created = date( 'Y-m-d H:i:s' );
            $this->add();
        }
        return (object) array(
            'id' => $vid,
            'nick' => "v".$id,
            'group' => "visitor",
            'presence' => 'online',
            'show' => "available",
            'avatar' => WEBIM_IMAGE('male.png'),
            'role' => 'visitor',
            'url' => "#",
            'status' => "",
        );
        */
	}
		
	/**
	 * 根据id列表读取访客列表
	 * 
	 * @param vids
	 * @return
	 */
	public List<WebimEndpoint> visitors(String[] vids) {
		/*
		List<WebimEndpoint> rtList = new ArrayList<WebimEndpoint>();
		if(vids.length == 0) return rtList;
		
		List<WebimVisitor> visitors = new ArrayList<WebimVisitor>();
		long[] ids = new long[vids.length];
		for(int i = 0; i < vids.length; i++) {
			ids[i] = Long.parseLong(vids[i].substring("vid:".length()));
		}

		//select * from webim_visitors where name in (ids);
		
		//map visitor to endpoint
		for(WebimVisitor v : visitors) {
			String vid = "vid:" + v.id;
			String nick = v.name;
			WebimEndpoint ep = new WebimEndpoint(vid, nick);
			String status = "";
			if(v.location != null) status += v.location;
			if(v.ipaddr != null) status += "(" + v.ipaddr + ")"; 
			ep.setGroup("visitor");
			ep.setAvatar("");//TODO:
			ep.setStatus(status);
			rtList.add(ep);
		}
		return rtList;
		*/
		return new ArrayList<WebimEndpoint>();
	}
	
}



