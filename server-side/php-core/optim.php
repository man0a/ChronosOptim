<?php

use Parse\ParseObject;
use Parse\ParseQuery;
use Parse\ParseUser;

function optim($teamId){
	// get team members' schedules off Parse
	$eventObjs=getEventsByTeam($teamId);
	// get sorted start and end times 
	list($start,$end)=prepare($eventObjs);
	// merge times
	$merged=merge($start,$end);
	// count busyness and create weights
	$blocks=getWeights($merged);

	return $blocks;
}

function merge($start,$end){
	$merged=array();
	sort($start);
	sort($end);
	$total=count($start)+count($end);
	$s=0;
	$e=0;
	while($s<count($start) && $e<count($end)){
		// when some start and end times same,
		// start times will be listed first
		if($start[$s]<=$end[$e]){
			$merged[]=$start[$s++];
		}else{
			$merged[]=-$end[$e++];
		}
	}
	if($s<count($start)){
		for($i=$s;$i<count($start);$i++){
			$merged[]=$start[$i];
		}
	}else if($e<count($end)){
		for($i=$e;$i<count($end);$i++){
			$merged[]=-$end[$i];
		}
	}
	return $merged;
}

function prepare($eventObjs){
	$start=array();
	$end=array();
	foreach($eventObjs as $event){
		// for now, ignore date
		$start[]=parseInt($event->get("startTime"));
		$end[]=parseInt($event->get("endTime"));
	}

	sort($start);
	sort($end);
	return array($start,$end);
}

function parseInt($str){
	// "11:00"
	$parts=explode(":",$str);
	return $parts[0]*60+$parts[1];
}



function getEventsByTeam($teamId){
	// get Team from Id
	$query=new ParseQuery("Team");
	$team=$query->get($teamId);

	// get Users from Team
	$members=$team->get("members");
	// get Events from Users

	$query=new ParseQuery("Event");
	$query->containedIn("userId",$members);
	$events=$query->find();
	return $events;
}

function getWeights($merged){
	$busy=0;
	$prev_busy=0;
	$blocks=array();
	$prev_time=-1;

	for($i=0;$i<count($merged);){
		$current_time=abs($merged[$i]);

		for($times=$i;$times<count($merged) && $current_time==abs($merged[$times]);$times++){
			if($merged[$times]>0){// start
				$busy++;
			}else{// end
				$busy--;
			}
		}
		if($prev_busy!=$busy){
			$blocks[]=new Weight($prev_time,$current_time,$prev_busy);
			$prev_time=$current_time;
			$prev_busy=$busy;
		}
		// advance iterator
		$i=$times;
	}
	$blocks[]=new Weight($prev_time,0,$prev_busy);
	return $blocks;
}


?>