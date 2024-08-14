<#import "macros.ftl" as macros>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Hello 1build (Spring Boot + Vue + Vuetify)</title>
  <!--link rel="icon" type="image/x-icon" href="favicon.ico"-->

  <!-- Fonts -->
  <@macros.importFonts/>

  <!-- Configured CSS -->
  <@macros.generateCssLinks path=cssConfig.path files=cssConfig.files />
</head>

<body>

<@macros.generateJsLinks path=jsConfig.path files=jsConfig.javascript />
<@macros.generateModuleLinks path=jsConfig.path files=jsConfig.modules />

<div id="app">
</div>

<script type="module">
  import App from '${componentImportPath}';

  window.app = Vue.createApp(App);
  window.vuetify= Vuetify.createVuetify();
  window.app.use(vuetify);
  window.app.mount('#app');

</script>

</body>
</html>