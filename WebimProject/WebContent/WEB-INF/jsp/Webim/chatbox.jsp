<%@ page language="java" contentType="text/html" pageEncoding="UTF8"%>
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0" name="viewport">
  <title>Webim ChatBox</title>
  <link rel="stylesheet" type="text/css" href="${context_path}/static/webim-chatbox.css"/>
  <script type="text/javascript" src="${context_path}/static/webim-chatbox.js"> </script>
  </head>
  
  <body id="chatbox">
  <div id="header">
  <img id="avatar" class="avatar" src="${context_path}/${avatar}"></img>
  <h4 id="user">${nick}</h4>
  </div>

  <div id="notice" class="chatbox-notice ui-state-highlight" style="display: none;"></div>
  <div id="content">
  <div id="histories"></div>
  </div>

  <div id="footer">
  <table style="width:100%"><tbody><tr><td width="100%">
  <input type="hidden" id="to" value="${uid}">
  <input type="text" data-inline="true" placeholder="请这里输入消息..." id="inputbox">
  </td></tr></tbody></table>
  </div>
  <script>
  (function(webim, options) {
	var path = options.path || "", suffix = options.suffix || "";
	function url(api) { return path + api + suffix; }
	webim.route({
		online: url("Webim/online"),
		offline: url("Webim/offline"),
		deactivate: url("Webim/refresh"),
		message: url("Webim/message"),
		presence: url("Webim/presence"),
		status: url("Webim/status"),
		setting: url("Webim/setting"),
		history: url("Webim/history"),
		buddies: url("Webim/buddies")
	});
    var im = new webim(null, options);
    var chatbox = new webim.chatbox(im, options);
    im.online();
  })(webim, {touid: '${uid}', path:'${context_path}/', suffix:'.do'});
  </script>
  </body>
</html>
  
