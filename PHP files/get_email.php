<?php
$lno=$_POST['lice_no'];
mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");
$q=mysql_query("SELECT email from user where license_number='$lno'");

while($row=mysql_fetch_assoc($q))
$json_output[]=$row;
print(json_encode($json_output));
mysql_close();
?>
