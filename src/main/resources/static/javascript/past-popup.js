$(document).ready(function() {
	
	$("#videoPopup div.video-close-button").click(function() {
		$("#videoPopup").hide();
	});

	// hide all the popups
	$(".kinlane div.close-button").click(function() {
		$("#deployVideoModal").hide();
		$("#deployUserGuideModal").hide();
		$("#deployKinModal").hide();
		$("#deployStickersModal").hide();
	});
    
    $('.btn-download-resources').click(function(event) {
		//if the button clicked is the video button
		if(event.currentTarget.classList.contains('watch-video-btn')) {
			// if the user haven't fill a popup before
			if ($.cookie('fill-email-campaign') != 'true') {
				// show the popup
				$("#deployVideoModal").show();
			} else {
				// display the document
				printDoc("video");
			}
		} else if(event.currentTarget.classList.contains('userguidelink')) {
			if ($.cookie('fill-email-campaign') != 'true') {
				$("#deployUserGuideModal").show();
			} else {
				printDoc("guide");
			}
		} else if(event.currentTarget.classList.contains('kinlaneguidelink')) {
			if ($.cookie('fill-email-campaign') != 'true') {
				$("#deployKinModal").show();
			} else {
				printDoc("guide");
			}
		} else if(event.currentTarget.classList.contains('paasguidelink')) {
			//$("#deployPaasModal").show();
			printDoc("paas");
		} else if(event.currentTarget.classList.contains('roadguidelink')) {
			//$("#deployRoadGuideModal").show();
			printDoc("road");
		} else if(event.currentTarget.classList.contains('stickerscampaignlink')) {
			// display the popup everytime because it is the actual resource
			$("#deployStickersModal").show();
		}
		
		// we stop the event propagation
		if (event.preventDefault) {
		  event.preventDefault();
		}
		event.returnValue = false;								
	});
    
    $('#campaignKinButton').click(function() {
		generalModal('kin');
	});
    
    $('#campaignVideoButton').click(function() {
		generalModal('video');
	});
    
    $('#campaignGuideButton').click(function() {
		generalModal('guide');
	});
    
    $('#campaignStickersButton').click(function() {
		stickersModal();
	});
    
	function generalModal(modalNumber){
		var emailField = "";
		var mixEvent = "";
		var modName = "";
		var button = "";
		var email = "";

		if (modalNumber == 'video') {
			// set mixpanel informations for the video
			emailField = "3min Demo Video";
			mixEvent = "Watched 3min Demo Video";
			modName = "#deployVideoModal";
			button = "campaignVideoButton";
			email = "campaignVideoEmail";
		} else if (modalNumber == 'guide') {
			emailField = "User Guide PDF";
			mixEvent = "Downloaded UserGuide PDF";
			modName = "#deployUserGuideModal";
			button = "campaignGuideButton";
			email = "campaignGuideEmail";
		} else if (modalNumber == 'kin') {
			emailField = "Kin Lane Guide";
			mixEvent = "Downloaded Kin Lane Guide";
			modName = "#deployKinModal";
			button = "campaignKinButton";
			email = "campaignKinEmail";
		}
		if (checkEmail(email, button)) {
			try {
				mixpanel.track("Shared email", {
					"email": $("#" + email).val(),
					"Email field location": emailField
				});
				mixpanel.alias($("#" + email).val(), mixpanel.get_distinct_id());
				mixpanel.people.set({"$email": $("#" + email).val()});
				mixpanel.track(mixEvent);
			} catch(err) {
				//nothing to do
			}
				
			// close popup
			$("#" + email).val("");
			$(modName).hide();
			
			// Set a one year cookie dedicated to all campaigns (unless stickers). 
			$.cookie('fill-email-campaign', 'true', {
				expires: 365
			});

			// call the function to open document
			printDoc(modalNumber);
		}
	}
	
	function printDoc(docInfo){
		var docLocation = "";
		if (docInfo == "video") {
			window.setTimeout($("#videoPopup").show(),1000);
		} else {
			if (docInfo == "guide") {
				docLocation = "http://restlet.com/learn/archives/misc/2.2/rf-user-guide-2-2.pdf";
			} else if (docInfo == "kin") {
				docLocation = "http://restlet.files.wordpress.com/2013/12/gigaom-research-a-field-guide-to-web-apis.pdf?utm_source=restlet-site&utm_medium=popup&utm_campaign=Kin%20Lane%20Guide";
			} else if (docInfo == "paas") {
				docLocation = "http://restlet.files.wordpress.com/2014/05/gigaom-research-paas-market-moves-beyond-deployment-and-scaling.pdf?utm_source=restlet-site&utm_medium=resources&utm_campaign=David%20Linthicum%20ReportRP";
			} else if (docInfo == "road") {
				docLocation = "http://blog.restlet.com/wp-content/uploads/2014/06/ROAD-Designing-a-RESTful-web-API1.pdf?utm_source=restlet-site&utm_medium=resources&utm_campaign=ROAD%20GuideRP";
			}
			window.open(docLocation, "_blank");
		}
	}
	
	function stickersModal() {
		if (checkEmail("campaignStickersEmail", "campaignButton") 
				&& checkFieldEmpty("campaignName", "campaignButton") 
				&& checkFieldEmpty("campaignAddress", "campaignButton")) {
			
			try {
				mixpanel.track("Downloaded Resource", {
					"Resource Name":"Restlet Stickers",
					"Resource Location":"Resources page",
					"Email": $("#campaignStickersEmail").val(),
					"Name": $("#campaignName").val(),
					"Mailing Address": $("#campaignAddress").val()
				});
			} catch(err) {
				//nothing to do
			}
			
			// close popup
			$("#campaignName").val("");
			$("#campaignStickersEmail").val("");
			$("#campaignAddress").val("");
			$("#deployStickersModal").hide();
		}
	}
	
});
