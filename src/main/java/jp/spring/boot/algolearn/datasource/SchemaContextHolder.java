package jp.spring.boot.algolearn.datasource;

import org.springframework.util.Assert;

import lombok.Data;

@Data
public class SchemaContextHolder {
//    private static ThreadLocal<SchemaType> contextHolder = new ThreadLocal<SchemaType>();
    public static SchemaType schemaType;

}