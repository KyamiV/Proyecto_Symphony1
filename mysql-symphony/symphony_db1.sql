-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: symphony_db
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actividades`
--

DROP TABLE IF EXISTS `actividades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actividades` (
  `id_actividad` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'pendiente',
  PRIMARY KEY (`id_actividad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividades`
--

LOCK TABLES `actividades` WRITE;
/*!40000 ALTER TABLE `actividades` DISABLE KEYS */;
/*!40000 ALTER TABLE `actividades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auditoria`
--

DROP TABLE IF EXISTS `auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditoria` (
  `id_auditoria` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(150) NOT NULL,
  `rol` varchar(50) DEFAULT NULL,
  `accion` varchar(150) NOT NULL,
  `detalle` varchar(255) DEFAULT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `modulo` varchar(100) NOT NULL,
  `fecha` datetime DEFAULT current_timestamp(),
  `ip_origen` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_auditoria`)
) ENGINE=InnoDB AUTO_INCREMENT=2278 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditoria`
--

LOCK TABLES `auditoria` WRITE;
/*!40000 ALTER TABLE `auditoria` DISABLE KEYS */;
INSERT INTO `auditoria` VALUES (1733,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-11-30 22:50:12','Panel administrador','2025-11-30 17:50:12',NULL),(1734,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-01 05:54:05','Inicio de sesión','2025-12-01 00:54:05',NULL),(1735,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-01 05:54:06','Panel administrador','2025-12-01 00:54:06',NULL),(1736,'Carla','administrador','Consultó listado completo de usuarios, docentes y estudiantes institucionales','—','2025-12-01 05:59:44','Gestión de usuarios','2025-12-01 00:59:44',NULL),(1737,'Carla','administrador','Consultó notas con filtros aplicados','—','2025-12-01 06:03:30','Notas musicales','2025-12-01 01:03:30',NULL),(1738,'Carla','administrador','Consultó el panel de tablas enviadas por docentes','—','2025-12-01 06:03:37','Tablas recibidas','2025-12-01 01:03:37',NULL),(1739,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-01 06:12:16','Gestión de clases institucionales','2025-12-01 01:12:16',NULL),(1740,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-01 06:36:55','Gestión de clases institucionales','2025-12-01 01:36:55',NULL),(1741,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-01 06:37:38','Panel administrador','2025-12-01 01:37:38',NULL),(1742,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-01 06:37:55','Inicio de sesión','2025-12-01 01:37:55',NULL),(1743,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-01 06:37:55','Panel docente','2025-12-01 01:37:55',NULL),(1744,'1','docente','Visualizó listado de estudiantes de la clase ID 1','—','2025-12-01 06:39:59','Clases','2025-12-01 01:39:59',NULL),(1745,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-01 06:40:34','Notas por clase','2025-12-01 01:40:34',NULL),(1746,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-01 06:40:54','Consulta de notas','2025-12-01 01:40:54',NULL),(1747,'Alexa','estudiante','Inicio de sesión exitoso','—','2025-12-01 06:42:36','Inicio de sesión','2025-12-01 01:42:36',NULL),(1748,'Alexa','estudiante','Accedió al panel principal','—','2025-12-01 06:42:36','Panel Estudiante','2025-12-01 01:42:36',NULL),(1749,'Alexa','estudiante','Visualizó sus notas por clase','—','2025-12-01 06:44:02','Notas','2025-12-01 01:44:02',NULL),(1750,'Alexa','estudiante','Visualizó sus observaciones institucionales','—','2025-12-01 06:44:11','Observaciones','2025-12-01 01:44:11',NULL),(1751,'Alexa','estudiante','Accedió al panel principal','—','2025-12-01 06:45:02','Panel Estudiante','2025-12-01 01:45:02',NULL),(1752,'Alexa','estudiante','Visualizó listado de clases disponibles e inscritas','—','2025-12-01 06:45:09','Clases','2025-12-01 01:45:09',NULL),(1753,'Alexa','estudiante','Visualizar certificados','—','2025-12-01 06:45:20','Certificados','2025-12-01 01:45:20',NULL),(1754,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-01 11:44:49','Inicio de sesión','2025-12-01 06:44:49',NULL),(1755,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-01 11:44:49','Panel administrador','2025-12-01 06:44:49',NULL),(1756,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-01 11:46:52','Inicio de sesión','2025-12-01 06:46:52',NULL),(1757,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-01 11:46:52','Panel docente','2025-12-01 06:46:52',NULL),(1758,'Alexa','estudiante','Inicio de sesión exitoso','—','2025-12-01 11:47:52','Inicio de sesión','2025-12-01 06:47:52',NULL),(1759,'Alexa','estudiante','Accedió al panel principal','—','2025-12-01 11:47:52','Panel Estudiante','2025-12-01 06:47:52',NULL),(1760,'Alexa','estudiante','Inicio de sesión exitoso','—','2025-12-01 21:02:59','Inicio de sesión','2025-12-01 16:02:59',NULL),(1761,'Alexa','estudiante','Accedió al panel principal','—','2025-12-01 21:02:59','Panel Estudiante','2025-12-01 16:02:59',NULL),(1762,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-02 17:51:58','Inicio de sesión','2025-12-02 12:51:58',NULL),(1763,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-02 17:51:58','Panel administrador','2025-12-02 12:51:58',NULL),(1764,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-02 18:10:56','Inicio de sesión','2025-12-02 13:10:56',NULL),(1765,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-02 18:10:57','Panel administrador','2025-12-02 13:10:57',NULL),(1766,'Carla','administrador','Consultó listado completo de usuarios, docentes y estudiantes institucionales','—','2025-12-02 18:11:03','Gestión de usuarios','2025-12-02 13:11:03',NULL),(1767,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-02 18:18:49','Inicio de sesión','2025-12-02 13:18:49',NULL),(1768,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-02 18:18:49','Panel administrador','2025-12-02 13:18:49',NULL),(1769,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-02 18:20:47','Gestión de clases institucionales','2025-12-02 13:20:47',NULL),(1770,'Manuelita','estudiante','Inicio de sesión exitoso','—','2025-12-02 18:39:03','Inicio de sesión','2025-12-02 13:39:03',NULL),(1771,'Manuelita','estudiante','Accedió al panel principal','—','2025-12-02 18:39:03','Panel Estudiante','2025-12-02 13:39:03',NULL),(1772,'Manuelita','estudiante','Visualizó listado de clases disponibles e inscritas','—','2025-12-02 18:39:08','Clases','2025-12-02 13:39:08',NULL),(1773,'Manuelita','estudiante','Se inscribió en clase ID 1','—','2025-12-02 18:45:52','Inscripción','2025-12-02 13:45:52',NULL),(1774,'Manuelita','estudiante','Visualizó listado de clases disponibles e inscritas','—','2025-12-02 18:45:53','Clases','2025-12-02 13:45:53',NULL),(1775,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-02 19:01:49','Inicio de sesión','2025-12-02 14:01:49',NULL),(1776,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-02 19:01:49','Panel administrador','2025-12-02 14:01:49',NULL),(1777,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-02 19:08:53','Gestión de clases institucionales','2025-12-02 14:08:53',NULL),(1778,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-02 19:14:02','Gestión de clases institucionales','2025-12-02 14:14:02',NULL),(1779,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-02 19:14:06','Gestión de clases institucionales','2025-12-02 14:14:06',NULL),(1780,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 19:31:22','Inicio de sesión','2025-12-02 14:31:22',NULL),(1781,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 19:31:23','Panel docente','2025-12-02 14:31:23',NULL),(1782,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-02 19:32:24','Inicio de sesión','2025-12-02 14:32:24',NULL),(1783,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-02 19:32:24','Panel administrador','2025-12-02 14:32:24',NULL),(1784,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-02 19:32:28','Gestión de clases institucionales','2025-12-02 14:32:28',NULL),(1785,'Carla','administrador','Desasignó docente de clase ID 1','—','2025-12-02 19:32:32','Gestión de clases','2025-12-02 14:32:32',NULL),(1786,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-02 19:32:39','Gestión de clases institucionales','2025-12-02 14:32:39',NULL),(1787,'Carla','administrador','Asignó docente ID 1 a clase ID 1','—','2025-12-02 19:32:43','Asignación de Docentes','2025-12-02 14:32:43',NULL),(1788,'Carla','administrador','Asignación de docente ID 1 a clase ID 1','—','2025-12-02 19:32:43','Asignaciones','2025-12-02 14:32:43',NULL),(1789,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 19:33:09','Inicio de sesión','2025-12-02 14:33:09',NULL),(1790,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 19:33:09','Panel docente','2025-12-02 14:33:09',NULL),(1791,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 19:33:47','Notas por clase','2025-12-02 14:33:47',NULL),(1792,'Luis Profe (ID: 1)','docente','Eliminó nota con ID 12 en clase 1','—','2025-12-02 19:33:53','Gestión de notas','2025-12-02 14:33:53',NULL),(1793,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 19:33:57','Panel docente','2025-12-02 14:33:57',NULL),(1794,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 19:34:04','Notas por clase','2025-12-02 14:34:04',NULL),(1795,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 19:36:19','Notas por clase','2025-12-02 14:36:19',NULL),(1796,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 19:46:04','Notas por clase','2025-12-02 14:46:04',NULL),(1797,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 20:01:00','Notas por clase','2025-12-02 15:01:00',NULL),(1798,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 20:01:04','Notas por clase','2025-12-02 15:01:04',NULL),(1799,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 20:01:10','Notas por clase','2025-12-02 15:01:10',NULL),(1800,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 20:09:59','Notas por clase','2025-12-02 15:09:59',NULL),(1801,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 20:11:23','Panel docente','2025-12-02 15:11:23',NULL),(1802,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 20:17:20','Notas por clase','2025-12-02 15:17:20',NULL),(1803,'Luis Profe (ID: 1)','docente','Registró nota para estudiante 9 en clase 1','—','2025-12-02 20:18:21','Registro de notas','2025-12-02 15:18:21',NULL),(1804,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 20:18:21','Notas por clase','2025-12-02 15:18:21',NULL),(1805,'Luis Profe','docente','Registró observación para estudiante ID=9 y la envió al estudiante','—','2025-12-02 20:38:39','Observaciones','2025-12-02 15:38:39',NULL),(1806,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-02 20:56:32','Consulta de notas por tabla','2025-12-02 15:56:32',NULL),(1807,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 20:56:56','Notas por clase','2025-12-02 15:56:56',NULL),(1808,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 20:56:59','Consulta de notas','2025-12-02 15:56:59',NULL),(1809,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 20:57:27','Consulta de notas','2025-12-02 15:57:27',NULL),(1810,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 20:58:41','Notas por clase','2025-12-02 15:58:41',NULL),(1811,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 20:58:43','Consulta de notas','2025-12-02 15:58:43',NULL),(1812,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:05:52','Notas por clase','2025-12-02 16:05:52',NULL),(1813,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-02 21:05:55','Consulta de notas por tabla','2025-12-02 16:05:55',NULL),(1814,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:12:11','Notas por clase','2025-12-02 16:12:11',NULL),(1815,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 21:20:52','Consulta de notas','2025-12-02 16:20:52',NULL),(1816,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1 (tabla ID 2)','—','2025-12-02 21:21:09','Consulta de notas por tabla','2025-12-02 16:21:09',NULL),(1817,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-02 21:21:25','Inicio de sesión','2025-12-02 16:21:25',NULL),(1818,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-02 21:21:25','Panel administrador','2025-12-02 16:21:25',NULL),(1819,'Carla','administrador','Consultó el panel de tablas enviadas por docentes','—','2025-12-02 21:21:46','Tablas recibidas','2025-12-02 16:21:46',NULL),(1820,'Carla','administrador','Consultó el panel de tablas enviadas por docentes','—','2025-12-02 21:21:50','Tablas recibidas','2025-12-02 16:21:50',NULL),(1821,'Carla','administrador','Consultó notas con filtros aplicados','—','2025-12-02 21:21:53','Notas musicales','2025-12-02 16:21:53',NULL),(1822,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-02 21:21:57','Tablas validadas','2025-12-02 16:21:57',NULL),(1823,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 21:22:41','Inicio de sesión','2025-12-02 16:22:41',NULL),(1824,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 21:22:41','Panel docente','2025-12-02 16:22:41',NULL),(1825,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:27:07','Notas por clase','2025-12-02 16:27:07',NULL),(1826,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 21:27:09','Consulta de notas','2025-12-02 16:27:09',NULL),(1827,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 21:31:58','Inicio de sesión','2025-12-02 16:31:58',NULL),(1828,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 21:31:58','Panel docente','2025-12-02 16:31:58',NULL),(1829,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:32:54','Notas por clase','2025-12-02 16:32:54',NULL),(1830,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 21:42:57','Consulta de notas','2025-12-02 16:42:57',NULL),(1831,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1 (tabla ID 2)','—','2025-12-02 21:44:30','Consulta de notas por tabla','2025-12-02 16:44:30',NULL),(1832,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1 (tabla ID 6)','—','2025-12-02 21:44:38','Consulta de notas por tabla','2025-12-02 16:44:38',NULL),(1833,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 21:45:30','Inicio de sesión','2025-12-02 16:45:30',NULL),(1834,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 21:45:30','Panel docente','2025-12-02 16:45:30',NULL),(1835,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:45:39','Notas por clase','2025-12-02 16:45:39',NULL),(1836,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 21:45:41','Consulta de notas','2025-12-02 16:45:41',NULL),(1837,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:46:47','Notas por clase','2025-12-02 16:46:47',NULL),(1838,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 21:46:50','Consulta de notas','2025-12-02 16:46:50',NULL),(1839,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 21:50:28','Inicio de sesión','2025-12-02 16:50:28',NULL),(1840,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 21:50:28','Panel docente','2025-12-02 16:50:28',NULL),(1841,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:50:36','Notas por clase','2025-12-02 16:50:36',NULL),(1842,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:50:51','Notas por clase','2025-12-02 16:50:51',NULL),(1843,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:50:56','Notas por clase','2025-12-02 16:50:56',NULL),(1844,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:53:17','Notas por clase','2025-12-02 16:53:17',NULL),(1845,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:53:56','Notas por clase','2025-12-02 16:53:56',NULL),(1846,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:55:14','Notas por clase','2025-12-02 16:55:14',NULL),(1847,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:55:53','Notas por clase','2025-12-02 16:55:53',NULL),(1848,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:56:07','Notas por clase','2025-12-02 16:56:07',NULL),(1849,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:57:33','Notas por clase','2025-12-02 16:57:33',NULL),(1850,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:57:51','Notas por clase','2025-12-02 16:57:51',NULL),(1851,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 21:57:56','Notas por clase','2025-12-02 16:57:56',NULL),(1852,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 22:03:33','Inicio de sesión','2025-12-02 17:03:33',NULL),(1853,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 22:03:33','Panel docente','2025-12-02 17:03:33',NULL),(1854,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-02 22:03:39','Consulta de notas por tabla','2025-12-02 17:03:39',NULL),(1855,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:03:45','Notas por clase','2025-12-02 17:03:45',NULL),(1856,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:03:47','Notas por clase','2025-12-02 17:03:47',NULL),(1857,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:04:09','Notas por clase','2025-12-02 17:04:09',NULL),(1858,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:06:02','Notas por clase','2025-12-02 17:06:02',NULL),(1859,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 22:35:41','Inicio de sesión','2025-12-02 17:35:41',NULL),(1860,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 22:35:42','Panel docente','2025-12-02 17:35:42',NULL),(1861,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:35:49','Notas por clase','2025-12-02 17:35:49',NULL),(1862,'Luis Profe (ID: 1)','docente','Eliminó nota con ID 13 en clase 1','—','2025-12-02 22:36:21','Gestión de notas','2025-12-02 17:36:21',NULL),(1863,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 22:40:08','Panel docente','2025-12-02 17:40:08',NULL),(1864,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:40:11','Notas por clase','2025-12-02 17:40:11',NULL),(1865,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:40:38','Notas por clase','2025-12-02 17:40:38',NULL),(1866,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-02 22:42:13','Inicio de sesión','2025-12-02 17:42:13',NULL),(1867,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-02 22:42:13','Panel docente','2025-12-02 17:42:13',NULL),(1868,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:42:19','Notas por clase','2025-12-02 17:42:19',NULL),(1869,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:46:44','Notas por clase','2025-12-02 17:46:44',NULL),(1870,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:47:13','Notas por clase','2025-12-02 17:47:13',NULL),(1871,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:51:35','Notas por clase','2025-12-02 17:51:35',NULL),(1872,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:51:51','Notas por clase','2025-12-02 17:51:51',NULL),(1873,'Luis Profe (ID: 1)','docente','Registró nota para estudiante 8 en clase 1','—','2025-12-02 22:53:11','Registro de notas','2025-12-02 17:53:11',NULL),(1874,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 22:53:11','Notas por clase','2025-12-02 17:53:11',NULL),(1875,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-02 23:01:33','Notas por clase','2025-12-02 18:01:33',NULL),(1876,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-02 23:01:42','Consulta de notas','2025-12-02 18:01:42',NULL),(1877,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-02 23:19:42','Inicio de sesión','2025-12-02 18:19:42',NULL),(1878,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-02 23:19:43','Panel administrador','2025-12-02 18:19:43',NULL),(1879,'Carla','administrador','Consultó el panel de tablas enviadas por docentes','—','2025-12-02 23:19:49','Tablas recibidas','2025-12-02 18:19:49',NULL),(1880,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-02 23:19:58','Tablas validadas','2025-12-02 18:19:58',NULL),(1881,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 00:17:24','Inicio de sesión','2025-12-02 19:17:24',NULL),(1882,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 00:17:24','Panel administrador','2025-12-02 19:17:24',NULL),(1883,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-03 00:17:31','Gestión de clases institucionales','2025-12-02 19:17:31',NULL),(1884,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 02:12:50','Inicio de sesión','2025-12-02 21:12:50',NULL),(1885,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 02:12:50','Panel docente','2025-12-02 21:12:50',NULL),(1886,'Alexa','estudiante','Inicio de sesión exitoso','—','2025-12-03 02:14:20','Inicio de sesión','2025-12-02 21:14:20',NULL),(1887,'Alexa','estudiante','Accedió al panel principal','—','2025-12-03 02:14:20','Panel Estudiante','2025-12-02 21:14:20',NULL),(1888,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 02:22:27','Inicio de sesión','2025-12-02 21:22:27',NULL),(1889,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 02:22:27','Panel administrador','2025-12-02 21:22:27',NULL),(1890,'Carla (ID: 3)','administrador','Registró nuevo usuario institucional: Shrek Profe (docente)','Correo: shrek@correo','2025-12-03 02:23:24','Gestión de usuarios','2025-12-02 21:23:24',NULL),(1891,'Carla','administrador','Consultó listado completo de usuarios, docentes y estudiantes institucionales','—','2025-12-03 02:23:24','Gestión de usuarios','2025-12-02 21:23:24',NULL),(1892,'zenitsu@correo','administrador','Registro de nuevo usuario: Zenitsu','—','2025-12-03 02:25:21','RegistroServlet','2025-12-02 21:25:21',NULL),(1893,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 02:28:19','Inicio de sesión','2025-12-02 21:28:19',NULL),(1894,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 02:28:19','Panel administrador','2025-12-02 21:28:19',NULL),(1895,'Carla','administrador','Consultó listado completo de usuarios, docentes y estudiantes institucionales','—','2025-12-03 02:29:19','Gestión de usuarios','2025-12-02 21:29:19',NULL),(1896,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-03 02:33:37','Gestión de clases institucionales','2025-12-02 21:33:37',NULL),(1897,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-03 02:35:00','Gestión de clases institucionales','2025-12-02 21:35:00',NULL),(1898,'Carla','administrador','Registró clase \'Guitarra acustica\' (Grupo A)','—','2025-12-03 02:35:29','Gestión de Clases','2025-12-02 21:35:29',NULL),(1899,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-03 02:35:29','Gestión de clases institucionales','2025-12-02 21:35:29',NULL),(1900,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-03 03:01:08','Gestión de clases institucionales','2025-12-02 22:01:08',NULL),(1901,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 03:01:13','Panel administrador','2025-12-02 22:01:13',NULL),(1902,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-03 03:01:20','Gestión de clases institucionales','2025-12-02 22:01:20',NULL),(1903,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 03:01:55','Panel administrador','2025-12-02 22:01:55',NULL),(1904,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-03 03:01:56','Gestión de clases institucionales','2025-12-02 22:01:56',NULL),(1905,'Carla','administrador','Asignó docente ID 1 a clase ID 2','—','2025-12-03 03:25:26','Asignación de Docentes','2025-12-02 22:25:26',NULL),(1906,'Carla','administrador','Asignación de docente ID 1 a clase ID 2','—','2025-12-03 03:25:26','Asignaciones','2025-12-02 22:25:26',NULL),(1907,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-03 03:28:44','Gestión de clases institucionales','2025-12-02 22:28:44',NULL),(1908,'Carla','administrador','Desasignó docente de clase ID 2','—','2025-12-03 03:29:21','Gestión de clases','2025-12-02 22:29:21',NULL),(1909,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 03:45:56','Horarios de clase','2025-12-02 22:45:56',NULL),(1910,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 03:50:00','Inicio de sesión','2025-12-02 22:50:00',NULL),(1911,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 03:50:00','Panel administrador','2025-12-02 22:50:00',NULL),(1912,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 03:50:05','Horarios de clase','2025-12-02 22:50:05',NULL),(1913,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 03:51:10','Inicio de sesión','2025-12-02 22:51:10',NULL),(1914,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 03:51:10','Panel administrador','2025-12-02 22:51:10',NULL),(1915,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 03:51:15','Horarios de clase','2025-12-02 22:51:15',NULL),(1916,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 04:04:01','Inicio de sesión','2025-12-02 23:04:01',NULL),(1917,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 04:04:01','Panel administrador','2025-12-02 23:04:01',NULL),(1918,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:04:03','Horarios de clase','2025-12-02 23:04:03',NULL),(1919,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:04:06','Horarios de clase','2025-12-02 23:04:06',NULL),(1920,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 04:12:30','Inicio de sesión','2025-12-02 23:12:30',NULL),(1921,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 04:12:31','Panel administrador','2025-12-02 23:12:31',NULL),(1922,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:12:35','Horarios de clase','2025-12-02 23:12:35',NULL),(1923,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:12:37','Horarios de clase','2025-12-02 23:12:37',NULL),(1924,'Carla','administrador','insertar horario para clase ID null','—','2025-12-03 04:13:01','Horarios de clase','2025-12-02 23:13:01',NULL),(1925,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:13:01','Horarios de clase','2025-12-02 23:13:01',NULL),(1926,'Carla','administrador','insertar horario para clase ID null','—','2025-12-03 04:13:20','Horarios de clase','2025-12-02 23:13:20',NULL),(1927,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:13:20','Horarios de clase','2025-12-02 23:13:20',NULL),(1928,'Carla','administrador','insertar horario para clase ID null','—','2025-12-03 04:13:34','Horarios de clase','2025-12-02 23:13:34',NULL),(1929,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:13:34','Horarios de clase','2025-12-02 23:13:34',NULL),(1930,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 04:16:44','Inicio de sesión','2025-12-02 23:16:44',NULL),(1931,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 04:16:44','Panel administrador','2025-12-02 23:16:44',NULL),(1932,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:16:48','Horarios de clase','2025-12-02 23:16:48',NULL),(1933,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:16:50','Horarios de clase','2025-12-02 23:16:50',NULL),(1934,'Carla','administrador','insertar horario para clase ID null','—','2025-12-03 04:17:17','Horarios de clase','2025-12-02 23:17:17',NULL),(1935,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:17:17','Horarios de clase','2025-12-02 23:17:17',NULL),(1936,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:20:53','Horarios de clase','2025-12-02 23:20:53',NULL),(1937,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:21:00','Horarios de clase','2025-12-02 23:21:00',NULL),(1938,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:21:00','Horarios de clase','2025-12-02 23:21:00',NULL),(1939,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:21:05','Horarios de clase','2025-12-02 23:21:05',NULL),(1940,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:21:27','Horarios de clase','2025-12-02 23:21:27',NULL),(1941,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:22:11','Horarios de clase','2025-12-02 23:22:11',NULL),(1942,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:22:47','Horarios de clase','2025-12-02 23:22:47',NULL),(1943,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 04:27:00','Inicio de sesión','2025-12-02 23:27:00',NULL),(1944,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 04:27:00','Panel administrador','2025-12-02 23:27:00',NULL),(1945,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:27:04','Horarios de clase','2025-12-02 23:27:04',NULL),(1946,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:27:07','Horarios de clase','2025-12-02 23:27:07',NULL),(1947,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:27:47','Horarios de clase','2025-12-02 23:27:47',NULL),(1948,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:27:51','Horarios de clase','2025-12-02 23:27:51',NULL),(1949,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:36:21','Horarios de clase','2025-12-02 23:36:21',NULL),(1950,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:42:38','Horarios de clase','2025-12-02 23:42:38',NULL),(1951,'Carla','administrador','Consultó horarios institucionales','—','2025-12-03 04:43:24','Horarios de clase','2025-12-02 23:43:24',NULL),(1952,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 04:52:17','Inicio de sesión','2025-12-02 23:52:17',NULL),(1953,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 04:52:18','Panel docente','2025-12-02 23:52:18',NULL),(1954,'1','docente','Visualizó listado de estudiantes de la clase ID 1','—','2025-12-03 04:52:41','Clases','2025-12-02 23:52:41',NULL),(1955,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 04:54:53','Notas por clase','2025-12-02 23:54:53',NULL),(1956,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 05:43:58','Inicio de sesión','2025-12-03 00:43:58',NULL),(1957,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 05:43:58','Panel docente','2025-12-03 00:43:58',NULL),(1958,'1','docente','Visualizó listado de estudiantes de la clase ID 1','—','2025-12-03 05:44:03','Clases','2025-12-03 00:44:03',NULL),(1959,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 05:45:20','Panel docente','2025-12-03 00:45:20',NULL),(1960,'1','docente','Visualizó listado de estudiantes de la clase ID 1','—','2025-12-03 05:47:10','Clases','2025-12-03 00:47:10',NULL),(1961,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 05:47:43','Notas por clase','2025-12-03 00:47:43',NULL),(1962,'Luis Profe (ID: 1)','docente','Actualizó preferencias: temaOscuro=false, mostrarIndicadores=true, idioma=en','—','2025-12-03 05:48:16','Configuración','2025-12-03 00:48:16',NULL),(1963,'Luis Profe (ID: 1)','docente','Actualizó preferencias: temaOscuro=false, mostrarIndicadores=true, idioma=es','—','2025-12-03 05:48:20','Configuración','2025-12-03 00:48:20',NULL),(1964,'1','docente','Visualizó listado de estudiantes de la clase ID 1','—','2025-12-03 05:50:23','Clases','2025-12-03 00:50:23',NULL),(1965,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 05:50:25','Consulta de notas','2025-12-03 00:50:25',NULL),(1966,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 05:50:46','Notas por clase','2025-12-03 00:50:46',NULL),(1967,'Luis Profe (ID: 1)','docente','Eliminó nota con ID 14 en clase 1','—','2025-12-03 06:05:13','Gestión de notas','2025-12-03 01:05:13',NULL),(1968,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:05:39','Notas por clase','2025-12-03 01:05:39',NULL),(1969,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:05:44','Notas por clase','2025-12-03 01:05:44',NULL),(1970,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:05:51','Notas por clase','2025-12-03 01:05:51',NULL),(1971,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:06:18','Notas por clase','2025-12-03 01:06:18',NULL),(1972,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:06:37','Notas por clase','2025-12-03 01:06:37',NULL),(1973,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:06:40','Notas por clase','2025-12-03 01:06:40',NULL),(1974,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 06:24:42','Inicio de sesión','2025-12-03 01:24:42',NULL),(1975,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 06:24:43','Panel docente','2025-12-03 01:24:43',NULL),(1976,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:24:48','Notas por clase','2025-12-03 01:24:48',NULL),(1977,'Luis Profe (ID: 1)','docente','Registró nota para estudiante 9 en clase 1','—','2025-12-03 06:24:59','Registro de notas','2025-12-03 01:24:59',NULL),(1978,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:24:59','Notas por clase','2025-12-03 01:24:59',NULL),(1979,'Luis Profe','docente','Registró observación para estudiante ID=8','—','2025-12-03 06:37:44','Observaciones','2025-12-03 01:37:44',NULL),(1980,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 06:47:49','Inicio de sesión','2025-12-03 01:47:49',NULL),(1981,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 06:47:49','Panel administrador','2025-12-03 01:47:49',NULL),(1982,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 06:48:06','Inicio de sesión','2025-12-03 01:48:06',NULL),(1983,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 06:48:06','Panel docente','2025-12-03 01:48:06',NULL),(1984,'Luis Profe (ID: 1)','docente','Eliminó observación con ID 3','—','2025-12-03 06:48:25','Gestión de observaciones','2025-12-03 01:48:25',NULL),(1985,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 06:50:30','Inicio de sesión','2025-12-03 01:50:30',NULL),(1986,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 06:50:31','Panel docente','2025-12-03 01:50:31',NULL),(1987,'Luis Profe','docente','Registró observación para estudiante ID=9','—','2025-12-03 06:51:08','Observaciones','2025-12-03 01:51:08',NULL),(1988,'Luis Profe (ID: 1)','docente','Eliminó observación con ID 4','—','2025-12-03 06:51:13','Gestión de observaciones','2025-12-03 01:51:13',NULL),(1989,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:57:41','Notas por clase','2025-12-03 01:57:41',NULL),(1990,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-03 06:57:50','Consulta de notas por tabla','2025-12-03 01:57:50',NULL),(1991,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 06:58:06','Notas por clase','2025-12-03 01:58:06',NULL),(1992,'Luis Profe (ID: 1)','docente','Guardó tabla de notas para clase ID 1','—','2025-12-03 06:58:30','Notas','2025-12-03 01:58:30',NULL),(1993,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 07:11:04','Inicio de sesión','2025-12-03 02:11:04',NULL),(1994,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 07:11:04','Panel docente','2025-12-03 02:11:04',NULL),(1995,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 07:11:09','Notas por clase','2025-12-03 02:11:09',NULL),(1996,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 07:11:12','Consulta de notas','2025-12-03 02:11:12',NULL),(1997,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 07:11:32','Panel docente','2025-12-03 02:11:32',NULL),(1998,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 07:11:38','Notas por clase','2025-12-03 02:11:38',NULL),(1999,'Luis Profe (ID: 1)','docente','Guardó tabla \'Tabla de Prueba \' para clase ID 1','—','2025-12-03 07:11:54','Notas','2025-12-03 02:11:54',NULL),(2000,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 07:15:44','Inicio de sesión','2025-12-03 02:15:44',NULL),(2001,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 07:15:44','Panel docente','2025-12-03 02:15:44',NULL),(2002,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-03 07:16:00','Consulta de notas por tabla','2025-12-03 02:16:00',NULL),(2003,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-03 07:16:13','Consulta de notas por tabla','2025-12-03 02:16:13',NULL),(2004,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 07:16:18','Notas por clase','2025-12-03 02:16:18',NULL),(2005,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 07:16:23','Consulta de notas','2025-12-03 02:16:23',NULL),(2006,'Luis Profe (ID: 1)','docente','Guardó tabla \'mmmmmm\' para clase ID 1','—','2025-12-03 07:16:37','Notas','2025-12-03 02:16:37',NULL),(2007,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 07:17:13','Consulta de notas','2025-12-03 02:17:13',NULL),(2008,'Luis Profe (ID: 1)','docente','Guardó tabla \'mmmmmm\' para clase ID 1','—','2025-12-03 07:17:19','Notas','2025-12-03 02:17:19',NULL),(2009,'Luis Profe (ID: 1)','docente','Eliminó nota con ID 18 en clase 1','—','2025-12-03 07:19:00','Gestión de notas','2025-12-03 02:19:00',NULL),(2010,'Luis Profe (ID: 1)','docente','Eliminó nota con ID 15 en clase 1','—','2025-12-03 07:19:05','Gestión de notas','2025-12-03 02:19:05',NULL),(2011,'Luis Profe (ID: 1)','docente','Registró nota para estudiante 9 en clase 1','—','2025-12-03 07:19:18','Registro de notas','2025-12-03 02:19:18',NULL),(2012,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 07:19:18','Notas por clase','2025-12-03 02:19:18',NULL),(2013,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 07:27:52','Consulta de notas','2025-12-03 02:27:52',NULL),(2014,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 07:36:15','Inicio de sesión','2025-12-03 02:36:15',NULL),(2015,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 07:36:16','Panel docente','2025-12-03 02:36:16',NULL),(2016,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 07:36:22','Notas por clase','2025-12-03 02:36:22',NULL),(2017,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 07:36:24','Consulta de notas','2025-12-03 02:36:24',NULL),(2018,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1 (tabla ID 2)','—','2025-12-03 07:40:56','Consulta de notas por tabla','2025-12-03 02:40:56',NULL),(2019,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 07:41:35','Inicio de sesión','2025-12-03 02:41:35',NULL),(2020,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 07:41:35','Panel docente','2025-12-03 02:41:35',NULL),(2021,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 07:41:41','Notas por clase','2025-12-03 02:41:41',NULL),(2022,'Luis Profe (ID: 1)','docente','Guardó tabla \'mmmmmm\' para clase ID 1','—','2025-12-03 07:41:52','Notas','2025-12-03 02:41:52',NULL),(2023,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 07:45:28','Inicio de sesión','2025-12-03 02:45:28',NULL),(2024,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 07:45:29','Panel docente','2025-12-03 02:45:29',NULL),(2025,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 07:45:36','Notas por clase','2025-12-03 02:45:36',NULL),(2026,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1 (tabla ID 12)','—','2025-12-03 07:53:40','Consulta de notas por tabla','2025-12-03 02:53:40',NULL),(2027,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 07:57:59','Inicio de sesión','2025-12-03 02:57:59',NULL),(2028,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 07:57:59','Panel docente','2025-12-03 02:57:59',NULL),(2029,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 07:58:08','Notas por clase','2025-12-03 02:58:08',NULL),(2030,'Luis Profe (ID: 1)','docente','Guardó tabla \'pturbs\' para clase ID 1','—','2025-12-03 07:58:23','Notas','2025-12-03 02:58:23',NULL),(2031,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 08:05:19','Notas por clase','2025-12-03 03:05:19',NULL),(2032,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 08:28:13','Inicio de sesión','2025-12-03 03:28:13',NULL),(2033,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 08:28:14','Panel docente','2025-12-03 03:28:14',NULL),(2034,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 08:29:32','Inicio de sesión','2025-12-03 03:29:32',NULL),(2035,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 08:29:33','Panel docente','2025-12-03 03:29:33',NULL),(2036,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 08:31:50','Inicio de sesión','2025-12-03 03:31:50',NULL),(2037,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 08:31:50','Panel docente','2025-12-03 03:31:50',NULL),(2038,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 08:33:44','Inicio de sesión','2025-12-03 03:33:44',NULL),(2039,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 08:33:44','Panel docente','2025-12-03 03:33:44',NULL),(2040,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 08:37:17','Inicio de sesión','2025-12-03 03:37:17',NULL),(2041,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 08:37:18','Panel docente','2025-12-03 03:37:18',NULL),(2042,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 08:39:09','Inicio de sesión','2025-12-03 03:39:09',NULL),(2043,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 08:39:09','Panel docente','2025-12-03 03:39:09',NULL),(2044,'1','docente','Eliminó tabla institucional ID 12','—','2025-12-03 08:39:20','Tablas guardadas','2025-12-03 03:39:20',NULL),(2045,'1','docente','Eliminó tabla institucional ID 13','—','2025-12-03 08:39:23','Tablas guardadas','2025-12-03 03:39:23',NULL),(2046,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 08:47:19','Inicio de sesión','2025-12-03 03:47:19',NULL),(2047,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 08:47:19','Panel docente','2025-12-03 03:47:19',NULL),(2048,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1 (tabla ID 2)','—','2025-12-03 08:48:40','Consulta de notas por tabla','2025-12-03 03:48:40',NULL),(2049,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 08:52:03','Notas por clase','2025-12-03 03:52:03',NULL),(2050,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 08:52:06','Consulta de notas','2025-12-03 03:52:06',NULL),(2051,'Luis Profe (ID: 1)','docente','Guardó tabla de notas para clase ID 1','—','2025-12-03 08:52:15','Notas','2025-12-03 03:52:15',NULL),(2052,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 09:10:22','Inicio de sesión','2025-12-03 04:10:22',NULL),(2053,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 09:10:23','Panel docente','2025-12-03 04:10:23',NULL),(2054,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 09:19:49','Inicio de sesión','2025-12-03 04:19:49',NULL),(2055,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 09:19:50','Panel docente','2025-12-03 04:19:50',NULL),(2056,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 09:58:21','Inicio de sesión','2025-12-03 04:58:21',NULL),(2057,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 09:58:21','Panel docente','2025-12-03 04:58:21',NULL),(2058,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 09:59:00','Notas por clase','2025-12-03 04:59:00',NULL),(2059,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 09:59:12','Notas por clase','2025-12-03 04:59:12',NULL),(2060,'Luis Profe (ID: 1)','docente','Registró nota para estudiante 9 en clase 1','—','2025-12-03 09:59:26','Registro de notas','2025-12-03 04:59:26',NULL),(2061,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 09:59:27','Notas por clase','2025-12-03 04:59:27',NULL),(2062,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 10:02:17','Notas por clase','2025-12-03 05:02:17',NULL),(2063,'Luis Profe (ID: 1)','docente','Guardó tabla de notas para clase ID 1','—','2025-12-03 10:04:08','Notas','2025-12-03 05:04:08',NULL),(2064,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 10:26:12','Inicio de sesión','2025-12-03 05:26:12',NULL),(2065,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 10:26:12','Panel docente','2025-12-03 05:26:12',NULL),(2066,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 10:26:22','Notas por clase','2025-12-03 05:26:22',NULL),(2067,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 10:44:17','Inicio de sesión','2025-12-03 05:44:17',NULL),(2068,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 10:44:17','Panel docente','2025-12-03 05:44:17',NULL),(2069,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 10:52:18','Notas por clase','2025-12-03 05:52:18',NULL),(2070,'Luis Profe (ID: 1)','docente','Eliminó nota con ID 20 en clase 1','—','2025-12-03 10:52:23','Gestión de notas','2025-12-03 05:52:23',NULL),(2071,'Luis Profe (ID: 1)','docente','Eliminó nota con ID 19 en clase 1','—','2025-12-03 10:52:26','Gestión de notas','2025-12-03 05:52:26',NULL),(2072,'Luis Profe (ID: 1)','docente','Registró nota para estudiante 9 en clase 1','—','2025-12-03 10:52:37','Registro de notas','2025-12-03 05:52:37',NULL),(2073,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 10:52:37','Notas por clase','2025-12-03 05:52:37',NULL),(2074,'Luis Profe (ID: 1)','docente','Guardó tabla de notas para clase ID 1','—','2025-12-03 10:52:48','Notas','2025-12-03 05:52:48',NULL),(2075,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 11:06:06','Inicio de sesión','2025-12-03 06:06:06',NULL),(2076,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 11:06:06','Panel docente','2025-12-03 06:06:06',NULL),(2077,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 11:08:31','Inicio de sesión','2025-12-03 06:08:31',NULL),(2078,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 11:08:31','Panel docente','2025-12-03 06:08:31',NULL),(2079,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 11:13:55','Inicio de sesión','2025-12-03 06:13:55',NULL),(2080,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 11:13:55','Panel docente','2025-12-03 06:13:55',NULL),(2081,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 11:27:03','Inicio de sesión','2025-12-03 06:27:03',NULL),(2082,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 11:27:03','Panel docente','2025-12-03 06:27:03',NULL),(2083,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 11:29:03','Inicio de sesión','2025-12-03 06:29:03',NULL),(2084,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 11:29:03','Panel docente','2025-12-03 06:29:03',NULL),(2085,'1','docente','Eliminó tabla institucional ID 16','—','2025-12-03 11:29:27','Tablas guardadas','2025-12-03 06:29:27',NULL),(2086,'1','docente','Eliminó tabla institucional ID 14','—','2025-12-03 11:29:30','Tablas guardadas','2025-12-03 06:29:30',NULL),(2087,'1','docente','Eliminó tabla institucional ID 15','—','2025-12-03 11:29:32','Tablas guardadas','2025-12-03 06:29:32',NULL),(2088,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 11:29:39','Notas por clase','2025-12-03 06:29:39',NULL),(2089,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 11:29:47','Consulta de notas','2025-12-03 06:29:47',NULL),(2090,'Luis Profe (ID: 1)','docente','Guardó tabla de notas para clase ID 1','—','2025-12-03 11:30:13','Notas','2025-12-03 06:30:13',NULL),(2091,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1 (tabla ID 17)','—','2025-12-03 11:30:27','Consulta de notas por tabla','2025-12-03 06:30:27',NULL),(2092,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1 (tabla ID 17)','—','2025-12-03 11:32:52','Consulta de notas por tabla','2025-12-03 06:32:52',NULL),(2093,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 11:37:01','Inicio de sesión','2025-12-03 06:37:01',NULL),(2094,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 11:37:02','Panel docente','2025-12-03 06:37:02',NULL),(2095,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 11:37:09','Notas por clase','2025-12-03 06:37:09',NULL),(2096,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 11:37:11','Consulta de notas','2025-12-03 06:37:11',NULL),(2097,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 11:37:16','Panel docente','2025-12-03 06:37:16',NULL),(2098,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-03 11:37:21','Consulta de notas por tabla','2025-12-03 06:37:21',NULL),(2099,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 11:42:45','Inicio de sesión','2025-12-03 06:42:45',NULL),(2100,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 11:42:45','Panel docente','2025-12-03 06:42:45',NULL),(2101,'Luis Profe (ID: 1)','docente','Envió tabla 17 al administrador con 1 notas','—','2025-12-03 11:46:22','Envío de tablas','2025-12-03 06:46:22',NULL),(2102,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 11:46:43','Notas por clase','2025-12-03 06:46:43',NULL),(2103,'Luis Profe (ID: 1)','docente','Registró nota para estudiante 8 en clase 1 (tabla 2)','—','2025-12-03 11:46:57','Registro de notas','2025-12-03 06:46:57',NULL),(2104,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 11:46:57','Notas por clase','2025-12-03 06:46:57',NULL),(2105,'Luis Profe (ID: 1)','docente','Guardó tabla de notas para clase ID 1','—','2025-12-03 11:47:12','Notas','2025-12-03 06:47:12',NULL),(2106,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 12:04:57','Inicio de sesión','2025-12-03 07:04:57',NULL),(2107,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 12:04:57','Panel docente','2025-12-03 07:04:57',NULL),(2108,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 12:17:53','Inicio de sesión','2025-12-03 07:17:53',NULL),(2109,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 12:17:54','Panel docente','2025-12-03 07:17:54',NULL),(2110,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-03 12:18:03','Consulta de notas por tabla','2025-12-03 07:18:03',NULL),(2111,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 12:20:24','Notas por clase','2025-12-03 07:20:24',NULL),(2112,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 12:20:35','Consulta de notas','2025-12-03 07:20:35',NULL),(2113,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 12:20:57','Inicio de sesión','2025-12-03 07:20:57',NULL),(2114,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 12:20:57','Panel docente','2025-12-03 07:20:57',NULL),(2115,'1','docente','Accedió a registrar notas en clase Sin nombre','—','2025-12-03 12:21:02','Notas por clase','2025-12-03 07:21:02',NULL),(2116,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 12:21:05','Consulta de notas','2025-12-03 07:21:05',NULL),(2117,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 12:39:23','Inicio de sesión','2025-12-03 07:39:23',NULL),(2118,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 12:39:23','Panel docente','2025-12-03 07:39:23',NULL),(2119,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 12:39:28','Notas por clase','2025-12-03 07:39:28',NULL),(2120,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 12:39:31','Consulta de notas','2025-12-03 07:39:31',NULL),(2121,'Luis Profe (ID: 1)','docente','Guardó tabla de notas para clase ID 1','—','2025-12-03 12:39:39','Notas','2025-12-03 07:39:39',NULL),(2122,'1','docente','Eliminó tabla institucional ID 17','—','2025-12-03 12:39:43','Tablas guardadas','2025-12-03 07:39:43',NULL),(2123,'1','docente','Eliminó tabla institucional ID 19','—','2025-12-03 12:39:54','Tablas guardadas','2025-12-03 07:39:54',NULL),(2124,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 12:42:48','Inicio de sesión','2025-12-03 07:42:48',NULL),(2125,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 12:42:49','Panel docente','2025-12-03 07:42:49',NULL),(2126,'1','docente','Accedió a registrar notas en clase Piano Basico','—','2025-12-03 12:43:01','Notas por clase','2025-12-03 07:43:01',NULL),(2127,'Luis Profe (ID: 1)','docente','Consultó notas registradas por docente','—','2025-12-03 12:43:03','Consulta de notas','2025-12-03 07:43:03',NULL),(2128,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 12:47:29','Inicio de sesión','2025-12-03 07:47:29',NULL),(2129,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 12:47:29','Panel docente','2025-12-03 07:47:29',NULL),(2130,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 12:50:29','Inicio de sesión','2025-12-03 07:50:29',NULL),(2131,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 12:50:30','Panel docente','2025-12-03 07:50:30',NULL),(2132,'Luis Profe (ID: 1)','docente','Envió tabla 18 al administrador','—','2025-12-03 12:50:34','Envío de tablas','2025-12-03 07:50:34',NULL),(2133,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 12:54:20','Inicio de sesión','2025-12-03 07:54:20',NULL),(2134,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 12:54:20','Panel administrador','2025-12-03 07:54:20',NULL),(2135,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-03 12:55:05','Bitácora','2025-12-03 07:55:05',NULL),(2136,'Carla','administrador','Filtrar auditoría','Filtro aplicado desde  hasta ','2025-12-03 12:56:05','Auditoría','2025-12-03 07:56:05',NULL),(2137,'Carla','administrador','Filtrar auditoría','Filtro aplicado desde 2025-12-02 hasta ','2025-12-03 12:56:13','Auditoría','2025-12-03 07:56:13',NULL),(2138,'Carla','administrador','Filtrar auditoría','Filtro aplicado desde 2025-12-02 hasta ','2025-12-03 12:56:34','Auditoría','2025-12-03 07:56:34',NULL),(2139,'Carla','administrador','Filtrar auditoría','Filtro aplicado desde  hasta ','2025-12-03 12:56:44','Auditoría','2025-12-03 07:56:44',NULL),(2140,'Carla','administrador','Filtrar auditoría','Filtro aplicado desde  hasta ','2025-12-03 12:57:08','Auditoría','2025-12-03 07:57:08',NULL),(2141,'Carla','administrador','Consultó notas con filtros aplicados','—','2025-12-03 12:57:29','Notas musicales','2025-12-03 07:57:29',NULL),(2142,'Carla','administrador','Filtrar auditoría','Filtro aplicado desde  hasta ','2025-12-03 12:58:35','Auditoría','2025-12-03 07:58:35',NULL),(2143,'Alexa','estudiante','Inicio de sesión exitoso','—','2025-12-03 12:59:47','Inicio de sesión','2025-12-03 07:59:47',NULL),(2144,'Alexa','estudiante','Accedió al panel principal','—','2025-12-03 12:59:47','Panel Estudiante','2025-12-03 07:59:47',NULL),(2145,'Alexa','estudiante','Visualizó su perfil académico y de usuario','—','2025-12-03 12:59:51','Perfil','2025-12-03 07:59:51',NULL),(2146,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 21:00:42','Inicio de sesión','2025-12-03 16:00:42',NULL),(2147,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 21:00:42','Panel docente','2025-12-03 16:00:42',NULL),(2148,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 21:14:33','Inicio de sesión','2025-12-03 16:14:33',NULL),(2149,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 21:14:33','Panel docente','2025-12-03 16:14:33',NULL),(2150,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 23:25:40','Inicio de sesión','2025-12-03 18:25:40',NULL),(2151,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 23:25:40','Panel administrador','2025-12-03 18:25:40',NULL),(2152,'Carla (ID: 3)','administrador','Listar inscripciones','Se consultaron 2 inscripciones de la tabla institucional.','2025-12-03 23:25:52','Inscripciones','2025-12-03 18:25:52',NULL),(2153,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 23:39:41','Inicio de sesión','2025-12-03 18:39:41',NULL),(2154,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 23:39:41','Inicio de sesión','2025-12-03 18:39:41',NULL),(2155,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 23:39:41','Panel administrador','2025-12-03 18:39:41',NULL),(2156,'Carla (ID: 3)','administrador','Listar inscripciones','Se consultaron 2 inscripciones de la tabla institucional.','2025-12-03 23:39:46','Inscripciones','2025-12-03 18:39:46',NULL),(2157,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 23:48:28','Inicio de sesión','2025-12-03 18:48:28',NULL),(2158,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 23:48:28','Panel administrador','2025-12-03 18:48:28',NULL),(2159,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 23:48:49','Inicio de sesión','2025-12-03 18:48:49',NULL),(2160,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 23:48:49','Panel docente','2025-12-03 18:48:49',NULL),(2161,'1','docente','Visualizó listado de estudiantes de la clase ID 1','—','2025-12-03 23:48:53','Clases','2025-12-03 18:48:53',NULL),(2162,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-03 23:50:15','Inicio de sesión','2025-12-03 18:50:15',NULL),(2163,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 23:50:15','Panel docente','2025-12-03 18:50:15',NULL),(2164,'Luis Profe (ID: 1)','docente','Consultó notas de la clase ID 1','—','2025-12-03 23:50:23','Consulta de notas por tabla','2025-12-03 18:50:23',NULL),(2165,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-03 23:50:27','Panel docente','2025-12-03 18:50:27',NULL),(2166,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-03 23:52:09','Inicio de sesión','2025-12-03 18:52:09',NULL),(2167,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-03 23:52:09','Panel administrador','2025-12-03 18:52:09',NULL),(2168,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-03 23:52:56','Tablas validadas','2025-12-03 18:52:56',NULL),(2169,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-03 23:53:04','Tablas validadas','2025-12-03 18:53:04',NULL),(2170,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-03 23:53:24','Tablas validadas','2025-12-03 18:53:24',NULL),(2171,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-03 23:53:28','Tablas validadas','2025-12-03 18:53:28',NULL),(2172,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-03 23:53:31','Tablas validadas','2025-12-03 18:53:31',NULL),(2173,'Carla','administrador','Consultó el panel de tablas enviadas por docentes','—','2025-12-03 23:58:12','Tablas recibidas','2025-12-03 18:58:12',NULL),(2174,'Carla','administrador','Validó tabla institucional con ID 18','—','2025-12-04 00:04:21','Certificación','2025-12-03 19:04:21',NULL),(2175,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-04 00:04:21','Tablas validadas','2025-12-03 19:04:21',NULL),(2176,'Carla','administrador','Emitió certificados de tabla ID 18','—','2025-12-04 00:09:07','Certificación','2025-12-03 19:09:07',NULL),(2177,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-04 00:09:07','Tablas validadas','2025-12-03 19:09:07',NULL),(2178,'Alexa','estudiante','Inicio de sesión exitoso','—','2025-12-04 00:09:38','Inicio de sesión','2025-12-03 19:09:38',NULL),(2179,'Alexa','estudiante','Accedió al panel principal','—','2025-12-04 00:09:38','Panel Estudiante','2025-12-03 19:09:38',NULL),(2180,'Alexa','estudiante','Visualizar certificados','—','2025-12-04 00:09:45','Certificados','2025-12-03 19:09:45',NULL),(2181,'Alexa','estudiante','Descargó certificado de Piano - Etapa 1','—','2025-12-04 00:16:51','Certificados','2025-12-03 19:16:51',NULL),(2182,'Alexa','estudiante','Visualizó su perfil académico y de usuario','—','2025-12-04 00:26:47','Perfil','2025-12-03 19:26:47',NULL),(2183,'Alexa','estudiante','Visualizó su perfil académico y de usuario','—','2025-12-04 00:28:38','Perfil','2025-12-03 19:28:38',NULL),(2184,'Alexa','estudiante','Visualizó su perfil académico y de usuario','—','2025-12-04 00:30:23','Perfil','2025-12-03 19:30:23',NULL),(2185,'Alexa','estudiante','Visualizó su perfil académico y de usuario','—','2025-12-04 00:34:08','Perfil','2025-12-03 19:34:08',NULL),(2186,'Alexa','estudiante','Actualizó su perfil académico y de usuario','—','2025-12-04 00:34:41','Perfil','2025-12-03 19:34:41',NULL),(2187,'Alexa','estudiante','Visualizó su perfil académico y de usuario','—','2025-12-04 00:34:41','Perfil','2025-12-03 19:34:41',NULL),(2188,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-04 00:36:11','Inicio de sesión','2025-12-03 19:36:11',NULL),(2189,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-04 00:36:11','Panel docente','2025-12-03 19:36:11',NULL),(2190,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-04 10:01:06','Inicio de sesión','2025-12-04 05:01:06',NULL),(2191,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 10:01:07','Panel administrador','2025-12-04 05:01:07',NULL),(2192,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-04 10:01:25','Gestión de clases institucionales','2025-12-04 05:01:25',NULL),(2193,'Carla','administrador','Asignó docente ID 1 a clase ID 2','—','2025-12-04 10:10:03','Asignación de Docentes','2025-12-04 05:10:03',NULL),(2194,'Carla','administrador','Asignación de docente ID 1 a clase ID 2','—','2025-12-04 10:10:03','Asignaciones','2025-12-04 05:10:03',NULL),(2195,'Luis Profe','docente','Inicio de sesión exitoso','—','2025-12-04 10:10:47','Inicio de sesión','2025-12-04 05:10:47',NULL),(2196,'Luis Profe (ID: 1)','docente','Accedió al panel con clases y métricas','—','2025-12-04 10:10:48','Panel docente','2025-12-04 05:10:48',NULL),(2197,'1','docente','Accedió a registrar notas en clase Guitarra acustica','—','2025-12-04 10:11:02','Notas por clase','2025-12-04 05:11:02',NULL),(2198,'1','docente','Visualizó listado de estudiantes de la clase ID 2','—','2025-12-04 10:11:19','Clases','2025-12-04 05:11:19',NULL),(2199,'Alexa','estudiante','Inicio de sesión exitoso','—','2025-12-04 13:19:47','Inicio de sesión','2025-12-04 08:19:47',NULL),(2200,'Alexa','estudiante','Accedió al panel principal','—','2025-12-04 13:19:49','Panel Estudiante','2025-12-04 08:19:49',NULL),(2201,'Alexa','estudiante','Visualizó listado de clases disponibles e inscritas','—','2025-12-04 13:19:54','Clases','2025-12-04 08:19:54',NULL),(2202,'Alexa','estudiante','Se inscribió en clase ID 2','—','2025-12-04 13:33:15','Inscripción','2025-12-04 08:33:15',NULL),(2203,'Alexa','estudiante','Visualizó listado de clases disponibles e inscritas','—','2025-12-04 13:33:17','Clases','2025-12-04 08:33:17',NULL),(2204,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-04 13:59:53','Inicio de sesión','2025-12-04 08:59:53',NULL),(2205,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 13:59:53','Panel administrador','2025-12-04 08:59:53',NULL),(2206,'Carla (ID: 3)','administrador','Listar inscripciones','Se consultaron 3 inscripciones de la tabla institucional.','2025-12-04 14:00:05','Inscripciones','2025-12-04 09:00:05',NULL),(2207,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-04 14:00:55','Gestión de clases institucionales','2025-12-04 09:00:55',NULL),(2208,'Carla','administrador','Consultó horarios institucionales','—','2025-12-04 14:01:14','Horarios de clase','2025-12-04 09:01:14',NULL),(2209,'Carla','administrador','Consultó listado completo de usuarios, docentes y estudiantes institucionales','—','2025-12-04 14:01:18','Gestión de usuarios','2025-12-04 09:01:18',NULL),(2210,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-04 14:21:45','Inicio de sesión','2025-12-04 09:21:45',NULL),(2211,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 14:21:46','Panel administrador','2025-12-04 09:21:46',NULL),(2212,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-04 14:21:52','Gestión de clases institucionales','2025-12-04 09:21:52',NULL),(2213,'Carla','administrador','Consultó listado completo de usuarios, docentes y estudiantes institucionales','—','2025-12-04 14:22:02','Gestión de usuarios','2025-12-04 09:22:02',NULL),(2214,'Carla (ID: 3)','administrador','Editó usuario institucional con ID 10','—','2025-12-04 14:30:50','Gestión de usuarios','2025-12-04 09:30:50',NULL),(2215,'Carla','administrador','Consultó listado completo de usuarios, docentes y estudiantes institucionales','—','2025-12-04 14:30:52','Gestión de usuarios','2025-12-04 09:30:52',NULL),(2216,'Carla (ID: 3)','administrador','Editó usuario institucional con ID 6','—','2025-12-04 14:31:02','Gestión de usuarios','2025-12-04 09:31:02',NULL),(2217,'Carla','administrador','Consultó listado completo de usuarios, docentes y estudiantes institucionales','—','2025-12-04 14:31:03','Gestión de usuarios','2025-12-04 09:31:03',NULL),(2218,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-04 14:41:27','Inicio de sesión','2025-12-04 09:41:27',NULL),(2219,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 14:41:27','Panel administrador','2025-12-04 09:41:27',NULL),(2220,'Carla','administrador','Editó usuario con ID 10 cambiando estado a activo','—','2025-12-04 14:42:41','Gestión de usuarios','2025-12-04 09:42:41',NULL),(2221,'Carla','administrador','Editó usuario con ID 10 cambiando estado a activo','—','2025-12-04 14:45:58','Gestión de usuarios','2025-12-04 09:45:58',NULL),(2222,'Carla','administrador','Editó usuario con ID 1 cambiando estado a activo','—','2025-12-04 14:46:01','Gestión de usuarios','2025-12-04 09:46:01',NULL),(2223,'Carla','administrador','Editó usuario con ID 10 cambiando estado a activo','—','2025-12-04 14:46:05','Gestión de usuarios','2025-12-04 09:46:05',NULL),(2224,'Carla','administrador','Editó usuario con ID 10 cambiando estado a activo','—','2025-12-04 14:49:00','Gestión de usuarios','2025-12-04 09:49:00',NULL),(2225,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-04 14:52:38','Inicio de sesión','2025-12-04 09:52:38',NULL),(2226,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 14:52:39','Panel administrador','2025-12-04 09:52:39',NULL),(2227,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-04 14:55:10','Inicio de sesión','2025-12-04 09:55:10',NULL),(2228,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 14:55:10','Panel administrador','2025-12-04 09:55:10',NULL),(2229,'Carla (ID: 3)','administrador','Editó usuario institucional con ID 11','—','2025-12-04 14:55:26','Gestión de usuarios','2025-12-04 09:55:26',NULL),(2230,'Carla','administrador','Eliminó usuario con ID 12','—','2025-12-04 14:55:52','Gestión de usuarios','2025-12-04 09:55:52',NULL),(2231,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-04 18:11:32','Inicio de sesión','2025-12-04 13:11:32',NULL),(2232,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 18:11:34','Panel administrador','2025-12-04 13:11:34',NULL),(2233,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:11:43','Bitácora','2025-12-04 13:11:43',NULL),(2234,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:14:45','Bitácora','2025-12-04 13:14:45',NULL),(2235,'Carla','administrador','Accedió a gestionar clases institucionales','—','2025-12-04 18:15:00','Gestión de clases institucionales','2025-12-04 13:15:00',NULL),(2236,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:15:05','Bitácora','2025-12-04 13:15:05',NULL),(2237,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:15:57','Bitácora','2025-12-04 13:15:57',NULL),(2238,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 18:16:00','Panel administrador','2025-12-04 13:16:00',NULL),(2239,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 18:16:01','Panel administrador','2025-12-04 13:16:01',NULL),(2240,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:16:06','Bitácora','2025-12-04 13:16:06',NULL),(2241,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:16:57','Bitácora','2025-12-04 13:16:57',NULL),(2242,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:17:26','Bitácora','2025-12-04 13:17:26',NULL),(2243,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:18:45','Bitácora','2025-12-04 13:18:45',NULL),(2244,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:19:33','Bitácora','2025-12-04 13:19:33',NULL),(2245,'Carla','administrador','Se realiza una prueba de funcionamiento de bitácora','—','2025-12-04 18:19:55','Notas','2025-12-04 13:19:55',NULL),(2246,'Carla','administrador','Confirmación de funcionalidad de bitácora','—','2025-12-04 18:24:33','Notas','2025-12-04 13:24:33',NULL),(2247,'Carla','administrador','Ver bitácora','Se consultaron 1 registros de bitácora.','2025-12-04 18:24:54','Bitácora','2025-12-04 13:24:54',NULL),(2248,'Carla','administrador','Ver bitácora','Se consultaron 1 registros de bitácora.','2025-12-04 18:24:55','Bitácora','2025-12-04 13:24:55',NULL),(2249,'Carla','administrador','Ver bitácora','Se consultaron 1 registros de bitácora.','2025-12-04 18:24:56','Bitácora','2025-12-04 13:24:56',NULL),(2250,'Carla','administrador','Ver bitácora','Se consultaron 1 registros de bitácora.','2025-12-04 18:24:56','Bitácora','2025-12-04 13:24:56',NULL),(2251,'Carla','administrador','Ver bitácora','Se consultaron 1 registros de bitácora.','2025-12-04 18:24:57','Bitácora','2025-12-04 13:24:57',NULL),(2252,'Carla','administrador','Ver bitácora','Se consultaron 0 registros de bitácora.','2025-12-04 18:26:21','Bitácora','2025-12-04 13:26:21',NULL),(2253,'Carla','administrador','prueba','—','2025-12-04 18:26:32','Notas','2025-12-04 13:26:32',NULL),(2254,'Carla','administrador','Ver bitácora','Se consultaron 1 registros de bitácora.','2025-12-04 18:26:36','Bitácora','2025-12-04 13:26:36',NULL),(2255,'Carla','administrador','Ver bitácora','Se consultaron 1 registros de bitácora.','2025-12-04 18:32:28','Bitácora','2025-12-04 13:32:28',NULL),(2256,'Carla','administrador','Ver bitácora','Se consultaron 1 registros de bitácora.','2025-12-04 18:32:32','Bitácora','2025-12-04 13:32:32',NULL),(2257,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-04 18:34:33','Inicio de sesión','2025-12-04 13:34:33',NULL),(2258,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-04 18:34:34','Panel administrador','2025-12-04 13:34:34',NULL),(2259,'Carla','administrador','Ver bitácora','Se consultaron 2 registros de bitácora.','2025-12-04 18:34:39','Bitácora','2025-12-04 13:34:39',NULL),(2260,'Carla','administrador','Ver bitácora','Se consultaron 2 registros de bitácora.','2025-12-04 18:37:10','Bitácora','2025-12-04 13:37:10',NULL),(2261,'Carla','administrador','Prueba de funcionamiento al registro de una acción en bitácora','—','2025-12-04 18:40:23','Notas','2025-12-04 13:40:23',NULL),(2262,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-05 02:32:36','Inicio de sesión','2025-12-04 21:32:36',NULL),(2263,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-05 03:25:02','Inicio de sesión','2025-12-04 22:25:02',NULL),(2264,'Carla (ID: 3)','administrador','Intentó editar usuario ID 6 pero no se actualizó.','—','2025-12-05 03:33:31','Gestión de usuarios','2025-12-04 22:33:31',NULL),(2265,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-05 03:42:02','Inicio de sesión','2025-12-04 22:42:02',NULL),(2266,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-05 03:42:03','Panel administrador','2025-12-04 22:42:03',NULL),(2267,'Carla','administrador','Consultó el panel de tablas institucionales validadas','—','2025-12-05 03:47:03','Tablas validadas','2025-12-04 22:47:03',NULL),(2268,'Carla','administrador','Consultó el panel de tablas enviadas por docentes','—','2025-12-05 03:47:39','Tablas recibidas','2025-12-04 22:47:39',NULL),(2269,'Carla','administrador','Consultó el panel de tablas enviadas por docentes','—','2025-12-05 03:47:41','Tablas recibidas','2025-12-04 22:47:41',NULL),(2270,'Carla','administrador','Ver bitácora','Se consultaron 9 registros de bitácora.','2025-12-05 03:47:48','Bitácora','2025-12-04 22:47:48',NULL),(2271,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-05 06:42:27','Inicio de sesión','2025-12-05 01:42:27',NULL),(2272,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-05 06:42:27','Panel administrador','2025-12-05 01:42:27',NULL),(2273,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-05 06:42:43','Inicio de sesión','2025-12-05 01:42:43',NULL),(2274,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-05 06:42:43','Panel administrador','2025-12-05 01:42:43',NULL),(2275,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-05 06:42:53','Panel administrador','2025-12-05 01:42:53',NULL),(2276,'Carla','administrador','Inicio de sesión exitoso','—','2025-12-05 09:43:03','Inicio de sesión','2025-12-05 04:43:03',NULL),(2277,'Carla (ID: 3)','administrador','Accedió al Panel administrador','—','2025-12-05 09:43:03','Panel administrador','2025-12-05 04:43:03',NULL);
/*!40000 ALTER TABLE `auditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bitacora`
--

DROP TABLE IF EXISTS `bitacora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bitacora` (
  `id_accion` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) NOT NULL,
  `rol` varchar(50) NOT NULL,
  `modulo` varchar(100) NOT NULL,
  `accion` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_accion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bitacora`
