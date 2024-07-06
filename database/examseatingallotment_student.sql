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
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `rollno` varchar(6) NOT NULL,
  `s_name` varchar(50) DEFAULT NULL,
  `password` varchar(15) DEFAULT NULL,
  `yearofstudy` int DEFAULT NULL,
  `dept` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`rollno`),
  KEY `idx_yearofstudy` (`yearofstudy`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('20C101','Amit Kumar','password41',4,'CIV'),('20C102','Sneha Sharma','password42',4,'CIV'),('20C103','Rajesh Verma','password43',4,'CIV'),('20C104','Sarita Singh','password44',4,'CIV'),('20C105','Vikas Patel','password45',4,'CIV'),('20C112','Sarah Johnson','secret123',2,'CIV'),('20I101','Neha Malhotra','password46',4,'IT'),('20I102','Rahul Saini','password47',4,'IT'),('20I103','Vikram Kapoor','password48',4,'IT'),('20I104','Suman Yadav','password49',4,'IT'),('20I105','Rajeev Gupta','password50',4,'IT'),('20M101','Anita Raj','password51',4,'MECH'),('20M102','Vishal Kumar','password52',4,'MECH'),('20M103','Shreya Verma','password53',4,'MECH'),('20M104','Kunal Sharma','password54',4,'MECH'),('20M105','Sara Reddy','password55',4,'MECH'),('20Z101','Rohit Rajan','password56',4,'CSE'),('20Z102','Pooja Jain','password57',4,'CSE'),('20Z103','Rajat Tiwari','password58',4,'CSE'),('20Z104','Meera Rathi','password59',4,'CSE'),('20Z105','Aryan Kumar','password60',4,'CSE'),('21C112','aa','secret123',2,'COM'),('21C201','Ramesh Iyer',NULL,3,'CIV'),('21C202','Shweta Rajan',NULL,3,'CIV'),('21C203','Vikram Reddy',NULL,3,'CIV'),('21C204','Ananya Nair',NULL,3,'CIV'),('21C205','Rajeshwari Menon',NULL,3,'CIV'),('21I201','Rajesh Patel',NULL,3,'IT'),('21I202','Deepa Rao',NULL,3,'IT'),('21I203','Vijay Kumar',NULL,3,'IT'),('21I204','Kavita Reddy',NULL,3,'IT'),('21I205','Sanjay Rao',NULL,3,'IT'),('21M201','Anjali Kumar',NULL,3,'MECH'),('21M202','Manoj Menon',NULL,3,'MECH'),('21M203','Priya Suresh',NULL,3,'MECH'),('21M204','Suresh Nair',NULL,3,'MECH'),('21M205','Divya Rao',NULL,3,'MECH'),('21Z201','Surya Menon','abcd',3,'CSE'),('21Z202','Lakshmi Nair',NULL,3,'CSE'),('21Z203','Krishna Kumar',NULL,3,'CSE'),('21Z204','Meera Ramesh',NULL,3,'CSE'),('21Z205','Aruna Iyer',NULL,3,'CSE'),('22C101','Alice Kumar','password21',2,'CIV'),('22C102','Rahul Sharma','password22',2,'CIV'),('22C103','Neha Verma','password23',2,'CIV'),('22C104','Sandeep Singh','password24',2,'CIV'),('22C105','Priya Patel','password25',2,'CIV'),('22I101','Anjali Raj','password31',2,'IT'),('22I102','Vishal Singh','password32',2,'IT'),('22I103','Shreya Sharma','password33',2,'IT'),('22I104','Kunal Verma','password34',2,'IT'),('22I105','Sara Reddy','password35',2,'IT'),('22M101','Vikram Malhotra','password26',2,'MECH'),('22M102','Riya Saini','password27',2,'MECH'),('22M103','Amit Kapoor','password28',2,'MECH'),('22M104','Sonia Yadav','password29',2,'MECH'),('22M105','Arjun Gupta','password30',2,'MECH'),('22Z101','Rohit Rajan','password36',2,'CSE'),('22Z102','Pooja Jain','password37',2,'CSE'),('22Z103','Rajat Tiwari','password38',2,'CSE'),('22Z104','Meera Rathi','password39',2,'CSE'),('22Z105','Aryan Kumar','password40',2,'CSE'),('23C101','John Doe','password1',1,'CIV'),('23C102','Jane Smith','password2',1,'CIV'),('23C103','Michael Johnson','password3',1,'CIV'),('23C104','Emily Brown','password4',1,'CIV'),('23C105','Robert Davis','password5',1,'CIV'),('23I101','Thomas White','password11',1,'IT'),('23I102','Patricia Harris','password12',1,'IT'),('23I103','Christopher Adams','password13',1,'IT'),('23I104','Nancy Scott','password14',1,'IT'),('23I105','Joseph Turner','password15',1,'IT'),('23M101','Mary Wilson','password6',1,'MECH'),('23M102','David Lee','password7',1,'MECH'),('23M103','Sarah Taylor','password8',1,'MECH'),('23M104','William Clark','password9',1,'MECH'),('23M105','Linda Hall','password10',1,'MECH'),('23Z101','Susan Martin','password16',1,'CSE'),('23Z102','Matthew Hall','password17',1,'CSE'),('23Z103','Dorothy Lewis','password18',1,'CSE'),('23Z104','James Allen','password19',1,'CSE'),('23Z105','Elizabeth Baker','password20',1,'CSE');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-06 15:58:17
