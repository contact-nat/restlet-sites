var campaignEmail = "campaignKinEmail";
var campaignButton = "campaignKinButton";
var deployModal = "deployKinModal";
var emailFieldLocation = "Kin Lane Guide";
var eventName = "Downloaded Kin Lane Guide";
var downloadPopupCookie = "kin-lane-white-paper-v3";
var pdfResources = "http://restlet.files.wordpress.com/2013/12/gigaom-research-a-field-guide-to-web-apis.pdf?utm_source=restlet-site&utm_medium=popup&utm_campaign=Kin%20Lane%20Guide";

$(document).ready(function() {	
	// close the modal popup when clicking on the cross buntton
	$("#" + deployModal + " div.close-button").click(function() {
		$("#" + campaignEmail).val("");
		$("#" + campaignButton).css("background","#efa10f");
		$("#" + campaignButton).css("line-height","30px");
		$("#" + deployModal).hide();
    });	
});

// if the behaviour for the new popup is different, don't edit this function
// create an other one and change the value of the variable mixpanelCall
var mixpanelGeneralCall = function(event) {
	if (checkEmail(campaignEmail, campaignButton)) {
		try {
			mixpanel.track("Shared email", {
				"email": $("#" + campaignEmail).val(),
				"Email field location":emailFieldLocation
			});
			mixpanel.alias($("#" + campaignEmail).val(), mixpanel.get_distinct_id());
			mixpanel.people.set({"$email": $("#" + campaignEmail).val()});
			mixpanel.track(eventName);
		} catch(err) {
			//nothing to do
		}
			
		// close popup
		$("#" + campaignEmail).val("");
		$("#" + deployModal).hide();
		
		// Set a one year cookie dedicated to this campaign. 
		$.cookie(downloadPopupCookie, 'true', {
			expires: 365
		});
		
		// Set a cookie for the popups in resources
		// we don't want to show the download popups one more time.
		$.cookie('fill-email-campaign', 'true', {
			expires: 365,
			path : '/'
		});
		
		// launch pdf download in a new tab
		window.open(pdfResources, "_blank");
	}
};

function loadMavenSnippet() {
	$('#maven-snippet').empty();
	$('#maven-snippet').append('&lt;repositories&gt;\n');
	$('#maven-snippet').append('    &lt;repository&gt;\n');
	$('#maven-snippet').append('        &lt;id&gt;maven-restlet&lt;/id&gt;\n');
	$('#maven-snippet').append('        &lt;name&gt;Restlet repository&lt;/name&gt;\n');
	$('#maven-snippet').append('        &lt;url&gt;http://maven.restlet.com&lt;/url&gt;\n');
	$('#maven-snippet').append('    &lt;/repository&gt;\n');
	$('#maven-snippet').append('&lt;/repositories&gt;\n');
	$('#maven-snippet').append('&lt;properties&gt;\n');
	$('#maven-snippet').append('    &lt;restlet-version&gt;' + version.mavenVersion + '&lt;/restlet-version&gt;\n');
	$('#maven-snippet').append('&lt;/properties&gt;\n');
	$('#maven-snippet').append('&lt;dependencies&gt;\n');
	$('#maven-snippet').append('    &lt;dependency&gt;\n');
	$('#maven-snippet').append('        &lt;groupId&gt;org.restlet.jse&lt;/groupId&gt;\n');
	$('#maven-snippet').append('        &lt;artifactId&gt;org.restlet&lt;/artifactId&gt;\n');
	$('#maven-snippet').append('        &lt;version&gt;&#36;{restlet-version}&lt;/version&gt;\n');
	$('#maven-snippet').append('    &lt;/dependency&gt;\n');
	$('#maven-snippet').append('    &lt;dependency&gt;\n');
	$('#maven-snippet').append('        &lt;groupId&gt;org.restlet.jse&lt;/groupId&gt;\n');
	$('#maven-snippet').append('        &lt;artifactId&gt;org.restlet.ext.jackson&lt;/artifactId&gt;\n');
	$('#maven-snippet').append('        &lt;version&gt;&#36;{restlet-version}&lt;/version&gt;\n');
	$('#maven-snippet').append('    &lt;/dependency&gt;\n');
	$('#maven-snippet').append('&lt;/dependencies&gt;\n');	
}

