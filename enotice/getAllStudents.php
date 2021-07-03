<?php  
include 'config.php';
$response = array();


$query  = "SELECT * FROM student";
$record = mysqli_query($connection,$query);

if(mysqli_num_rows($record)>0){
    $response["student"] = array();
    while($row = mysqli_fetch_assoc($record))
        {      
               
            $student = array();
            $student["id"] = $row['id'];        
            $student["name"] = $row['name'];
            $student["branch"] = $row['branch'];
            $student["username"] = $row['username'];
            $student["password"] = $row['password'];
            $student["mobile_no"] = $row['mobile_no'];
            $student["subscribed_group_id"] = $row['subscribed_group_id'];
            array_push($response["student"], $student); 

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