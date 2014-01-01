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

1. Create webim_histories table:

 CREATE TABLE webim_histories (
	    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
	    `send` tinyint(1) DEFAULT NULL,
	    `type` varchar(20) DEFAULT NULL,
	    `to` varchar(50) NOT NULL,
	    `from` varchar(50) NOT NULL,
	    `nick` varchar(20) DEFAULT NULL COMMENT 'from nick',
	    `body` text,
	    `style` varchar(150) DEFAULT NULL,
	    `timestamp` double DEFAULT NULL,
	    `todel` tinyint(1) NOT NULL DEFAULT '0',
	    `fromdel` tinyint(1) NOT NULL DEFAULT '0',
	    `created_at` date DEFAULT NULL,
	    `updated_at` date DEFAULT NULL,
	    PRIMARY KEY (`id`),
	    KEY `todel` (`todel`),
	    KEY `fromdel` (`fromdel`),
	    KEY `timestamp` (`timestamp`),
	    KEY `to` (`to`),
	    KEY `from` (`from`),
	    KEY `send` (`send`)
	) ENGINE=MyISAM;

2. Create webim_settings table:

  CREATE TABLE webim_settings(
 	    `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
	    `uid` mediumint(8) unsigned NOT NULL,
	    `web` blob,
	    `air` blob,
	    `created_at` DATETIME DEFAULT NULL,
	    `updated_at` DATETIME DEFAULT NULL,
	    PRIMARY KEY (`id`)
	)ENGINE=MyISAM;

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
