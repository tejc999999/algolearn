package jp.spring.boot.algolearn.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDataSourceResolver extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        if (SchemaContextHolder.schemaType == null) {
            // デフォルト
            return SchemaType.H2DB.toString();

        } else if(SchemaContextHolder.schemaType == SchemaType.H2DB) {

        	return SchemaType.H2DB.toString();

        } else if(SchemaContextHolder.schemaType == SchemaType.MARIADB) {

        	return SchemaType.MARIADB.toString();

        }else{
        	// デフォルト
        	
            return SchemaType.H2DB.toString();
        }
    }

}