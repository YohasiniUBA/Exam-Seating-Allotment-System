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
-- Table structure for table `exam_timetable`
--

DROP TABLE IF EXISTS `exam_timetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_timetable` (
  `year` int DEFAULT NULL,
  `dept` varchar(4) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `slot` varchar(50) DEFAULT NULL,
  `course_code` varchar(10) DEFAULT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  KEY `fk_year` (`year`),
  KEY `idx_exam_timetable_date` (`date`),
  CONSTRAINT `fk_year` FOREIGN KEY (`year`) REFERENCES `student` (`yearofstudy`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_timetable`
--

LOCK TABLES `exam_timetable` WRITE;
/*!40000 ALTER TABLE `exam_timetable` DISABLE KEYS */;
INSERT INTO `exam_timetable` VALUES (1,'CIV','2023-12-01','2:00 PM - 5:00 PM','19C101','Introduction to Civil Engineering'),(1,'CIV','2023-12-02','2:00 PM - 5:00 PM','19C102','Civil Engineering Fundamentals'),(1,'CIV','2023-12-03','2:00 PM - 5:00 PM','19C103','Structural Analysis'),(1,'CIV','2023-12-04','2:00 PM - 5:00 PM','19C104','Environmental Engineering'),(2,'CIV','2023-12-01','9:00 AM - 12:00 PM','19C301','Transportation Engineering'),(2,'CIV','2023-12-02','9:00 AM - 12:00 PM','19C302','Geotechnical Engineering'),(2,'CIV','2023-12-03','9:00 AM - 12:00 PM','19C303','Hydraulics and Water Resources'),(2,'CIV','2023-12-04','9:00 AM - 12:00 PM','19C304','Structural Design'),(3,'CIV','2023-12-01','2:00 PM - 5:00 PM','19C501','Construction Management'),(3,'CIV','2023-12-02','2:00 PM - 5:00 PM','19C502','Environmental Impact Assessment'),(3,'CIV','2023-12-03','2:00 PM - 5:00 PM','19C503','Advanced Structural Analysis'),(3,'CIV','2023-12-04','2:00 PM - 5:00 PM','19C504','Transportation Planning'),(4,'CIV','2023-12-01','9:00 AM - 12:00 PM','19C701','Advanced Geotechnical Engineering'),(4,'CIV','2023-12-02','9:00 AM - 12:00 PM','19C702','Structural Dynamics'),(4,'CIV','2023-12-03','9:00 AM - 12:00 PM','19C703','Civil Engineering Project Management'),(4,'CIV','2023-12-04','9:00 AM - 12:00 PM','19C704','Sustainable Infrastructure Design'),(1,'MECH','2023-12-01','2:00 PM - 5:00 PM','19M101','Introduction to Mechanical Engineering'),(1,'MECH','2023-12-02','2:00 PM - 5:00 PM','19M102','Mechanical Engineering Fundamentals'),(1,'MECH','2023-12-03','2:00 PM - 5:00 PM','19M103','Thermodynamics'),(1,'MECH','2023-12-04','2:00 PM - 5:00 PM','19M104','Mechanical Design'),(2,'MECH','2023-12-01','9:00 AM - 12:00 PM','19M301','Manufacturing Processes'),(2,'MECH','2023-12-02','9:00 AM - 12:00 PM','19M302','Dynamics of Machinery'),(2,'MECH','2023-12-03','9:00 AM - 12:00 PM','19M303','Heat and Mass Transfer'),(2,'MECH','2023-12-04','9:00 AM - 12:00 PM','19M304','Machine Design'),(3,'MECH','2023-12-01','2:00 PM - 5:00 PM','19M501','Engineering Materials and Metallurgy'),(3,'MECH','2023-12-02','2:00 PM - 5:00 PM','19M502','Automotive Engineering'),(3,'MECH','2023-12-03','2:00 PM - 5:00 PM','19M503','Vibrations and Noise Control'),(3,'MECH','2023-12-04','2:00 PM - 5:00 PM','19M504','Fluid Mechanics'),(4,'MECH','2023-12-01','9:00 AM - 12:00 PM','19M701','Advanced Solid Mechanics'),(4,'MECH','2023-12-02','9:00 AM - 12:00 PM','19M702','Thermal Engineering'),(4,'MECH','2023-12-03','9:00 AM - 12:00 PM','19M703','Mechatronics'),(4,'MECH','2023-12-04','9:00 AM - 12:00 PM','19M704','Robotics and Automation'),(1,'IT','2023-12-01','2:00 PM - 5:00 PM','19I101','Introduction to Information Technology'),(1,'IT','2023-12-02','2:00 PM - 5:00 PM','19I102','Programming Fundamentals'),(1,'IT','2023-12-03','2:00 PM - 5:00 PM','19I103','Data Structures and Algorithms'),(1,'IT','2023-12-04','2:00 PM - 5:00 PM','19I104','Database Management Systems'),(2,'IT','2023-12-01','9:00 AM - 12:00 PM','19I301','Web Development'),(2,'IT','2023-12-02','9:00 AM - 12:00 PM','19I302','Operating Systems'),(2,'IT','2023-12-03','9:00 AM - 12:00 PM','19I303','Software Engineering'),(2,'IT','2023-12-04','9:00 AM - 12:00 PM','19I304','Computer Networks'),(3,'IT','2023-12-01','2:00 PM - 5:00 PM','19I501','Artificial Intelligence'),(3,'IT','2023-12-02','2:00 PM - 5:00 PM','19I502','Database Administration'),(3,'IT','2023-12-03','2:00 PM - 5:00 PM','19I503','Software Testing'),(3,'IT','2023-12-04','2:00 PM - 5:00 PM','19I504','Cybersecurity'),(4,'IT','2023-12-01','9:00 AM - 12:00 PM','19I701','Mobile App Development'),(4,'IT','2023-12-02','9:00 AM - 12:00 PM','19I702','Big Data Analytics'),(4,'IT','2023-12-03','9:00 AM - 12:00 PM','19I703','Machine Learning'),(4,'IT','2023-12-04','9:00 AM - 12:00 PM','19I704','Cloud Computing'),(1,'CSE','2023-12-01','2:00 PM - 5:00 PM','19Z101','Introduction to Computer Science'),(1,'CSE','2023-12-02','2:00 PM - 5:00 PM','19Z102','Programming Fundamentals'),(1,'CSE','2023-12-03','2:00 PM - 5:00 PM','19Z103','Data Structures and Algorithms'),(1,'CSE','2023-12-04','2:00 PM - 5:00 PM','19Z104','Database Management Systems'),(2,'CSE','2023-12-01','9:00 AM - 12:00 PM','19Z301','Web Development'),(2,'CSE','2023-12-02','9:00 AM - 12:00 PM','19Z302','Operating Systems'),(2,'CSE','2023-12-03','9:00 AM - 12:00 PM','19Z303','Software Engineering'),(2,'CSE','2023-12-04','9:00 AM - 12:00 PM','19Z304','Computer Networks'),(3,'CSE','2023-12-01','2:00 PM - 5:00 PM','19Z501','Artificial Intelligence'),(3,'CSE','2023-12-02','2:00 PM - 5:00 PM','19Z502','Database Administration'),(3,'CSE','2023-12-03','2:00 PM - 5:00 PM','19Z503','Software Testing'),(3,'CSE','2023-12-04','2:00 PM - 5:00 PM','19Z504','Cybersecurity'),(4,'CSE','2023-12-01','9:00 AM - 12:00 PM','19Z701','Mobile App Development'),(4,'CSE','2023-12-02','9:00 AM - 12:00 PM','19Z702','Big Data Analytics'),(4,'CSE','2023-12-03','9:00 AM - 12:00 PM','19Z703','Machine Learning'),(4,'CSE','2023-12-04','9:00 AM - 12:00 PM','19Z704','Cloud Computing');
/*!40000 ALTER TABLE `exam_timetable` ENABLE KEYS */;
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
