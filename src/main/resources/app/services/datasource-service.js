"use strict"
class DatasourceService {
  getDatabaseTypes() {
    return [
      { id: 'PostgreSQL', value: 'PostgreSQL' },
      { id: 'Oracle', value: 'Oracle' },
      { id: 'MS-SQL-Server', value: 'MS SQL Server' },
      { id: 'My-SQL', value: 'My SQL' }
    ];
  }
}

// Create and export a singleton instance
const datasourceService = new DatasourceService();
export default datasourceService;
