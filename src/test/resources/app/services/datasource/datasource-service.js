"use strict"
class DatasourceService {
  getDatabaseTypes() {
    return ${databaseTypes};
  }
}

// Create and export a singleton instance
const datasourceService = new DatasourceService();
export default datasourceService;
