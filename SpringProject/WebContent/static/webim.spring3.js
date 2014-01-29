//custom
(function(webim) {

	var path = _IMC.path;
	webim.extend(webim.setting.defaults.data, _IMC.setting);

	webim.route( {
		online: path + "Webim/online.html",
		offline: path + "Webim/offline.html",
		deactivate: path + "Webim/refresh.html",
		message: path + "Webim/message.html",
		presence: path + "Webim/presence.html",
		status: path + "Webim/status.html",
		setting: path + "Webim/setting.html",
		history: path + "Webim/history.html",
		clear: path + "Webim/history/clear.html",
		download: path + "Webim/history/download.html",
		members: path + "Webim/members.html",
		join: path + "Webim/join.html",
		leave: path + "Webim/leave.html",
		buddies: path + "Webim/buddies.html",
		upload: path + "Webim/upload.html",
		notifications: path + "Webim/notifications.html"
	} );

	webim.ui.emot.init({"dir": path + "static/images/emot/default"});
	var soundUrls = {
		lib: path + "static/assets/sound.swf",
		msg: path + "static/assets/sound/msg.mp3"
	};
	var ui = new webim.ui(document.body, {
		imOptions: {
			jsonp: _IMC.jsonp
		},
		soundUrls: soundUrls,
		buddyChatOptions: {
            downloadHistory: !_IMC.is_visitor,
			simple: _IMC.is_visitor,
			upload: _IMC.upload && !_IMC.is_visitor
		},
		roomChatOptions: {
			upload: _IMC.upload
		}
	}), im = ui.im;

	if( _IMC.user ) im.setUser( _IMC.user );
	if( _IMC.menu ) ui.addApp("menu", { "data": _IMC.menu } );
	if( _IMC.enable_shortcut ) ui.layout.addShortcut( _IMC.menu );

	ui.addApp("buddy", {
		showUnavailable: _IMC.show_unavailable,
		is_login: _IMC['is_login'],
		disable_login: true,
		collapse: false,
		disable_user: _IMC.is_visitor,
        simple: _IMC.is_visitor,
		loginOptions: _IMC['login_options']
	});
    if(!_IMC.is_visitor) {
        if( _IMC.enable_room) ui.addApp("room", { discussion: false });
        if( _IMC.enable_noti) ui.addApp("notification");
    }
    ui.addApp("setting", {"data": webim.setting.defaults.data});
	ui.render();
	_IMC['is_login'] && im.autoOnline() && im.online();
})(webim);
