<?php

require_once 'API.class.php';
class MyAPI extends API
{
	// The database
	private $mysqli = NULL;

	public function __construct($request, $origin) {
		$this->config = include('config.php');
		$this->initDB();
		$this->sanitizeHTTPParameters();

		parent::__construct($request);
	}

	private function sanitizeHTTPParameters() {
		foreach ($_GET as $key => $value) {
			$_GET[$key] = escapeshellcmd($this->mysqli->real_escape_string($value));
		}
		foreach ($_POST as $key => $value) {
			$_POST[$key] = escapeshellcmd($this->mysqli->real_escape_string($value));
		}
	}

	private function encryptPassword($password) {
		return $this->mysqli->real_escape_string(crypt($password, $this->config['salt']));
	}

	private function initDB() {
		$this->mysqli = new mysqli( $this->config['hostname'],
									$this->config['username'],
									$this->config['password'],
									$this->config['databaseName']);

		if (mysqli_connect_errno()) {
			echo "<br><br>There seems to be a problem with our database. Reload the page or try again later.";
			exit();
		}
	}

	private function select($sql) {
		$res = mysqli_query($this->mysqli, $sql);
		return mysqli_fetch_array($res, MYSQLI_ASSOC);
	}

	private function selectMultiple($sql) {
		$res = mysqli_query($this->mysqli, $sql);
		$finalArray = array();

		while($temp = mysqli_fetch_array($res, MYSQLI_ASSOC)) {
			array_push($finalArray, $temp);
		}

		return $finalArray;
	}

	private function insert($sql) {
		mysqli_query($this->mysqli, $sql);
	}

	// Returns an associative array with all of the information about a meeting
	// Contains more than just the information in the meeting table (ex. includes the participant ids)
	private function getFullMeeting($meetingID) {
		$meetingArray = $this->select("SELECT * FROM Meeting WHERE meetingID = $meetingID");

		// Add userIDs of participants to array
		$userIDs = $this->selectMultiple("SELECT * FROM MeetingToUser WHERE meetingID = $meetingID");
		$meetingArray['members'] = $userIDs;

		return $meetingArray;
	}

	//--------------------- API ENDPOINTS ------------------------\\
	protected function user() {
		if(isset($_GET['userID']) && isset($_GET['password'])) {
			$userID = $_GET['userID'];
			$password = $_GET['password'];

			return $this->select("SELECT * FROM User WHERE userID = $userID and password = '$password'");
		} else if(isset($_GET['email']) && isset($_GET['password'])) {
			$email = $_GET['email'];
			$password = $_GET['password'];

			return $this->select("SELECT * FROM User WHERE email = '$email' AND password = '$password'");
		} else if(isset($_GET['searchTerm'])) {
			$searchTerm = $_GET['searchTerm'];
			return $this->selectMultiple("SELECT userID, firstName, lastName, email FROM User WHERE
				LCASE(firstName) LIKE LCASE('%$searchTerm%')
				OR LCASE(lastName) LIKE LCASE('%$searchTerm%')");
		} else if (isset($_GET['barcode'])) {
			$barcode = $_GET['barcode'];
			return $this->select("SELECT * FROM User WHERE barcode = $barcode");
		} else {
			return NULL;
		}
	}

	protected function schedule() {
		if (isset($_GET['userID'])) {
			$userID = $_GET['userID'];

			return $this->selectMultiple("SELECT * FROM Period WHERE userID = $userID");
		} else {
			return NULL;
		}
	}

	protected function meeting() {
		if(isset($_GET['meetingID'])) {
			return $this->getFullMeeting($_GET['meetingID']);
		} else if(isset($_GET['userID'])) {
			$userID = $_GET['userID'];

			$meetingsForUser = $this->selectMultiple("SELECT meetingID FROM MeetingToUser WHERE userID = $userID");

			$returnArray = array();
			foreach($meetingsForUser as $meetingArray) {
				array_push($returnArray, $this->getFullMeeting($meetingArray['meetingID']));
			}

			return $returnArray;
		} else if(isset($_POST['name']) &&
			isset($_POST['day']) && 
			isset($_POST['period']) &&
			isset($_POST['creatorID']) &&
			isset($_POST['memberIDS'])) {

			$this->insert("INSERT INTO Meeting (name, dayIndex, periodIndex, creatorID) VALUES ('{$_POST['name']}', {$_POST['day']}, {$_POST['index']}, {$_POST['creatorID']})");
			$meetingArray = $this->select("SELECT meetingID FROM Meeting WHERE creatorID = {$_POST['creatorID']} and dayIndex = {$_POST['day']} and periodIndex = {$_POST['period']}");
			$meetingID = $meetingArray['meetingID'];
			foreach($_POST['memberIDS'] as $memberID) {
				$this->insert("INSERT INTO MeetingToUser (meetingID, userID) VALUES ($meetingID, $memberID)");
			}

		} else {
			return NULL;
		}
	}
 }

 ?>
