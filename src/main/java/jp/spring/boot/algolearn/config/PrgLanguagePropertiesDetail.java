package jp.spring.boot.algolearn.config;

import lombok.Data;

@Data
public class PrgLanguagePropertiesDetail {

	/**
	 * 名前
	 */
    private String name;

    /**
     * ファイル名
     */
    private String fileName;

    /**
     * ビルド後ファイル名
     */
    private String buildFileName;

	/**
	 * ビルドコマンドパス
	 */
    private String buildCmdPath;

	/**
	 * 実行コマンドパス
	 */
    private String executeCmdPath;

	/**
	 * 作業フォルダパス
	 */
    private String workFolderPath;
}
