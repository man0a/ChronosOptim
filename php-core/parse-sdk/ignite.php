<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Application files must go in directory containing parse-sdk/.
define( 'PARSE_SDK_DIR', './parse-sdk/src/Parse/' );
require_once( 'autoload.php' );
use Parse\ParseClient;
// Init parse: app_id, rest_key, master_key
$app_id='z0r0EqW0wMp66JIyWd6HO1SeoO5FHFg1LDftvSrh';
$rest_key='w6Gyn1FTzWWvD4yxUeLsP5pl2gDhuG83Krpdu64A';
$master_key='C6PGrY4tfb78LTDomH9qIPFb30SBkOMCeEYfihII';

ParseClient::initialize($app_id, $rest_key, $master_key);

?>