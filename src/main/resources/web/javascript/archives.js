var cBranches;
var cReleases;
var cEditions;
var cTypesDistribution;
var tabDir;
var handleFragment = false;
var redirectDownload = false;

function loadBranches() {
	cBranches.empty();
	for ( var i = 0; b = branches[i]; i++) {
		cBranches.append('<li id="' + b + '">' + b + '</li>');
	}
	;
	cBranches.children().click(function() {
		refreshBranch(this.id);
	});
};

function listDistributions() {
	tabDir.empty();
	for ( var i = 0; d = distributions[i]; i++) {
		if ((distributionId == d.fileType)
				&& (branch == d.version.substring(0, 3))
				&& (version.id == d.version)
				&& (!edition || ("all" == edition.id) || (d.edition == edition.id))) {
			displayDistribution(tabDir, d);
		}
	}
	;
};
function displayDistribution(tab, distribution) {
	var v = getVersion(distribution.version);
	if (v) {
		var str = '<tr><td><a href="/download/current?release=' + v.fullVersionCompact + '&edition=' + distribution.edition  + '&distribution=' + distribution.fileType + '" class="file">';
		// str += '<img alt="File:"';
		// if("exe" == distribution.fileType){
		// str += ' src="/images/executable.png">';
		// } else {
		// str += ' src="/images/zipfile.png">';
		// }
		str += distribution.fileName;
		str += '</a></td>';
		str += '<td>' + distribution.fileSize + '</td>';
		if (v) {
			str += '<td>' + v.published + '</td>';
		} else {
			str += '<td>-</td>';
		}
		str += '</tr>';
	}
	tab.append(str);
}

function refreshBranch(branchId) {
	setBranch(branchId);

	var versionId = null;
	var first = true;
	cReleases.empty();
	for ( var i = 0; v = versions[i]; i++) {
		if (v.minorVersion == branch) {
			cReleases.append('<li id="' + v.id + '">' + v.fullVersionCompact
					+ '</li>');
			if (((version != null) && v.id == version.id) || first) {
				versionId = v.id;
				first = false;
			}
		}
	}
	;
	cReleases.children().click(function() {
		refreshRelease(this.id);
	});
	refreshRelease(versionId);
}

function refreshRelease(releaseId) {
	setVersion(releaseId);

	var editionId = null;
	var first = true;
	cEditions.empty();
	for ( var j = 0; e0 = version.editions[j]; j++) {
		for ( var i = 0; e = editions[i]; i++) {
			if (e.id == e0.id) {
				cEditions.append('<li id="' + e.id + '">' + e.longname
						+ '</li>');
				if ((edition != null && e.id == edition.id) || first) {
					editionId = e.id;
					first = false;
				}
			}
		}
		;
	}
	cEditions.children().click(function() {
		refreshEdition(this.id);
	});
	refreshEdition(editionId);
}

function refreshEdition(editionId) {
	setEdition(editionId);

	cTypesDistribution.empty();
	var distroId = null;
	var first = true;
	for ( var j = 0; d = distributions[j]; j++) {
		if (((d.version == version.id) && (d.edition == edition.id))) {
			if ("zip" == d.fileType) {
				cTypesDistribution.append('<li id="zip">ZIP file</li>');
				if (d.fileType == distributionId || first) {
					distroId = d.fileType;
				}
				first = false;
			} else if ("exe" == d.fileType || first) {
				cTypesDistribution
						.append('<li id="exe">Windows installer</li>');
				if (d.fileType == distributionId || first) {
					distroId = d.fileType;					
				}
				first = false;
			} else if ("maven" == d.fileType || first) {
				cTypesDistribution
						.append('<li id="maven">Maven</li>');
				if (d.fileType == distributionId || first) {
					distroId = d.fileType;					
				}
				first = false;
			} else if ("p2" == d.fileType || first) {
				cTypesDistribution
						.append('<li id="p2">Eclipse</li>');
				if (d.fileType == distributionId || first) {
					distroId = d.fileType;					
				}
				first = false;
			}

		}
	}
	cTypesDistribution.children().click(function() {
		refresh(this.id);
	});

	refresh(distroId);
};

