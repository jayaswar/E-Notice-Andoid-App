<?php  
include 'config.php';
$response = array();

if (isset($_GET['id'])) {

	$id = $_GET['id'];


	$query  = "SELECT * FROM `group` WHERE admin_id ='$id'";
	$record = mysqli_query($connection,$query);

	if(mysqli_num_rows($record)>0){
	  $response["code"] = "400";
	  echo json_encode($response);
	}else {
		$response["code"] = "200";
		echo json_encode($response);
	}
}else{
  $response["code"] = "400";
	echo json_encode($response);
}

mysqli_close($connection);

?>