var cBranches;

function loadBranches() {
	cBranches.empty();
     for (var i = 0; b = branches[i]; i++) {
    	 if(("1.2" != b) && ("1.0" != b) && ("0.0" != b)){
        	 cBranches.append('<li id="' + b + '">' + b + '</li>');    		 
    	 }
     };
     cBranches.children().click(function() {
          setBranch(this.id);
          $(location).attr('href',"/learn/javadocs/" + branch + "/");
     });
};
function refresh() {
	$("#" + cBranches.attr('id') + '-bt').empty();
	$("#" + cBranches.attr('id') + '-bt').append("<strong>" + branch + "</strong>");
}

/**
 * Initializes the branch selector/cookies.
 * @param sb
 * 		The selector of branches.
 */
function init(cb) {
	cBranches = cb;
	loadBranches();
	branch = $.cookie('branch');
	if (!branch) {
		$.cookie('branch', getDefaultBranch($.cookie('release')), {path: '/' });
		branch = $.cookie('branch');
	}
    refresh();
}
