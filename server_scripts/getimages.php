<?php

#$latitude = $_REQUEST["latitude"];
#$longitude = $_REQUEST["longitude"];
$id = $_REQUEST["resID"];

$mysqli = new mysqli("localhost","root","","foodfamous");
if($mysqli->errno)
{
	echo "error connecting to the database";
	echo $mysqli->errno;
	
}

$image_table = "image_table";
$query2 = "SELECT * from $image_table WHERE ID=$id";
$result2 =  $mysqli->query($query2);
if(!$result2)
{
	
	echo "error in reading table";
	echo $mysqli->error;
	
}
//return output array
//$return_images=[];

//arrays of each elements
$return_totalarray=[];

$resID_array=[];
$imageurl_array=[];
$foodname_array=[];
$review_array=[];
$foodquality_array=[];
$foodprice_array=[];
$foodservice_array=[];
$foodrating_array=[];

while($row = $result2->fetch_assoc()) {

	$fid = $row['ID'];
	$furl = $row['Url'];
	$ffoodname =$row['Foodname'];
	$freview = $row['Review'];
	$ffoodquality=$row['Foodquality'];
	$ffoodprice =$row['Foodprice'];
	$ffoodservice =$row['Foodservice'];
	$ffoodrating= $row['Foodrating'];
     /*
    array_push($return_images,['resID'=>$fid,'imageurl'=>urlencode($furl),'foodname'=>$ffoodnname,'review'=>$freview,
									'foodquality'=>$ffoodquality,'foodprice'=>$ffoodprice,'foodservice'=>$ffoodservice,'foodrating'=>$ffoodrating]);
       */
     array_push($resID_array,$fid);
     array_push($imageurl_array,urlencode($furl));
     array_push($foodname_array,$ffoodname);
     array_push($review_array,$freview);
     array_push($foodquality_array,$ffoodquality);
 	 array_push($foodprice_array,$ffoodprice);
     array_push($foodservice_array,$ffoodservice);
     array_push($foodrating_array,$ffoodrating);	 
	
}

	array_push($return_totalarray,$resID_array);
	array_push($return_totalarray,$imageurl_array);
	array_push($return_totalarray,$foodname_array);
	array_push($return_totalarray,$review_array);
	array_push($return_totalarray,$foodquality_array);
	array_push($return_totalarray,$foodprice_array);
	array_push($return_totalarray,$foodservice_array);
	array_push($return_totalarray,$foodrating_array);
	
header('Content-type: application/json');
//echo json_encode( $return_images);
echo json_encode($return_totalarray);


$mysqli->close();





?>

