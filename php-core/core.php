<?php
require_once('./parse-sdk/ignite.php');
use Parse\ParseObject;
use Parse\ParseQuery;
use Parse\ParseUser;
use Parse\ParseInstallation;
use Parse\ParsePush;

require_once("./classes.php");
require_once("./optim.php");

main();

function main(){
	switch($_SERVER["REQUEST_METHOD"]){
		case 'GET':$input=trim($_GET["data"]);break;
		case 'POST':$input=trim($_POST["data"]);break;
		default:echo "Unsupported request type. Do GET or POST.";return;
	}

	$buffer=explode("||",$input);
	array_pop($buffer);
	setGlobals();
	foreach($buffer as $str){
		$str=str_replace("|","&",$str);
		// echo $str;
		parse_str($str,$post);
		handle($post);
		echo "||";
	}
}
function handle($post){
	if(!isset($post["client"])){
		// malformed POST request
		// echo $post;
		echo "Malformed POST request: Please specify a 'client' key.";
		return;
	}
	$client=trim($post["client"]);
	if(strcmp($client,"!")==0){
		addUser();
		return;
	}
	if(!isset($post["x"])){
		// malformed POST request
		echo "Malformed POST request: 'client' key but no 'x' key.";
		return;
	}
	$type=trim($post["x"]);
	switch($type){
		case "createE":addEvent($post);break;
		case "createT":addTeam($post);break;
		case "createM":addMember($post);break;
		case "deleteE":delEvent($post);break;
		case "deleteT":delTeam($post);break;
		case "deleteM":delMember($post);break;
		case "changeU":changeUser($post);break;
		case "pullU":pullUsers();break;
		case "optim":optim($post);break;
		case "installU":installUser($post);break;
		case "broadcast":broadcast($post);break;
		default:
			echo "Malformed POST request: invalid 'x' value.";
			return;
	}
	

}



// ADD METHODS
function addEvent($post) {
	$fields=array("title","description","date","startTime","endTime","location","subtitle");
	$event=ParseObject::create("Event");
	foreach($fields as $field){
		$event->set($field,trim($post[$field]));
	}
	$event->set("userId",trim($post["client"]));
	$event->save();
	$objId=$event->getObjectId();
	$oldId=trim($post["id"]);
	echo "+event:".$oldId.",".$objId;
}
function addTeam($post) {
	$team=ParseObject::create("Team");
	$team->set("name",trim($post["name"]));
	$team->set("description",trim($post["description"]));
	$team->setArray("members",array(trim($post["client"])));
	$team->save();
	$oldId=trim($post["id"]);
	$objId=$team->getObjectId();
	echo "+team:".$oldId.",".$objId;
}
function addMember($post) {
	// get team
	$query=new ParseQuery("Team");
	try{
		$team=$query->get(trim($post["id"]));
	}catch(Exception $ex){
		echo "Failed query.";
		return;
	}

	// get id of user
	$alias=trim($post["name"]);
	$query=new ParseQuery("Users");
	try{
		$query->equalTo("uname",$alias);
		$users=$query->find();
	}catch(Exception $ex){
		echo "Failed query.";
		return;
	}
	$id=$users[0]->getObjectId();
	$team->add("members",array($id));
	$team->save();
	echo ":)";
}
function addUser(){
	// client not yet registered, respond 
	$user=ParseObject::create("Users");
	$user->save();
	$objId=$user->getObjectId();
	$user->set("uname","user ".$objId);
	$user->set("installation","!");// omit "!"
	$user->save();
	echo "+user:".$objId;
}



// DELETE METHODS
function delEvent($post){
	$objId = trim($post["id"]);
	$query = new ParseQuery("Event");
	
	try {
  		$y = $query->get($objId);
  	} catch (Exception $ex) {
  		echo "Could not query: event ".$objId;
  		return;
  	}
	$y->destroy();
	echo ":)".$objId;
}
function delTeam($post) {
	$objId = trim($post["id"]);
	$query = new ParseQuery("Team");
	
	try {
  		$y = $query->get($objId);
  	} catch (Exception $ex) {
  		echo "Could not query: team ".$objId;
  		return;
  	}
	$y->destroy();
	echo ":)".$objId;
}
function delMember($post) {
	$objId=trim($post["id"]);
	$uname=trim($post["name"]);
	$query=new ParseQuery("Team");
	try {
  		$y=$query->get($objId);
  	} catch (Exception $ex) {
  		echo "Failed query.";
  		return;
  	}
  	$y->remove("members",$uname);
  	$y->save();
	echo ":)";
}


