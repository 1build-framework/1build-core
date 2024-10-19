
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
  import router from '/app/routers/router';

  const App = {
    name: 'App',

    setup() {
      const Main = Vue.defineAsyncComponent(() => import('${componentImportPath}'));
      const OneBuildSpinner = Vue.defineAsyncComponent(() => import('/onebuild/components/onebuild-spinner'));

      return () => [
        Vue.h(Main),
        Vue.h(OneBuildSpinner)
      ];
    }
  };

  const app = Vue.createApp(App);
  const pinia = Pinia.createPinia();
  const vuetify = Vuetify.createVuetify();

  app.use(pinia);
  app.use(vuetify);
  app.use(router);
  app.mount('#onebuild-app');
</script>
</#macro>