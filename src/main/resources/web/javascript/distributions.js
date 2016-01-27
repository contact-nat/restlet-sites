var cQualifiers;
var cEditions;
var cTypesDistribution;
var handleFragment = false;
var redirectDownload = false;

function loadQualifiers() {
	cQualifiers.empty();
	for ( var i = 0; q = qualifiers[i]; i++) {
		cQualifiers.append('<li id="' + q.id + '">' + q.name + '</li>');
	};
	cQualifiers.children().click(function() {
		setQualifier(this.id);
		refresh();
	});
};
function loadEditions() {
	cEditions.empty();
	for ( var i = 0; edition = editions[i]; i++) {
		if ("all" != edition.id) {
			cEditions.append('<li id="' + edition.id + '">' + edition.longname
					+ '</li>');
		}
	};
	cEditions.children().click(function() {
		setEdition(this.id);
		refresh();
	});
};
function loadDistributions() {
	distribution = null;
	cTypesDistribution.empty();
	cTypesDistribution.append('<li id="zip">ZIP file</li>');
	cTypesDistribution.append('<li id="exe">Windows installer</li>');
	cTypesDistribution.append('<li id="maven">Maven</li>');
	cTypesDistribution.append('<li id="p2">Eclipse</li>');
	cTypesDistribution.children().click(function() {
		setDistribution(this.id);
		refresh();
	});
};

function fillRightSidebar(classname){
	var id = classname;
	id += " .right-sidebar";
	$(id).empty();
	$(id).append('<div class="newsletter"><div class="newsletter-header"><div></div><div>Subscribe below to receive exciting Restlet news</div></div><div id="email-submit"><input type="email" name="EMAIL" id="newsLetterEmail" placeholder="Enter your email"><button id="newsLetterOkButton" name="subscribe">Notify me</button></div><div class="clearBoth"></div></div><div class="tutorials"><div><a href="/learn/tutorial/"><img src="/images/tutorial-icon.png"/></a></div><div><a href="/learn/tutorial/">Tutorial</a></div></div><div class="user-guide"><div><a href="/learn/guide/"><img src="/images/user-guide-icon.png"/></a></div><div><a href="/learn/guide/">User guide</a></div></div>\n');
}

function refresh() {
	// Look for the current distribution.
	distribution = getDistribution(distributionId);
	$("#" + cQualifiers.attr('id') + '-bt').empty();
	$("#" + cQualifiers.attr('id') + '-bt').append("<strong>" + qualifier.name + "</strong>");
	$("#" + cEditions.attr('id') + '-bt').empty();
	$("#" + cEditions.attr('id') + '-bt').append("<strong>" + edition.longname + "</strong>");
	$("#" + cTypesDistribution.attr('id') + '-bt').empty();
	
	if ("zip" == distributionId) {
		$("#" + cTypesDistribution.attr('id') + '-bt').append("<strong>ZIP file</strong>");
	} else if ("exe" == distributionId) {
		$("#" + cTypesDistribution.attr('id') + '-bt').append("<strong>Windows installer</strong>");
	} else if ("maven" == distributionId) {
		$("#" + cTypesDistribution.attr('id') + '-bt').append("<strong>Maven</strong>");
	} else if ("p2" == distributionId) {
		$("#" + cTypesDistribution.attr('id') + '-bt').append("<strong>Eclipse</strong>");
	}

	if (handleFragment) {
		window.location.hash = "#release=" + qualifier.id +"&edition=" + edition.id + "&distribution=" + distributionId;
	}

	setDownloadButton();
}

/**
 * Initializes the data model, cookies, and selectors.
 * @param sq
 * 		The selector of qualifiers.
 * @param se
 * 		The selector of editions.
 * @param std
 * 		The selector of type of distributions.
 * @param hf
 * 		True indicates that the uri will reflect the selectors's value in the uri fragment.
 * @param rd
 * 		True indicates if the click on the download button redirects to the download page.
 */
function init(sq, se, std, hf, rd) {
	cQualifiers = sq;
	cEditions = se;
	cTypesDistribution = std;
	if (hf) {
		handleFragment = true;
	} else {
		handleFragment = false;
	}
	if (rd) {
		redirectDownload = true;
	} else {
		redirectDownload = false;
	}

	loadQualifiers();
	loadEditions();
	loadDistributions();

	var hash = window.location.hash;
	var itemId = getParameterByName(hash, "release", $.cookie('release'));

	qualifier = getQualifier(itemId);
	if (!qualifier) {
		qualifier = getQualifier(getDefaultQualifier($.cookie('branch')));
	}
	$.cookie('release', qualifier.id, {path : '/'});

	version = getVersion(qualifier.version);

	itemId = getParameterByName(hash, "edition", $.cookie('edition'));
	edition = getEdition(itemId);
	if (!edition) {
		edition = getEdition(getDefaultEdition(version));
	}
	$.cookie('edition', edition.id, {path : '/'});
	
	itemId = getParameterByName(hash, "distribution", $.cookie('distribution'));
	distribution = getDistribution(itemId);
	if (!distribution) {
		distribution = getDistribution(getDefaultDistribution(version, edition));
	}
	if ("file" == distribution.type) {
		distributionId = distribution.fileType;
	} else {
		distributionId = distribution.type;
	}
	$.cookie('distribution', distributionId, {path : '/'});
	
	refresh();
}
