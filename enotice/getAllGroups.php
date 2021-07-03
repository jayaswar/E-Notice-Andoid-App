<?php  
include 'config.php';
$response = array();


$query  = "SELECT * FROM `group`";
$record = mysqli_query($connection,$query);

if(mysqli_num_rows($record)>0){
    $response["group"] = array();
    while($row = mysqli_fetch_assoc($record))
        {      
            $group = array();
            $group["id"] = $row['id'];        
            $group["name"] = $row['name'];
            $group["admin"] = $row['admin'];
            $group["username"] = $row['username'];
            $group["password"] = $row['password'];
            $group["created_at"] = $row['created_at'];
            array_push($response["group"], $group); 

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