package jp.spring.boot.algolearn.config;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * サーバー設定関連プロパティファイル設定値
 * 
 * @author tejc999999
 *
 */
@Data
public class PrgLanguageProperties {
	
	private Map<String, PrgLanguagePropertiesDetail> map;

    /**
     * コンストラクタ
     * 
     * @param プログラミング言語設定マップ
     */
    public PrgLanguageProperties(Map<String, PrgLanguagePropertiesDetail> map) {
    	this.map = map;
    }
}
