<?php 
	$lno=$_POST['lice_no'];
        $name=$_POST['u_name'];
        $age=$_POST['u_age'];
        $sex=$_POST['u_sex'];
        $add=$_POST['u_add'];
        $email=$_POST['u_email'];
        $mobile=$_POST['u_mob'];
        $pwd=$_POST['u_pwd'];
        
	

	mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");


$result_exited = mysql_query("SELECT * from user WHERE mobile = '$mobile'");
        $no_of_rows = mysql_num_rows($result_exited );
        if ($no_of_rows > 0) {
           
        $response["success"] = 2;
        $response["message"] = "Customer already registred";        
        // echoing JSON response
        echo json_encode($response);
   


 } else {
 // mysql inserting a new row
    $result = mysql_query ("INSERT INTO `user`(`license_number`, `name`, `age`, `sex`, `address`, `email`, `mobile`, `password`) VALUES ('$lno','$name','$age','$sex','$add','$email','$mobile','$pwd')");

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


	