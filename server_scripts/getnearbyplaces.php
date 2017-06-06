<?php

$latitude = $_REQUEST["latitude"];
$longitude = $_REQUEST["longitude"];
error_reporting(E_ERROR | E_PARSE);
$mysqli = new mysqli("localhost","root","","foodfamous");
if($mysqli->errno)
{
	echo "error connecting to the database";
	echo $mysqli->errno;
	
}

#$image_table = "image_table";
$query2 = "SELECT * from restaurant_table";
$result2 =  $mysqli->query($query2);
if(!$result2)
{
	
	echo "error in reading table";
	echo $mysqli->error;
	
}
$return_string=[];
while($row = $result2->fetch_assoc()) {
	$fid = $row['ID'];
	$flatitude = $row['Latitude'];
	$flongitude = $row['Longitude'];
	$fresname = $row['RestaurantName'];
	$dist = distance($latitude,$longitude,$flatitude,$flongitude);
	#echo $dist;
	if($dist<=10)
	{        
            array_push($return_string,['resID'=>$fid,'latitude'=>$flatitude, 'longitude'=>$flongitude,'resname'=> $fresname]);
           	#$return_string = $return_string.$flatitude.",".$flongitude.",".$fresname."/";
	}
}
header('Content-type: application/json');
echo json_encode( $return_string );

function distance($lat1, $lon1, $lat2, $lon2) {

  $theta = $lon1 - $lon2;
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
  $dist = acos($dist);
  $dist = rad2deg($dist);
  $miles = $dist * 60 * 1.1515;
  return abs($miles);
      
}
$mysqli->close();



?>