<?php

include 'config.php';

if (isset($_GET['name']) && isset($_GET['admin'])){

$name=$_GET['name'];
$admin = $_GET['admin'];
$admin_id = $_GET['admin_id'];
$username = $_GET['username'];
$password = $_GET['password'];

 //check if user is already exist
 $insert_query="SELECT * FROM  `group`  WHERE name ='$name' OR admin='$admin'";
   $result = mysqli_query($connection,$insert_query);
   


  if(mysqli_num_rows($result) > 0) {
  
  						//mobno already available
                         $response['code'] = "401";      
                         echo json_encode($response);
	}
	else{
	//registration query
	$insert_query="INSERT into `group`(name,admin,admin_id, username, password) values ('$name','$admin','$admin_id', '$username', '$password') ";

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

}

mysqli_close($connection);

?>