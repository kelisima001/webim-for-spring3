开发指南
=================

1. 复制代码
------------------------------

WebimProject/
    src/
        webim/

代码复制后，可根据项目包命名规则，重新修改包名称。

例如，修改为: com.example.webim

2. 复制类库
------------------------------

WebimProject/
    WebContent/
        WEB-INF/
            lib/
                org.json-20120521.jar
                webim.client-5.5.2-20140705.jar

3. 复制JSP文件
------------------------------

WebimProject/
    WebContent/
        WEB-INF/
            jsp/
                Webim/


4. 复制前端静态资源文件
------------------------------

WebimProject/
    WebContent/
        static/

5. 配置spring的servlet文件

component-scan新增"webim"包，参考:

WebimProject/
    WebContent/
        WEB-INF/
            webim-servlet.xml*


6. 启动项目，访问Webim/boot.html页面(注: 后缀根据spring配置可能不同)

7. 按README.md和代码注释，开发WebimPlugin.java, WebimModel.java接口



