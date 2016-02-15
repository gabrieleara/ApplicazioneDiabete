CREATE DATABASE  IF NOT EXISTS `diabete` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `diabete`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: diabete
-- ------------------------------------------------------
-- Server version	5.6.21

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
-- Table structure for table `glicemia`
--

DROP TABLE IF EXISTS `glicemia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `glicemia` (
  `paziente` varchar(30) NOT NULL,
  `tempo` datetime NOT NULL,
  `valore` double NOT NULL,
  PRIMARY KEY (`paziente`,`tempo`),
  CONSTRAINT `glicemia_ibfk_1` FOREIGN KEY (`paziente`) REFERENCES `paziente` (`paziente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `glicemia`
--

LOCK TABLES `glicemia` WRITE;
/*!40000 ALTER TABLE `glicemia` DISABLE KEYS */;
INSERT INTO `glicemia` VALUES ('Annalisa Gioli','2016-02-15 00:00:00',110),('Annalisa Gioli','2016-02-15 00:15:00',100),('Annalisa Gioli','2016-02-15 00:30:00',90),('Annalisa Gioli','2016-02-15 00:45:00',70),('Annalisa Gioli','2016-02-15 01:00:00',60),('Annalisa Gioli','2016-02-15 01:15:00',50),('Annalisa Gioli','2016-02-15 01:30:00',60),('Annalisa Gioli','2016-02-15 01:45:00',65),('Annalisa Gioli','2016-02-15 02:00:00',67),('Annalisa Gioli','2016-02-15 02:15:00',70),('Annalisa Gioli','2016-02-15 02:30:00',100),('Annalisa Gioli','2016-02-15 02:45:00',120),('Annalisa Gioli','2016-02-15 03:00:00',125),('Annalisa Gioli','2016-02-15 03:15:00',120),('Annalisa Gioli','2016-02-15 03:30:00',117),('Annalisa Gioli','2016-02-15 03:45:00',118),('Annalisa Gioli','2016-02-15 04:00:00',126),('Annalisa Gioli','2016-02-15 04:15:00',140),('Annalisa Gioli','2016-02-15 04:30:00',170),('Annalisa Gioli','2016-02-15 04:45:00',190),('Annalisa Gioli','2016-02-15 05:00:00',221),('Annalisa Gioli','2016-02-15 05:15:00',220),('Annalisa Gioli','2016-02-15 05:30:00',189),('Annalisa Gioli','2016-02-15 05:45:00',210),('Annalisa Gioli','2016-02-15 06:00:00',230),('Annalisa Gioli','2016-02-15 06:15:00',250),('Annalisa Gioli','2016-02-15 06:30:00',276),('Annalisa Gioli','2016-02-15 06:45:00',290),('Annalisa Gioli','2016-02-15 07:00:00',300),('Annalisa Gioli','2016-02-15 07:15:00',310),('Annalisa Gioli','2016-02-15 07:30:00',290),('Annalisa Gioli','2016-02-15 07:45:00',280),('Annalisa Gioli','2016-02-15 08:00:00',240),('Annalisa Gioli','2016-02-15 08:15:00',220),('Annalisa Gioli','2016-02-15 08:30:00',210),('Annalisa Gioli','2016-02-15 08:45:00',180),('Annalisa Gioli','2016-02-15 09:00:00',120),('Annalisa Gioli','2016-02-15 09:15:00',110),('Annalisa Gioli','2016-02-15 09:30:00',120),('Annalisa Gioli','2016-02-15 09:45:00',110),('Annalisa Gioli','2016-02-15 10:00:00',120),('Annalisa Gioli','2016-02-15 10:15:00',110),('Annalisa Gioli','2016-02-15 10:30:00',90),('Annalisa Gioli','2016-02-15 10:45:00',100),('Annalisa Gioli','2016-02-15 11:00:00',100),('Annalisa Gioli','2016-02-15 11:15:00',110),('Annalisa Gioli','2016-02-15 11:30:00',90),('Annalisa Gioli','2016-02-15 11:45:00',80),('Annalisa Gioli','2016-02-15 12:00:00',70),('Annalisa Gioli','2016-02-15 12:15:00',60),('Annalisa Gioli','2016-02-15 12:30:00',50),('Annalisa Gioli','2016-02-15 12:45:00',60),('Annalisa Gioli','2016-02-15 13:00:00',100),('Annalisa Gioli','2016-02-15 13:15:00',110),('Annalisa Gioli','2016-02-15 13:30:00',120),('Annalisa Gioli','2016-02-15 13:45:00',130),('Annalisa Gioli','2016-02-15 14:00:00',140),('Annalisa Gioli','2016-02-15 14:15:00',150),('Annalisa Gioli','2016-02-15 14:30:00',160),('Annalisa Gioli','2016-02-15 14:45:00',170),('Annalisa Gioli','2016-02-15 15:00:00',190),('Annalisa Gioli','2016-02-15 15:15:00',200),('Annalisa Gioli','2016-02-15 15:30:00',210),('Annalisa Gioli','2016-02-15 15:45:00',240),('Annalisa Gioli','2016-02-15 16:00:00',235),('Annalisa Gioli','2016-02-15 16:15:00',250),('Annalisa Gioli','2016-02-15 16:30:00',260),('Annalisa Gioli','2016-02-15 16:45:00',220),('Annalisa Gioli','2016-02-15 17:00:00',210),('Annalisa Gioli','2016-02-15 17:15:00',190),('Annalisa Gioli','2016-02-15 17:30:00',180),('Annalisa Gioli','2016-02-15 17:45:00',187),('Annalisa Gioli','2016-02-15 18:00:00',177),('Annalisa Gioli','2016-02-15 18:15:00',175),('Annalisa Gioli','2016-02-15 18:30:00',140),('Annalisa Gioli','2016-02-15 18:45:00',110),('Annalisa Gioli','2016-02-15 19:00:00',90),('Annalisa Gioli','2016-02-15 19:15:00',80),('Annalisa Gioli','2016-02-15 19:30:00',70),('Annalisa Gioli','2016-02-15 19:45:00',75),('Annalisa Gioli','2016-02-15 20:00:00',90),('Annalisa Gioli','2016-02-15 20:15:00',110),('Annalisa Gioli','2016-02-15 20:30:00',105),('Annalisa Gioli','2016-02-15 20:45:00',95),('Annalisa Gioli','2016-02-15 21:00:00',98),('Annalisa Gioli','2016-02-15 21:15:00',110),('Annalisa Gioli','2016-02-15 21:30:00',115),('Annalisa Gioli','2016-02-15 21:45:00',116),('Annalisa Gioli','2016-02-15 22:00:00',109),('Annalisa Gioli','2016-02-15 22:15:00',108),('Annalisa Gioli','2016-02-15 22:30:00',105),('Annalisa Gioli','2016-02-15 22:45:00',106),('Annalisa Gioli','2016-02-15 23:00:00',100),('Annalisa Gioli','2016-02-15 23:15:00',96),('Annalisa Gioli','2016-02-15 23:30:00',98),('Annalisa Gioli','2016-02-15 23:45:00',80);
/*!40000 ALTER TABLE `glicemia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insulina`
--

DROP TABLE IF EXISTS `insulina`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `insulina` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paziente` varchar(30) NOT NULL,
  `tempo` datetime NOT NULL,
  `tipo` tinyint(1) NOT NULL,
  `unita` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `paziente` (`paziente`),
  CONSTRAINT `insulina_ibfk_1` FOREIGN KEY (`paziente`) REFERENCES `paziente` (`paziente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insulina`
--

LOCK TABLES `insulina` WRITE;
/*!40000 ALTER TABLE `insulina` DISABLE KEYS */;
/*!40000 ALTER TABLE `insulina` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paziente`
--

DROP TABLE IF EXISTS `paziente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paziente` (
  `paziente` varchar(30) NOT NULL,
  PRIMARY KEY (`paziente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paziente`
--

LOCK TABLES `paziente` WRITE;
/*!40000 ALTER TABLE `paziente` DISABLE KEYS */;
INSERT INTO `paziente` VALUES ('Annalisa Gioli'),('Neil Young'),('Primo Carnera'),('Syd Barrett'),('Woody Allen');
/*!40000 ALTER TABLE `paziente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-15 11:15:31
