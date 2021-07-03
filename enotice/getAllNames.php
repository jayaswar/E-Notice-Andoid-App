<?php  
include 'config.php';
$response = array();

$query  = "SELECT * FROM `group`,`student`";
$record = mysqli_query($connection,$query);

if(mysqli_num_rows($record)>0){
    $response["group_and_user"] = array();
    while($row = mysqli_fetch_assoc($record))
        {      
            $group_and_user = array();
            $group_and_user["group_id"] = $row['id'];        
            $group_and_user["group_name"] = $row['name'];
            $group_and_user["group_admin"] = $row['admin'];
            $group_and_user["group_username"] = $row['username'];
            $group_and_user["group_password"] = $row['password'];
            $group_and_user["group_created_at"] = $row['created_at'];
            $group_and_user["student_id"] = $row['id'];        
            $group_and_user["student_name"] = $row['name'];
            $group_and_user["student_branch"] = $row['branch'];
            $group_and_user["student_email"] = $row['email'];
            $group_and_user["student_bio"] = $row['bio'];
            $group_and_user["student_username"] = $row['username'];
            $group_and_user["student_password"] = $row['password'];
            $group_and_user["student_mobile_no"] = $row['mobile_no'];
            $group_and_user["student_subscribed_group_id"] = $row['subscribed_group_id'];
            array_push($response["group_and_user"], $group_and_user); 

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