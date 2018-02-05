<?php
 

// array for JSON response
$response = array();


// check for required fields

    
   
    $password = $_POST['password'];    
    $mobile = $_POST['mobileno'];
  
    
    


        
//hostname = www.gopajibaba.com
//username = gopaj_shop
//password = 123456
//databasename = gopajiba_shoping

       mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");

         
        $result = mysql_query("SELECT * FROM user WHERE mobile = '$mobile'");
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $response  = mysql_fetch_array($result);
                if($password == $response['password'])
          
          
        {
             $response["success"] = 1;
             $response["message"] = "Successfully loged In";
             echo json_encode($response);
        }else
          {
             $response["success"] = 0;
             $response["message"] = "Error try again";
             echo json_encode($response);

            }
        
          
        } else {
             $response["success"] = 0;
             $response["message"] = "Wrong email address";
            // echoing JSON response
             echo json_encode($response);
        }
 

       



mysql_close();
?>