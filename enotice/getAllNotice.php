<?php  
include 'config.php';
$response = array();

 $id=$_GET['id'];
   $query  = "SELECT `group`.name as group_name,notice.* FROM notice JOIN `group` ON `group`.id = notice.group_id WHERE `notice`.type='Notice' AND FIND_IN_SET(notice.group_id ,'$id')>0 ";
$record = mysqli_query($connection,$query);

if(mysqli_num_rows($record)>0){
    $response["notice"] = array();
    while($row = mysqli_fetch_assoc($record))
        {      
            $user = array();
            $user["id"] = $row['id'];
            $user["group_name"] = $row['group_name'];        
            $user["title"] = $row['title'];
            $user["desc"] = $row['desc'];
            $user["image_name"] = $row['image_name'];
            $user["group_id"] = $row['group_id'];
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