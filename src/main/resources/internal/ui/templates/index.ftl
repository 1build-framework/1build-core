<#import "macros.ftl" as macros>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Hello 1build (Spring Boot + Vue + Vuetify)</title>
  <!--link rel="icon" type="image/x-icon" href="favicon.ico"-->

  <@macros.materialFonts/>

  <@macros.cssTags path=cssConfig.path files=cssConfig.files />
</head>

<body>

<@macros.scriptTags path=jsConfig.path scripts=jsConfig.javascript modules=jsConfig.modules/>

<@macros.appScript/>

</body>
</html>
