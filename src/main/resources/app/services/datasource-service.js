"use strict"
class DatasourceService {

  /**
   * @returns {{id: string, value: string}[]}
   */
  getDatabaseTypes() {
    return ${databaseTypes};
  }
}

// Create and export a singleton instance
const datasourceService = new DatasourceService();
export default datasourceService;