// CHANGE METHODS
function changeUser($post){
	$objId=trim($post["client"]);
	$newName=trim($post["name"]);
	$query=new ParseQuery("Users");
	try {
  		$y=$query->get($objId);
  	} catch (Exception $ex) {
  		echo "Failed query.";
  		return;
  	}
  	$y->set("uname",$newName);
  	$y->save();
  	echo ":)".$newName;
}

function installUser($post){
	$objId=trim($post["client"]);
	$installId=trim($post["id"]);

	$query=new ParseQuery("Users");
	try {
  		$y=$query->get($objId);
  	} catch (Exception $ex) {
  		echo "Failed query.";
  		return;
  	}
  	$y->set("installation",$installId);
  	$y->save();
  	echo ":)";
}

// BROADCAST METHOD
function broadcast($post){
	$client=trim($post["client"]);
	$teamId=trim($post["teamId"]);
	// $fields=array("title","description","date","startTime","endTime","location","subtitle");
	// $event=ParseObject::create("Event");
	// foreach($fields as $field){
		// $event->set($field,trim($post[$field]));
	// }

	// get team from id
	$query=new ParseQuery("Team");
	try {
  		$team=$query->get($teamId);
  	} catch (Exception $ex) {
  		echo "Failed query.";
  		return;
  	}	

  	// get users from team
  	$userIds=$team->get("members");
	$query=new ParseQuery("Users");
	$query->containedIn("objectId",$userIds);
	$query->notEqualTo("objectId",$client);
	// $query->equalTo("objectId",$client);
	try {
  		$query->containedIn($userIds);
  		$users=$query->find();
  	} catch (Exception $ex) {
  		echo "Failed query.";
  		return;
  	}

	// get installations from users
  	$installs=[];
  	foreach($users as $user){
  		$installs[]=$user->get("installation");
  	}
  	
	$uname=$user->get("uname");
	$date=trim($post["date"]);
	$startTime=trim($post["startTime"]);
	$endTime=trim($post["endTime"]);
	$teamName=$team->get("name");

  	// notify teammates
  	// query, push, send
	$pushQuery=ParseInstallation::query();
	$pushQuery->containedIn("objectId",$installs);
	$push=array(
		"title"=>$uname." invited you",
		// "alert"=>"simple"
		"alert"=>$date." ".$startTime."-".$endTime." > ".$teamName." meeting"
	);

	ParsePush::send(array(
		"where"=>$pushQuery,
		"data"=>$push
	));



	// notify self
	$query=new ParseQuery("Users");
	try {
  		$user=$query->get($client);
  	} catch (Exception $ex) {
  		echo "Failed query.";
  		return;
  	}
  	// push, query, send
	$pushQuery=ParseInstallation::query();
	$pushQuery->equalTo("objectId",$user->get("installation"));
	$c=count($users);
	// if($c==1){
	// 	$push=array(
	// 		"title"=>"Invitation sent",
	// 		"alert"=>"Your teammate was invited."
	// 	);
	// }else{
	// 	$push=array(
	// 		"title"=>"Invitations sent",
	// 		"alert"=>"Your ".$c." teammates were invited."
	// 	);
	// }

	ParsePush::send(array(
		"where"=>$pushQuery,
		"data"=>$push
	));
}



function pullUsers(){
	$query=new ParseQuery("Users");
	try{
		$y=$query->find();
	}catch(Exception $ex){
		echo "Failed query.";
		return;
	}
	if(count($y)==0){
		echo "=users=empty";
		return;
	}
	$names="=users:".$y[0]->get("uname");
	for($i=1;$i<count($y);$i++){
		$names.=",".$y[$i]->get("uname");
	}
	echo $names;
}

function setGlobals(){
	$t=time();
	date_default_timezone_set("America/New_York");// EST
	$h=idate("H",$t);
	$m=idate("i",$t);
	$s=idate("s",$t);
	$GLOBALS["OFFSET"]=$t-($h*60+$m)*60-$s;
	$GLOBALS["BASE_LINE"]=$t;
}
?>
