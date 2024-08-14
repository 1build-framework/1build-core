"use strict"

const {ref} = Vue;

export default {
  name: 'Search',

  setup() {
    console.log("Enter: setup()")
    const searchText = ref('');
    const isSearching = ref(false);

    const onSearch = () => {
      searchText.value = searchText.value + 'World';
      isSearching.value = true;
      setTimeout(() => {
        isSearching.value = false;
      }, 2000);
    };

    return { searchText, isSearching, onSearch };
  },

  template: `
  ${html}
  `
}
/*

<style scoped>
  .v-container {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>
*/
