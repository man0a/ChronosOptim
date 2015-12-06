<?php

class Block{
	public $start;
	public $end;

	function Block($start,$end){
		$this->start=$start;
		$this->end=$end;
	}
}

class Weight extends Block{
	public $weight;

	function Weight($s,$e,$w){
		$this->Block($s,$e);
		$this->weight=$w;
	}

	function toStr(){
		$s=$this->str($this->start);
		$e=$this->str($this->end);
		return "{$s} to {$e} ({$this->weight})<br>";
	}

	function str($timeInt){
		$min="".($timeInt%60);
		if(strlen($min)==1){
			$min="0".$min;
		}
		return floor($timeInt/60).":".$min;
	}
}

?>