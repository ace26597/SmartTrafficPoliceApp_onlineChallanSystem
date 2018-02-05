<?php 
	
        $insrno=$_POST['insno'];
        $insrtype=$_POST['instype'];
        $insrcomp=$_POST['inscomp'];
        $insvf=$_POST['vf'];
        $insvto=$_POST['vto'];
        $vhno=$_POST['vno']
        
        
	

	mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");
	$q=mysql_query ("INSERT INTO `insurance`(`insurance_no`, `insurance_type`, `insurance_company`, `valid_from`, `valid_to`,`vno`) VALUES ('$insrno','$insrtype','$insrcomp','$insvf','$insvto','$insvhno')");
mysql_close();
 ?>