/*
 * WebimHistory.java
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

/* CREATE TABLE webim_histories (
 * 	`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 * 	`send` tinyint(1) DEFAULT NULL,
 * 	`type` varchar(20) DEFAULT NULL,
 * 	`to` varchar(50) NOT NULL,
 * 	`from` varchar(50) NOT NULL,
 * 	`nick` varchar(20) DEFAULT NULL COMMENT 'from nick',
 * 	`body` text,
 * 	`style` varchar(150) DEFAULT NULL,
 *	`timestamp` double DEFAULT NULL,
 *	`todel` tinyint(1) NOT NULL DEFAULT '0',
 *	`fromdel` tinyint(1) NOT NULL DEFAULT '0',
 *	`createdat` date DEFAULT NULL,
 *	`updatedat` date DEFAULT NULL,
 *	PRIMARY KEY (`id`),
 *	KEY `todel` (`todel`),
 *	KEY `fromdel` (`fromdel`),
 *	KEY `timestamp` (`timestamp`),
 *	KEY `to` (`to`),
 *	KEY `from` (`from`),
 *	KEY `send` (`send`)
 * ) ENGINE=MyISAM;
 */

import java.util.Date;

/**
 * Webim历史消息
 * 
 * @author Ery Lee <ery.lee @ gmail.com>
 * @since 1.0
 */
public class WebimHistory {

	private long id;
	private String type = "chat";
	private int send = 1;
	private String to;
	private String from;
	private String nick;
	private String body;
	private String style = "";
	private double timestamp;
	private Date createdat;

	public WebimHistory() {

	}

	public WebimHistory(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSend() {
		return send;
	}

	public void setSend(int i) {
		this.send = i;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	public Date getCreatedat() {
		return createdat;
	}

	public void setCreatedat(Date createdat) {
		this.createdat = createdat;
	}

	public String toString() {
		return String.format("History(id=%d, from=%s, to=%s, nick=%s, body=%s",
				id, from, to, nick, body);
	}

}