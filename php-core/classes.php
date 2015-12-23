<?php

class Block{
	public $start;
	public $end;

	function Block($start,$end){
		$this->start=$start;
		$this->end=$end;
	}

	function toStr(){
		$s=$this->toDate($this->start);
		$e=$this->toDate($this->end);
		$day=substr($s,0,10);
		$start=substr($s,11);
		$end=substr($e,11);
		return "$day,$start,$end";
	}

	function start(){
		return substr($this->toDate($this->start),11);
	}
	function end(){
		return substr($this->toDate($this->end),11);
	}

	function toDate($x){
		return date("m-d-Y H:i",60*$x+$GLOBALS["OFFSET"]);
	}
}

class Weight extends Block{
	public $weight;

	function Weight($s,$e,$w){
		$this->Block($s,$e);
		$this->weight=$w;
	}

	function toStr(){
		return parent::toStr().",{$this->weight}";
	}
}

?>