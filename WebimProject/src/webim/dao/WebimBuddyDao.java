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

import org.springframework.stereotype.Repository;

/**
 * Webim好友表 <br>
 * 
 * MySQL数据脚本: <br>
 * 
 * DROP TABLE IF EXISTS webim_buddies;
 * CREATE TABLE webim_buddies (
 *     `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 *     `uid` varchar(40) DEFAULT NULL,
 *     `fid` varchar(40) DEFAULT NULL,
 *     `created` datetime DEFAULT NULL,
 *     UNIQUE KEY `webim_buddy_key` (`uid`, `fid`)
 *     PRIMARY KEY (`id`)
* )ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 * 
 * @since 5.8
 */
@Repository("webimRoomDao")
public class WebimBuddyDao {

	/**
	 * 是否好友关系, MySQL查询语句<br>
	 * 
	 * select id from webim_buddies with uid = uid and fid = with;
	 * 
	 * @param uid 当前用户id
	 * @param with 好友id
	 * @return true，是好友关系
	 */
	public boolean isBuddy(String uid, String with) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 插入一条好友关系, MySQL语句:<br>
	 * 
	 * insert into webim_buddies(uid, fid) values(uid, with);
	 * insert into webim_buddies(uid, fid) values(with, uid);
	 * 
	 * @param uid 当前用户id
	 * @param with 好友id
	 */
	public void addBuddy(String uid, String with) {
		// TODO Auto-generated method stub
	}

}
