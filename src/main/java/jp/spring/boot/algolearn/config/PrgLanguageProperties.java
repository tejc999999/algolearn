package jp.spring.boot.algolearn.config;

import java.util.Map;

import lombok.Data;

/**
 * サーバー設定関連プロパティファイル設定値(Server settings related property file settings)
 * @author tejc999999
 */
@Data
public class PrgLanguageProperties {

    private Map<String, PrgLanguagePropertiesDetail> map;

    /**
     * コンストラクタ(Constructor)
     * @param プログラミング言語設定マップ(Programming language setting map)
     */
    public PrgLanguageProperties(Map<String, PrgLanguagePropertiesDetail> map) {
        this.map = map;
    }
}