--

LOCK TABLES `bitacora` WRITE;
/*!40000 ALTER TABLE `bitacora` DISABLE KEYS */;
/*!40000 ALTER TABLE `bitacora` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bitacora_validaciones`
--

DROP TABLE IF EXISTS `bitacora_validaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bitacora_validaciones` (
  `id_accion` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) NOT NULL,
  `rol` varchar(50) NOT NULL,
  `modulo` varchar(100) NOT NULL,
  `accion` varchar(255) NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_accion`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bitacora_validaciones`
--

LOCK TABLES `bitacora_validaciones` WRITE;
/*!40000 ALTER TABLE `bitacora_validaciones` DISABLE KEYS */;
INSERT INTO `bitacora_validaciones` VALUES (1,'Carla','administrador','Notas','prueba','2025-12-04 18:26:32'),(2,'Carla','administrador','Panel administrador','Administrador accedió al panel institucional','2025-12-04 18:34:33'),(3,'Carla','administrador','Notas','Prueba de funcionamiento al registro de una acción en bitácora','2025-12-04 18:40:23'),(4,'Carla','administrador','Gestión de usuarios','Administrador editó usuario ID 6','2025-12-05 03:33:31'),(5,'Carla','administrador','Panel administrador','Administrador accedió al panel institucional','2025-12-05 03:42:03'),(6,'Carla','administrador','Tablas validadas','Administrador consultó tablas institucionales validadas','2025-12-05 03:47:03'),(7,'Carla','administrador','Auditoría','Administrador consultó registros de auditoría institucional','2025-12-05 03:47:29'),(8,'Carla','administrador','Tablas recibidas','Administrador consultó tablas institucionales recibidas de docentes','2025-12-05 03:47:39'),(9,'Carla','administrador','Tablas recibidas','Administrador consultó tablas institucionales recibidas de docentes','2025-12-05 03:47:41'),(10,'Carla','administrador','Panel administrador','Administrador accedió al panel institucional','2025-12-05 06:42:27'),(11,'Carla','administrador','Panel administrador','Administrador accedió al panel institucional','2025-12-05 06:42:43'),(12,'Carla','administrador','Panel administrador','Administrador accedió al panel institucional','2025-12-05 06:42:53'),(13,'Carla','administrador','Panel administrador','Administrador accedió al panel institucional','2025-12-05 09:43:03');
/*!40000 ALTER TABLE `bitacora_validaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificados`
--

DROP TABLE IF EXISTS `certificados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `certificados` (
  `id_certificado` int(11) NOT NULL AUTO_INCREMENT,
  `id_estudiante` int(11) NOT NULL,
  `id_clase` int(11) NOT NULL,
  `fecha_emision` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'emitido',
  PRIMARY KEY (`id_certificado`),
  KEY `id_estudiante` (`id_estudiante`),
  KEY `id_clase` (`id_clase`),
  CONSTRAINT `certificados_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `certificados_ibfk_2` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificados`
--

LOCK TABLES `certificados` WRITE;
/*!40000 ALTER TABLE `certificados` DISABLE KEYS */;
/*!40000 ALTER TABLE `certificados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificados_estudiante`
--

DROP TABLE IF EXISTS `certificados_estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `certificados_estudiante` (
  `id_certificado` int(11) NOT NULL AUTO_INCREMENT,
  `id_tabla` int(11) NOT NULL,
  `id_clase` int(11) NOT NULL,
  `id_docente` int(11) NOT NULL,
  `id_estudiante` int(11) NOT NULL,
  `instrumento` varchar(100) DEFAULT NULL,
  `etapa` varchar(100) DEFAULT NULL,
  `fecha_emision` date NOT NULL DEFAULT curdate(),
  `estado` varchar(20) NOT NULL,
  `url_certificado` varchar(255) NOT NULL DEFAULT 'pendiente.pdf',
  `usuario_admin` varchar(100) NOT NULL,
  PRIMARY KEY (`id_certificado`),
  KEY `id_clase` (`id_clase`),
  KEY `id_docente` (`id_docente`),
  KEY `id_estudiante` (`id_estudiante`),
  CONSTRAINT `certificados_estudiante_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`),
  CONSTRAINT `certificados_estudiante_ibfk_2` FOREIGN KEY (`id_docente`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `certificados_estudiante_ibfk_3` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificados_estudiante`
--

LOCK TABLES `certificados_estudiante` WRITE;
/*!40000 ALTER TABLE `certificados_estudiante` DISABLE KEYS */;
INSERT INTO `certificados_estudiante` VALUES (2,6,1,1,8,'Piano','Etapa 1','2025-11-30','Emitido','','Carla'),(4,2,1,1,8,'Piano','Etapa 1','2025-11-30','Emitido','certificados/certificado_8_1.pdf','Carla'),(5,7,1,1,8,'Piano','Etapa 1','2025-11-30','Emitido','certificados/certificado_8_1.pdf','Carla'),(6,18,1,1,8,'Piano','Etapa 1','2025-12-03','Emitido','certificados/certificado_8_1.pdf','Carla'),(7,18,1,1,9,'Piano','Etapa 1','2025-12-03','Emitido','certificados/certificado_9_1.pdf','Carla');
/*!40000 ALTER TABLE `certificados_estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clases`
--

DROP TABLE IF EXISTS `clases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clases` (
  `id_clase` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) DEFAULT NULL,
  `nombre_clase` varchar(150) NOT NULL,
  `instrumento` varchar(100) DEFAULT NULL,
  `etapa` varchar(100) DEFAULT NULL,
  `grupo` varchar(50) DEFAULT NULL,
  `cupo` int(11) DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `id_docente` int(11) DEFAULT NULL,
  `fecha_limite` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'activa',
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT NULL,
  `dia_semana` varchar(20) DEFAULT NULL,
  `hora_inicio` time DEFAULT NULL,
  `hora_fin` time DEFAULT NULL,
  `aula` varchar(100) DEFAULT NULL,
  `usuario_editor` varchar(100) DEFAULT NULL,
  `fecha_asignacion` date DEFAULT NULL,
  PRIMARY KEY (`id_clase`),
  KEY `fk_clase_docente` (`id_docente`),
  CONSTRAINT `fk_clase_docente` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id_docente`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clases`
--

LOCK TABLES `clases` WRITE;
/*!40000 ALTER TABLE `clases` DISABLE KEYS */;
INSERT INTO `clases` VALUES (1,NULL,'Piano Basico','Piano','Etapa 1','B',1,'2025-11-28','2025-12-31',1,'2025-11-26','asignada','2025-11-27 00:00:00','2025-12-02 00:00:00',NULL,NULL,NULL,NULL,NULL,'2025-11-27'),(2,NULL,'Guitarra acustica','Guitarra','Etapa 6','A',1,'2025-12-11','2026-02-26',1,'2025-12-03','asignada','2025-12-02 00:00:00','2025-12-04 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `clases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clases_asignadas`
--

DROP TABLE IF EXISTS `clases_asignadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clases_asignadas` (
  `id_asignacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_clase` int(11) NOT NULL,
  `id_docente` int(11) NOT NULL,
  `horario` varchar(50) NOT NULL DEFAULT '',
  `aula` varchar(50) NOT NULL DEFAULT '',
  `dia` varchar(20) NOT NULL DEFAULT '',
  `fecha_asignacion` datetime NOT NULL DEFAULT current_timestamp(),
  `estado` varchar(20) DEFAULT 'asignada',
  `registrada_por` varchar(100) DEFAULT NULL,
  `tipo_registro` varchar(50) DEFAULT 'manual',
  `observacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_asignacion`),
  KEY `idx_asignacion_clase` (`id_clase`),
  KEY `idx_asignacion_docente` (`id_docente`),
  CONSTRAINT `fk_asignacion_clase` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_asignacion_docente` FOREIGN KEY (`id_docente`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clases_asignadas`
--

LOCK TABLES `clases_asignadas` WRITE;
/*!40000 ALTER TABLE `clases_asignadas` DISABLE KEYS */;
INSERT INTO `clases_asignadas` VALUES (5,1,1,'','','','2025-12-02 00:00:00','asignada','Carla','Asignación manual','Clase asignada al docente desde panel administrador'),(7,2,1,'','','','2025-12-04 00:00:00','asignada','Carla','Asignación manual','Clase asignada al docente desde panel administrador');
/*!40000 ALTER TABLE `clases_asignadas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clases_estudiantes`
--

DROP TABLE IF EXISTS `clases_estudiantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clases_estudiantes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_clase` int(11) NOT NULL,
  `id_estudiante` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_clase` (`id_clase`),
  KEY `id_estudiante` (`id_estudiante`),
  CONSTRAINT `clases_estudiantes_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `clases_estudiantes_ibfk_2` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clases_estudiantes`
--

LOCK TABLES `clases_estudiantes` WRITE;
/*!40000 ALTER TABLE `clases_estudiantes` DISABLE KEYS */;
/*!40000 ALTER TABLE `clases_estudiantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colores_roles`
--

DROP TABLE IF EXISTS `colores_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `colores_roles` (
  `id_color` int(11) NOT NULL AUTO_INCREMENT,
  `rol` varchar(50) NOT NULL,
  `color_hex` varchar(10) NOT NULL,
  PRIMARY KEY (`id_color`),
  UNIQUE KEY `rol` (`rol`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colores_roles`
--

LOCK TABLES `colores_roles` WRITE;
/*!40000 ALTER TABLE `colores_roles` DISABLE KEYS */;
INSERT INTO `colores_roles` VALUES (1,'general','#198754'),(2,'administrador','#198754'),(3,'docente','#196186'),(4,'estudiante','#198754');
/*!40000 ALTER TABLE `colores_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_docente`
--

DROP TABLE IF EXISTS `configuracion_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuracion_docente` (
  `id_config` int(11) NOT NULL AUTO_INCREMENT,
  `id_docente` int(11) NOT NULL,
  `tema_oscuro` tinyint(1) DEFAULT 0,
  `mostrar_indicadores` tinyint(1) DEFAULT 1,
  `idioma` varchar(10) DEFAULT 'es',
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_config`),
  KEY `id_docente` (`id_docente`),
  CONSTRAINT `configuracion_docente_ibfk_1` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id_docente`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_docente`
--

LOCK TABLES `configuracion_docente` WRITE;
/*!40000 ALTER TABLE `configuracion_docente` DISABLE KEYS */;
INSERT INTO `configuracion_docente` VALUES (1,1,0,1,'es','2025-12-03 05:48:20');
/*!40000 ALTER TABLE `configuracion_docente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_sistema`
--

DROP TABLE IF EXISTS `configuracion_sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuracion_sistema` (
  `id_config` int(11) NOT NULL AUTO_INCREMENT,
  `clave` varchar(255) DEFAULT NULL,
  `valor` varchar(255) NOT NULL,
  PRIMARY KEY (`id_config`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_sistema`
--

LOCK TABLES `configuracion_sistema` WRITE;
/*!40000 ALTER TABLE `configuracion_sistema` DISABLE KEYS */;
INSERT INTO `configuracion_sistema` VALUES (1,'correo_soporte','soporte@symphonysias.edu.co'),(2,'version_sistema','1.0.0'),(3,'nombre_institucional','SymphonySIAS');
/*!40000 ALTER TABLE `configuracion_sistema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `docentes`
--

DROP TABLE IF EXISTS `docentes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `docentes` (
  `id_docente` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `instrumento` varchar(100) DEFAULT NULL,
  `nivel_tecnico` varchar(100) DEFAULT NULL,
  `etapa` varchar(100) DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `correo` varchar(150) DEFAULT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'activo',
  PRIMARY KEY (`id_docente`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `docentes_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docentes`
--

LOCK TABLES `docentes` WRITE;
/*!40000 ALTER TABLE `docentes` DISABLE KEYS */;
INSERT INTO `docentes` VALUES (1,6,NULL,'Intermedio',NULL,'Luis ','Profe','docente@correo','','2025-11-26','Villa Bonita Cra #16A','123456','activo'),(2,11,NULL,NULL,NULL,'Shrek','Profe','shrek@correo','','2025-12-02','','','activo');
/*!40000 ALTER TABLE `docentes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estudiantes`
--

DROP TABLE IF EXISTS `estudiantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudiantes` (
  `id_estudiante` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(150) DEFAULT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `correo` varchar(150) DEFAULT NULL,
  `instrumento` varchar(100) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `etapa_pedagogica` varchar(100) DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'activo',
  `nivel_tecnico` varchar(50) DEFAULT NULL,
  `progreso` varchar(50) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  PRIMARY KEY (`id_estudiante`),
  KEY `fk_estudiante_usuario_nueva` (`id_usuario`),
  CONSTRAINT `estudiantes_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_estudiante_usuario_nueva` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiantes`
--

LOCK TABLES `estudiantes` WRITE;
/*!40000 ALTER TABLE `estudiantes` DISABLE KEYS */;
INSERT INTO `estudiantes` VALUES (8,10,'Alexa','ApellidoReal','estudiante@correo','Piano','#55 florida','0000000123','Etapa 1','2025-11-30','activo','Básico','Iniciado',''),(9,1,'Manuelita','ApellidoReal','estudiante1@correo','Pendiente','Sin dirección','0000000000','Etapa 1','2025-11-26','activo','Básico','Iniciado','');
/*!40000 ALTER TABLE `estudiantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etapas_musicales`
--

DROP TABLE IF EXISTS `etapas_musicales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etapas_musicales` (
  `id_etapa` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_etapa` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_etapa`),
  UNIQUE KEY `nombre_etapa` (`nombre_etapa`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etapas_musicales`
--

LOCK TABLES `etapas_musicales` WRITE;
/*!40000 ALTER TABLE `etapas_musicales` DISABLE KEYS */;
INSERT INTO `etapas_musicales` VALUES (7,'Intermedia',NULL,'2025-11-26 23:56:36');
/*!40000 ALTER TABLE `etapas_musicales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horarios`
--

DROP TABLE IF EXISTS `horarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `horarios` (
  `id_horario` int(11) NOT NULL AUTO_INCREMENT,
  `id_clase` int(11) NOT NULL,
  `dia` varchar(20) NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `aula` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_horario`),
  KEY `id_clase` (`id_clase`),
  CONSTRAINT `horarios_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horarios`
--

LOCK TABLES `horarios` WRITE;
/*!40000 ALTER TABLE `horarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `horarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horarios_clase`
--

DROP TABLE IF EXISTS `horarios_clase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `horarios_clase` (
  `id_horario` int(11) NOT NULL AUTO_INCREMENT,
  `id_clase` int(11) NOT NULL,
  `dia_semana` varchar(20) NOT NULL,
  `fecha` date DEFAULT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `aula` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_horario`),
  KEY `clase_id` (`id_clase`),
  CONSTRAINT `horarios_clase_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horarios_clase`
--

LOCK TABLES `horarios_clase` WRITE;
/*!40000 ALTER TABLE `horarios_clase` DISABLE KEYS */;
INSERT INTO `horarios_clase` VALUES (1,1,'Martes','2026-01-27','08:00:00','10:00:00','Aula 5b'),(2,1,'MiÃ©rcoles','2026-01-28','07:00:00','11:00:00','Aula 5b'),(3,1,'Lunes','2026-01-26','10:00:00','15:00:00','Aula 5b');
/*!40000 ALTER TABLE `horarios_clase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horarios_docente`
--

DROP TABLE IF EXISTS `horarios_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `horarios_docente` (
  `id_horario` int(11) NOT NULL AUTO_INCREMENT,
  `id_docente` int(11) NOT NULL,
  `dia_semana` varchar(20) NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `aula` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_horario`),
  KEY `docente_id` (`id_docente`),
  CONSTRAINT `horarios_docente_ibfk_1` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id_docente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horarios_docente`
--

LOCK TABLES `horarios_docente` WRITE;
/*!40000 ALTER TABLE `horarios_docente` DISABLE KEYS */;
/*!40000 ALTER TABLE `horarios_docente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inscripciones`
--

DROP TABLE IF EXISTS `inscripciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inscripciones` (
  `id_inscripcion` int(11) NOT NULL AUTO_INCREMENT,
  `id_estudiante` int(11) NOT NULL,
  `id_clase` int(11) NOT NULL,
  `fecha_inscripcion` date NOT NULL,
  `estado` varchar(20) DEFAULT 'activa',
  PRIMARY KEY (`id_inscripcion`),
  KEY `fk_inscripcion_estudiante` (`id_estudiante`),
  KEY `fk_inscripcion_clase` (`id_clase`),
  CONSTRAINT `fk_inscripcion_clase` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_inscripcion_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inscripciones`
--

LOCK TABLES `inscripciones` WRITE;
/*!40000 ALTER TABLE `inscripciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `inscripciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inscripciones_clase`
--

DROP TABLE IF EXISTS `inscripciones_clase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inscripciones_clase` (
  `id_inscripcion` int(11) NOT NULL AUTO_INCREMENT,
  `id_clase` int(11) NOT NULL,
  `id_estudiante` int(11) NOT NULL,
  `fecha_inscripcion` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_inscripcion`),
  KEY `id_clase` (`id_clase`),
  KEY `id_estudiante` (`id_estudiante`),
  CONSTRAINT `inscripciones_clase_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `inscripciones_clase_ibfk_2` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inscripciones_clase`
--

LOCK TABLES `inscripciones_clase` WRITE;
/*!40000 ALTER TABLE `inscripciones_clase` DISABLE KEYS */;
INSERT INTO `inscripciones_clase` VALUES (1,1,8,'2025-11-27 05:11:02'),(2,1,9,'2025-12-02 13:45:52'),(3,2,8,'2025-12-04 08:33:15');
/*!40000 ALTER TABLE `inscripciones_clase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrumentos`
--

DROP TABLE IF EXISTS `instrumentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrumentos` (
  `id_instrumento` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `cupo_maximo` int(11) DEFAULT 0,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_instrumento`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrumentos`
--

LOCK TABLES `instrumentos` WRITE;
/*!40000 ALTER TABLE `instrumentos` DISABLE KEYS */;
INSERT INTO `instrumentos` VALUES (1,'violÃ­n',5,'2025-11-26 23:49:09');
/*!40000 ALTER TABLE `instrumentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas`
--

DROP TABLE IF EXISTS `notas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas` (
  `id_nota` int(11) NOT NULL AUTO_INCREMENT,
  `id_clase` int(11) NOT NULL,
  `id_estudiante` int(11) NOT NULL,
  `nota` decimal(4,2) NOT NULL,
  `observacion` varchar(255) DEFAULT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  `estado` varchar(50) DEFAULT 'pendiente',
  `id_tabla` int(11) NOT NULL,
  PRIMARY KEY (`id_nota`),
  KEY `id_clase` (`id_clase`),
  KEY `id_estudiante` (`id_estudiante`),
  KEY `fk_notas_tabla` (`id_tabla`),
  CONSTRAINT `fk_notas_tabla` FOREIGN KEY (`id_tabla`) REFERENCES `tablas_guardadas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `notas_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `notas_ibfk_2` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas`
--

LOCK TABLES `notas` WRITE;
/*!40000 ALTER TABLE `notas` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_clase`
--

DROP TABLE IF EXISTS `notas_clase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_clase` (
  `id_nota` int(11) NOT NULL AUTO_INCREMENT,
  `id_estudiante` int(11) NOT NULL,
  `id_docente` int(11) DEFAULT NULL,
  `id_tabla` int(11) NOT NULL,
  `id_clase` int(11) NOT NULL,
  `competencia` varchar(100) NOT NULL,
  `instrumento` varchar(100) DEFAULT NULL,
  `etapa` varchar(50) DEFAULT NULL,
  `nota` decimal(4,2) NOT NULL,
  `observacion` text DEFAULT NULL,
  `registrada_por` varchar(100) DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'registrada',
  `fecha` datetime NOT NULL,
  `fecha_registro` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_nota`),
  KEY `id_estudiante` (`id_estudiante`),
  KEY `idx_notas_docente` (`id_docente`),
  KEY `idx_notas_clase` (`id_clase`),
  KEY `fk_nota_tabla` (`id_tabla`),
  CONSTRAINT `fk_nota_clase` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_nota_docente` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id_docente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_nota_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_nota_tabla` FOREIGN KEY (`id_tabla`) REFERENCES `tablas_guardadas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_tabla_guardada` FOREIGN KEY (`id_tabla`) REFERENCES `tablas_guardadas` (`id`),
  CONSTRAINT `notas_clase_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `notas_clase_ibfk_2` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_clase`
--

LOCK TABLES `notas_clase` WRITE;
/*!40000 ALTER TABLE `notas_clase` DISABLE KEYS */;
INSERT INTO `notas_clase` VALUES (22,8,1,2,1,'Principal','','',4.50,'excelente','Luis Profe','registrada','2025-12-03 00:00:00','2025-12-03 06:46:57');
/*!40000 ALTER TABLE `notas_clase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_musicales`
--

DROP TABLE IF EXISTS `notas_musicales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_musicales` (
  `id_nota` int(11) NOT NULL AUTO_INCREMENT,
  `id_estudiante` int(11) NOT NULL,
  `id_clase` int(11) NOT NULL,
  `calificacion` decimal(4,2) NOT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  `observaciones` text DEFAULT NULL,
  `estudiante` varchar(150) DEFAULT NULL,
  `instrumento` varchar(100) DEFAULT NULL,
  `etapa` varchar(100) DEFAULT NULL,
  `nota` varchar(50) DEFAULT NULL,
  `docente` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id_nota`),
  KEY `id_estudiante` (`id_estudiante`),
  KEY `id_clase` (`id_clase`),
  CONSTRAINT `notas_musicales_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `notas_musicales_ibfk_2` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_musicales`
--

LOCK TABLES `notas_musicales` WRITE;
/*!40000 ALTER TABLE `notas_musicales` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas_musicales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `observaciones`
--

DROP TABLE IF EXISTS `observaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observaciones` (
  `id_observacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_estudiante` int(11) NOT NULL,
  `instrumento` varchar(100) NOT NULL,
  `etapa_pedagogica` varchar(100) NOT NULL,
  `comentario` text NOT NULL,
  `docente` varchar(100) NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `enviada` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id_observacion`),
  KEY `id_estudiante` (`id_estudiante`),
  CONSTRAINT `observaciones_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observaciones`
--

LOCK TABLES `observaciones` WRITE;
/*!40000 ALTER TABLE `observaciones` DISABLE KEYS */;
INSERT INTO `observaciones` VALUES (1,8,'Piano','Etapa 1','Clase Inicio de Piano, Evaluada primera etapa','Luis Profe','2025-11-30 18:27:49',1),(2,9,'Piano','Etapa 1','Falta Armonia, se le recomienda practicar lo visto en clase ','Luis Profe','2025-12-02 20:38:39',1);
/*!40000 ALTER TABLE `observaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programas`
--

DROP TABLE IF EXISTS `programas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programas` (
  `id_programa` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'activo',
  PRIMARY KEY (`id_programa`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programas`
--

LOCK TABLES `programas` WRITE;
/*!40000 ALTER TABLE `programas` DISABLE KEYS */;
/*!40000 ALTER TABLE `programas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tablas_certificacion`
--

DROP TABLE IF EXISTS `tablas_certificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tablas_certificacion` (
  `id_certificacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_clase` int(11) NOT NULL,
  `id_estudiante` int(11) NOT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'pendiente',
  `fecha_emision` date DEFAULT NULL,
  `fecha_validacion` date DEFAULT NULL,
  PRIMARY KEY (`id_certificacion`),
  KEY `id_clase` (`id_clase`),
  KEY `id_estudiante` (`id_estudiante`),
  CONSTRAINT `tablas_certificacion_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tablas_certificacion_ibfk_2` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiantes` (`id_estudiante`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tablas_certificacion`
--

LOCK TABLES `tablas_certificacion` WRITE;
/*!40000 ALTER TABLE `tablas_certificacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `tablas_certificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tablas_guardadas`
--

DROP TABLE IF EXISTS `tablas_guardadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tablas_guardadas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_clase` int(11) DEFAULT 0,
  `id_docente` int(11) DEFAULT NULL,
  `nombre` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `aula` varchar(100) DEFAULT NULL,
  `dia` varchar(20) DEFAULT NULL,
  `usuario_editor` varchar(100) DEFAULT NULL,
  `registrada_por` varchar(100) DEFAULT NULL,
  `fecha_envio` datetime DEFAULT current_timestamp(),
  `fecha_validacion` datetime DEFAULT NULL,
  `enviada_por` varchar(150) DEFAULT NULL,
  `validada` enum('Sí','No') NOT NULL DEFAULT 'No',
  `fecha` date DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enviada` tinyint(1) DEFAULT 0,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `estado` varchar(50) DEFAULT 'Pendiente',
  `instrumento` varchar(100) DEFAULT NULL,
  `etapa` varchar(100) DEFAULT NULL,
  `hora_inicio` time DEFAULT NULL,
  `hora_fin` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tabla_clase` (`id_clase`),
  KEY `fk_tabla_docente` (`id_docente`),
  CONSTRAINT `fk_guardadas_clase` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`),
  CONSTRAINT `fk_guardadas_docente` FOREIGN KEY (`id_docente`) REFERENCES `docentes` (`id_docente`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tablas_guardadas`
--

LOCK TABLES `tablas_guardadas` WRITE;
/*!40000 ALTER TABLE `tablas_guardadas` DISABLE KEYS */;
INSERT INTO `tablas_guardadas` VALUES (2,1,1,'Tabla de la Clase de Piano','Prueba de funcionamiento con un estudiante',NULL,NULL,'Luis Profe','Luis Profe','2025-11-30 00:00:00','2025-11-30 03:01:28','1','Sí','2025-11-29','2025-11-30 08:01:28',1,'2025-11-29 15:47:33','Pendiente',NULL,NULL,NULL,NULL),(18,1,1,'Tabla de la Clase de Piano','111111',NULL,NULL,'Luis Profe','Luis Profe','2025-12-03 00:00:00','2025-12-03 19:04:21','1','Sí','2025-12-03','2025-12-04 00:04:21',1,'2025-12-03 06:47:12','Pendiente','Piano','Etapa 1',NULL,NULL);
/*!40000 ALTER TABLE `tablas_guardadas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tablas_validadas`
--

DROP TABLE IF EXISTS `tablas_validadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tablas_validadas` (
  `id_validacion` int(11) NOT NULL AUTO_INCREMENT,
  `modulo` varchar(100) NOT NULL,
  `estado` varchar(50) DEFAULT 'pendiente',
  `usuario` varchar(150) DEFAULT NULL,
  `fecha_validacion` datetime DEFAULT current_timestamp(),
  `id_clase` int(11) DEFAULT NULL,
  `clase` varchar(150) DEFAULT NULL,
  `instrumento` varchar(100) DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  PRIMARY KEY (`id_validacion`),
  KEY `id_clase` (`id_clase`),
  CONSTRAINT `tablas_validadas_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tablas_validadas`
--

LOCK TABLES `tablas_validadas` WRITE;
/*!40000 ALTER TABLE `tablas_validadas` DISABLE KEYS */;
/*!40000 ALTER TABLE `tablas_validadas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `correo` varchar(150) NOT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `clave` varchar(255) NOT NULL,
  `rol` varchar(50) NOT NULL,
  `estado` varchar(50) DEFAULT 'activo',
  `fecha_ingreso` date DEFAULT NULL,
  `usuario_creador` varchar(100) DEFAULT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Manuelita','ApellidoReal','estudiante1@correo',NULL,NULL,'4eca955fc7b0b32200f49f311a6678e91d8de397bd50f072abb8f49468f8efa9','estudiante','activo',NULL,'sistema','2025-11-26 22:39:59'),(3,'Carla',NULL,'admin@correo',NULL,NULL,'4eca955fc7b0b32200f49f311a6678e91d8de397bd50f072abb8f49468f8efa9','administrador','activo',NULL,'sistema','2025-11-26 22:43:00'),(6,'Luis Profe',NULL,'docente@correo',NULL,NULL,'4eca955fc7b0b32200f49f311a6678e91d8de397bd50f072abb8f49468f8efa9','docente','activo',NULL,NULL,'2025-11-26 23:29:29'),(10,'Alexa','ApellidoReal','estudiante@correo','#55 florida','0000000123','4eca955fc7b0b32200f49f311a6678e91d8de397bd50f072abb8f49468f8efa9','estudiante','activo',NULL,'sistema','2025-11-30 13:45:29'),(11,'Shrek Profecito',NULL,'shrek@correo',NULL,NULL,'4eca955fc7b0b32200f49f311a6678e91d8de397bd50f072abb8f49468f8efa9','docente','activo',NULL,NULL,'2025-12-02 21:23:24');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ZERO_IN_DATE,NO_ZERO_DATE,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER crear_estudiante_despues_usuario
AFTER INSERT ON usuarios
FOR EACH ROW
BEGIN
    IF NEW.rol = 'estudiante' THEN
        INSERT INTO estudiantes (
            id_usuario,
            nombre,
            correo,
            direccion,
            telefono,
            etapa_pedagogica,
            fecha_ingreso,
            estado,
            nivel_tecnico,
            progreso,
            observaciones
        ) VALUES (
            NEW.id_usuario,
            NEW.nombre,
            NEW.correo,
            NEW.direccion,
            NEW.telefono,
            'Etapa 1',          -- valor por defecto
            NEW.fecha_registro, -- sincroniza con fecha de creación del usuario
            NEW.estado,
            'Básico',           -- valor inicial
            'Iniciado',         -- valor inicial
            ''                  -- observaciones vacío
        );
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `validaciones`
--

DROP TABLE IF EXISTS `validaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `validaciones` (
  `id_validacion` int(11) NOT NULL AUTO_INCREMENT,
  `modulo` varchar(100) DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL,
  `usuario` varchar(100) DEFAULT NULL,
  `fecha_validacion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_validacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `validaciones`
--

LOCK TABLES `validaciones` WRITE;
/*!40000 ALTER TABLE `validaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `validaciones` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-06  2:13:00
