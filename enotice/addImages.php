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
				if(isset($_FILES['pic']['name'])){
					
					//uploading file and storing it to database as well 
					try{
						move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH . $_FILES['pic']['name']);

$group_id=$_POST['group_id'];
$image_name=$_POST['image_name'];
$desc=$_POST['desc'];
$title=$_POST['title'];
$type=$_POST['type'];

$query="INSERT into notice(`title`,`desc`,`image_name`,`group_id`,`type`) values ('$title','$desc','$image_name','$group_id','$type')";

$insert_result=mysqli_query($connection,$query);

if ($insert_result) {

	$response['code'] = "200"; 
                                        $response['msg'] = "Image Uploaded successfully";  
                                        echo json_encode($response);
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
					
						}else{
					$response['code'] = "400";
					$response['msg'] = "Required params not available";
					 echo json_encode($response);
				}
			
			break;
			
			default: 
				$response['error'] = true;
				$response['message'] = 'Invalid api call';
		}
		
	}
	
	?>