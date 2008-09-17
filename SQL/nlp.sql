/*
SQLyog Enterprise - MySQL GUI v6.15
MySQL - 5.0.45-community-nt-log : Database - nlp
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `nlp`;

USE `nlp`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `word_table` */

DROP TABLE IF EXISTS `word_table`;

CREATE TABLE `word_table` (
  `word` varchar(255) default NULL,
  `root` varchar(255) default NULL,
  `pos` varchar(255) default NULL,
  `indy` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`indy`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Data for the table `word_table` */

insert  into `word_table`(`word`,`root`,`pos`,`indy`) values ('build','build','verb',1),('building','build','noun',2),('building','build','noun',3),('built','build','verb',4),('go','go','vernb',5),('going','go','verb',6),('gone','go','verb',7),('went','go','verb',8),('house','house','noun',9),('house','house','verb',10),('house','house','adjective',11),('housing','house','verb',12),('housed','house','verb',13),('aeroplane','air','noun',14),('aircraft','air','noun',15),('aerobic','air','adjective',16),('aerate','air','verb',17),('erosion','erode','noun',18),('erode','erode','verb',19),('air','air','noun',20);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
