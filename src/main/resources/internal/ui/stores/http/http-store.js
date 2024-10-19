"use strict"

const { defineStore } = Pinia;

const useHttpStore = defineStore('httpStore', {
  state: () => ({
    loadingCount: 0,
    message: null,
    show: false
  }),

  actions: {
    startLoading() {
      this.loadingCount++;
    },

    stopLoading() {
      if (this.loadingCount > 0) {
        this.loadingCount--;
      }
    },

    setError(message) {
      this.show = true;
      this.message = message;
    },

    setInfo(message) {
      this.show = false;
      this.message = message;
    },

    clearError() {
      this.show = false;
      this.message = null;
    },

    getLoadingCount() {
      return this.loadingCount;
    }
  },

  getters: {
    isLoading() {
      return this.loadingCount > 0;
    }
  }
});
export default useHttpStore;