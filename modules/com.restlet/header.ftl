<#global javascript_files=[] />
<#global stylesheet_files=[] />
<#global sections              = hierarchy.sections />
<#global labels                = labels.labels />
<#if pp.sourceDirectory?contains("error")>
    <#global currentSection = "error" />
<#elseif (pp.sourceDirectory?index_of("/") > -1)>
    <#global currentSection = pp.sourceDirectory?substring(0, pp.sourceDirectory?index_of("/")) />
<#else>
    <#global currentSection = "-" />
</#if>
<#if !(currentSubSection?has_content)>
    <#global currentSubSection = pp.sourceFileName />
    <#assign tab = pp.sourceDirectory?split("/") />
    <#if (tab?size>1) && !(tab[1] == "")>
        <#global currentSubSection = tab[1] />
    <#else>
        <#if (pp.sourceFileName?index_of(".") > -1) >
            <#global currentSubSection = pp.sourceFileName?substring(0, pp.sourceFileName?index_of(".")) />
        <#else>
            <#global currentSubSection = pp.sourceFileName />
        </#if>
    </#if>
</#if>

<#global campaignPopupSection>
<!-- Popup panel -->
    <div class="modalDialog kinlane" id="deployModal">
		<div>
			<div class="close-button"></div>
			<div class="doc-preview"></div>
			<div class="doc-desc">
				<strong class="title">Get 40% off Restlet book</strong>
				<div class="doc-content">
					<div class="text-holder">
						<strong class="subhead"><span id="beginning">Restlet in Action</span> by Jerome Louvel, Thierry Boileau &amp; Thierry Templier.</strong>
						<p>Enter your email below and receive a special promo code to purchase the book or e-book with 40% off! <strong>Code limited to the first 100 participants.</strong></p>
					</div>
				</div>	
				<div class="doc-download">
					<div class="row"><input type="email" id="campaignEmail" placeholder="Email" class="required-email"/>
						<div class="row button">
							<button id="campaignButton" onclick="change()">Get Code</button>
							<script type="text/javascript">
								function change() {
									if (checkEmail("campaignEmail", "campaignButton")) {
										var button = document.getElementById("campaignButton");
										button.style.background='#77cb38';
										button.style.lineHeight='16px';
										button.innerHTML = "Code:\nRESTLET40";
									}
								}
							</script>
						</div>
					</div>
					<span class="txt-info">Your details will never be shared to a 3rd party. We hate spam just as much as you do.</span>
				</div>
				<div class="doc-footer">
					<p></p>
				</div>
				<div class="clearBoth"></div>
			</div>
			<div class="clearBoth"></div>
		</div>
	</div>
</#global>

<#global downloadSection>
<div class="downloadsection">
    <div class='span3 center section'>
        <form class="form-horizontal">
            <div class="control-group">
                <label class="control-label">Release:</label>
                <div class="controls">
                    <div class="btn-group">
                        <button id="cBranch-bt" class="btn no-radius" data-toggle="dropdown"><strong>Stable</strong></button>
                        <button class="btn dropdown-toggle no-radius" data-toggle="dropdown"><strong><span class="caret"></span></strong></button>
                        <ul id="cBranch" class="dropdown-menu" role="menu" aria-labelledby="drop5">
                            <!-- dropdown menu links -->
                        </ul>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Edition:</label>
                <div class="controls">
                    <div class="btn-group">
                        <button id="cEdition-bt" class="btn no-radius" data-toggle="dropdown"><strong>JSE</strong></button>
                        <button class="btn dropdown-toggle no-radius"
                            data-toggle="dropdown">
                            <strong><span class="caret"></span></strong>
                        </button>
                        <ul id="cEdition" class="dropdown-menu">
                            <!-- dropdown menu links -->
                        </ul>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Distribution:</label>
                <div class="controls">
                    <div class="btn-group">
                        <button id="cDistribution-bt" class="btn no-radius" data-toggle="dropdown"><strong>ZIP file</strong></button>
                        <button class="btn dropdown-toggle no-radius" data-toggle="dropdown"><strong><span class="caret"></span></strong></button>
                        <ul id="cDistribution" class="dropdown-menu">
                            <!-- dropdown menu links -->
                        </ul>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <div id="download" class='span3 right section download'>
                            <!-- download link -->
    </div>
    <div class="span3 right section download download-description-list">
      <ul>
        <li>Open Source</li>
        <li>RESTful</li>
        <li>Client &amp; Server</li>
        <li>Scalable</li>
        <li>Secure</li>
      </ul>
    </div>
 	<div class="clearBoth"></div>
</div>
</#global>

