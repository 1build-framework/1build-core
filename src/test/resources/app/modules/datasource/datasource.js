"use strict"

const { ref } = Vue;

import datasourceService from "/app/services/datasource/datasource-service";
export default {

  name: 'Datasource',

  setup() {
    const databaseType = ref('');
    const jdbcUrl = ref('');
    const username = ref('');
    const password = ref('');


    const databaseTypes = datasourceService.getDatabaseTypes();

    databaseTypes.forEach((databaseType) => {
      console.log("databaseType loaded from service ", databaseType);
    });

    console.log("datasourceTypes loaded from service ", databaseTypes);

    return {
      databaseType,
      jdbcUrl,
      username,
      password,
      databaseTypes
    };
  },

  template: `${html}`
}