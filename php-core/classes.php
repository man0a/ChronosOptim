<?php

class Block{
	public $start;
	public $end;

	function Block($start,$end){
		$this->start=$start;
		$this->end=$end;
	}

	function toStr($baseline){
		$s=$this->toDate($this->start,$baseline);
		$e=$this->toDate($this->end,$baseline);
		$day=substr($s,0,10);
		$start=substr($s,11);
		$end=substr($e,11);
		return "date=$day,start=$start,end=$end";
	}

	function toDate($x,$baseline){

		return date("m-d-Y H:i",60*$x+$GLOBALS["OFFSET"]);
	}
}

class Weight extends Block{
	public $weight;

	function Weight($s,$e,$w){
		$this->Block($s,$e);
		$this->weight=$w;
	}

	function toStr($baseline){
		return parent::toStr($baseline).",weight:{$this->weight}";
	}
}

?>