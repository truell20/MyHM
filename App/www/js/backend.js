var url = "localhost/ApPosition/Backend/"

function getUser(userID, email, password) {
	var result = $.ajax({
		url: url+"user", 
		async: false,
		method: "GET",
		data: {userID: userID, password: password}
	});
	return result.responseJSON
}

function searchForUser(searchTerm) {

}