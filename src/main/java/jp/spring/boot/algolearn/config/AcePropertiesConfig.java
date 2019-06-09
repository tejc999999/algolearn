package jp.spring.boot.algolearn.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 設定ファイル読み込み
 * 
 * @author tejc999999
 *
 */
@Configuration
@PropertySource(value = {"classpath:ace.properties"})
public class AcePropertiesConfig {

	/**
	 * 開発エディタテーマ一覧
	 */
	@Value("#{'${ace.development.editor.theme}'.split(',')}")
    private List<String> themeList;

	/**
	 * 開発エディタモード一覧
	 */
	@Value("#{'${ace.development.editor.mode}'.split(',')}")
    private List<String> modeList;

	
	/**
	 * プロパティ（開発エディタのモードとテーマ一覧）を取得する
	 * 
	 * @return プロパティ
	 */
    @Bean
    public AceProperties aceProperties() {
        return new AceProperties(themeList, modeList);
    }
}
