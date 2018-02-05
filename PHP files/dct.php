<?php
$lno=$_POST['lice_no'];
$dname=$_POST['docname'];
mysql_connect("www.gopajibaba.com","gopaj_trf","123456");
	mysql_select_db("gopajiba_traffic");

$q=mysql_query("SELECT url FROM user_document WHERE document_name = '$dname' and license_number='$lno'");

while($row=mysql_fetch_assoc($q))
$json_output[]=$row;
print(json_encode($json_output));
mysql_close();
?>
