<?php

$path = $_REQUEST["path"];
$foodname = $_REQUEST["foodname"];
$resname= $_REQUEST["resname"];
$latitude= $_REQUEST["latitude"];
$longitude = $_REQUEST["longitude"];
$review= $_REQUEST["review"];
$quality = $_REQUEST["quality"];
$price =$_REQUEST["price"];
$service =$_REQUEST["service"];
$rating =$_REQUEST["rating"];


#$path = "https://dashboard?h_";
#$latitude= 4.1;
#$longitude = 5.0;
#$resname= "paradise";
#$comment ="great";
#echo  "Hello";

$mysqli = new mysqli("localhost","root","","foodfamous");
if($mysqli->errno)
{
	echo "error connecting to the database";
	echo $mysqli->errno;
	
}

$query1 = "CREATE TABLE IF NOT EXISTS restaurant_table(
                      ID INT NOT NULL AUTO_INCREMENT,
                      Latitude  VARCHAR(200),
					  Longitude varchar(200),
                      RestaurantName varchar(255),
                      PRIMARY KEY (ID))";
$result1 = $mysqli->query($query1);
if(!$result1)
{
	
	echo "error in creating restaurant table";
	echo $mysqli->error;
	
}
$res_table = "restaurant_table";
$query2 = "SELECT ID from $res_table WHERE Latitude='$latitude' and Longitude='$longitude'";
$result2 =  $mysqli->query($query2);
if(!$result2)
{
	
	echo "error in reading res table";
	echo $mysqli->error;
	
}
$i=0;
while($row = $result2->fetch_assoc()) {
	$fid = $row['ID'];
	#$flatitude = $row['Latitude'];
	#$flongitude = $row['Longitude'];
	#$fresname = $row['Resname'];
	#$fcomment = $row['Comment'];
	//$append_images="";
	
		//echo $foodname;
	    $query3 ="INSERT INTO image_table(Url,Foodname,Review,Foodquality,Foodprice,Foodservice,Foodrating,ID) VALUES('$path','$foodname','$review',
		                                                         '$quality','$price','$service','rating',$fid)";
		$result3 =  $mysqli->query($query3);
        if(!$result3)
        {
	
	         #echo "error in insertinng in image table";
	         #echo $mysqli->error;
	
        }
		
		$i++; 
		
	
	
	}
if($i==0)
{	
	
	    $query6 = "INSERT INTO $res_table(Latitude,Longitude,RestaurantName) VALUES('$latitude','$longitude','$resname')" ;
		$result6 = $mysqli->query($query6);
        if(!$result6)
        {
	
	         echo "error in inserting into res table";
	         echo $mysqli->error;
	
        }	
		$query7 = "SELECT ID from $res_table WHERE Latitude='$latitude' and Longitude='$longitude'" ;
		$result7 = $mysqli->query($query7);
        if(!$result7)
        {
	
	         echo "error in selecting from res table";
	         echo $mysqli->error;
	
        }	
	    $row = $result7->fetch_assoc();
	    $fid = $row['ID'];
		
		$query4 = "CREATE TABLE IF NOT EXISTS image_table( Url VARCHAR(500),
		                                                   Foodname VARCHAR(200),
		                                                   Review VARCHAR(1000),
														   Foodquality VARCHAR(20),
														   Foodprice VARCHAR(20),
														   Foodservice VARCHAR(20),
														   Foodrating VARCHAR(20),
                                                           ID int,
                                                           PRIMARY KEY (Url)  														  
		                                                 )" ;
		$result4 = $mysqli->query($query4);
        if(!$result4)
        {
	
	         echo "error in creating  image table";
	         echo $mysqli->error;
	
        }		
		$query5 ="INSERT INTO image_table(Url,Foodname,Review,Foodquality,Foodprice,Foodservice,Foodrating,ID) VALUES('$path','$foodname','$review',
		                                                         '$quality','$price','$service','$rating',$fid)";	
        $result5 =  $mysqli->query($query5);
        if(!$result5)
        {
	
	         #echo "error in inserting into image table";
	         #echo $mysqli->error;
	
        }

}
echo "SUCCESS";
$mysqli->close();
												   
?>