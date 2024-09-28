"use strict"

const { defineStore } = Pinia;

// Define and export the store
export default defineStore('versionStore', {
  state: () => JSON.parse(`${storeData}`)
});