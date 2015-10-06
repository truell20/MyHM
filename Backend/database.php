<?php

function initDB() {
	$mysqli = new mysqli("ApPosition.db.12061709.hostedresource.com", "ApPosition", "Fustercluck2!", "ApPosition");
	if (mysqli_connect_errno()) { 
		echo "<br><br>There seems to be a problem with our database. Reload the page or try again later.";
		exit(); 
	}
	return $mysqli;
}

?>