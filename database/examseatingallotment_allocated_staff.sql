-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: examseatingallotment
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `allocated_staff`
--

DROP TABLE IF EXISTS `allocated_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `allocated_staff` (
  `staffid` varchar(5) NOT NULL,
  `date` date NOT NULL,
  `hallno` varchar(255) NOT NULL,
  `seatnos` varchar(255) NOT NULL,
  PRIMARY KEY (`staffid`,`date`,`hallno`,`seatnos`),
  KEY `date` (`date`),
  KEY `hallno` (`hallno`),
  CONSTRAINT `allocated_staff_ibfk_1` FOREIGN KEY (`staffid`) REFERENCES `staff` (`staffid`),
  CONSTRAINT `allocated_staff_ibfk_2` FOREIGN KEY (`date`) REFERENCES `exam_timetable` (`date`),
  CONSTRAINT `allocated_staff_ibfk_3` FOREIGN KEY (`hallno`) REFERENCES `halls` (`hallno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `allocated_staff`
--

LOCK TABLES `allocated_staff` WRITE;
/*!40000 ALTER TABLE `allocated_staff` DISABLE KEYS */;
INSERT INTO `allocated_staff` VALUES ('S101','2023-12-01','A401','1-20'),('S102','2023-12-01','A401','21-30'),('S103','2023-12-01','A402','1-20'),('S104','2023-12-01','A403','1-20'),('S105','2023-12-01','A403','21-30'),('S106','2023-12-02','A401','1-20'),('S107','2023-12-02','A401','21-30'),('S108','2023-12-02','A402','1-20'),('S109','2023-12-02','A403','1-20'),('S110','2023-12-02','A403','21-30'),('S101','2023-12-03','A401','1-20'),('S102','2023-12-03','A401','21-30'),('S103','2023-12-03','A402','1-20'),('S104','2023-12-03','A403','1-20'),('S105','2023-12-03','A403','21-30'),('S106','2023-12-04','A401','1-20'),('S107','2023-12-04','A401','21-30'),('S108','2023-12-04','A402','1-20'),('S109','2023-12-04','A403','1-20'),('S110','2023-12-04','A403','21-30');
/*!40000 ALTER TABLE `allocated_staff` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-06 15:58:18
