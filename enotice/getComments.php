<?php  
include 'config.php';
$response = array();

$id=$_GET['id'];


$query  = "SELECT * FROM comments where notice_id = '$id' ";
$record = mysqli_query($connection,$query);

if(mysqli_num_rows($record)>0){
    $response["notice"] = array();
    while($row = mysqli_fetch_assoc($record))
        {      
            $user = array();
            $user["id"] = $row['id'];        
            $user["group_id"] = $row['group_id'];
            $user["notice_id"] = $row['notice_id'];
            $user["text"] = $row['text'];
            $user["student_id"] = $row['student_id'];
            $user["created_at"] = $row['created_at'];
            array_push($response["notice"], $user); 

    }

  $response["code"] = "200";
  echo json_encode($response);
}else
{

$response["code"] = "400";
echo json_encode($response);
}


mysqli_close($connection);

?>