<#if !(title??)>
<#list sections.section as section>
      <#if section.@id == currentSection>
         <#assign title="Restlet Framework - ${section.label?trim}"  />
      </#if>
</#list>
</#if>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
      <title>${title}</title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <meta name="keywords"  content="REST, Java, framework, toolkit, HTTP, GWT, GAE, Android, JSE, JEE, Servlet, NIO" />
      <meta charset="utf-8" /> 
      <#if ("learn"=currentSection!"")>
      	<#if ("guide" == currentSubSection!"")>
      		<meta name="description" content="Restlet Framework Guide - Learn More about Coding Powerful and Scalable Custom Web APIs in Java." />
	  	</#if>
	  	<#if ("tutorial" == currentSubSection!"")>
      		<meta name="description" content="Restlet Framework Tutorial - Learn More about Coding Powerful and Scalable Custom Web APIs in Java." />
	  	</#if>
	  </#if>
	  <#if ("discover"=currentSection!"")>
      	<#if ("features" == currentSubSection!"")>
      		<meta name="description" content="Discover Restlet Framework. Client and Server Consistent API, Powerful Routing and Filtering, Comprehensive Web Security, more." />
	  	</#if>
	  	<#if ("firststeps" == currentSubSection!"")>
      		<meta name="description" content="Discover Restlet Framework - Taste the Simplicity of the Restlet Framework in less than 12 minutes." />
	  	</#if>
	  </#if>
	  <#if ("participate"=currentSection!"")>
      	<#if ("governance" == currentSubSection!"")>
      		<meta name="description" content="Governance Policy for the Restlet Framework." />
	  	</#if>
	  	<#if ("services" == currentSubSection!"")>
      		<meta name="description" content="Restlet Services - Professional Services, Restlet Framework Plans, APISpark Plans, Consulting, Latest References." />
	  	</#if>
	  </#if>
	  <#if ("download"=currentSection!"")>
      	<#if ("legal" == currentSubSection!"")>
      		<meta name="description" content="Restlet Licenses and trademarks - Resltlet is an Open Source Project That is Distributed Under Several Licenses at the Same Time." />
	  	</#if>
	  	<#if ("faq" == currentSubSection!"")>
      		<meta name="description" content="Download Restlet Framework - Frequently Asked Questions." />
	  	</#if>
	  </#if>
	  <#if ("about"=currentSection!"")>
      	<#if ("index" == currentSubSection!"")>
      		<meta name="description" content="RESTFul Web APIs Serving Customers All Over the World." />
	  	</#if>
	  	<#if ("team" == currentSubSection!"")>
      		<meta name="description" content="Restlet Team - Our company is composed of highly experiences engineers pioneers of web APIs, book authors and conference speakers." />
	  	</#if>
	  	<#if ("board" == currentSubSection!"")>
      		<meta name="description" content="Restlet Board - Composed of Highly Experienced Entrepreneurs and Investors, Pioneers of Professional Open Source, Cloud Computing and PaaS, both in Europe and North America." />
	  	</#if>
	  	<#if ("hiring" == currentSubSection!"")>
      		<meta name="description" content="Search Restlet job opening and find out more about Restlet." />
	  	</#if>
	  </#if>	  
      ${metaheader!""}
      <link type="image/gif" href="/images/favicon-restlet-org.gif" rel="icon">
      <link rel="stylesheet" type="text/css"             href='/stylesheets/bootstrap.css' />
      <link rel="stylesheet" type="text/css"             href='/stylesheets/bootstrap-responsive.css' />
      <link rel="stylesheet" type="text/css"             href='/stylesheets/styles.css' />
      <link rel="alternate"  type="application/atom+xml" href="/feeds/summary" title="${labels.summaryRestletBlog?trim}" />
      <link rel="alternate"  type="application/rss+xml"  href="http://blog.restlet.com/feed/?cat=15314" title="${labels.restletBlog?trim}" />
<#list stylesheet_files as stylesheet_file>
      <link rel="stylesheet" type="text/css"            href="${stylesheet_file}" />
</#list>
<#if stylesheet??>
      <style type="text/css">
      ${stylesheet}
      </style>
</#if>
      <script                type="text/javascript"     src="/javascript/jquery-1.9.0.min.js"></script>
      <script                type="text/javascript"     src="/javascript/jquery-cookie.js"></script>
      <script                type="text/javascript"     src="/javascript/common.js"></script>
<#list javascript_files as javascript_file>
      <script                type="text/javascript"     src="${javascript_file}"></script>
