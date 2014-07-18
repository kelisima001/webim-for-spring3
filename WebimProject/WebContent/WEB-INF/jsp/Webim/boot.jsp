<%@ page language="java" contentType="text/javascript"
    pageEncoding="UTF8"%>

    var _IMC = {
                product: 'spring3',
                version: '${version}',
                path: '${context_path}/',
                is_login: '${is_login}',
                is_visitor: ${is_visitor},
                user: '',
                setting: '${setting}',
                menu: '',
                enable_chatlink: ${enable_chatlink},
                enable_shortcut: ${enable_shortcut},
                enable_menu: ${enable_menu},
                enable_room: ${enable_room},
                enable_noti: ${enable_noti},
                discussion: ${enable_discussion},
                theme: '${theme}',
                local: 'zh-CN',
                opacity: '${opacity}',
                show_unavailable: ${show_unavailable},
                upload: false,
                min: window.location.href.indexOf("webim_debug") != -1 ? "" : ".min"
            };

            _IMC.script = window.webim ? '' : ('<link href="' + _IMC.path + 'static/webim'+ _IMC.min + '.css?' + _IMC.version + '" media="all" type="text/css" rel="stylesheet"/><link href="' + _IMC.path + 'static/themes/' + _IMC.theme + '/jquery.ui.theme.css?' + _IMC.version + '" media="all" type="text/css" rel="stylesheet"/><script src="' + _IMC.path + 'static/webim' + _IMC.min + '.js?' + _IMC.version + '" type="text/javascript"></script><script src="' + _IMC.path + 'static/i18n/webim-' + _IMC.local + '.js?' + _IMC.version + '" type="text/javascript"></script>');

            _IMC.script += '<script src="' + _IMC.path + 'static/webim.'+ _IMC.product + '.js?' + _IMC.version + '" type="text/javascript"></script>';

            document.write( _IMC.script );
