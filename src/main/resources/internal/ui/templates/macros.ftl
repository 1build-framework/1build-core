<#macro cssTags path files={}>
<#if files??>
  <!-- CSS Links -->
<#list files as key, value>
  <link href="${path}/${value}" rel="stylesheet">
</#list>
</#if>
</#macro>

<#macro scriptTags path scripts={} modules={}>
  <#if scripts??>
  <!-- JS Links -->
    <#list scripts as key, value>
  <script type="text/javascript" src="${path}/${value}"></script>
    </#list>
  </#if>

  <#if modules??>
  <!-- JS Module Links -->
    <#list modules as key, value>

  <script type="module" src="${path}/${value}"></script>
    </#list>
  </#if>
</#macro>


<#macro materialFonts>
  <!-- Fonts -->
  <link href="/public/css/materialdesignicons.css" rel="stylesheet">
</#macro>

<#macro appScript>
<!-- App tag to attach the application -->
<div id="onebuild-app">
</div>

<!-- Vue wiring and configuration -->
<script type="module">
  import App from '${componentImportPath}';

  const app = Vue.createApp(App);
  const pinia = Pinia.createPinia();
  const vuetify = Vuetify.createVuetify();

  app.use(pinia);
  app.use(vuetify);
  app.mount('#onebuild-app');

</script>
</#macro>