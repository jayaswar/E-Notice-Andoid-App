<?php  
include 'config.php';
$response = array();
 $secret_key="";

if (isset($_GET['token']) && isset($_GET['user_id'])) {


$token = $_GET['token'];
$user_id = $_GET['user_id'];


     $query  = "UPDATE student set firebase_token='$token' WHERE id ='$user_id'";
    $record = mysqli_query($connection,$query);


if($record){
  

  $response["code"] = "200";
  $response["msg"] = "Firebase Token Updated Successfully";
  echo json_encode($response);
 
}else
{

$response["code"] = "400";
 $response["msg"] = "Firebase Token Updation Failed";
echo json_encode($response);
}

}else{


$response["code"] = "400";
 $response["msg"] = "No DATA Found";
echo json_encode($response);
}

              

mysqli_close($connection);

?>