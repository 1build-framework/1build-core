"use strict"

import useHttpStore from '/onebuild/stores/http-store.js';



export const httpClient = axios.create({
  baseURL: `/`,
  timeout: 120000
});


httpClient.interceptors.request.use(request => {
  try {
    const httpStore = useHttpStore();
    httpStore.startLoading();
    console.log('Starting Request with loading ', httpStore.getLoadingCount());
  } catch(e) {
    console.error('Error starting loading:', e);
  }
  return request;
}, error => {
  try {
    const httpStore = useHttpStore();
    httpStore.stopLoading();
  } catch (e) {
    console.error('Error stopping loading:', e);
  }
  return Promise.reject(error);
});

httpClient.interceptors.response.use(response => {
  try {
  const httpStore = useHttpStore();
  httpStore.stopLoading();
  httpStore.clearError();
  } catch (e) {
    console.error('Error stopping loading:', e);
  }
  return response;
}, error => {
  try {
    const httpStore = useHttpStore();
    httpStore.stopLoading();
    httpStore.setError('An error occurred' || error.message);
  } catch (e) {
    console.error('Error stopping loading:', e);
  }
  return Promise.reject(error);
});