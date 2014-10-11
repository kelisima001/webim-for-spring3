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
package webim.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import webim.model.WebimHistory;

/**
 * Webim历史消息数据访问对象。
 * 
 * MySQL Table:<br>
 * 
 * <pre>
 * CREATE TABLE webim_histories (
 * 	`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 * 	`send` tinyint(1) DEFAULT NULL,
 * 	`type` varchar(20) DEFAULT NULL,
 * 	`to` varchar(50) NOT NULL,
 * 	`from` varchar(50) NOT NULL,
 * 	`nick` varchar(20) DEFAULT NULL COMMENT 'from nick',
 * 	`body` text,
 * 	`style` varchar(150) DEFAULT NULL,
 * `timestamp` double DEFAULT NULL,
 * `todel` tinyint(1) NOT NULL DEFAULT '0',
 * `fromdel` tinyint(1) NOT NULL DEFAULT '0',
 * `createdat` date DEFAULT NULL,
 * `updatedat` date DEFAULT NULL,
 * PRIMARY KEY (`id`),
 * KEY `timestamp` (`timestamp`),
 * KEY `to` (`to`),
 * KEY `from` (`from`),
 * KEY `send` (`send`)
 * ) ENGINE=MyISAM;
 * </pre>
 * 
 * Oracle Table: <br>
 * 
 * <pre>
 * 
 * ----------------------------
 * 
 * create table WEBIM_HISTORIES
 * (
 *   ID            NUMBER(19) not null,
 *   MSG_TYPE      VARCHAR2(20),
 *   SEND          NUMBER(1),
 *   TO_USER       VARCHAR2(50),
 *   FROM_USER     VARCHAR2(50),
 *   NICK          VARCHAR2(20),
 *   MSG_BODY      VARCHAR2(4000),
 *   STYLE         VARCHAR2(150),
 *   MSG_TIMESTAMP FLOAT,
 *   ISDEL         VARCHAR2(1),
 *   CREATED       DATE,
 *   UPDATED       DATE,
 *   TODEL         VARCHAR2(1),
 *   FROMDEL       VARCHAR2(1)
 * );
 * -- Add comments to the columns 
 * comment on column WEBIM_HISTORIES.ID  is '逻辑主键';
 * comment on column WEBIM_HISTORIES.MSG_TYPE is '消息类型';
 * comment on column WEBIM_HISTORIES.SEND is '发送标识';
 * comment on column WEBIM_HISTORIES.TO_USER  is '接收方';
 * comment on column WEBIM_HISTORIES.FROM_USER  is '发送方';
 * comment on column WEBIM_HISTORIES.NICK is '昵称';
 * comment on column WEBIM_HISTORIES.MSG_BODY  is '消息内容';
 * comment on column WEBIM_HISTORIES.STYLE is '风格';
 * comment on column WEBIM_HISTORIES.MSG_TIMESTAMP  is '时间戳';
 * comment on column WEBIM_HISTORIES.ISDEL  is '删除标识';
 * comment on column WEBIM_HISTORIES.CREATED  is '创建时间';
 * comment on column WEBIM_HISTORIES.UPDATED  is '更新时间';
 * -- Create/Recreate primary, unique and foreign key constraints 
 * alter table WEBIM_HISTORIES
 *  add constraint WEBIM_HISTORIES_PK primary key (ID);
 *   
 * ----------------------------
 * </pre>
 */
@Repository("webimHistoryDao")
public class WebimHistoryDao {

	/**
	 * 读取与with用户聊天记录，MySQL查询逻辑:<br>
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
	 *            记录条数
	 * @return 聊天记录
	 */
	public List<WebimHistory> getHistories(String uid, String with,
			String type, int limit) {
		// TODO Auto-generated method stub
		return new ArrayList<WebimHistory>();
	}

	/**
	 * 读取用户的离线消息，MySQL脚本:<br>
	 * 
	 * "SELECT * FROM  webim_histories WHERE `to` = ? and send != 1 ORDER BY timestamp DESC LIMIT %d"
	 * , limit;
	 * 
	 * @param uid
	 *            用户uid
	 * @return 返回离线消息
	 */
	public List<WebimHistory> getOfflineHistories(String uid, int limit) {
		// TODO Auto-generated method stub
		return new ArrayList<WebimHistory>();
	}

	public void insertHistory(WebimHistory history) {
		// TODO Auto-generated method stub
	}

	/**
	 * 清除与with用户聊天记录，MySQL脚本:<br>
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
		// TODO Auto-generated method stub
	}

	/**
	 * 离线消息转换为历史消息，MySQL脚本:<br>
	 * 
	 * "UPDATE webim_histories SET send = 1 where to = ? and send = 0");
	 * 
	 * @param uid
	 *            用户uid
	 */
	public void offlineHistoriesReaded(String uid) {
		// TODO Auto-generated method stub
		
	}

}
