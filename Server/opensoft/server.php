<?php
include('config.php');
$command = $_REQUEST['command'];
$search = $_REQUEST['query'];
$connect = mysqli_connect($servername,$username,$password,$dbname);
if(strtolower($command) === 'search')
{
	$jsonData = '{ "user":"John", "age":22, "country":"United States" }';
	$phpArray = json_decode($jsonData,true);
	$a=$phpArray['country'];
	$a = 'honey';
	//$query = "SELECT *,MATCH(title) AGAINST('$search') AS score1,MATCH(description) AGAINST('$search') AS score2 FROM content		 WHERE MATCH(title, description)  AGAINST('$search') ORDER BY (score1*3 + score2) DESC;";
	$query = "SELECT * FROM content WHERE lower(title) LIKE lower('$search%') OR lower(description) LIKE lower('$search%') ORDER BY contentID desc;";
	$result = mysqli_query($connect,$query);
	$var = array();
	$i = 0;
	while($res = mysqli_fetch_array($result))
	{
		$var[$i] = array('title' => $res['title'],'description' => $res['description'],'link' => "http://".$server_ip."/opensoft/data/".$res['link'],
		'category' => $res['category'], 'size' => $res['size'],'rating' => $res['rating'],'downloads' => $res['downloads']);
		$i = $i + 1;
	}
	$json = json_encode($var);
	echo $json;
}
?>
