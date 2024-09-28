"use strict"

import useHttpStore from '/onebuild/stores/http-store.js';

export default {
  name: 'onebuild-spinner',

  setup() {
    const httpStore = useHttpStore();

    return {
      httpStore,
    };
  },

  template: `${html}`
}