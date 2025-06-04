"use strict"

const { defineStore } = Pinia;
const { reactive, ref } = Vue;

export default defineStore(`${storeName}`, {
  state: () => ({
    domainObjects: ref([]),
    selected: ref([]),
    domainObject: reactive(${domainObject}),
    navigateObject: reactive({})
  }),

  actions: {

    setNavigate(item) {
      Object.assign(this.navigateObject, item);
    },

    resetForm() {
      for (let key in this.domainObject) {
        if (this.domainObject.hasOwnProperty(key)) {
          this.domainObject[key] = null;
        }
      }
    },

    assignValues(source) {
      console.log('Assigning values:', this.domainObject.value, source);
      if(source) {
        for (let key in source) {
          if (source.hasOwnProperty(key) && this.domainObject.hasOwnProperty(key)) {
            this.domainObject[key] = source[key];
          }
        }
      }
    },

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