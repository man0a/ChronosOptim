<?php
require_once('./parse-sdk/ignite.php');
use Parse\ParseObject;
use Parse\ParseQuery;
use Parse\ParseUser;

// save something to class TestObject
$testObject = ParseObject::create("TestObject");
$testObject->set("foo", $_POST["poop"]);
$testObject->save();
// get the object ID
echo $testObject->getObjectId();
?>