"use strict"

import { httpClient } from '/onebuild/services/http-client.js';

class HttpDatabaseService {
  constructor() {
    if (!HttpDatabaseService.instance) {
      HttpDatabaseService.instance = this;
    }
    return HttpDatabaseService.instance;
  }

  async findById(path, id) {
    try {
      const response = await httpClient.get(path, {
        params: { id }
      });
      return response.data;
    } catch (error) {
      console.error('Error finding record by ID:', error);
      throw error;
    }
  }

  async findAll(path) {
    try {
      const response = await httpClient.get(path);
      return response.data;
    } catch (error) {
      console.error('Error finding all records:', error);
      throw error;
    }
  }

  async save(path, record) {
    try {
      const response = await httpClient.post(path, record);
      return response.data;
    } catch (error) {
      console.error('Error saving record:', error);
      throw error;
    }
  }

  async updateById(path, id, record) {
    try {
      const response = await httpClient.put(path + '/' + id, record);
      return response.data;
    } catch (error) {
      console.error('Error updating record by ID:', error);
      throw error;
    }
  }

  async deleteById(path, id) {
    try {
      const response = await httpClient.delete(path + '/' + id);
      return response.data;
    } catch (error) {
      console.error('Error deleting record by ID:', error);
      throw error;
    }
  }
}

const httpDatabaseService = new HttpDatabaseService();
Object.freeze(httpDatabaseService);
export default httpDatabaseService;
