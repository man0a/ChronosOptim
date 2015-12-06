<?php
require_once('./parse-sdk/ignite.php');
use Parse\ParseObject;
use Parse\ParseQuery;
use Parse\ParseUser;

require_once("./classes.php");
require_once("./optim.php");

main();

function main(){
	if(!isset($_POST["id"])){
		echo "bogus";
		return;
	}

	if(strcmp(trim($_POST["id"]),"!")==0){
		$user=ParseObject::create("Users");		
		$user->save();
		$objId=$user->getObjectId();
		$user->set("uname",$objId);
		$user->save();
		echo "+user:".$objId;
	}else{
		echo "solid";
	}
	// $weights=optim("GfCEpAfie6");
	// foreach($weights as $weight){
	// 	$str=$weight->toStr();
	// 	echo $str."<br>";
	// }
// 	$x = $_GET["action"];
// 	echo "pass action ";

// 	$user = array("Users", "uname", "userID", "members");	
// 	$event = array("Event", "userID", "location", "date", "startTime", "endTime", "description", "title", "subtitle");
// 	$user = array("Users", "uname", "userID", "password",);
// 	$fields=array($event,$user,$channel,$broadcast);

}

function add($array) {
	
	$y = ParseObject::create($array[0]);
	
	if($array[0] == "Event" or $array[0] == "Channels" or $array[0] == "Broadcasts") {
		linkId($array[0], $y);
	} else {
		$y->set($column1, $_GET[$column1]);
	}
	
	for($i = 2; $i < count($array); $i++) {
		$y->set($array[$i], $_GET[$array[$i]]); 
	}
	$y->save();
}

function del($tablename) {
	$objId = $_GET["objectId"];
	$query = new ParseQuery($tablename);
	
	try {
  		$y = $query->get($objId);
  		$y->destroy();
  		echo "User deleted successfully";
  	} catch (Exception $ex) {
  		echo "Error: Not able to find Id";
  	} 
}

function change($array) {
	$objId = $_GET["objectId"];
	$query = new ParseQuery($array[0]); 
			
	try {
		$y = $query->get($objId);
		
		for($i = 1; $i < count($array); $i++) {
			$fname=$array[$i];
			if(isset($_GET[$fname]) && !empty($_GET[$fname])) {
				$y->set($fname, $_GET[$fname]);
			}
		}
		
		$y->save();
	} catch (Exception $ex) {
		echo "Error: Not available";
	}
}




// $testObject = ParseObject::create("TestObject");
// $testObject->set("foo", $_GET["action"]);
// $testObject->save();
// // get the object ID
// echo $testObject->getObjectId();


// $obj=ParseObject::create("Event");
// $obj->set("location","Vertica");
// $obj->set("startTime","Vertica");
// $obj->set("endTime","Vertica");
// $obj->set("userId","Yy5uFVA51l");
// $obj->set("date","12-03-2015");
// $obj->set("subtitle","");
// $obj->set("description","");
// $obj->set("title","Office Hours");
// $obj->save();
// echo $obj->getObjectId()

?>
