package jp.spring.boot.algolearn.config;

import java.util.List;
import lombok.Data;

/**
 * ACE関連プロパティファイル設定値
 * 
 * @author tejc999999
 *
 */
@Data
public class AceProperties {

	/**
	 * 開発エディタテーマ一覧
	 */
    private List<String> themeList;

	/**
	 * 開発エディタモード一覧
	 */
    private List<String> modeList;

    /**
     * コンストラクタ
     * 
     * @param テーマ一覧
     * @param モード一覧
     */
    public AceProperties(List<String> themeList, List<String> modeList) {
    	this.themeList = themeList;
    	this.modeList = modeList;
    }
}
