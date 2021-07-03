-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 20, 2021 at 11:35 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `enotice`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`) VALUES
(1, 'admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `group_id` varchar(255) NOT NULL,
  `notice_id` varchar(255) NOT NULL,
  `text` varchar(255) NOT NULL,
  `student_id` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`id`, `group_id`, `notice_id`, `text`, `student_id`, `created_at`) VALUES
(1, '1', '5', 'ghh', '1', '2021-03-08 10:33:16'),
(2, '1', '5', 'hii', '1', '2021-03-08 10:40:07'),
(3, '1', '3', 'hello', '1', '2021-03-08 10:40:13');

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

CREATE TABLE `group` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `admin` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `admin_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`id`, `name`, `admin`, `username`, `password`, `created_at`, `admin_id`) VALUES
(1, 'abc', 'Akshay', '', '', '2021-03-06 06:46:26', ''),
(2, 'anc', 'nsjzkx', '', '', '2021-03-06 06:48:39', ''),
(3, 'Group 1 ', 'nsjzkx', '', '', '2021-03-06 06:49:12', ''),
(4, 'Nm', 'vb', '', '', '2021-03-06 09:58:45', ''),
(5, 'Group 34', 'vb', '', '', '2021-03-08 12:10:03', ''),
(6, 'Testing', 'Akshay', '', '', '2021-03-11 18:13:40', '8'),
(7, 'Testing 2', 'Akshay', '', '', '2021-03-11 18:15:30', '8'),
(8, 'New Group', 'Akshay', '', '', '2021-04-19 10:39:18', '8');

-- --------------------------------------------------------

--
-- Table structure for table `notice`
--

CREATE TABLE `notice` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `desc` varchar(255) NOT NULL,
  `image_name` varchar(255) NOT NULL,
  `group_id` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `type` enum('News','Notice') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `notice`
--

INSERT INTO `notice` (`id`, `title`, `desc`, `image_name`, `group_id`, `created_at`, `type`) VALUES
(1, 'vg', 'ggh', 'Title_57.jpg.png', '1', '2021-03-08 10:03:48', 'News'),
(2, 'ghhgg', 'cgg', 'Title_60.jpg', '1', '2021-03-08 10:09:15', 'News'),
(3, 'sggsgs', 'aklalx. c x d', 'IMG_20210308_154204.jpg', '1', '2021-03-08 10:12:06', 'News'),
(4, 'gwhay', 'zvbzbz', 'IMG_20210308_154443.jpg', '1', '2021-03-08 10:15:00', 'News'),
(5, 'gwhay', 'zvbzbz', 'IMG_20210308_154513.jpg', '1', '2021-03-08 10:15:15', 'News'),
(6, 'New Notice', 'New new', 'IMG_20210308_161054.jpg', '2', '2021-03-08 10:40:56', 'News'),
(7, 'gzbsnsmsajkai', 'anmzjxnx d s s', 'IMG_20210312_013823.jpg', '1', '2021-03-11 20:08:24', 'Notice'),
(8, 'New Notice', 'fgghh', 'IMG_20210419_191537.jpg', '1', '2021-04-19 13:45:36', 'Notice'),
(9, 'fddv fxg n j ', 'sthcfsfjjiijh', 'IMG_20210419_191943.jpg', '1', '2021-04-19 13:49:42', 'Notice');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `branch` varchar(255) NOT NULL,
  `mobile_no` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `subscribed_group_id` varchar(255) NOT NULL,
  `bio` varchar(255) NOT NULL,
  `firebase_token` varchar(255) NOT NULL,
  `profile_pic` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`id`, `name`, `email`, `branch`, `mobile_no`, `username`, `password`, `subscribed_group_id`, `bio`, `firebase_token`, `profile_pic`) VALUES
(1, 'Ashwini', 'ashwini.saraf4@gmail.com', 'Entc', '9765718372', '7@gs.w', 'hhaiaa', ',0,2', 'Android Developer.. Update new', 'dmRX1DKVR3KDgJK2wsIqOn:APA91bGm499oJ3mlcqZEHnoAWYoLY4iZvEc9k2suaf4YH0yUF9medDcek-rUlK5kn28zAOZWzpKpCucjPWovim-SOAimW_SUrJD2kmUQJH2-P5NVi2_11ac6xGk7UMQcFN24-QslH9-Q', ''),
(2, '0', 'ashwini.saraf4@gmail.com', '0', '9765718372', '3ai4@l', 'g@rhwh', '', '', '', ''),
(3, '0', 'bbh', '0', '97657183', '11167b', 'gbvvbh', '', '', '', ''),
(4, '0', 'sbsh', '0', '', '397s68', 'hhvvss', '', '', '', ''),
(5, '0', 'bsbsb', '0', '', '8sb767', 'bhbvhh', '', '', '', ''),
(6, '0', 'ashwini.saraf4@gmail.com', '0', '9765718371', '@1.5@f', 'rb4@ra', '', '', '', ''),
(7, 'vb', 'vh', 'bhh', '1111111111', '11111v', 'hbhvhb', '', '', '', ''),
(8, 'Akshay', 'ashwini.saraf4@gmail.com', 'IT', '9765718371', 's6c7s1', '4ahhnI', ',0,1', 'hiii', 'dmRX1DKVR3KDgJK2wsIqOn:APA91bGm499oJ3mlcqZEHnoAWYoLY4iZvEc9k2suaf4YH0yUF9medDcek-rUlK5kn28zAOZWzpKpCucjPWovim-SOAimW_SUrJD2kmUQJH2-P5NVi2_11ac6xGk7UMQcFN24-QslH9-Q', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notice`
--
ALTER TABLE `notice`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `group`
--
ALTER TABLE `group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `notice`
--
ALTER TABLE `notice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
