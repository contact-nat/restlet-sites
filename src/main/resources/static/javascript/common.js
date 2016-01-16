// integration of mixpanel : staging env
(function(e,b){if(!b.__SV){var a,f,i,g;window.mixpanel=b;b._i=[];b.init=function(a,e,d){function f(b,h){var a=h.split(".");2==a.length&&(b=b[a[0]],h=a[1]);b[h]=function(){b.push([h].concat(Array.prototype.slice.call(arguments,0)))}}var c=b;"undefined"!==typeof d?c=b[d]=[]:d="mixpanel";c.people=c.people||[];c.toString=function(b){var a="mixpanel";"mixpanel"!==d&&(a+="."+d);b||(a+=" (stub)");return a};c.people.toString=function(){return c.toString(1)+".people (stub)"};i="disable track track_pageview track_links track_forms register register_once alias unregister identify name_tag set_config people.set people.set_once people.increment people.append people.track_charge people.clear_charges people.delete_user".split(" ");
for(g=0;g<i.length;g++)f(c,i[g]);b._i.push([a,e,d])};b.__SV=1.2;a=e.createElement("script");a.type="text/javascript";a.async=!0;a.src=("https:"===e.location.protocol?"https:":"http:")+'//cdn.mxpnl.com/libs/mixpanel-2.2.min.js';f=e.getElementsByTagName("script")[0];f.parentNode.insertBefore(a,f)}})(document,window.mixpanel||[]);
mixpanel.init("${ant["mixpanel.token"]!""}",{debug:true});

// integration of google analytics
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	  ga('create', 'UA-32616835-2', 'restlet.com');
	  ga('send', 'pageview');
	  
// integration of twitter
!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");

// donne la version du navigateur
var doc = document.documentElement;
doc.setAttribute('data-useragent', navigator.userAgent);

/**
 * Check the email field, return true if everything is fine, false otherwise.
 * @param field 
 * 		The identifier of the email input field.
 * @param button
 * 		The identifier of the email button.
 * @returns {Boolean}
 */
function checkEmail(field, button) {
    var emailField = $("#" + field);
    var emailButton = $("#" + button);
    var email = emailField.val();
    var re = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    var emailRegexp = new RegExp(re);
    
    if (email != "") {
          if (emailRegexp.test(email)) {
        	  emailButton.removeAttr("disabled");
        	  emailField.removeClass("error");
        	  return true;
          } else {
        	  emailField.addClass("error");
        	  // emailButton.attr("disabled", true);
          }
    } else {
    	emailField.addClass("error");
    	//emailButton.attr("disabled", true);
    }
	  return false;        	  
} 

function checkFieldEmpty(field, button) {
    var myField = $("#" + field);
    var myButton = $("#" + button);
        
    if (myField.val().length && myField.val() !== myField.prop('defaultValue')) {
		myField.removeAttr("disabled");
		myField.removeClass("error");
		return true;
    } else {
    	myField.addClass("error");
    	//emailButton.attr("disabled", true);
    }
	return false;        	  
}

function getMixpanelIdFromCookie() {
	var mp_cookie = $.cookie(document.cookie.match("mp_.*_mixpanel")[0]);
	if (mp_cookie === undefined) {
		return undefined;
	} else {
		return JSON.parse(mp_cookie).distinct_id;
	}
}

//javascript function to get the mpi parameters containing the mixpanel distinct_id
function getParameterByName(query, name, defaultValue) {
	var result = defaultValue;
	if (query) {
		name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
		var regexS = "[#\\?&]?" + name + "=([^&#]*)";
		var regex = new RegExp(regexS);
		var results = regex.exec(query);
		if (results != null) {
			result = decodeURIComponent(results[1].replace(/\+/g, " "));
		}
	}
	return result;
}

function hasMixpanelCookie() {
	return document.cookie.match("mp_.*_mixpanel");
}

$(document).ready(function() {
  	if (!hasMixpanelCookie()) {
	  	var mpi = getParameterByName(window.location.search, "mpi", null);
	  	
		if (mpi) {
			try {
				mixpanel.identify(mpi);
			} catch(err) {
				//nothing to do
			}
		}
	}

	$("#footerNewsLetterOkButton").click(
	    function(event) {
	        if (checkEmail("footerNewsLetterEmail","footerNewsLetterOkButton")) {
	            try {
	            	callback = function() {
		                $("#footerNewsLetterEmail").val("");
		                $("#footerNewsLetterEmail").attr("disabled", true);
		                $("#footerNewsLetterOkButton").html("&#10003;");
		                $("#footerNewsLetterOkButton").css("font-size","24px");
		                $("#footerNewsLetterOkButton").attr("disabled", true);
		            }
		        	mixpanel.track("Shared email", {
		                "email": $("#footerNewsLetterEmail").val(),
		                "Email field location":"RF footer: newsletter sign up field",
		                "Product":"Restlet Framework"
		            }, callback());
	            } catch (err) {
	            	callback();
	            }
	        }
	    }
	);
	try {
		mixpanel.track_links(".discoverlink", "Clicked on Discover link");
	    mixpanel.track_links(".downloadlink", "Clicked on Download link");
	    mixpanel.track_links(".learnlink", "Clicked on Learn link");
	    mixpanel.track_links(".participatelink", "Clicked on Participate link");
	} catch(err) {
		//nothing to do
	}
	    
    $(".apisparklink").click(function() {
    	this.href = this.href.concat("?mpi=").concat(getMixpanelIdFromCookie());
    });
});