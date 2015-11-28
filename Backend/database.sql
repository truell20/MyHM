-- MySQL dump 10.16  Distrib 10.1.8-MariaDB, for Win32 (AMD64)
--
-- Host: ApPosition.db.12061709.hostedresource.com    Database: ApPosition
-- ------------------------------------------------------
-- Server version	5.5.43-37.2-log

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
-- Table structure for table `Meeting`
--

DROP TABLE IF EXISTS `Meeting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Meeting` (
  `meetingID` mediumint(9) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `beginning` datetime NOT NULL,
  `end` datetime NOT NULL,
  PRIMARY KEY (`meetingID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Meeting`
--

LOCK TABLES `Meeting` WRITE;
/*!40000 ALTER TABLE `Meeting` DISABLE KEYS */;
INSERT INTO `Meeting` VALUES (1,'ApPosition Meeting','2015-10-21 00:00:00','2015-10-21 00:09:00');
/*!40000 ALTER TABLE `Meeting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MeetingToUser`
--

DROP TABLE IF EXISTS `MeetingToUser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MeetingToUser` (
  `meetingID` mediumint(8) unsigned NOT NULL,
  `userID` mediumint(8) unsigned NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MeetingToUser`
--

LOCK TABLES `MeetingToUser` WRITE;
/*!40000 ALTER TABLE `MeetingToUser` DISABLE KEYS */;
INSERT INTO `MeetingToUser` VALUES (1,1);
/*!40000 ALTER TABLE `MeetingToUser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Period`
--

DROP TABLE IF EXISTS `Period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Period` (
  `userID` mediumint(8) unsigned NOT NULL,
  `day` tinyint(3) unsigned NOT NULL,
  `period` tinyint(3) unsigned NOT NULL,
  `name` varchar(30) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Period`
--

LOCK TABLES `Period` WRITE;
/*!40000 ALTER TABLE `Period` DISABLE KEYS */;
INSERT INTO `Period` VALUES (1,0,0,'French');
INSERT INTO `Period` VALUES (1,0,2,'Bach to Stravinsky');
/*!40000 ALTER TABLE `Period` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `userID` mediumint(9) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(24) NOT NULL,
  `lastname` varchar(24) NOT NULL,
  `email` varchar(40) NOT NULL,
  `password` varchar(15) NOT NULL,
  `currentLocation` varchar(20) DEFAULT NULL,
  `lastTimeLocationUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'Michael','Truell','michael_truell@horacemann.org','password','Tillinghast','2015-10-19 17:49:53');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-28 17:43:46
