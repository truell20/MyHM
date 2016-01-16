-- phpMyAdmin SQL Dump
-- version 2.11.11.3
-- http://www.phpmyadmin.net
--
-- Host: 68.178.143.51
-- Generation Time: Jan 16, 2016 at 02:02 PM
-- Server version: 5.5.43
-- PHP Version: 5.1.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `ApPosition`
--

-- --------------------------------------------------------

--
-- Table structure for table `Meeting`
--

CREATE TABLE `Meeting` (
  `meetingID` mediumint(9) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `dayIndex` smallint(5) unsigned NOT NULL,
  `periodIndex` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`meetingID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=23 ;

--
-- Dumping data for table `Meeting`
--

INSERT INTO `Meeting` VALUES(1, 'Test Meeting', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `MeetingToUser`
--

CREATE TABLE `MeetingToUser` (
  `meetingID` mediumint(8) unsigned NOT NULL,
  `userID` mediumint(8) unsigned NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `MeetingToUser`
--

INSERT INTO `MeetingToUser` VALUES(1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Period`
--

CREATE TABLE `Period` (
  `userID` mediumint(8) unsigned NOT NULL,
  `day` tinyint(3) unsigned NOT NULL,
  `period` tinyint(3) unsigned NOT NULL,
  `name` varchar(30) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Period`
--

INSERT INTO `Period` VALUES(1, 0, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 2, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 3, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 4, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 5, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 6, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 7, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 8, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 9, 0, 'French 3 H');
INSERT INTO `Period` VALUES(1, 0, 2, 'Music History');
INSERT INTO `Period` VALUES(1, 2, 2, 'Music History');
INSERT INTO `Period` VALUES(1, 4, 2, 'Music History');
INSERT INTO `Period` VALUES(1, 7, 2, 'Music History');
INSERT INTO `Period` VALUES(1, 9, 2, 'Music History');
INSERT INTO `Period` VALUES(1, 0, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 1, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 2, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 3, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 4, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 5, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 6, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 7, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 8, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 9, 3, 'Alg 2 H');
INSERT INTO `Period` VALUES(1, 0, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 2, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 3, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 4, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 5, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 6, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 7, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 8, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 9, 4, 'English 10');
INSERT INTO `Period` VALUES(1, 0, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 1, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 2, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 3, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 4, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 5, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 6, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 7, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 8, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 9, 6, 'Physics H');
INSERT INTO `Period` VALUES(1, 0, 7, 'US History');
INSERT INTO `Period` VALUES(1, 1, 7, 'US History');
INSERT INTO `Period` VALUES(1, 3, 7, 'US History');
INSERT INTO `Period` VALUES(1, 4, 7, 'US History');
INSERT INTO `Period` VALUES(1, 5, 7, 'US History');
INSERT INTO `Period` VALUES(1, 6, 7, 'US History');
INSERT INTO `Period` VALUES(1, 7, 7, 'US History');
INSERT INTO `Period` VALUES(1, 8, 7, 'US History');
INSERT INTO `Period` VALUES(1, 9, 7, 'US History');

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `userID` mediumint(9) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(24) NOT NULL,
  `lastName` varchar(24) NOT NULL,
  `email` varchar(40) NOT NULL,
  `password` varchar(15) NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `User`
--

INSERT INTO `User` VALUES(1, 'Michael', 'Truell', 'michael_truell@horacemann.org', 'sa3tHJ3/KuYvI');
INSERT INTO `User` VALUES(3, 'Luca', 'Koval', 'luca_koval@horacemann.org', 'password');
INSERT INTO `User` VALUES(4, 'Ben', 'Spector', 'benjamin_spector@horacemann.org', 'password');
