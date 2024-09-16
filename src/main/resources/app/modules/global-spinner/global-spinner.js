"use strict"

import useLoadingStore from '/app/stores/loading-store.js';

export default {
  name: 'global-spinner',

  setup() {
    const loadingStore = useLoadingStore();

    return {
      loadingStore,
    };
  }
};