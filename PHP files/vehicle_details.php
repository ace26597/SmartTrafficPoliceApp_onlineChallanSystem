<?php 
	
        $vhno=$_POST['vno'];
        $vhtype=$_POST['vtype'];
        $clr=$_POST['color'];
        $lno=$_POST['lice_no'];
        $ino=$_POST['insurance_no'];
        
        
mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");

$result_exited = mysql_query("SELECT * from vehicle_details WHERE vno = '$vhno'");
        $no_of_rows = mysql_num_rows($result_exited );
        if ($no_of_rows > 0) {
           
        $response["success"] = 2;
        $response["message"] = "Vehicle already registred";        
        // echoing JSON response
        echo json_encode($response);
   


 } else {
 // mysql inserting a new row
    $result = mysql_query ("INSERT INTO `vehicle_details`(`vno`, `vtype`, `color`, `license_number`, `insurance_no`) VALUES ('$vhno','$vhtype','$clr','$lno','$ino')");
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
 

            
        }




	

	
mysql_close();
 ?>