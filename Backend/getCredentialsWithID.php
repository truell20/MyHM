<?php
	include "database.php";
	if (isset($_GET['userID'])) {
		$mysqli = initDB();
		if (mysqli_connect_errno()) { // If something went wrong
			print("error"); // print error
			exit(); // exit out of the program
		}
		$userID = $_GET['userID'];

		$sql = "SELECT firstName, lastName, email, password FROM User WHERE userID = ".$userID;
		$res = mysqli_query($mysqli, $sql);
		$resultArray = mysqli_fetch_array($res, MYSQLI_ASSOC);
		
		echo json_encode($resultArray);	
	}

?>