<#global archivesSection>
<div class="downloadsection">
    <div class='span3 center section'>
        <form class="form-horizontal">
            <div class="control-group">
                <label class="control-label">Branch:</label>
                <div class="controls">
                    <div class="btn-group">
                        <button id="cBranch-bt" class="btn no-radius" data-toggle="dropdown"><strong>2.1</strong></button>
                        <button class="btn dropdown-toggle no-radius" data-toggle="dropdown"><strong><span class="caret"></span></strong></button>
                        <ul id="cBranch" class="dropdown-menu" role="menu" aria-labelledby="drop5">
                            <!-- dropdown menu links -->
                        </ul>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Release:</label>
                <div class="controls">
                    <div class="btn-group">
                        <button id="cRelease-bt" class="btn no-radius" data-toggle="dropdown"><strong>2.1.7</strong></button>
                        <button class="btn dropdown-toggle no-radius" data-toggle="dropdown"><strong><span class="caret"></span></strong></button>
                        <ul id="cRelease" class="dropdown-menu" role="menu" aria-labelledby="drop5">
                            <!-- dropdown menu links -->
                        </ul>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Edition:</label>
                <div class="controls">
                    <div class="btn-group">
                        <button id="cEdition-bt" class="btn no-radius" data-toggle="dropdown"><strong>Zip</strong></button>
                        <button class="btn dropdown-toggle no-radius"
                            data-toggle="dropdown">
                            <strong><span class="caret"></span></strong>
                        </button>
                        <ul id="cEdition" class="dropdown-menu">
                            <!-- dropdown menu links -->
                        </ul>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Distribution:</label>
                <div class="controls">
                    <div class="btn-group">
                        <button id="cDistribution-bt" class="btn no-radius" data-toggle="dropdown"><strong>ZIP file</strong></button>
                        <button class="btn dropdown-toggle no-radius" data-toggle="dropdown"><strong><span class="caret"></span></strong></button>
                        <ul id="cDistribution" class="dropdown-menu">
                            <!-- dropdown menu links -->
                        </ul>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <div id="download" class='span3 right section download'>
                            <!-- download link -->
    </div>
    <div class="span3 right section download download-description-list">
      <ul>
        <li>Open Source</li>
        <li>RESTful</li>
        <li>Client &amp; Server</li>
        <li>Scalable</li>
        <li>Secure</li>
      </ul>
    </div>
    <div class="clearBoth"></div>
</div>
</#global>

<#global branchSwitch>
<div class='center branchwitch'>
    <form class="form-horizontal">
        <div class="control-group">
            <label class="control-label">Branch:</label>
            <div class="controls">
                <div class="btn-group">
                    <button id="cBranch-bt" class="btn no-radius" data-toggle="dropdown"><strong>Stable</strong></button>
                    <button class="btn dropdown-toggle no-radius" data-toggle="dropdown"><strong><span class="caret"></span></strong></button>
                    <ul id="cBranch" class="dropdown-menu" role="menu" aria-labelledby="drop5">
                        <!-- dropdown menu links -->
                    </ul>
                </div>
            </div>
        </div>
    </form>
</div>
</#global>
<#global editButton>
<div class='center editButton'>
        <div class="control-group">
            <div class="controls">
                    <div class="btn no-radius"><strong><a href="https://github.com/restlet/restlet-sites/edit/master/modules/com.restlet/${pp.sourceFile?replace("html", "md")}" title="Edit this page">Edit</a></strong></div>
            </div>
        </div>
</div>
</#global>

<#macro navigationtitle sourcedirectory filepath version>
<#local key = (sourcedirectory?substring(16) + filepath?substring(0, (filepath?length) -5) + ".md")?replace("/", ".") />
<#if nodes[(version + "."  + key + ".title")]?has_content>${nodes[(version + "."  + key + ".title")]}</#if>
</#macro>

<#macro navigationbar sourcedirectory filepath version>
    <#local key = (sourcedirectory?substring(16) + filepath?substring(0, (filepath?length) -5) + ".md")?replace("/", ".") />
<div class="guide navigation bar">
    <div class="clearBoth"></div>
    <hr />
    <#if nodes[(version + "." + key + ".title")]?has_content>
        <@navigationlink nodes key "prev" "Previous" version/>
        <@navigationlink nodes key "up" "Up" version />
        <@navigationlink nodes key "next" "Next" version />
    </#if>
</div>
</#macro>

<#macro navigationlink map key property label version>
<div class="guide navigation ${property}">
    <#if map[version + "."  + key + "." + property]?has_content>
<#local source=nodes[version + "."  + key + ".url"]>
<#local tab=source?split("/") />
<#local target=nodes[map[version + "."  + key + "." + property] + ".url"]>
<div><a href="<#list tab as segment><#if segment_has_next>../</#if></#list>${target}">${label}</a></div>
<div>${nodes[map[version + "."  + key + "." + property] + ".title"]}</div>
    <#else>
         <#if "up" == property>
<div><a href="/learn/guide/${version}">${label}</a></div>
<div>Table of contents</div>
         <#else>
&nbsp;
         </#if>
    </#if>
</div>
</#macro>

<#macro h3 title="-" anchor="-" spanclass="span6">
    <div class="${spanclass}">
      <#if !(title=="-")><h3><#if !(anchor=="-")><a class="anchor" name="${anchor}"></a></#if>${title}</h3></#if>
      <#nested>
    </div>
</#macro>
