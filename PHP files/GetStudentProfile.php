<?php
 

// array for JSON response
$response = array();


// check for required fields
if (isset($_GET['srno'])) {  
   
    //here name of user which he wants to be the image 
    $srno =$_GET['srno'];
    $lno =$_GET['lice_no'];
    
              mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");
 
        $result = mysql_query("SELECT url FROM user_document WHERE document_name = '$srno' and license_number='$lno'");
        // check for result  
       // we will check here for the file is present in the database or not
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $response  = mysql_fetch_array($result);             
        
           if($response)
        {
             $response["success"] = 1;
             $response["message"] = "Successful";
             echo json_encode($response);
        }else
          {
             $response["success"] = 0;
             $response["message"] = "Error try again";
             echo json_encode($response);
            }
        
		}else{
				
		  $response["success"] = 0;
                  $response["message"] = "Error try again";
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