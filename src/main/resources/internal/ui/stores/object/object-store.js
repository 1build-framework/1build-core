"use strict"

const { defineStore } = Pinia;
const { ref } = Vue;

export default defineStore(`${storeName}`, {
  state: () => ({
    domainObjects: ref([]),
    domainObject: reactive(${domainObject});
  }),

  actions: {
    setAll(domainObjects) {
      this.domainObjects.value = domainObjects;
    },

    update(updatedObject) {
      console.log("Total Objects ", this.domainObjects.value.length);
      const index = this.domainObjects.value.findIndex(obj => obj.id === updatedObject.id);
      if (index !== -1) {
        this.domainObjects.value[index] = { ...this.domainObjects.value[index], ...updatedObject };
      } else {
        this.domainObjects.value.push(updatedObject);
      }
    },

    remove(removedObject) {
      const index = this.domainObjects.findIndex(obj => obj.id === removedObject.id);
      if (index !== -1) {
        this.domainObjects.splice(index, 1);
      }
    },

    clearAll() {
      this.domainObjects.value.length = 0;
    },

    getters: {
      getAll() {
        return this.domainObjects.value;
      }
    }
  },
});