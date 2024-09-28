
<#macro cssTags(cssResources)>
  <#list cssResources as resource>
<link href="${resource}" rel="stylesheet">
  </#list>
</#macro>

<#macro scriptTags(jsResources)>
  <#list jsResources as resource>
<script type="text/javascript" src="${resource}"></script>
  </#list>
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
  import useHttpStore from '/onebuild/stores/http-store.js';
  import onebuildSpinner from '/onebuild/components/onebuild-spinner';

  import App from '${componentImportPath}';

  //const httpStore = useHttpStore();

  const app = Vue.createApp(App);
  const pinia = Pinia.createPinia();
  const vuetify = Vuetify.createVuetify();

  app.use(pinia);
  app.use(vuetify);
  app.mount('#onebuild-app');
</script>
</#macro>