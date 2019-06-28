package jp.spring.boot.algolearn.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DatabaseInfo {

    private String dataSourceName;

    private String additionalUrl;

    private String additionalUserName;

    private String additionalPassword;

}
