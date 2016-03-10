/*
  Summary:
    A name is entered and then submit is called with first as true,
    looking up score and switching display to the main game. After
    each click the score is updated and submitted to the servlet.

  Variables:
    clicks: total amount of times the button is pressed
    url:    address of java servlet

  Functions:
    ready:  sets submit button to execute call and name textbox to
            automaticaly be focused and execute call upon enter being
	    pressed
    submit: executes call to servlet, updating score and recieving
            previous score if 'first' is true
    update: changes from login screen to main application
*/
var clicks = 0;
var url = "http://localhost:8080/clicker/setScore";

$(document).ready(function(){
    $("#submit").click(function(){submit(true)});
    $("#name").focus().select();
    $('#name').keypress(function(event){
	if(event.which == '13')
            submit(true);
    });
});

function submit(first){
    var name = $("#name").val();
    var xhttp = new XMLHttpRequest();
    $("#name").hide();
    xhttp.onreadystatechange = function() {
	if (xhttp.readyState == 4 && xhttp.status == 200) {
	    if(first)
		update(xhttp.responseText);
	}
    };
    xhttp.open("GET", url+"?name="+name+"&score="+clicks, true);
    xhttp.send();

}

function update(score){
    clicks = score;
    $("#header").after("<P id=\"counter\">Total Clicks: " + score +
		       "</P><INPUT TYPE=\"BUTTON\" CLASS=\"myButton\"" +
		       "ID=\"clicker\" VALUE=\"Click Me!\"/><BR/><BR/>");
    $("#clicker").click(function(){
	clicks++;
	$("#counter").html("Total Clicks: " + clicks);
	submit(false);
    })
    $("#submit").hide();
}
