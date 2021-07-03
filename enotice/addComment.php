<?php

include 'config.php';

if (isset($_GET['group_id']) && isset($_GET['notice_id']) && isset($_GET['text']) && isset($_GET['student_id'])) {

$group_id=$_GET['group_id'];
$notice_id = $_GET['notice_id'];
$text = $_GET['text'];
$student_id = $_GET['student_id'];


	//registration query
	$insert_query="INSERT into comments(group_id,notice_id,`text`,`student_id`) values ('$group_id','$notice_id','$text','$student_id') ";

	$insert_result=mysqli_query($connection,$insert_query);

 					if(!$insert_result)
                  						{
                         					$response['code'] = "400";    
                         					echo json_encode($response);

                  						}
                  						else{

                       						$response['code'] = "200";   
                       						echo json_encode($response);
                       					}
	

}

mysqli_close($connection);

?>