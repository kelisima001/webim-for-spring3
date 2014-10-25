# WebIM for Spring3


## 简介

[NexTalk](http://nextalk.im)为Spring框架提供的快速WebIM开发包。可为Spring框架开发站点或应用提供立即可用的站内即时消息。

Spring3开发包以代码接口方式，与站点的用户体系、好友关系、数据库无缝集成。WebIM的前端界面，集成后直接嵌入站点右下角。并支持在站点页面的任意位置，添加聊天按钮:

![Spring3 Screenshot](http://nextalk.im/static/img/screenshots/spring3.png)

## NexTalk

***NexTalk***是基于WEB标准协议设计的，主要应用于WEB站点的，简单开放的即时消息系统。可快速为社区微博、电子商务、企业应用集成即时消息服务。

NexTalk架构上分解为：***WebIM业务服务器*** + ***消息路由服务器*** 两个独立部分，遵循 ***Open Close***的架构设计原则。WebIM插件方式与第三方的站点或应用的用户体系开放集成，独立的消息服务器负责稳定的连接管理、消息路由和消息推送。

![NexTalk Architecture](http://nextalk.im/static/img/design/WebimForSpring.png)

NexTalk的架构设计上有以下几个特点：

1. 开放设计，通过开放源码的WebIM开发包，可与站点或应用的用户体系、群组关系、消息处理无缝集成。
2. 快速集成，NexTalk为常用的WEB框架提供了直接可用的开发包。5分钟启动demo，1天可完成开发集成。
3. 方便定制，WebIM插件和前端代码全部开源，好友关系、消息路由全部可通过WebIM插件接口定制或扩展。例如实现动态好友关系，消息拦截过滤等。

## 功能列表

功能 | 发布版本
---- | ----
集成在浏览器右下⾓前端界⾯ | 1.0
一对一聊天 (站点访客、⽤户、管理员间即时聊天) | 1.0
群组聊天(聊天室)，临时讨论组聊天 | 1.0
⺴站在线客服，访客与客服聊天 | 3.0
⺴站⻚⾯嵌⼊聊天按钮，例如"在线客服" | 3.0
离线好友显⽰，发送离线消息 | 5.0
⽤户现场状态设置 | 1.0
⽤户间发送表情 | 1.0
用户间传送图⽚、⽂件 | 3.0
消息拦截、过滤、敏感词处理 | 4.0
简单的聊天机器人支持 | 5.0
可移动聊天窗口支持 | 5.0
手机版独立聊天窗口 | 5.5
界⾯菜单隐藏或定制，界⾯透明背景、缩放⽀持 | 3.0
⽤户界⾯提⽰⾳、收缩⼯具条、弹出窗⼝设置 | 3.0
简单的开源桌⾯客户端 | 5.0
Android手机客户端SDK | 6.0
iOS手机客户端SDK | 6.0

## 开发指南

### 源码下载

[http://nextalk.im/packages/spring3](http://nextalk.im/packages/spring3)

### 运行演示

Webim for Spring3开发包，自带WebimProject的演示项目，导入Eclipse即可运行。

1. 导入'WebimProject'项目到[Eclipse EE](http://eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/lunasr1)
2. Eclipse中创建Tomcat Server，启动项目: 'Run as' -> 'Run on Server' 
3. 浏览器访问: http://localhost:8080/WebimProject/

### 项目集成

#### 集成代码

```
WebimProject/
	src/
		webim/
			controller/
			dao/
			service
			
```

注: 默认'webim'包的名称，可根据项目包命名规则重构。例如修改为: 'com.example.webim'。

#### 集成依赖库

```
WebimProject/
	WebContent/
		WEB-INF/ 
			lib/
				org.json-20120521.jar
				webim.client-5.7.1-20141012.jar
```

#### 集成JSP文件

```
WebimProject/
	WebContent/
		WEB-INF/
			jsp/
				Webim/
```

#### 集成前端静态资源

```
WebimProject/
	WebContent/
		static/
```

#### Spring配置文件

spring配置文件component-scan增加"webim"包。

#### 启动项目验证

启动Spring项目，访问Webim/boot.do页面(注: 后缀根据spring配置可能不同)。

成功应返回一段javascript，内容类似:

```
var _IMC = {
	product: 'spring3',
   version: '5.7',
   path: '/WebimProject/',
   is_login: '1',
   is_visitor: false,
   user: '',
   setting: {},
   menu: '',
   enable_chatlink: true,
   enable_shortcut: false,
   enable_menu: false,
   enable_room: true,
   enable_noti: true,
   discussion: true,
   theme: 'base',
   local: 'zh-CN',
   jsonp: false,
   opacity: '80',
   show_unavailable: true,
   upload: false,
   min: window.location.href.indexOf("webim_debug") != -1 ? "" : ".min"};

   _IMC.script = window.webim ? '' : ('<link href="' + _IMC.path + 'static/webim'+ _IMC.min + '.css?' + _IMC.version + '" media="all" type="text/css" rel="stylesheet"/><link href="' + _IMC.path + 'static/themes/' + _IMC.theme + '/jquery.ui.theme.css?' + _IMC.version + '" media="all" type="text/css" rel="stylesheet"/><script src="' + _IMC.path + 'static/webim' + _IMC.min + '.js?' + _IMC.version + '" type="text/javascript"></script><script src="' + _IMC.path + 'static/i18n/webim-' + _IMC.local + '.js?' + _IMC.version + '" type="text/javascript"></script>');
   _IMC.script += '<script src="' + _IMC.path + 'static/webim.'+ _IMC.product + '.js?' + _IMC.version + '" type="text/javascript"></script>';
   document.write( _IMC.script );
```

### 实现WebimPlugin

WebimPlugin.java是与Spring项目的用户体系、好友关系以及群组关系集成类。

好友关系集成方法: 

方法名 | 参数 | 返回 |  说明
---- | ---- | ---- |   ---- | 
endpoint | HttpServletRequest | WebimEndpoint | 根据当前登陆用户，返回WebimEndpoint对象。当前登陆用户信息一般从session或项目的用户服务读取。
buddies | uid | WebimEndpoint List |  根据当前登陆用户ID，读取该用户的好友列表
buddiesByIds | uid, ids| WebimEndpoint List |  根据输入的用户id列表(ids)，返回用户列表

群组关系集成方法(***如不支持群组，无需实现***):
 
方法名 | 参数 | 返回 | 说明
---- | ---- | ---- |  ---- 
findRoom | roomId | WebimRoom | 根据roomId查找room
rooms | uid | WebimRoom List | 根据当前用户ID，返回该用户的room列表
roomsByIds | uid, ids | WebimRoom List | 根据群组id列表(ids)，返回群组列表
members | roomId | WebimMember List | 根据roomId返回群组成员列表

其他方法，包括敏感词、机器人、菜单、通知，详见WebimPlugin.java代码。

### 创建数据库表

WebIM自身需要创建几张数据库表，用于保存聊天记录、用户设置、临时讨论组、访客信息。

默认MySQL的数据库脚本，在install.sql文件。Oracle以及其他数据库脚本，请联系NexTalk。

数据库表 | 说明
--------- | ------
webim_histories |  历史聊天记录表
webim_settings | 用户个人WebIM设置表
webim_buddies | 好友关系表(注: 如果项目没有自身的好友关系，可以通过该表存储)
webim_visitors | 访客信息表
webim_rooms | 临时讨论组表(注: WebPlugin中是集成站点的固定群组，webim_rooms表是存储WebIM自己的临时讨论组)
webim_members | 临时讨论组成员表
webim_blocked | 群组是否block

### 数据访问对象(Dao)

WebIM自身的数据库访问类(Dao)在'webim.dao'包。各项目ORM框架不同，WebIM没有提供默认实现，而是封装了Dao方法，并在代码注释中提供了详细Sql查询语句。


类 | 数据库表 | 是否必须 | 说明
---- | ---- | ---- | ---- | 
WebimHistoryDao | webim_histories | 是  | 历史聊天记录存储和查询
WebimSettingDao | webim_settings | 是 | 用户WebIM设置存储和访问
WebimRoomDao | webim_rooms, webim_members | 否 | 临时讨论组，讨论组成员(注: 如不支持讨论组，无需实现)
WebimBuddyDao | webim_buddies | 否 | 好友关系存储和查询(注: 如站点自身有好友个关系，无需实现)
WebimVisitorDao | webim_visitors | 否 | 访客存储和查询(注: 如不支持访客，无需实现)

### 配置参数

配置参数默认保存在Webim.properties文件，正式项目应将配置保存到数据库或者XML文件。

Webim.properties内容示例:

```
#############################
# Webim配置文件
#############################

# Webim插件版本
webim.version=5.7

# Webim插件是否开启
webim.isopen=true

# Webim消息服务器列表,逗号分割列表支持集群
webim.server=t1.nextalk.im:8000,t2.nextalk.im:8000

# Webim插件与消息服务器通信域名
webim.domain=localhost

# Webim插件与消息服务器通信APIKEY
webim.apikey=public

# Webim插件界面Theme
webim.theme=base

# Webim插件本地语言
webim.local=zh-CN

# Webim插件表情库
webim.emot=default

# Webim插件工具条透明度
webim.opacity=80

# Webim插件支持群组聊天
webim.enable_room=true

# Webim插件支持临时讨论组
webim.enable_discussion=true

# Webim插件显示通知按钮
webim.enable_noti=true

# Webim插件支持快捷工具栏
webim.enable_shortcut=false

# Webim插件支持聊天按钮
webim.enable_chatlink=true

# Webim插件显示菜单栏
webim.enable_menu=false

# Webim插件显示不在线好友
webim.show_unavailable=true

# Webim插件支持访客
webim.enable_visitor=true

# Webim插件支持文件上传
webim.enable_upload=false

# Webim插件是否支持JSONP的跨域请求
webim.jsonp=false

# Webim插件是否支持机器人
webim.robot=true
```

配置参数说明:

参数 | 类型  | 默认 | 说明
---- | ---- |---- | -----
webim.isopen | bool |  true | 是否开启WebIM
webim.server | string  | t.nextalk.im:8000 | WebIM消息服务器列表,逗号分割列表支持集群
webim.domain | string  | localhost | WebIM插件与消息服务器通信的认证域名
webim.apikey | string  | public | WebIM插件与消息服务器通信的认证APIKEY
webim.theme | string  | base | WebIM插件界面Theme
webim.local | string  | zh-CN | WebIM插件本地语言
webim.emot | string  | default | WebIM插件表情库: emot, qq
webim.opacity | inteter | 80 | WebIM插件工具条透明度
webim.enable_room | bool | true | WebIM插件是否支持群组聊天
webim.enable_discussion | bool | true  | WebIM插件支持临时讨论组
webim.enable_noti | bool | true   | WebIM插件显示通知按钮
webim.enable_shortcut | bool | false  |  WebIM插件支持快捷工具栏
webim.enable_chatlink | bool | true  |  WebIM插件支持聊天按钮
webim.enable_menu | bool | false  |  WebIM插件显示菜单栏
webim.show_unavailable | bool | true  |  WebIM插件显示不在线好友
webim.enable_visitor | bool | true  |  WebIM插件支持访客
webim.enable_upload | bool | false  |  WebIM插件支持文件上传
webim.jsonp | bool | false  |  WebIM插件是否支持JSONP的跨域请求
webim.robot | bool | true  |  WebIM插件是否支持机器人

### 定制界面

webim.spring3.js

### 开启运行

Spring站点或应用，在需要显示WebIM的页面，直接嵌入WebIM的boot链接:

```
<!-- 一般在footer或layout页面的</body>前，'do'后缀根据Spring配置修改 -->

<script type="text/javascript" src="Webim/boot.do"></script>
```

### 聊天按钮

Spring站点加载WebIM的页面，可以在任何位置添加下面的格式的“聊天按钮":

```
<a class="webim-chatbtn" href="/chat/1">Chat with User1</a>
<a class="webim-chatbtn" href="/chat/1">Chat with User2</a>
...
```

## 模型对象与客户端类


WebIM的通用Java模型对象和客户端类，打包在***WebContent\WEB-INF\lib\webim-client-$vsn-$date.jar***。


类/接口 | 说明
------ | ------
WebimClient | 与消息服务器通信的客户端类，采用JSON/HTTP协议设计
WebimCluster | 集群支持接口，将当前登陆用户按自定义规则分配到不同消息服务器
WebimEndpoint | 核心的即时消息路由端点类，标识一个用户、访客、服务或其他对象
WebimUser | 用户模型类，继承WebimEndpoint
WebimVisitor | 访客模型类，继承WebimEndpoint
WebimRobot | 机器人模型类，继承WebimEndpoint
WebimMessage | 即时消息模型类
WebimHistory | 历史消息模型类
WebimPresence | 现场状态模型类
WebimStatus | 状态模型类，例如用户正在输入
WebimRoom | 群组模型类
WebimMember | 群组成员模型类
WebimNotification | 站内通知
WebimMenu | 底栏按钮

#### WebimEndpoint

WebimEndpoint是WebIM的核心模型对象，标识一个即时消息路由端点。WebIM的用户、访客、服务或机器人对象继承该类。

WebimEndpoint对象属性:

属性 | 类型 | 是否必须 | 默认 | 说明
---- | ---- | ---- | ---- | ----- |
id | string | 是 | | 端点标识，一般为用户的uid。访客、机器人之类对象格式为: tag:id，例如访客的id: 'vid:v123'
nick | string | 是 || 端点(用户)昵称，界面显示该名称
presence | string | 是 | offline | 端点在线状态: online或offline
show | string | 是 | unavailable | 端点的详细现场状态: available, away, chat, dnd, invisible, unavailable
group | string | 是 | friend | 端点所属分组，界面显示的分组名称
url | string | 否 | | 端点(用户)主页	
avatar | string | 否 || 端点(用户)头像URL地址
status | string | 否 || 端点(用户)的状态签名，界面显示
status_time | string | 否 ||  状态最近更新时间

模型对象详细属性说明，请参考GitHub项目源码: [https://github.com/webim/webim-java](https://github.com/webim/webim-java)。

## 开发者(Developer)

公司: [NexTalk.IM](http://nextalk.im)

作者: [Feng Lee](mailto:feng.lee@nextalk.im) 

QQ: 1852861655

版本: 5.7.1 (2014/10/15)
 
