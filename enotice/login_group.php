<?php  
include 'config.php';
$response = array();

if (isset($_GET['username']) && isset($_GET['password'])) {

$username = $_GET['username'];
$mypassword = $_GET['password'];


  $query  = "SELECT * FROM student WHERE username ='$username' AND  password ='$mypassword'";
$record = mysqli_query($connection,$query);

if(mysqli_num_rows($record)>0){
    $response["user"] = array();
    while($row = mysqli_fetch_assoc($record))
        {      
            $user = array();
            $user["id"] = $row['id'];        
            $user["name"] = $row['name'];
            $user["branch"] = $row['branch'];
            $user["email"] = $row['email'];
            $user["bio"] = $row['bio'];
            $user["username"] = $row['username'];
            $user["password"] = $row['password'];
            $user["mobile_no"] = $row['mobile_no'];
            $user["subscribed_group_id"] = $row['subscribed_group_id'];
            $user["profile_pic"] = $row['profile_pic'];
            array_push($response["user"], $user); 
			
		}

	$insert_query="SELECT * FROM  `group`  WHERE username ='$username' AND password='$mypassword'";
	$result = mysqli_query($connection,$insert_query);
   
	$row = mysqli_fetch_assoc($result);
	
	$response["group_id"] = $row['id'];

	if(mysqli_num_rows($result) > 0) {
  
  						//mobno already available
                         $response['code'] = "200";      
                         echo json_encode($response);
	}else{
		$response["code"] = "400";
		echo json_encode($response);
		
	}
	

}else
{

$response["code"] = "400";
echo json_encode($response);
}
}else{
  $response["code"] = "400";
echo json_encode($response);
}

mysqli_close($connection);

?>