"use strict";

import httpDatabaseService from '/onebuild/services/http-service';
import useObjectStore from '${objectStorePath}';
import validators from '/app/commons/validators';
import router from '/app/routers/router';

const { onMounted, ref } = Vue;
export default {
  name: `${componentName}`,

  components: {
  },

  setup() {
    const form = ref(null);
    const listHeading = '${listHeading}';
    const store = useObjectStore();
    const { useRoute } = VueRouter;

    const route = useRoute();

    //Headers
    const headers = ref(${tableHeaders});

    //Save method
    const save = () => {
      form.value.validate().then(result => {
        if(result.valid) {
          httpDatabaseService.save('${basePath}', store.domainObject).then(obj => {
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
      store.assignValues(item);
    };

    const deleteItem = (item) => {
      console.log('Delete item:', item);
      httpDatabaseService.delete('${basePath}', [item.id]).then(() => {
        store.remove(item);
        load();
        resetForm();
      });
    };

    const deleteSelected = () => {
      console.log('Delete selected item:', store.selected);
      httpDatabaseService.delete('${basePath}', store.selected).then(() => {
        store.selected.forEach(item => {
          store.remove(item);
        });
        load();
        resetForm();
      });
    };

    const navigateItem = (name, parentIdName, item) => {
      store.setNavigate(item);
      const query = {
        "idName": parentIdName,
        "idValue": item.id
      };

      const navigationData = { "name": name, "query": query };
      console.log('Navigate to:', name, navigationData);
      router.push(navigationData);
    };

    const resetForm = () => {
      store.resetForm();
      form.value?.reset();
      store.selected = [];
    };

    const load = () => {
      console.log('Load data', route.query);
      const parameters = {};
      if(route.query && route.query?.idName && route.query?.idValue) {
        parameters[route.query.idName] = +route.query.idValue;
      }
      httpDatabaseService.find('${basePath}', parameters).then(objects => {
        store.setAll(objects.data);
      });
    };

    onMounted(() => {
      load();
    });

    return {
      route,
      form,
      store,
      headers,
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