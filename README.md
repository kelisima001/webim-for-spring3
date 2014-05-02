webim-for-spring3
=================

webim for spring mvc3.2

Demo
====

1. Import 'SprintProject' Project to Eclipse EE.

2. Create Tomcat Server and Run

3. Access: http://localhost:8080/SpringProject/

Developer Guide
===============

Create Database
---------------

import install.sql


Coding Dao
----------

1. WebimHistoryDao.java

2. WebimSettingDao.java

3. WebimDao.java


Coding Service
--------------

WebimService.java, modify these methods:

1. public long currentUid(): should return current login uid

2. public WebimEndpoint currentEndpoint(): should return current endpoint


Coding Config
-------------

You should change the WebimConfig.java, and load configurations from database or xml.

Insert Webim Javascript
-----------------------

Insert Javascript code below to web pages that need to display Webim:

	<script type="text/javascript" src="/SpringProject/Webim/boot.html"></script>
