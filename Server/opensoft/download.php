<?php
include('config.php');
$link = $_REQUEST['query'];
$uuid = $_REQUEST['uuid'];
file_put_contents('file.txt',$link."    ".$uuid);
$connect = mysqli_connect($servername,$username,$password,$dbname);
$append = "http://".$server_ip."/opensoft/data/";
$query = "UPDATE content SET downloads=downloads+1 WHERE concat('$append',link)='$link';";
mysqli_query($connect,$query);
$query = "SELECT contentID FROM content WHERE concat('$append',link)='$link';";
$result = mysqli_fetch_array(mysqli_query($connect,$query));
$contentID = $result['contentID'];
$uniqueID = "123";
$timestamp = time();
$query = "INSERT INTO downloaded VALUES($contentID,$uuid,$timestamp);";
mysqli_query($connect,$query);
?>