/*
 * WebimMenu.java
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

/**
 * Webim菜单条，显示在下面条幅右侧。
 * 
 * @author Ery Lee <ery.lee at gmail.com>
 * @since 1.0
 */
public class WebimMenu {

	private String title;

	private String icon;

	private String link;

	public WebimMenu() {
		
	}
	public WebimMenu(String title, String icon, String link) {
		this.title = title;
		this.icon = icon;
		this.link = link;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String toString() {
		return String.format("Menu(title=%s, icon=%s, link=%s)", title, icon,
				link);
	}

}
