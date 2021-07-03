<?php 
    
    //Constants for database connection

include('config.php');

    //We will upload files to this folder
    //So one thing don't forget, also create a folder named uploads inside your project folder i.e. MyApi folder
    define('UPLOAD_PATH', 'files/');
    

    //An array to display the response
    $response = array();

    //if the call is an api call 
    if(isset($_GET['apicall'])){
        
        //switching the api call 
        switch($_GET['apicall']){
            
            //if it is an upload call we will upload the image
            case 'uploadpic':
                
                //first confirming that we have the image and tags in the request parameter
                    
                    //uploading file and storing it to database as well 
                    try{

if (isset($_FILES['pic']['name'])) {

    move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH . $_FILES['pic']['name']);

}

                        
$name = $_POST['name'];
$email = $_POST['email'];
$branch = $_POST['branch'];
$mobile_no = $_POST['mobile_no'];
$bio = $_POST['bio'];
$user_id = $_POST['id'];
$profile_img = $_POST['profile_img'];

$query  = "UPDATE student set name='$name',email='$email',branch='$branch',mobile_no='$mobile_no',bio='$bio',profile_pic='$profile_img' WHERE id ='$user_id'";

$insert_result=mysqli_query($connection,$query);

if ($insert_result) {

    $query  = "SELECT * FROM student WHERE id='$user_id' ";
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

  $response["code"] = "200";
  echo json_encode($response);
}else
{

$response["code"] = "400";
echo json_encode($response);
}
    # code...
}else
{

    $response['code'] = "400"; 
                                        $response['msg'] = "Image Uploading Failed";  
                                        echo json_encode($response);

}



                                        

                                       

                                }catch(Exception $e){
                                                $response['code'] = "400";
                                                $response['msg'] = 'Could not upload file';
                                                 echo json_encode($response);
                                    }
                
            
            break;
            
            default: 
                $response['error'] = true;
                $response['message'] = 'Invalid api call';
        }
        
    }
    
    ?>