<?php

use Parse\ParseObject;
use Parse\ParseQuery;
use Parse\ParseUser;

function optim($post){
	if(!isset($post["id"])){
		echo "Malformed POST request: no 'id' key.";
		return;
	}
	$teamId=trim($post["id"]);

	// get team members' schedules off Parse
	$events=getEventsByTeam($teamId);
	// merge events
	$merged=blockMerge($events);
	// count busyness and create weights
	$weights=getWeights($merged);

	$blocks=constrain($weights);
	// echo count($blocks);

	$output="+optim:";
	$c=0;
	foreach($blocks as $block){
		// echo $block->toStr()."<br>";
		$output.=$block->toStr().";";
		if(++$c==25){
			break;
		}
	}
	echo $output;
}

function blockMerge($blocks){
	$start=array();
	$end=array();
	foreach($blocks as $block){
		$start[]=$block->start;
		$end[]=$block->end;
	}
	return merge($start,$end);
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
		// get start and end times, as integers (minutes)
		$evDateStr=$event->get("date");
		$evDate=date_timestamp_get(date_create_from_format("m-d-Y",$evDateStr));
	    $numMins=floor(($evDate-$GLOBALS["BASE_LINE"])/60);
		$start[]=$numMins+parseInt($event->get("startTime"));
		$end[]=$numMins+parseInt($event->get("endTime"));
	}

	sort($start);
	sort($end);
	return array($start,$end);
}

function parseInt($str){
	// "11:00" -> 660
	$parts=explode(":",$str);
	return $parts[0]*60+$parts[1];
}
function parseStr($int){
	// 660 -> "11:00"
	$h=$int/60;
	if($h<10){
		$h="0".$h;
	}
	$m=$int%60;
	if($m<10){
		$m="0".$m;
	}
	return $h.":".$m;
}


function getEventsByTeam($teamId){
	// get Team from Id
	$query=new ParseQuery("Team");
	$team=$query->get($teamId);
	// get User from Team
	$members=$team->get("members");
	// get Events
	$query=new ParseQuery("Event");
	$events=array();

	// for each User on Team
	foreach($members as $member){
		$query->equalTo("userId",$member);
		$eventObjs=$query->find();// get events
		list($start,$end)=prepare($eventObjs);// strip start and end
		$merged=merge($start,$end);// merge
		$blocks=squash($merged);// squash
		$events=array_merge($events,$blocks);// add to list
	}

	return $events;
}

function squash($merged){
	$busy=0;
	$blocks=array();
	$block_start=0;

	for($i=0;$i<count($merged);$i=$times){
		$current_time=abs($merged[$i]);

		for($times=$i;$times<count($merged) && $current_time==abs($merged[$times]);$times++){
			if($merged[$times]>0){// start
				if($busy==0){
					$block_start=$current_time;
				}
				$busy++;
			}else{// end
				$busy--;
				if($busy==0){
					$blocks[]=new Block($block_start,$current_time);
				}
			}
		}
	}
	return $blocks;
}

function getWeights($merged){
	$busy=0;
	$prev_busy=0;
	$blocks=array();
	$prev_time=0;

	for($i=0;$i<count($merged);$i=$times){
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
	}
	// delete first block
	array_shift($blocks);
	// sort by weight
	usort($blocks,"cmp");
	return $blocks;
}

function cmp($a,$b){
	$x=$a->weight;
	$y=$b->weight;
	if($x==$y){
		return 0;
	}
	return ($x<$y)?-1:1;
}

function constrain($weights){
	$blocks=[];

	foreach($weights as $weight){
		$s=parseInt($weight->start());
		$e=parseInt($weight->end());

		if($s<540){// before 9am
			$weight->start="09:00";
			$s=540;
		}
		if($e>1020){// after 5pm
			$weight->end="17:00";
			$e=1020;
		}
		if($e-$s>=60){// at least one hour
			$blocks[]=$weight;
		}
	}
	return $blocks;
}

?>