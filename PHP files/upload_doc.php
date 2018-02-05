<?php
// array for JSON response
    $response = array();
    // Get image string posted from Android App
    $base=$_REQUEST['image'];
    // Get file name posted from Android App
     $filename = $_REQUEST['srno']; 
   $lno=$_POST['lice_no'];
$dname=$_POST['docname'];
    // Decode Image
    $binary=base64_decode($base);
    header('Content-Type: bitmap; charset=utf-8');
   
     $file = fopen('pics/'.$filename.'.jpg', 'wb');
    // Create File
    fwrite($file, $binary);
    fclose($file);

 mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");
 $url="http://gopajibaba.com/traffic/pics/".$filename.".jpg";
 
  mysql_query("INSERT INTO `user_document`(`license_number`, `document_name`, `url`) VALUES ('$lno','$dname','$url')");  

       $response["success"] = 1;
$response["message"] = "Successfully loged In";
             echo json_encode($response); 
mysql_close();
  
?>