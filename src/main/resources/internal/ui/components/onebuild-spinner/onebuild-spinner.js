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

  template: `
    <v-overlay
      :model-value="httpStore.isLoading"
      class="align-center justify-center">
      <v-progress-circular
        color="primary"
        size="64"
        indeterminate>
      </v-progress-circular>
    </v-overlay>
    
    <v-snackbar
      v-model="httpStore.show"
      multi-line>
      {{ httpStore.message }}
      <template v-slot:actions>
        <v-btn
          color="red"
          variant="text"
          @click="snackbar = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
`
}