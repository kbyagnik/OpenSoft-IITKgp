CREATE DATABASE  IF NOT EXISTS `opensoft` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `opensoft`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: opensoft
-- ------------------------------------------------------
-- Server version	5.5.24-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `content`
--

DROP TABLE IF EXISTS `content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content` (
  `contentID` int(11) NOT NULL,
  `title` varchar(45) NOT NULL,
  `description` varchar(150) DEFAULT NULL,
  `link` varchar(100) NOT NULL,
  `category` varchar(10) NOT NULL,
  `size` int(11) NOT NULL,
  `rating` decimal(2,1) NOT NULL,
  `downloads` int(11) NOT NULL,
  `users_rated` int(11) NOT NULL,
  PRIMARY KEY (`contentID`),
  UNIQUE KEY `contentID_UNIQUE` (`contentID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content`
--

LOCK TABLES `content` WRITE;
/*!40000 ALTER TABLE `content` DISABLE KEYS */;
INSERT INTO `content` VALUES (1,'CSL 451 grades pdf file','This is a pdf file of grades of all students enrol...','text/grades.pdf','text',9167,3.0,6,1),(2,'king kong','This is an audio song named king kong. Language is English. Enjoy!','audio/kingkong.mp3','audio',3487868,4.0,3,2),(3,'facebook video','This is a facebook video for Aniket Sachdeva posted by Sajal Srivastava on his timeline.','video/facebook.mp4','video',1548477,0.0,0,0);
/*!40000 ALTER TABLE `content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `downloaded`
--

DROP TABLE IF EXISTS `downloaded`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `downloaded` (
  `contentID` int(11) NOT NULL,
  `uuid` varchar(45) NOT NULL,
  `date` int(11) NOT NULL,
  PRIMARY KEY (`contentID`,`uuid`,`date`),
  KEY `emailf_idx` (`uuid`),
  CONSTRAINT `contentIDf` FOREIGN KEY (`contentID`) REFERENCES `content` (`contentID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `downloaded`
--

LOCK TABLES `downloaded` WRITE;
/*!40000 ALTER TABLE `downloaded` DISABLE KEYS */;
INSERT INTO `downloaded` VALUES (1,'0',1422459876),(1,'0',1422460102),(1,'0',1422460250),(1,'0',1422460316),(1,'0',1422460693),(2,'0',1422460512),(2,'0',1422460530),(2,'0',1422460678);
/*!40000 ALTER TABLE `downloaded` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `emailID` varchar(45) NOT NULL,
  `password` char(32) NOT NULL,
  PRIMARY KEY (`emailID`),
  UNIQUE KEY `emailID_UNIQUE` (`emailID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('ankitk94@yahoo.com','b1f4f9a523e36fd969f4573e25af4540');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-01-28 21:39:04
