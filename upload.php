<?php
//create uploads and put permissions
$target_dir = "./";
$target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
$uploadOk = 1;
$FileType = pathinfo($target_file,PATHINFO_EXTENSION);
$key = "123";
$secretKey = $_POST["secretKey"];

//Check password
if($secretKey != $key){
	echo "You don't have permission to use this";
	$uploadOk = 0;
}

// Check file size bigger than 100 kb
else if ($_FILES["fileToUpload"]["size"] > 100000) {
 echo "Sorry, your file is too large.";
 $uploadOk = 0;
}

// Check if $uploadOk is set to 0 by an error
if ($uploadOk == 0) {
 echo "\nYour file was not uploaded.";
// if everything is ok, try to upload file
} else {
 if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
 echo "The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.";
 } else {
 echo "Sorry, there was an error uploading your file.";
 }
}
?>