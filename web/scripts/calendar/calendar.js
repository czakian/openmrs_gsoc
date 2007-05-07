
function showCalendar(obj) {
	makeCalendar(obj);
	if(self.gfPop)
		gfPop.fPopCalendar(obj);
	return false;
}

function makeCalendar(obj) {
	var id = "gToday:normal.htm";
	if (document.getElementById(id) == null) {
		var iframe = document.createElement("iframe");
		iframe.width=174;
		iframe.height=189;
		iframe.name=id;	// also defined in ipopeng.jsp as an IE hack.
		iframe.id = id;
		iframe.src= '/@WEBAPP.NAME@/scripts/calendar/ipopeng.htm';
		iframe.scrolling='no';
		iframe.frameBorder='0';
		iframe.style.visibility = 'visible';
		iframe.style.zIndex='999';
		iframe.style.position='absolute';
		iframe.style.top='-500px';
		iframe.style.left= '-500px';
		var bodies = document.getElementsByTagName("body");
		var body = bodies[0];
		iframe.name = id;
		body.appendChild(iframe);
	}
}

var oldCalOnload = window.onload;
if (typeof window.onload != 'function') {
	window.onload = makeCalendar;
} else {
	window.onload = function() {
		oldCalOnload();
		makeCalendar();
	}
}