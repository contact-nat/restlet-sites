    $(document).ready(function() {
		var crGeneral = new ClientResource("/feeds/general");
		crGeneral.get(function(representation) {
			var jsonRepresentation = new JsonRepresentation(representation);

			var listContent = "";
   			var elements = jsonRepresentation.getObject();
   			if(elements.length && elements.length>0){
	   			listContent = "<h4 class=\"blog\"><a href=\"http://blog.restlet.com/feed/?cat=894539\">Restlet - General&nbsp;<img src=\"/images/icons/16/feed-icon\" alt=\"Syndication feed\" style=\"margin:5px\"/></a></h4>";
	   			listContent += "<ul class=\"blog\">";
	   			for (var i=0; i < elements.length; i++) {
	   				listContent += createFeedElement(elements[i]);
	   			}
	   			listContent += "</ul>";
   			}
   			$("#restletFeedGeneral").html(listContent);
		}, MediaType.APPLICATION_JSON);
		var crReleases = new ClientResource("/feeds/releases");
		crReleases.get(function(representation) {
			var jsonRepresentation = new JsonRepresentation(representation);

			var listContent = "";
   			var elements = jsonRepresentation.getObject();
   			if(elements.length && elements.length>0){
	   			listContent = "<h4 class=\"blog\"><a href=\"http://blog.restlet.com/feed/?cat=894542\">Restlet - Releases&nbsp;<img src=\"/images/icons/16/feed-icon\" alt=\"Syndication feed\" style=\"margin:5px\"/></a></h4>";
	   			listContent += "<ul class=\"blog\">";
	   			for (var i=0; i < elements.length; i++) {
	   				listContent += createFeedElement(elements[i]);
	   			}
	   			listContent += "</ul>";
   			}
   			$("#restletFeedReleases").html(listContent);
		}, MediaType.APPLICATION_JSON);
    });

	createFeedElement = function(element) {
		var result = "<li>";
		result += element.published;
		result += "&nbsp;<a href=\""+element.uri+"\">"+element.title+"</a>";
		result += "</li>";
		return result;
	};