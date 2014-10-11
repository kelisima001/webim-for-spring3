/*
 * WebimRoomDao.java
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
package webim.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import webim.model.WebimEndpoint;
import webim.model.WebimMember;
import webim.model.WebimRoom;

/**
 * Webim临时讨论组数据访问对象.
 * 
 * MySQL数据库表: <br>
 * 
 * DROP TABLE IF EXISTS webim_rooms; 
 * CREATE TABLE webim_rooms ( 
 *     `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
 *     `owner` varchar(40) NOT NULL, 
 *     `name` varchar(40) NOT NULL, 
 *     `nick` varchar(60) NOT NULL DEFAULT '', 
 *     `topic` varchar(60) DEFAULT NULL, 
 *     `url` varchar(100) DEFAULT '#', 
 *     `created` datetime DEFAULT NULL, 
 *     `updated` datetime DEFAULT NULL, 
 *     PRIMARY KEY (`id`), UNIQUE KEY `webim_room_name` (`name`) 
 * ) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 * 
 * DROP TABLE IF EXISTS webim_members; 
 * CREATE TABLE webim_members ( 
 *     `id` int(11) unsigned NOT NULL AUTO_INCREMENT, 
 *     `room` varchar(60) NOT NULL, 
 *     `uid` varchar(40) NOT NULL, 
 *     `nick` varchar(60) NOT NULL, 
 *     `joined` datetime DEFAULT NULL, 
 *     PRIMARY KEY (`id`), 
 *     UNIQUE KEY `webim_member_room_uid` (`room`,`uid`) 
 * ) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 * 
 * DROP TABLE IF EXISTS webim_blocked;
 * CREATE TABLE webim_blocked (
 *     `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 *     `room` varchar(60) NOT NULL,
 *     `uid` varchar(40) NOT NULL,
 *     `blocked` datetime DEFAULT NULL,
 *     PRIMARY KEY (`id`),
 *     UNIQUE KEY `webim_blocked_room_uid` (`uid`,`room`)
 * ) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 *
 * 
 * @author Feng Lee <feng.lee@nextalk.im>
 * @since 5.7
 *
 */
@Repository("webimRoomDao")
public class WebimRoomDao {

	public WebimRoom getRoom(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<WebimRoom> getRoomsOfUser(String uid) {
		// TODO Auto-generated method stub
		return new ArrayList<WebimRoom>();
	}

	public List<WebimRoom> getRoomsByIds(String uid, String[] ids) {
		// TODO Auto-generated method stub
		return new ArrayList<WebimRoom>();
	}

	public List<WebimMember> getMembersOfRoom(String roomId) {
		// TODO Auto-generated method stub
		return new ArrayList<WebimMember>();
	}

	public void insertRoom(WebimRoom room) {
		// TODO Auto-generated method stub
		
	}

	public void inviteMembersToRoom(String roomId, List<WebimEndpoint> members) {
		// TODO Auto-generated method stub
	}

	public void joinRoom(String roomId, WebimMember webimMember) {
		// TODO Auto-generated method stub
		
	}

	public void leaveRoom(String roomId, String uid) {
		// TODO Auto-generated method stub
		
	}

	public void blockRoom(String roomId, String uid) {
		// TODO Auto-generated method stub
		
	}

	public void unblockRoom(String roomId, String uid) {
		// TODO Auto-generated method stub
		
	}

	public boolean isRoomBlocked(String roomId, String uid) {
		return false;
	}

}
