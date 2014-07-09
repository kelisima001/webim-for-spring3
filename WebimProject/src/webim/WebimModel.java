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

import webim.client.WebimEndpoint;
import webim.client.WebimHistory;
import webim.client.WebimMember;
import webim.client.WebimMessage;
import webim.client.WebimRoom;


/**
 * WebIM数据库接口
 * 
 * @author Ery Lee <ery.lee at gmail.com>
 * 
 * @since 5.4
 */

public class WebimModel {
	

	/**
	 * 读取与with用户聊天记录，查询逻辑:<br>
	 * 
	 * <pre>
	 *     if (type == "chat")
	 *       {
	 *           
	 *           "SELECT * FROM webim_Histories WHERE `type` = 'chat' 
	 *           AND ((`to`=%s AND `from`=%s AND `fromdel` != 1) 
	 *           OR (`send` = 1 AND `from`=%s AND `to`=%s AND `todel` != 1))  
	 *           ORDER BY timestamp DESC LIMIT %d", $with, $uid, $with, $uid, $limit );
	 *           
	 *       }
	 *       else
	 *       {
	 *           
	 *           "SELECT * FROM  webim_histories 
	 *               WHERE `to`=%s AND `type`='grpchat' AND send = 1 
	 *               ORDER BY timestamp DESC LIMIT %d", , $with, $limit);
	 *           
	 *       }
	 * </pre>
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
		return new ArrayList<WebimHistory>();
	}

	/**
	 * 读取用户的离线消息，SQL脚本:<br>
	 * 
	 * "SELECT * FROM  webim_histories WHERE `to` = ? and send != 1 ORDER BY timestamp DESC LIMIT %d"
	 * , limit;
	 * 
	 * @param uid
	 *            用户uid
	 * @return 返回离线消息
	 */
	public List<WebimHistory> offlineHistories(String uid, int limit) {
		//TODO:
		return new ArrayList<WebimHistory>();
	}
	
	public void insertHistory(String uid, WebimMessage msg) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 插入一条聊天记录，参考库表与WebimHistory字段。
	 * 
	 * @param history
	 *            聊天记录
	 */
	public void insertHistory(WebimHistory history) {
		//TODO:
	}
	
	/**
	 * 清除与with用户聊天记录，SQL脚本:<br>
	 * 
	 * "UPDATE webim_histories SET fromdel = 1 Where from = @0 and to = @1 and type = 'chat'"
	 * <br>
	 * "UPDATE webim_histories SET todel = 1 Where to = @0 and from = @1 and type = 'chat'"
	 * <br>
	 * "DELETE FROM webim_histories WHERE fromdel = 1 AND todel = 1"
	 * 
	 * @param uid
	 *            用户uid
	 * @param with
	 *            对方id,可根据需要转换为long
	 */
	public void clearHistories(String uid, String with) {
		//TODO: clear histories
	}

	/**
	 * 离线消息转换为历史消息，SQL脚本:<br>
	 * 
	 * "UPDATE webim_histories SET send = 1 where to = ? and send = 0");
	 * 
	 * @param uid
	 *            用户uid
	 */
	public void offlineHistoriesReaded(String uid) {
		//TODO:
	}

	/**
	 * 读取用户配置数据。<br>
	 * 
	 * <ol>
	 * <li>数据库查询SQL脚本："select data from webim_settings where uid = ?", uid</li>
	 * <li>如果data为空，返回: "{}"</li>
	 * </ol>
	 * 
	 * @param uid
	 *            用户uid
	 * @return 配置数据，JSON格式
	 */
	public String getSetting(String uid) {
		return "";
	}

	/**
	 * 设置用户配置数据。<br>
	 * 
	 * <ol>
	 * <li>数据库SQL脚本: "update webim_settings set data =@0  where uid = @1", uid,
	 * data</li>
	 * <li>应该先读取配置检查是否存在，如不存在插入，存在更新。</li>
	 * </ol>
	 * 
	 * @param uid
	 *            用户uid
	 * @param data
	 *            配置数据，JSON格式
	 */
	public void saveSetting(String uid, String data) {
		//TODO: DEMO CODE
	}
	
	/**
	 * 根据roomId读取临时讨论组
	 * 
	 * @param roomId
	 * @return 临时讨论组
	 */
	public WebimRoom findRoom(String roomId) {
		return null;
	}

	/**
	 * 读取当前用户的临时讨论组
	 * 
	 * @param uid
	 * 
	 * @return 群组列表
	 */
	public List<WebimRoom> rooms(String uid) {
		//TODO:
		return new ArrayList<WebimRoom>();
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
		//TODO:
		return new ArrayList<WebimRoom>();
	}

	/**
	 * 读取临时讨论组成员列表
	 * 
	 * @param room 临时讨论组ID
	 * @return 成员列表
	 */
	public List<WebimMember> members(String room) {
		return new ArrayList<WebimMember>();
	}
	
	/**
	 * 创建临时讨论组
	 * 
	 * @param owner
	 * @param name
	 * @param nick
	 */
	public WebimRoom createRoom(String owner, String name, String nick) {
		//TODO: insert into webim_rooms table
		return new WebimRoom(name, nick);
	}
	
	/**
	 * 邀请成员加入临时讨论组
	 * 
	 * @param roomId 讨论组name
	 * @param members 成员列表
	 */
	public void inviteRoom(String roomId, List<WebimEndpoint> members) {
		//TODO: invite members to room
	}
	
	/**
	 * 加入临时讨论组
	 * 
	 * @param room 讨论组name
	 * @param uid
	 * @param nick
	 */
	public void joinRoom(String room, String uid, String nick) {
		//TODO: 
	}
	
	/**
	 * 离开讨论组
	 * 
	 * @param room
	 * @param uid
	 */
	public void leaveRoom(String room, String uid) {
		//TODO: 
	}
	
	/**
	 * 屏蔽讨论组
	 * 
	 * @param room
	 * @param uid
	 */
	public void blockRoom(String room, String uid) {
		//TODO:
	}
	
	/**
	 * 解除屏蔽
	 * 
	 * @param room
	 * @param uid
	 */
	public void unblockRoom(String room, String uid) {
		//TODO:
	}
	
	/**
	 * 讨论组是否屏蔽
	 * 
	 * @param room
	 * @param uid
	 * 
	 * @return is blocked
	 */
	public boolean isRoomBlocked(String room, String uid) {
		//TODO:
		return false;
	}
	
	/**
	 * 读取访客
	 * 
	 * @return
	 */
	WebimEndpoint visitor(String vid) {
		//TODO: should be from database
		return new WebimEndpoint(vid, vid);
	}
	
	/**
	 * 保存访客
	 * 
	 * @return
	 */
	WebimEndpoint insertVisitor() {
		return new WebimEndpoint("vid:1", "visitor1");
	}
	
	/**
	 * 根据id列表读取访客列表
	 * 
	 * @param vids
	 * @return
	 */
	List<WebimEndpoint> visitors(String[] vids) {
		return new ArrayList<WebimEndpoint>();
	}
	
}
