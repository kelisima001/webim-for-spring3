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
import java.util.Map;

import org.springframework.stereotype.Repository;

import webim.model.WebimVisitor;

/**
 * Webim访客数据访问对象
 * 
 * @author Feng Lee <feng.lee at nextalk.im>
 *
 */
@Repository("webimVisitorDao")
public class WebimVisitorDao {

	public WebimVisitor find(String vid) {
		//TODO: 
		WebimVisitor v = new WebimVisitor(vid, "V"+vid);
		v.setAvatar("static/images/male.png");
		return v;
	}

	public WebimVisitor insert(Map<String, String> data) {
		// TODO Auto-generated method stub
		String vid = data.get("name");
		WebimVisitor v = new WebimVisitor(data.get("name"), "V"+vid);
		v.setIpaddr(data.get("ipaddr"));
		v.setReferer(data.get("referer"));
		v.setGroup("visitor");
		v.setAvatar("static/images/male.png");
		return v;
	}

	public List<WebimVisitor> findAll(String[] vids) {
		List<WebimVisitor> list = new ArrayList<WebimVisitor>();
		for(int i = 0; i < vids.length; i++) {
			WebimVisitor v = new WebimVisitor(vids[i], "V"+vids[i]);
			v.setAvatar("static/images/male.png");
			list.add(v);
		}
		return list;
	}

}