function setDownloadButton() {
	$('#download').empty();
	if (distribution && "file" == distribution.type) {
		// Update download button
		var urlChangesLog = "/learn/";
		if (qualifier != null && "unstable" == qualifier.id) {
			urlChangesLog += "snapshot";
		} else {
			urlChangesLog += version.minorVersion;
		}
		urlChangesLog += "/changes";

		$('#download').append(
				'<p><button id="rfDownloadButton" class="btn btn-large btn-success" type="button"  onClick="ga(\'send\', \'event\', { eventCategory: \'Download\', eventAction: \'Click\', eventLabel: \'Framework\', eventValue: 0});"><em class="download-icon"></em>Download '
						+ version.fullVersionCompact + '</button></p>');

		$('#download').append('<p><a href="' + urlChangesLog + '">What\'s new?</a></p>');
		$('#download').append('<p>' + version.published + ' release</p>');
		
		// Update the cookie branch with the branch value
		$.cookie('branch', version.minorVersion, {path : '/'});
		
		if ($.cookie('branch') === "1.2") {
			$.cookie('branch', '2.0', {path : '/'});
		}
		
		var release = "";
		if (location.pathname.search("past") != -1) {
			release = version.fullVersionCompact;
		} else {
			release = qualifier.name;
		}

		$('#' + campaignButton).click(
			function(event) {
				mixpanelGeneralCall(event);
			}
		);
		
		if (distribution.fileType == "maven") {
			$('#rfDownloadButton').click(
					function() {
						loadMavenSnippet();

						$('#eclipse_infos').css('display','none');
						$('#maven_infos').css('display','none');
						$('#firststeps_infos').css('display','none');
						
						var hrefCallback = function() {
							$('.left-description').css('display','block');
							$('#maven_infos').css('display','block');
							fillRightSidebar('#maven_infos');

							if ("true" != $.cookie(downloadPopupCookie)) {
								// open campaign popup
								$("#" + deployModal).show();								
							}
						}
						
						try {
							// in case Mixpanel servers don't get back to us in time
							// we use the same timeout value as the one defined in Mixpanel config
							window.setTimeout(hrefCallback, mixpanel.get_config('track_links_timeout'));
							
							// fire the tracking event, if the event is done before the
							// timeout, the previous hrefCallback call is cancelled by
							// the call to document.location.href
							mixpanel.track("Downloaded Restlet Framework", {
								"Version":version.id,
								"Release":release,
								"Edition":edition.middlename,
								"Distribution":distribution.fileType
							}, hrefCallback);
						} catch(err) {
							hrefCallback();
						}
					});
		} else if (distribution.fileType == "p2") {
			$('#rfDownloadButton').click(
					function() {
						loadMavenSnippet();
						$('#eclipse_infos').css('display','none');
						$('#maven_infos').css('display','none');
						$('#firststeps_infos').css('display','none');
						
						var hrefCallback = function() {
							$('.left-description').css('display','block');
							$('#eclipse_infos').css('display','block');
							fillRightSidebar('#eclipse_infos');

							if ("true" != $.cookie(downloadPopupCookie)) {
								// open campaign popup
								$("#" + deployModal).show();								
							}
						}
						
						try {
							// in case Mixpanel servers don't get back to us in time
							// we use the same timeout value as the one defined in Mixpanel config
							window.setTimeout(hrefCallback, mixpanel.get_config('track_links_timeout'));
							
							// fire the tracking event, if the event is done before the
							// timeout, the previous hrefCallback call is cancelled by
							// the call to document.location.href
							mixpanel.track("Downloaded Restlet Framework", {
								"Version":version.id,
								"Release":release,
								"Edition":edition.middlename,
								"Distribution":"eclipse"
							}, hrefCallback);
						} catch(err) {
							hrefCallback();
						}
					});			
		} else if (redirectDownload && (location.pathname.search("past") != -1)) {
			$('#rfDownloadButton').click(
					function() {
						loadMavenSnippet();
						$('#eclipse_infos').css('display','none');
						$('#maven_infos').css('display','none');
						$('.left-description').css('display','block');
						fillRightSidebar('#firststeps_infos');
						$('#firststeps_infos').css('display','block');

						var hrefCallback = function(event) {
							// open campaign popup
							if ("true" != $.cookie(downloadPopupCookie)) {
								// open campaign popup
								$("#" + deployModal).show();								
							}

							// download selected restlet framework file, we delay the download from 500ms
							// in order to all background images are downloaded (safari/chrome bug)
							window.setTimeout(function () {
									document.location.href = "/download/past?distribution=" + distribution.fileType + "&release=" + version.id + "&edition=" + edition.id;
								}, 500);
						}
						
						try {
							// in case Mixpanel servers don't get back to us in time
							// we use the same timeout value as the one defined in Mixpanel config
							var trackTimeout = window.setTimeout(hrefCallback, mixpanel.get_config('track_links_timeout'));
							
							// fire the tracking event, if the event is done before the
							// timeout, we call a window.clearTimeout
							mixpanel.track("Downloaded Restlet Framework", {
								"Version":version.id,
								"Release":release,
								"Edition":edition.middlename,
								"Distribution":distribution.fileType
							}, function() {
								window.clearTimeout(trackTimeout);
								hrefCallback();
							});
						} catch(err) {
							hrefCallback();
						}
					});			
		} else {
			$('#rfDownloadButton').click(
					function() {
						loadMavenSnippet();
						$('#eclipse_infos').css('display','none');
						$('#maven_infos').css('display','none');
						fillRightSidebar('#firststeps_infos');
						$('#firststeps_infos').css('display','block');
						
						var hrefCallback = function(event) {
							// open campaign popup
							if ("true" != $.cookie("restlet-in-action-campaign")) {
								// open campaign popup
								$("#" + deployModal).show();								
							}
							// download selected restlet framework file, we delay the download from 500ms
							// in order to all background images are downloaded (safari/chrome bug)
							window.setTimeout(function () {
									document.location.href = "/download/" + version.minorVersion + "/" + distribution.fileName;
								}, 500);
						}
						
						try {
							// in case Mixpanel servers don't get back to us in time
							// we use the same timeout value as the one defined in Mixpanel config
							var trackTimeout = window.setTimeout(hrefCallback, mixpanel.get_config('track_links_timeout'));
							
							// fire the tracking event, if the event is done before the
							// timeout, we call a window.clearTimeout
							mixpanel.track("Downloaded Restlet Framework", {
								"Version":version.id,
								"Release":release,
								"Edition":edition.middlename,
								"Distribution":distribution.fileType
							}, function() {
								window.clearTimeout(trackTimeout);
								hrefCallback();
							});
						} catch(err) {
							hrefCallback();
						}
					});
		}
	}
}