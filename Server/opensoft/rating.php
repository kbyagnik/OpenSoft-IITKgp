<?php
include('config.php');
$command = $_REQUEST['command'];
$rating=$_REQUEST['rating'];
$link = $_REQUEST['query'];
file_put_contents('file1.txt',$command."    ".$rating."    ".$link);
$rating = floatval($rating);
if ($command==="rating")
{
	$connect = mysqli_connect($servername,$username,$password,$dbname);
	$append = "http://".$server_ip."/opensoft/data/";
	$query = "SELECT users_rated FROM content WHERE concat('$append',link)='$link';";
	$users_rated = mysqli_fetch_array(mysqli_query($connect,$query));
	$users_rated = $users_rated['users_rated'];
	$query = "UPDATE content SET rating=((rating*(users_rated))+$rating)/(users_rated+1) WHERE concat('$append',link)='$link';";
	mysqli_query($connect,$query);
	$query = "UPDATE content SET users_rated=(users_rated+1) WHERE concat('$append',link)='$link';";
	mysqli_query($connect,$query);
}
?>