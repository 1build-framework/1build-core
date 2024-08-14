"use strict"

const { ref } = Vue;

export default {
  name: 'App',

  setup() {
    const drawer = ref(true);
    return { drawer };
  },

  template: `${html}`
}