<?php 
$lno=$_POST['lice_no'];
	$famt=$_POST['fine_amt'];
        $vhno=$_POST['vno'];
        $lat=$_POST['lat'];
        $lng=$_POST['lng'];
        $pid=$_POST['police_id'];
     
        mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");



 // mysql inserting a new row
    $result = mysql_query ("INSERT INTO `main_collection`(`license_number`, `fine_amt`, `vno`, `lat`, `longi`, `police_id`) VALUES ('$lno','$famt','$vhno','$lat','$lng','$pid')");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "successfully Registered.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        // echoing JSON response
        echo json_encode($response);
    }
 

            
        





mysql_close();
?>


	