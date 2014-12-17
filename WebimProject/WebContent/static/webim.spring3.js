//custom
(function(webim) {
	
	var path = _IMC.path;
	
	webim.extend(webim.setting.defaults.data, _IMC.setting);

	var suffix = _IMC.suffix ? _IMC.suffix : ".do";
	
	webim.route({
		online : path + "Webim/online" + suffix,
		offline : path + "Webim/offline" + suffix,
		buddies : path + "Webim/buddies" + suffix,
		deactivate : path + "Webim/refresh" + suffix,
		message : path + "Webim/message" + suffix,
		presence : path + "Webim/presence" + suffix,
		status : path + "Webim/status" + suffix,
		setting : path + "Webim/setting" + suffix,
		history : path + "Webim/history" + suffix,
		clear : path + "Webim/history/clear" + suffix,
		download : path + "Webim/history/download" + suffix,
		// room actions
		invite : path + "Webim/room/invite" + suffix,
		join : path + "Webim/room/join" + suffix,
		leave : path + "Webim/room/leave" + suffix,
		block : path + "Webim/room/block" + suffix,
		unblock : path + "Webim/room/unblock" + suffix,
		members : path + "Webim/room/members" + suffix,
		// notifications
		notifications : path + "Webim/notifications" + suffix,
		// upload files
		upload : path + "Webim/upload" + suffix
	});

	webim.ui.emot.init({
		"dir" : path + "static/images/emot/default"
	});
	var soundUrls = {
		lib : path + "static/assets/sound.swf",
		msg : path + "static/assets/sound/msg.mp3"
	};
	var ui = new webim.ui(document.body, {
		imOptions : {
			jsonp : _IMC.jsonp,
			domain: document.domain,
			storage: 'local'
		},
		soundUrls : soundUrls,
		// layout: "layout.popup",
		layoutOptions : {
			unscalable : _IMC.is_visitor
		},
		buddyChatOptions : {
			downloadHistory : !_IMC.is_visitor,
			// simple: _IMC.is_visitor,
			upload : _IMC.upload && !_IMC.is_visitor
		},
		roomChatOptions : {
			downloadHistory : !_IMC.is_visitor,
			upload : _IMC.upload
		}
	}), im = ui.im;
	// 全局化
	window.webimUI = ui;

	if (_IMC.user)
		im.setUser(_IMC.user);
	if (_IMC.menu)
		ui.addApp("menu", {
			"data" : _IMC.menu
		});
	if (_IMC.enable_shortcut)
		ui.layout.addShortcut(_IMC.menu);

	ui.addApp("buddy", {
		showUnavailable : _IMC.show_unavailable,
		is_login : _IMC['is_login'],
		disable_login : true,
		collapse : false,
		// disable_user: _IMC.is_visitor,
		// simple: _IMC.is_visitor,
		loginOptions : _IMC['login_options']
	});
	if (!_IMC.is_visitor) {
		if (_IMC.enable_room)
			ui.addApp("room", {
				discussion : (_IMC.discussion && !_IMC.is_visitor)
			});
		if (_IMC.enable_noti)
			ui.addApp("notification");
	}
	if (_IMC.enable_chatlink) {
		ui.addApp("chatbtn");
	}
	ui.addApp("setting", {
		"data" : webim.setting.defaults.data,
		"copyright" : true
	});
	ui.render();
	_IMC['is_login'] && im.autoOnline() && im.online();
})(webim);
