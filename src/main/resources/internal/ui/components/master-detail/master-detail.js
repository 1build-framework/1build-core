"use strict";

import httpDatabaseService from '/onebuild/services/http-service';
import useObjectStore from '${objectStorePath}';
import validators from '${validatorsPath}';
import router from '/app/routers/router';

const { reactive, computed, onMounted, ref, watch } = Vue;
export default {
  name: `${componentName}`,

  components: {
  },

  setup() {
    const form = ref(null);
    const listHeading = '${listHeading}';
    const selected = ref([]);
    const store = useObjectStore();
    const { useRoute } = VueRouter;

    const route = useRoute();
    // const parentId = route.params.parentId;

    //Domain Object
    //const domainObject = reactive(${domainObject});

    //Headers
    const headers = ref(${tableHeaders});

    //Save method
    const save = () => {
      form.value.validate().then(result => {
        if(result.valid) {
          httpDatabaseService.save('${basePath}', domainObject).then(obj => {
            if (obj.data?.id) {
              store.update(obj.data);
              load();
              resetForm();
            } else {
              console.error("Failed to save data");
            }
          });
        } else {
          console.error("Form is invalid");
        }
      });
    };

    const editItem = (item) => {
      console.log('Edit item:', item);
      assignValues(domainObject, item);
    };

    const deleteItem = (item) => {
      console.log('Delete item:', item);
      httpDatabaseService.deleteById('${basePath}', item.id).then(() => {
        store.remove(item);
        load();
        resetForm();
      });
    };

    const deleteSelected = () => {
      console.log('Delete selected item:', selected.value);
      httpDatabaseService.delete('${basePath}', selected.value).then(() => {
        selected.value.forEach(item => {
          store.remove(item);
        });
        load();
        resetForm();
      });
    };

    const navigateItem = (name, parameterName, item) => {
      const params = {};
      params[parameterName] = item.id;
      const navigationData = { name: name, params: params };
      console.log('Navigate to:', name, item, navigationData);

      router.push(navigationData);
    };

    const resetForm = () => {
      for (let key in domainObject) {
        if (domainObject.hasOwnProperty(key)) {
          domainObject[key] = null;
        }
      }
      form.value?.reset();
      selected.value = [];
    };

    const load = () => {
      httpDatabaseService.findAll('${basePath}', route.params?.parentId).then(objects => {
        store.setAll(objects.data);
      });
    };

    const assignValues = (target, source) => {
      console.log('Assigning values:', target, source);
      if(source) {
        for (let key in source) {
          if (source.hasOwnProperty(key) && target.hasOwnProperty(key)) {
            target[key] = source[key];
          }
        }
      }
    };

    // Load domainObjects data when component is mounted
    onMounted(() => {
      load();
    });

    /*watch(selected, (newValue, oldValue) => {
      const selectedId = selected.value?.[0] ?? null;
      if(selectedId !== null) {
        const selectedItem = store.domainObjects.value.find(obj => obj.id === selectedId);
        assignValues(domainObject, selectedItem);
      }
    });*/

    return {
      route,
      form,
      domainObject,
      store,
      headers,
      selected,
      listHeading,
      save,
      editItem,
      deleteItem,
      deleteSelected,
      resetForm,
      load,
      navigateItem,
      ...validators
    };
  },

  template: `
    ${html}
  `
};