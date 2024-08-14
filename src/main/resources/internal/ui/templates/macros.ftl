<#macro generateCssLinks path files={}>
<#if files??>
<#list files as key, value>
  <link href="${path}/${value}" rel="stylesheet">
</#list>
</#if>
</#macro>

<#macro generateJsLinks path files={}>
<#if files??>
<#list files as key, value>
  <script type = "text/javascript" src="${path}/${value}"></script>
</#list>
</#if>
</#macro>

<#macro generateModuleLinks path files={}>
<#if files??>
<#list files as key, value>
  <script type="module" src="${path}/${value}"></script>
</#list>
</#if>
</#macro>

<#macro importFonts>
<link href="/public/css/materialdesignicons.css" rel="stylesheet">
</#macro>