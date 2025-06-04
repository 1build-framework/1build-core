"use strict"

import { httpClient } from '/onebuild/services/http-client';
import useHttpStore from '/onebuild/stores/http-store';

class HttpDatabaseService {
  defaultMessages = [
    { name: "findById", message: "Data not found" },
    { name: "findAll", message: "Data not found" },
    { name: "save", message: "Data saved successfully" },
    { name: "updateById", message: "Data updated successfully" },
    { name: "deleteById", message: "Data deleted successfully" }
  ];

  constructor() {
    if (!HttpDatabaseService.instance) {
      HttpDatabaseService.instance = this;
    }
    return HttpDatabaseService.instance;
  }

  find(path, parameters) {
    console.log("Finding records with parameters:", path, parameters);
    return httpClient.get(path, {
      params: parameters
    });
  }

  save(path, record) {
    if (record.id) {
      return httpClient.put(path + '/' + record.id, record);
    } else {
      return httpClient.post(path, record);
    }
  }

  deleteById(path, id) {
    if(id) {
      return httpClient.delete(path + '/' + id);
    }
  }

  delete(path, ids) {
    if(ids) {
      return httpClient.delete(path, {
        headers: {
          'X-DELETE-IDS': ids.join(',')
        }
      });
    }
  }

  async processResponse(operation, response) {
    const httpStore = useHttpStore(); // Assuming httpStore is globally accessible

    try {
      let { status, data } = response;
      console.log("Response status:", status);
      if (status >= 200 && status < 300) {
        if (data) {
          if(Array.isArray(data) && data.length === 0) {
            console.log("Empty results");
            httpStore.setInfo(this.defaultMessages.find(m => m.name === operation).message);
          }
          return data;
        } else {
          httpStore.setInfo(this.defaultMessages.find(m => m.name === operation).message);
          return null;
        }
      } else if (status === 401 || status === 403) {
        // Authentication/Authorization issues
        httpStore.setError("Authentication error. Please login.");
        console.log("Authentication error:", response);
      } else if (status === 400 || status === 500) {
        if(response && response.response) {
          data = response.response.data;
          // Error responses
          if (data && data.message) {
            httpStore.setError(data.message);
            if (data.details) {
              console.error("Error details:", data.details);
            }
          } else {
            httpStore.setError("An unknown error occurred.");
          }
        } else {
          httpStore.setError("No error response was found.");
        }
      }
    } catch (error) {
      httpStore.setError("A client error occurred while processing the response.");
    }
  }

}

const httpDatabaseService = new HttpDatabaseService();
Object.freeze(httpDatabaseService);
export default httpDatabaseService;