function refresh(distroId) {
	setDistribution(distroId);
	// récupération de la distribution courante.
	distribution = getDistribution(distroId);
	if (!distribution) {
		distribution = getDistribution(getDefaultDistribution(version, edition));
		distributionId = distribution.fileType;
	}

	$("#" + cBranches.attr('id') + '-bt').empty();
	$("#" + cBranches.attr('id') + '-bt').append("<strong>" + branch + "</strong>");
	$("#" + cReleases.attr('id') + '-bt').empty();
	$("#" + cReleases.attr('id') + '-bt').append("<strong>" + version.fullVersionCompact + "</strong>");
	$("#" + cEditions.attr('id') + '-bt').empty();
	$("#" + cEditions.attr('id') + '-bt').append("<strong>" + edition.longname + "</strong>");
	$("#" + cTypesDistribution.attr('id') + '-bt').empty();
	window.location.hash = "#branch=" + branch + "&release=" + version.id + "&edition=" + edition.id + "&distribution=" + distributionId;
	if ("zip" == distributionId) {
		$("#" + cTypesDistribution.attr('id') + '-bt').append("<strong>ZIP file</strong>");
	} else if ("exe" == distributionId) {
		$("#" + cTypesDistribution.attr('id') + '-bt').append("<strong>Windows installer</strong>");
	} else if ("maven" == distributionId) {
		$("#" + cTypesDistribution.attr('id') + '-bt').append("<strong>Maven</strong>");
	} else if ("p2" == distributionId) {
		$("#" + cTypesDistribution.attr('id') + '-bt').append("<strong>Eclipse</strong>");
	}
	setDownloadButton();
	// listDistributions();
}

function fillRightSidebar(classname){
	var id = classname;
	id += " .right-sidebar";
	$(id).empty();
	$(id).append('<div class="newsletter"><div class="newsletter-header"><div></div><div>Subscribe below to receive exciting Restlet news</div></div><div id="email-submit"><input type="email" name="EMAIL" id="newsLetterEmail" placeholder="Enter your email"><button id="newsLetterOkButton" name="subscribe">Notify me</button></div><div class="clearBoth"></div></div><div class="tutorials"><div><a href="/learn/tutorial/"><img src="/images/tutorial-icon.png"/></a></div><div><a href="/learn/tutorial/">Tutorial</a></div></div><div class="user-guide"><div><a href="/learn/guide/"><img src="/images/user-guide-icon.png"/></a></div><div><a href="/learn/guide/">User guide</a></div></div>\n');
}

/**
 * Initializes the data model, cookies, and selectors.
 * 
 * @param sb
 *            The selector of branches.
 * @param sr
 *            The selector of releases.
 * @param se
 *            The selector of editions.
 * @param std
 *            The selector of type of distributions.
 * @param dir
 *            The div where to display the listing.
 */
function init(sb, sr, se, std, dir, hf, rd) {
	cBranches = sb;
	cReleases = sr;
	cEditions = se;
	cTypesDistribution = std;
	tabDir = dir;
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

	var hash = window.location.hash;
	var itemId = getParameterByName(hash, "branch", $.cookie('branch'));

	branch = itemId;
	if (!branch) {
		branch = getDefaultBranch($.cookie('release'));
	}
	$.cookie('branch', branch, {
		path : '/'
	});

	itemId = getParameterByName(hash, "release", $.cookie('release'));
	qualifier = getQualifier(itemId);
	if (!qualifier) {
		$.cookie('release', itemId, {
			path : '/'
		});
		version = getVersion(itemId);
	} else {
		$.cookie('release', qualifier.id, {
			path : '/'
		});
		version = getVersion(qualifier.version);
	}

	itemId = getParameterByName(hash, "edition", $.cookie('edition'));
	edition = getEdition(itemId);
	if (!edition) {
		edition = getEdition(getDefaultEdition(version));
	}
	$.cookie('edition', edition.id, {
		path : '/'
	});

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
	$.cookie('distribution', distributionId, {
		path : '/'
	});

	loadBranches();
	refreshBranch(branch);
}
