"use strict"

import useVersionStore from '/app/stores/version-store.js';

const { computed } = Vue;

export default {
  name: 'version',

  setup() {
    const versionStore = useVersionStore();

    return {
      versionStore
    };
  },

  template: `${html}`
}