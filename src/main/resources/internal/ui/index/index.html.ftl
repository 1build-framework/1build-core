<#import "macros.ftl" as macros>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Hello 1build (Spring Boot + Vue + Vuetify)</title>
  <!--link rel="icon" type="image/x-icon" href="favicon.ico"-->

  <@macros.materialFonts/>
  <@macros.cssTags cssResources=cssResources />
</head>

<body>

<@macros.scriptTags jsResources=jsResources/>

<@macros.appScript/>

</body>
</html>