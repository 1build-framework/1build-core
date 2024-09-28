"use strict"

const { defineStore } = Pinia;

const useHttpStore = defineStore('httpStore', {
  state: () => ({
    loadingCount: 0,
    errorMessage: ''
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
      this.errorMessage = message;
    },

    clearError() {
      this.errorMessage = '';
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