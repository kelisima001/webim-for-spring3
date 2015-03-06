<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head>
<body>
<div id="result">
[{"name":"XXX.zip","size":57716,"type":"application\/zip","url":"http:\/\/d.nextalk.im\/webim-for-php5\/webim\/\/static\/images\/files\/1425615513-6097-823.zip","deleteUrl":"http:\/\/d.nextalk.im\/webim-for-php5\/webim\/\/static\/images\/?file=1425615513-6097-823.zip","deleteType":"DELETE"}]
</div>
<script type="text/javascript">
var result = "[{\"name\":\"XXX.zip\",\"size\":57716,\"type\":\"application\\\/zip\",\"url\":\"http:\\\/\\\/d.nextalk.im\\\/webim-for-php5\\\/webim\\\/\\\/static\\\/images\\\/files\\\/1425615513-6097-823.zip\",\"deleteUrl\":\"http:\\\/\\\/d.nextalk.im\\\/webim-for-php5\\\/webim\\\/\\\/static\\\/images\\\/?file=1425615513-6097-823.zip\",\"deleteType\":\"DELETE\"}]";
window.name = result;
try {
	var target = parent && parent.postMessage 
		? parent 
		: (parent && parent.document.postMessage ? parent.document : undefined);

	if (typeof target != "undefined") {
		target.postMessage(result, "*");
	}
} catch (e) {/**/}
</script>
</body>
</html>