</#list>
<#if ("learn"=currentSection!"") && (("javadocs" == currentSubSection!"") || ("guide" == currentSubSection!"") || ("tutorial" == currentSubSection!""))>
      <script                type="text/javascript"     src="/javascript/jsclass-core.js"></script>
      <script                type="text/javascript"     src="/javascript/json-minified.js"></script>
      <script                type="text/javascript"     src="/javascript/restlet-client.js"></script>
      <script                type="text/javascript"     src="/javascript/bootstrap.min.js"></script>
      <script                type="text/javascript"     src="/javascript/data.js"></script>
    <#if ("javadocs" == currentSubSection!"")>
      <script                type="text/javascript"     src="/javascript/javadocs.js"></script>
    <#elseif ("guide" == currentSubSection!"")>
      <script                type="text/javascript"     src="/javascript/userguide.js"></script>
    <#elseif ("tutorial" == currentSubSection!"")>
      <script                type="text/javascript"     src="/javascript/tutorial.js"></script>
    </#if>
</#if>
      <script type="text/javascript">
<#if pp.sourceFile?matches("learn\\/[0-9]\\.[0-9]$") >
        $.cookie('branch', '${pp.sourceFile?substring(6)}', {path: '/' });
<#elseif pp.sourceFile?matches("learn\\/[0-9]\\.[0-9]/.*$") >
        $.cookie('branch', '${pp.sourceFile?substring(6, 9)}', {path: '/' });
</#if>
<#if javascript??>
      ${javascript}
</#if>

<#if ("learn"=currentSection!"") && (("javadocs" == currentSubSection!"") || ("guide" == currentSubSection!"") || ("tutorial" == currentSubSection!""))>
    $(document).ready(function() {
        init($("#cBranch"));
    });
</#if>
      </script>
   </head>
   <body>

   <!--[if IE 8]><div id="IE8"><div id="IE9"><![endif]-->
   <!--[if IE 9]><div id="IE9"><![endif]-->
    <div class='hero ac <#if headerClass?has_content>${headerClass}<#else>smallHeader</#if>'>
      <div class='container'>
<#compress>
  <#if header?has_content>${header}
  <#elseif "error"=currentSection><h3>${errorPageTitle!"Error page"} <#noparse>(${status.code})</#noparse></h3>
  <#else>
    <#list sections.section as section>
      <#if section.@id == currentSection>
       <h3>${section.label?trim} <span id="search">
       		<form method="get" id="searchform" action="http://restlet.com/search-results">
       			<div class="search-div">
					<input class="text" type="text" value name="q" id="q">
					<input class="submit" type="submit" id="searchsubmit" value="OK">
       			</div>
       		</form>
       	</span></h3>
      </#if>
	</#list>
  </#if>
</#compress>
      </div>
    </div>


<#compress>
<#list sections.section as section>
   <#if (section.@id == currentSection) && section.sections.section?has_content>
    <div class="container subsections">
      <ul class="pages">
      <#list section.sections.section as subsection>
         
        <li<#if subsection.@id == currentSubSection> class="active"</#if>><a href="/${section.@id}/${subsection.@id}">${subsection.label?trim}</a></li>
         
      </#list>
      </ul>
    </div>
    <div class="clearBoth"></div>
   </#if>
</#list>
</#compress>
    <div class='container<#if "-"=currentSection> topics</#if> content'>
    <#if ("learn"=currentSection!"") && (("javadocs" == currentSubSection!"") || ("guide" == currentSubSection!"") || ("tutorial" == currentSubSection!""))>${branchSwitch!""}</#if>
		${content}
    </div>
    <div class="content footerWrapper">
        <div class="footer">
        <br>
        <#if (("learn"=currentSection!"") && (("guide" == currentSubSection!"") || ("tutorial" == currentSubSection!"") || ("roadmap" == currentSubSection!"")) || ("discover"=currentSection!"") && (("firststeps" == currentSubSection!"") || ("faq" == currentSubSection!"")) || ("participate"=currentSection!"") && (("index" == currentSubSection!"")))>
        <div id="disqus_thread"></div>
    <script type="text/javascript">
        /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
        var disqus_shortname = 'restletblog'; // required: replace example with your forum shortname

        /* * * DON'T EDIT BELOW THIS LINE * * */
        (function() {
            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
        })();
    </script>
    <noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
    <a href="http://disqus.com" class="dsq-brlink">comments powered by <span class="logo-disqus">Disqus</span></a>
        </div>
        </#if>
    </div>

	<!--[if IE 8]></div></div><![endif]-->
	<!--[if IE 9]></div><![endif]-->
   </body>
</html>
