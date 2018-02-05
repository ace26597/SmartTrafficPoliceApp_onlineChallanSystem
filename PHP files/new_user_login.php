<?php
 

// array for JSON response
$response = array();


// check for required fields
if (isset($_POST['password'])  && isset($_POST['email']) && isset($_POST['gcm_id'])) {
    
   
    $password = $_POST['password'];    
    $email = $_POST['email'];
    $gcm_id =$_POST['gcm_id'];
    
    


        
//hostname = www.gopajibaba.com
//username = gopaj_shop
//password = 123456
//databasename = gopajiba_shoping

       mysql_connect("www.gopajibaba.com","gopaj_shop","123456");
       mysql_select_db("gopajiba_shoping");

         mysql_query("UPDATE shop_registration SET gcm_id = '' WHERE gcm_id = '$gcm_id'");  
 
        $result = mysql_query("SELECT * FROM shop_registration WHERE shop_email = '$email'");
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $response  = mysql_fetch_array($result);
                if($password == $response['shop_password'])
           {

      
              $update = mysql_query("UPDATE shop_registration SET gcm_id = '$gcm_id' WHERE shop_email = '$email'");  
        
           if($update)
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
        
          

           }else {
             $response["success"] = 0;
             $response["message"] = "Wrong password please try again";
            // echoing JSON response
             echo json_encode($response);
        }


        } else {
             $response["success"] = 0;
             $response["message"] = "Wrong email address";
            // echoing JSON response
             echo json_encode($response);
        }
 

       



} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    // echoing JSON response
    echo json_encode($response);
}

mysql_close();
?>