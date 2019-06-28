package jp.spring.boot.algolearn.datasource;

import lombok.Data;

@Data
public class SchemaContextHolder {
    // private static ThreadLocal<SchemaType> contextHolder = new ThreadLocal<SchemaType>();
    public static SchemaType schemaType;

}
