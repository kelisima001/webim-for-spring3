/*
 * WebimConfig.java
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 *
 * 测试的配置文件，配置消息服务器的地址、端口、通信DOMAIN、APIKEY<br>
 * 
 * 默认配置写在Webim.properties文件.
 *
 * @author erylee <ery.lee at gmail.com>
 *
 */
@Service("webimConfig")
public class WebimConfig {

	private Map<String, Object> data = new HashMap<String, Object>();

	public WebimConfig() {
		Properties prop = new Properties();
		try {
			prop.load(getClass().getResourceAsStream("/webim/Webim.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<Object> keys = prop.keySet();
		for (Iterator<Object> it = keys.iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = prop.getProperty(key);
			key = key.substring("webim.".length());
			if ("true".equals(value) || "false".equals(value)) {
				data.put(key, Boolean.valueOf(value));
			} else {
				data.put(key, value);
			}
		}
	}

	public Object get(String key) {
		return data.get(key);
	}

	public boolean getBoolean(String key) {
		return ((Boolean) data.get(key)).booleanValue();
	}

}
