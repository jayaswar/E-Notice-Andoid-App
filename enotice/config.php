<?php

$database="enotice";
$username="root";
$password="";

/*$con=mysqli_connect("localhost","$username","$password") or die("Could not connect My Sql"); 
mysqli_select_db("$database",$con) or die("Could not connect Database");*/


$connection = mysqli_connect("localhost",$username,$password);

if (!$connection) {
    error_log("Failed to connect to MySQL: " . mysqli_error($connection));
    die('Internal server error');
}

// 2. Select a database to use 
$db_select = mysqli_select_db($connection, $database);
if (!$db_select) {
    error_log("Database selection failed: " . mysqli_error($connection));
    die('Internal server error');
}

?>