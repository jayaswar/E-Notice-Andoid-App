<?php

include 'config.php';

if (isset($_GET['name']) && isset($_GET['email']) && isset($_GET['mobile_no']) && isset($_GET['branch'])){

$name=$_GET['name'];
$email = $_GET['email'];
$mobile_no = $_GET['mobile_no'];
$branch = $_GET['branch'];

$characters = $email.$mobile_no; 

$characters_ = $email.$branch; 

    $username = ''; 
    $password='';  
    for ($i = 0; $i < 6; $i++) { 
        $index = rand(0, strlen($characters) - 1);
        $index_ = rand(0, strlen($characters_) - 1); 
        $username .= $characters[$index]; 
        $password .= $characters_[$index_]; 
    } 

 //check if user is already exist
      $insert_query="SELECT * FROM  student  WHERE mobile_no ='$mobile_no' ";
  $result = mysqli_query($connection,$insert_query);

  if( mysqli_num_rows($result) > 0) {
  
  						//mobno already available
                         $response['code'] = "401";      
                         echo json_encode($response);
	}
	else{
	//registration query
	$insert_query="INSERT into student(name,mobile_no,email,branch,password,username) values 
						('$name','$mobile_no','$email','$branch','$password','$username') ";

	$insert_result=mysqli_query($connection,$insert_query);

 					if(!$insert_result)
                  						{
                         					$response['code'] = "400";    
                         					echo json_encode($response);

                  						}
                  						else{

                       						$response['code'] = "200"; 
                                  $response['password'] = $password; 
                                  $response['username'] = $username;   
                       						echo json_encode($response);
                       					}
	}

}

mysqli_close($connection);

?>