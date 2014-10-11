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
 * Webim用户设置数据访问对象 <br>
 * 
 * MySQL数据脚本: <br>
 * 
 * <pre>
 * DROP TABLE IF EXISTS webim_settings;
 * CREATE TABLE webim_settings (
 *     `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 *     `uid` varchar(40) NOT NULL DEFAULT '',
 *     `data` text,
 *     `created` datetime DEFAULT NULL,
 *     `updated` datetime DEFAULT NULL,
 *     UNIQUE KEY `webim_setting_uid` (`uid`),
 *     PRIMARY KEY (`id`)
 * ) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 *
 * Oracle数据脚本:
 * 
 * create table WEBIM_SETTINGS (
 *    USERNAME VARCHAR2(32),
 *    SETTING  VARCHAR2(1000)
 * );
 * -- Add comments to the columns 
 * comment on column WEBIM_SETTINGS.USERNAME is '用户名';
 * comment on column WEBIM_SETTINGS.SETTING is '配置信息';
 * 
 * </pre>
 * 
 * @author Feng Lee <feng.lee@nextalk.im>
 * 
 * @since 5.7
 */

@Repository("webimSettingDao")
public class WebimSettingDao {

	/**
	 * 读取用户配置数据，MySQL数据库查询SQL脚本：<br>
	 * 
	 * "select data from webim_settings where uid = ?"<br>
	 * 
	 * 如果data为空，返回: "{}"
	 * 
	 * @param uid
	 *            用户uid
	 * @return 配置数据，JSON格式
	 */
	public String get(String uid) {
		//TODO:
		return "{}";
	}
	
	/**
	 * 设置用户配置数据，MySQL数据库脚本: <br>
	 * 
	 * "update webim_settings set data = ?  where uid = ?" <br>
	 * 
	 * 应该先读取配置检查是否存在，不存在插入，否则更新。
	 * 
	 * @param uid
	 *            用户uid
	 * @param data
	 *            配置数据，JSON格式
	 */
	public void set(String uid, String data) {
		// TODO Auto-generated method stub
	}

}
