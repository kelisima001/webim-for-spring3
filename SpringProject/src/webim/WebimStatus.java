/*
 * WebimStatus.java
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

import java.util.Map;

/**
 * Webim用户状态，比如"正在输入..."
 * 
 * @author Ery Lee <ery.lee @ gmail.com>
 * @since 1.0 
 */
public class WebimStatus {

	private String to;
	private String show;
	private String status;

	public WebimStatus() {
	}
	
	public WebimStatus(String to, String show, String status) {
		this.setTo(to);
		this.setShow(show);
		this.setStatus(status);
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void feed(Map<String, String> data) {
		data.put("to", to);
		data.put("show", show);
		data.put("status", status);
	}

	public String toString() {
		return String.format("Status(to=%s, show=%s, status=%s)", to, show, status);
	}
	
}
