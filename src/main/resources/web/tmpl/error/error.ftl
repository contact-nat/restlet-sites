<#global content>
   <h5>Description</h5>

<#noparse>
<div style="text-align: left">
<#if status.description??>
	${status.description}
<#else>
    No description available for this result status.
</#if>
</div>
</#noparse>

   <h5>Technical details</h5>
<div style="text-align: left">
<#noparse>
<p style="text-align: left">You can get technical details <a href="${status.uri!""}">here</a>.<br>
<#if contactEmail??>
	For further assistance, you can contact the <a href="mailto:${contactEmail}">administrator</a>.<br>
</#if>
</p>
<#if homeUri??>
<p style="text-align: left">
	Please continue your visit at our <a href="${homeUri}">home page</a>.<br>
</p>
</#if>
</#noparse>
</div>
</#global>
