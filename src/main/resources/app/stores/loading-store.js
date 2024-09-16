"use strict"

const { defineStore } = Pinia;

export default defineStore('loading', {
  state: () => ({
    activeRequests: 0,
  }),

  getters: {
    isLoading(state) {
      return state.activeRequests > 0;
    },
  },

  actions: {
    startLoading() {
      this.activeRequests++;
    },

    stopLoading() {
      if (this.activeRequests > 0) {
        this.activeRequests--;
      }
    },
  },
});
