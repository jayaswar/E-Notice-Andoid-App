<?php

include 'config.php';

if (isset($_GET['group_id']) && isset($_GET['student_id'])) {


$group_id = $_GET['group_id'];
$student_id = $_GET['student_id'];


	//registration query
	 $insert_query="UPDATE student SET subscribed_group_id='$group_id' WHERE `id`='$student_id' ";

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