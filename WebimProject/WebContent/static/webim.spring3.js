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
		buddies: path + "Webim/buddies.html",
        //room actions
		invite: path + "Webim/invite.html",
		join: path + "Webim/join.html",
		leave: path + "Webim/leave.html",
		block: path + "Webim/block.html",
		unblock: path + "Webim/unblock.html",
		members: path + "Webim/members.html",
        //notifications
		notifications: path + "Webim/notifications.html"
        //upload files
		upload: path + "Webim/upload.html",
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
		//layout: "layout.popup",
        layoutOptions: {
            unscalable: _IMC.is_visitor
        },
		buddyChatOptions: {
            downloadHistory: !_IMC.is_visitor,
			//simple: _IMC.is_visitor,
			upload: _IMC.upload && !_IMC.is_visitor
		},
		roomChatOptions: {
            downloadHistory: !_IMC.is_visitor,
			upload: _IMC.upload
		}
	}), im = ui.im;
    //全局化
    window.webimUI = ui;

	if( _IMC.user ) im.setUser( _IMC.user );
	if( _IMC.menu ) ui.addApp("menu", { "data": _IMC.menu } );
	if( _IMC.enable_shortcut ) ui.layout.addShortcut( _IMC.menu );

	ui.addApp("buddy", {
		showUnavailable: _IMC.show_unavailable,
		is_login: _IMC['is_login'],
		disable_login: true,
		collapse: false,
		//disable_user: _IMC.is_visitor,
        //simple: _IMC.is_visitor,
		loginOptions: _IMC['login_options']
	});
    if(!_IMC.is_visitor) {
        if( _IMC.enable_room )ui.addApp("room", { discussion: (_IMC.discussion && !_IMC.is_visitor) });
        if(_IMC.enable_noti )ui.addApp("notification");
    }
    if(_IMC.enable_chatlink) ui.addApp("chatbtn");
    ui.addApp("setting", {"data": webim.setting.defaults.data, "copyright": true});
	ui.render();
	_IMC['is_login'] && im.autoOnline() && im.online();
})(webim);
