-- MySQL dump 10.13  Distrib 9.1.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: food_factory
-- ------------------------------------------------------
-- Server version	9.1.0

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
-- Table structure for table `flavour_ingredients`
--

DROP TABLE IF EXISTS `flavour_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flavour_ingredients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) NOT NULL,
  `ingredient_name` varchar(255) NOT NULL,
  `weight` double NOT NULL,
  `order_index` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_flavour_ingredient` (`flavour_name`,`ingredient_name`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flavour_ingredients`
--

LOCK TABLES `flavour_ingredients` WRITE;
/*!40000 ALTER TABLE `flavour_ingredients` DISABLE KEYS */;
INSERT INTO `flavour_ingredients` VALUES (1,'老坛酸菜牛肉面','酸菜',10,1),(2,'老坛酸菜牛肉面','牛肉',10,2),(3,'老坛酸菜牛肉面','面饼',500,3),(4,'红烧牛肉面','牛肉',20,1),(5,'红烧牛肉面','胡萝卜',5,2),(6,'红烧牛肉面','面饼',450,3),(7,'鲜虾鱼板面','虾',10,2),(8,'鲜虾鱼板面','板',10,1),(9,'鲜虾鱼板面','面饼',500,3),(10,'鲜虾鱼板面','鱼',10,4),(56,'调味料1','青花椒',100,1),(57,'调味料1','辣椒',200,2),(58,'调味料2','辣椒粉',100,3),(59,'调味料2','花椒粉',50,1),(60,'调味料2','香辣粉',75,2),(61,'调味料3','辣椒粉',50,2),(62,'调味料3','花椒粉',100,1),(63,'调味料3','胡椒粉',150,3),(64,'调味料4','辣椒粉',50,3),(65,'调味料4','花椒粉',100,2),(66,'调味料4','胡椒粉',150,1),(71,'番茄鸡蛋面','番茄bing',10,2),(72,'番茄鸡蛋面','鸡蛋',10,1),(73,'番茄鸡蛋面','面饼',500,3),(74,'番茄鸡蛋面','虾',10,4);
/*!40000 ALTER TABLE `flavour_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredients`
--

DROP TABLE IF EXISTS `ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL,
  `counts` int NOT NULL,
  `completed` tinyint(1) DEFAULT '0',
  `completed_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredients`
--

LOCK TABLES `ingredients` WRITE;
/*!40000 ALTER TABLE `ingredients` DISABLE KEYS */;
INSERT INTO `ingredients` VALUES (1,'胡椒粉',100,0,NULL,'2025-01-18 10:41:07'),(2,'辣椒粉',100,0,NULL,'2025-01-18 10:41:09'),(3,'胡椒粉',50,0,NULL,'2025-01-18 10:41:10'),(4,'香辣鸡腿堡',100,0,NULL,'2025-01-18 10:41:10');
/*!40000 ALTER TABLE `ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `menuCode` varchar(10) NOT NULL,
  `menuName` varchar(50) NOT NULL,
  `menuLevel` tinyint NOT NULL,
  `menuParentCode` varchar(10) DEFAULT NULL,
  `menuClick` varchar(50) NOT NULL,
  `menuRight` varchar(50) NOT NULL,
  `menuComponent` varchar(100) NOT NULL,
  `menuIcon` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_menuCode` (`menuCode`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'001','管理员管理',1,NULL,'SudoSetting','0','Index/SudoSetting','el-icon-s-custom'),(2,'002','配方管理',1,NULL,'FormulaManagement','1','Index/FormulaManagement','el-icon-user-solid'),(3,'003','任务管理',1,NULL,'TodoManagement','1','Index/TodoManagement','el-icon-s-custom'),(4,'004','实时任务',1,NULL,'Management','1,2','Management','el-icon-s-custom');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_2025_01_23`
--

DROP TABLE IF EXISTS `table_2025_01_23`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_2025_01_23` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) NOT NULL,
  `ingredient_name` varchar(255) NOT NULL,
  `weight` double NOT NULL,
  `order_index` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_flavour_ingredient` (`flavour_name`,`ingredient_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_2025_01_23`
--

LOCK TABLES `table_2025_01_23` WRITE;
/*!40000 ALTER TABLE `table_2025_01_23` DISABLE KEYS */;
INSERT INTO `table_2025_01_23` VALUES (1,'草莓口味','新鲜草莓',150.5,1);
/*!40000 ALTER TABLE `table_2025_01_23` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_2025_01_24`
--

DROP TABLE IF EXISTS `table_2025_01_24`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_2025_01_24` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) NOT NULL,
  `ingredient_name` varchar(255) NOT NULL,
  `weight` double NOT NULL,
  `order_index` int DEFAULT NULL,
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_flavour_ingredient` (`flavour_name`,`ingredient_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_2025_01_24`
--

LOCK TABLES `table_2025_01_24` WRITE;
/*!40000 ALTER TABLE `table_2025_01_24` DISABLE KEYS */;
INSERT INTO `table_2025_01_24` VALUES (1,'红烧牛肉面','牛肉',1000,1,1,0),(2,'红烧牛肉面','胡萝卜',250,2,1,0),(3,'红烧牛肉面','面饼',22500,3,1,0),(4,'老坛酸菜牛肉面','牛肉',1500,2,2,0),(5,'老坛酸菜牛肉面','酸菜',1500,1,2,0),(6,'老坛酸菜牛肉面','面饼',75000,3,2,0),(7,'鲜虾鱼板面','板',3000,1,3,0),(8,'鲜虾鱼板面','虾',3000,2,3,0),(9,'鲜虾鱼板面','面饼',150000,3,3,0),(10,'鲜虾鱼板面','鱼',3000,4,3,0);
/*!40000 ALTER TABLE `table_2025_01_24` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_2025_01_25`
--

DROP TABLE IF EXISTS `table_2025_01_25`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_2025_01_25` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) DEFAULT NULL,
  `ingredient_name` varchar(255) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_2025_01_25`
--

LOCK TABLES `table_2025_01_25` WRITE;
/*!40000 ALTER TABLE `table_2025_01_25` DISABLE KEYS */;
INSERT INTO `table_2025_01_25` VALUES (1,'红烧牛肉面','牛肉',1000,1,1,0),(2,'红烧牛肉面','胡萝卜',250,2,1,0),(3,'红烧牛肉面','面饼',22500,3,1,0),(4,'红烧牛肉面','牛肉',2000,1,2,0),(5,'红烧牛肉面','胡萝卜',500,2,2,0),(6,'红烧牛肉面','面饼',45000,3,2,0),(7,'老坛酸菜牛肉面','牛肉',1000,2,3,0),(8,'老坛酸菜牛肉面','酸菜',1000,1,3,0),(9,'老坛酸菜牛肉面','面饼',50000,3,3,0),(10,'老坛酸菜牛肉面','牛肉',200,2,4,0),(11,'老坛酸菜牛肉面','酸菜',200,1,4,0),(12,'老坛酸菜牛肉面','面饼',10000,3,4,0),(13,'红烧牛肉面','牛肉',400,1,5,0),(14,'红烧牛肉面','胡萝卜',100,2,5,0),(15,'红烧牛肉面','面饼',9000,3,5,0),(16,'调味料1','辣椒',4000,2,6,0),(17,'调味料1','青花椒',2000,1,6,0),(18,'调味料2','花椒粉',5000,1,7,0),(19,'调味料2','辣椒粉',10000,3,7,0),(20,'调味料2','香辣粉',7500,2,7,0);
/*!40000 ALTER TABLE `table_2025_01_25` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_2025_01_27`
--

DROP TABLE IF EXISTS `table_2025_01_27`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_2025_01_27` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) DEFAULT NULL,
  `ingredient_name` varchar(255) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_2025_01_27`
--

LOCK TABLES `table_2025_01_27` WRITE;
/*!40000 ALTER TABLE `table_2025_01_27` DISABLE KEYS */;
INSERT INTO `table_2025_01_27` VALUES (1,'调味料1','辣椒',200,2,1,0),(2,'调味料1','青花椒',100,1,1,0),(3,'调味料2','花椒粉',250,1,2,0),(4,'调味料2','辣椒粉',500,3,2,0),(5,'调味料2','香辣粉',375,2,2,0);
/*!40000 ALTER TABLE `table_2025_01_27` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_2025_01_28`
--

DROP TABLE IF EXISTS `table_2025_01_28`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_2025_01_28` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) DEFAULT NULL,
  `ingredient_name` varchar(255) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_2025_01_28`
--

LOCK TABLES `table_2025_01_28` WRITE;
/*!40000 ALTER TABLE `table_2025_01_28` DISABLE KEYS */;
INSERT INTO `table_2025_01_28` VALUES (1,'调味料1','辣椒',1000,2,1,0),(2,'调味料1','青花椒',500,1,1,0),(3,'番茄鸡蛋牛肉面','牛肉',100,2,2,0),(4,'番茄鸡蛋牛肉面','番茄',50,1,2,0);
/*!40000 ALTER TABLE `table_2025_01_28` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_2025_01_29`
--

DROP TABLE IF EXISTS `table_2025_01_29`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_2025_01_29` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) DEFAULT NULL,
  `ingredient_name` varchar(255) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_2025_01_29`
--

LOCK TABLES `table_2025_01_29` WRITE;
/*!40000 ALTER TABLE `table_2025_01_29` DISABLE KEYS */;
INSERT INTO `table_2025_01_29` VALUES (1,'老坛酸菜牛肉面','牛肉',30,2,1,1),(2,'老坛酸菜牛肉面','酸菜',30,1,1,1),(3,'老坛酸菜牛肉面','面饼',1500,3,1,1),(4,'调味料1','辣椒',800,2,2,1),(5,'调味料1','青花椒',400,1,2,1),(6,'调味料2','花椒粉',400,1,3,1),(7,'调味料2','辣椒粉',800,3,3,1),(8,'调味料2','香辣粉',600,2,3,1),(9,'调味料2','花椒粉',600,1,4,1),(10,'调味料2','辣椒粉',1200,3,4,1),(11,'调味料2','香辣粉',900,2,4,1),(12,'调味料1','辣椒',10000,2,5,1),(13,'调味料1','青花椒',5000,1,5,1),(14,'红烧牛肉面','牛肉',20,1,6,1),(15,'红烧牛肉面','胡萝卜',5,2,6,1),(16,'红烧牛肉面','面饼',450,3,6,1),(17,'调味料3','胡椒粉',7500,3,7,1),(18,'调味料3','花椒粉',5000,1,7,1),(19,'调味料3','辣椒粉',2500,2,7,1),(20,'调味料3','胡椒粉',150,3,8,1),(21,'调味料3','花椒粉',100,1,8,1),(22,'调味料3','辣椒粉',50,2,8,1);
/*!40000 ALTER TABLE `table_2025_01_29` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_2025_02_04`
--

DROP TABLE IF EXISTS `table_2025_02_04`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_2025_02_04` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) DEFAULT NULL,
  `ingredient_name` varchar(255) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `order_index` int DEFAULT NULL,
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_2025_02_04`
--

LOCK TABLES `table_2025_02_04` WRITE;
/*!40000 ALTER TABLE `table_2025_02_04` DISABLE KEYS */;
INSERT INTO `table_2025_02_04` VALUES (1,'调味料4','胡椒粉',600,1,1,1),(2,'调味料4','花椒粉',400,2,1,1),(3,'调味料4','辣椒粉',200,3,1,1),(4,'调味料4','胡椒粉',750,1,2,1),(5,'调味料4','花椒粉',500,2,2,1),(6,'调味料4','辣椒粉',250,3,2,1);
/*!40000 ALTER TABLE `table_2025_02_04` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_template`
--

DROP TABLE IF EXISTS `table_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_template` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flavour_name` varchar(255) NOT NULL,
  `ingredient_name` varchar(255) NOT NULL,
  `weight` double NOT NULL,
  `order_index` int DEFAULT NULL,
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_flavour_ingredient` (`flavour_name`,`ingredient_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_template`
--

LOCK TABLES `table_template` WRITE;
/*!40000 ALTER TABLE `table_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `table_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_2025_01_23`
--

DROP TABLE IF EXISTS `task_2025_01_23`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_2025_01_23` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) DEFAULT NULL,
  `task_count` int DEFAULT NULL,
  `completed_count` int DEFAULT '0',
  `task_index` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_2025_01_23`
--

LOCK TABLES `task_2025_01_23` WRITE;
/*!40000 ALTER TABLE `task_2025_01_23` DISABLE KEYS */;
INSERT INTO `task_2025_01_23` VALUES (1,'红烧牛肉面',50,0,1);
/*!40000 ALTER TABLE `task_2025_01_23` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_2025_01_24`
--

DROP TABLE IF EXISTS `task_2025_01_24`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_2025_01_24` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) DEFAULT NULL,
  `task_count` int DEFAULT NULL,
  `completed_count` int DEFAULT '0',
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_2025_01_24`
--

LOCK TABLES `task_2025_01_24` WRITE;
/*!40000 ALTER TABLE `task_2025_01_24` DISABLE KEYS */;
INSERT INTO `task_2025_01_24` VALUES (1,'红烧牛肉面',50,0,1,0),(2,'老坛酸菜牛肉面',150,0,2,0),(3,'鲜虾鱼板面',300,0,3,0);
/*!40000 ALTER TABLE `task_2025_01_24` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_2025_01_25`
--

DROP TABLE IF EXISTS `task_2025_01_25`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_2025_01_25` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) DEFAULT NULL,
  `task_count` int DEFAULT NULL,
  `completed_count` int DEFAULT '0',
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_2025_01_25`
--

LOCK TABLES `task_2025_01_25` WRITE;
/*!40000 ALTER TABLE `task_2025_01_25` DISABLE KEYS */;
INSERT INTO `task_2025_01_25` VALUES (1,'红烧牛肉面',50,0,1,0),(2,'红烧牛肉面',100,0,2,0),(3,'老坛酸菜牛肉面',100,0,3,0),(4,'老坛酸菜牛肉面',20,0,4,0),(5,'红烧牛肉面',20,0,5,0),(6,'调味料1',20,0,6,0),(7,'调味料2',100,0,7,0);
/*!40000 ALTER TABLE `task_2025_01_25` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_2025_01_27`
--

DROP TABLE IF EXISTS `task_2025_01_27`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_2025_01_27` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) DEFAULT NULL,
  `task_count` int DEFAULT NULL,
  `completed_count` int DEFAULT '0',
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_2025_01_27`
--

LOCK TABLES `task_2025_01_27` WRITE;
/*!40000 ALTER TABLE `task_2025_01_27` DISABLE KEYS */;
INSERT INTO `task_2025_01_27` VALUES (1,'调味料1',1,0,1,0),(2,'调味料2',5,0,2,0);
/*!40000 ALTER TABLE `task_2025_01_27` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_2025_01_28`
--

DROP TABLE IF EXISTS `task_2025_01_28`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_2025_01_28` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) DEFAULT NULL,
  `task_count` int DEFAULT NULL,
  `completed_count` int DEFAULT '0',
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_2025_01_28`
--

LOCK TABLES `task_2025_01_28` WRITE;
/*!40000 ALTER TABLE `task_2025_01_28` DISABLE KEYS */;
INSERT INTO `task_2025_01_28` VALUES (1,'调味料1',5,0,1,0),(2,'番茄鸡蛋牛肉面',5,0,2,0);
/*!40000 ALTER TABLE `task_2025_01_28` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_2025_01_29`
--

DROP TABLE IF EXISTS `task_2025_01_29`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_2025_01_29` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) DEFAULT NULL,
  `task_count` int DEFAULT NULL,
  `completed_count` int DEFAULT '0',
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_2025_01_29`
--

LOCK TABLES `task_2025_01_29` WRITE;
/*!40000 ALTER TABLE `task_2025_01_29` DISABLE KEYS */;
INSERT INTO `task_2025_01_29` VALUES (1,'老坛酸菜牛肉面',3,0,1,1),(2,'调味料1',4,0,2,1),(3,'调味料2',8,0,3,1),(4,'调味料2',12,0,4,1),(5,'调味料1',50,0,5,1),(6,'红烧牛肉面',1,0,6,1),(7,'调味料3',50,0,7,1),(8,'调味料3',1,0,8,1);
/*!40000 ALTER TABLE `task_2025_01_29` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_2025_02_04`
--

DROP TABLE IF EXISTS `task_2025_02_04`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_2025_02_04` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) DEFAULT NULL,
  `task_count` int DEFAULT NULL,
  `completed_count` int DEFAULT '0',
  `task_index` int DEFAULT NULL,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_2025_02_04`
--

LOCK TABLES `task_2025_02_04` WRITE;
/*!40000 ALTER TABLE `task_2025_02_04` DISABLE KEYS */;
INSERT INTO `task_2025_02_04` VALUES (1,'调味料4',4,0,1,1),(2,'调味料4',5,0,2,1);
/*!40000 ALTER TABLE `task_2025_02_04` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `roleID` int DEFAULT '2',
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'sudo','$2a$12$.Z23esvdVeswFIgv/LYnnOQJmw.vXzu/8QELOFRovY0Lv83iRyPXG',0,1,'2025-01-15 12:34:56','2025-01-15 12:34:56'),(2,'admin','$2a$12$nme4fOvOZPp71cB6X4K.BOYPQMvTK6Mb15TOyOtbVUqjr/nqHn62q',1,1,'2025-01-18 06:12:27','2025-01-27 08:38:34'),(3,'admin2','$2a$12$nme4fOvOZPp71cB6X4K.BOYPQMvTK6Mb15TOyOtbVUqjr/nqHn62q',1,1,'2025-01-28 12:32:09','2025-01-28 12:32:09'),(4,'user','$2a$12$nme4fOvOZPp71cB6X4K.BOYPQMvTK6Mb15TOyOtbVUqjr/nqHn62q',2,1,'2025-01-28 12:32:22','2025-01-28 12:32:22');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-04 10:20:18
