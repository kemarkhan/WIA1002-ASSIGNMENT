-- MySQL dump 10.13  Distrib 8.0.37, for Win64 (x86_64)
--
-- Host: localhost    Database: nba
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `injuryreserve`
--

DROP TABLE IF EXISTS `injuryreserve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `injuryreserve` (
  `id` int NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `injury` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `injuryreserve`
--

LOCK TABLES `injuryreserve` WRITE;
/*!40000 ALTER TABLE `injuryreserve` DISABLE KEYS */;
INSERT INTO `injuryreserve` VALUES (13932867,'Added to Injury Reserve','test','Michael Porter Jr.'),(13932868,'Added to Injury Reserve','test','Aaron Gordon'),(13932869,'Added to Injury Reserve','test','Nikola Jokic'),(13932870,'Added to Injury Reserve','test','Kentavious Caldwell-Pope');
/*!40000 ALTER TABLE `injuryreserve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nba_teams`
--

DROP TABLE IF EXISTS `nba_teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nba_teams` (
  `team_id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(50) NOT NULL,
  `team_name` varchar(50) NOT NULL,
  PRIMARY KEY (`team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nba_teams`
--

LOCK TABLES `nba_teams` WRITE;
/*!40000 ALTER TABLE `nba_teams` DISABLE KEYS */;
INSERT INTO `nba_teams` VALUES (1,'San Antonio','Spurs'),(2,'Golden State','Warriors'),(3,'Boston','Celtics'),(4,'Miami','Heat'),(5,'Los Angeles','Lakers'),(6,'Phoenix','Suns'),(7,'Orlando','Magic'),(8,'Denver','Nuggets'),(9,'Oklahoma City','Thunder'),(10,'Houston','Rockets');
/*!40000 ALTER TABLE `nba_teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `queue`
--

DROP TABLE IF EXISTS `queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `queue` (
  `name` varchar(255) DEFAULT NULL,
  `ID` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `queue`
--

LOCK TABLES `queue` WRITE;
/*!40000 ALTER TABLE `queue` DISABLE KEYS */;
INSERT INTO `queue` VALUES ('Max Christie',13932890),('Rui Hachimura',13932891);
/*!40000 ALTER TABLE `queue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `height` varchar(50) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL,
  `salary` double DEFAULT NULL,
  `points` double DEFAULT NULL,
  `rebounds` double DEFAULT NULL,
  `assists` double DEFAULT NULL,
  `steals` double DEFAULT NULL,
  `blocks` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (13932888,'Austin Reaves','6-5',197,'G',1000,14,8,4,2,0),(13932889,'D\'Angelo Russell','6-3',193,'G',1000,11,4,7,1,0),(13932890,'Max Christie','6-5',190,'G',1000,0,0,0,0,0),(13932891,'Rui Hachimura','6-8',230,'F',1000,6,3,0,0,0);
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teamdistances`
--

DROP TABLE IF EXISTS `teamdistances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teamdistances` (
  `distance_id` int NOT NULL AUTO_INCREMENT,
  `source_team_id` int NOT NULL,
  `destination_team_id` int NOT NULL,
  `distance_km` int NOT NULL,
  PRIMARY KEY (`distance_id`),
  KEY `source_team_id` (`source_team_id`),
  KEY `destination_team_id` (`destination_team_id`),
  CONSTRAINT `teamdistances_ibfk_1` FOREIGN KEY (`source_team_id`) REFERENCES `nba_teams` (`team_id`),
  CONSTRAINT `teamdistances_ibfk_2` FOREIGN KEY (`destination_team_id`) REFERENCES `nba_teams` (`team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teamdistances`
--

LOCK TABLES `teamdistances` WRITE;
/*!40000 ALTER TABLE `teamdistances` DISABLE KEYS */;
INSERT INTO `teamdistances` VALUES (1,2,5,554),(2,2,8,1507),(3,2,9,2214),(4,2,10,1901),(5,5,6,577),(6,5,9,1901),(7,8,9,942),(8,9,6,678),(9,9,1,983),(10,9,10,778),(11,10,1,500),(12,10,7,458),(13,10,3,2584),(14,10,4,268),(15,8,3,2845),(16,7,4,268),(17,3,4,3045),(18,7,1,1137);
/*!40000 ALTER TABLE `teamdistances` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-22 20:32:42
