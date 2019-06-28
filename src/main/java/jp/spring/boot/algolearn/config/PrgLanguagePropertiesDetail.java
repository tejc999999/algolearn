package jp.spring.boot.algolearn.config;

import lombok.Data;

/**
 * プログラミング言語設定情報(Programming language setting information)
 * @author tejc999999
 */
@Data
public class PrgLanguagePropertiesDetail {

    /**
     * プログラミング言語名前(Programming language name)
     */
    private String name;

    /**
     * ファイル名(File name)
     */
    private String fileName;

    /**
     * ビルド後ファイル名(File name after build)
     */
    private String buildFileName;

    /**
     * ビルドコマンドパス(build command path)
     */
    private String buildCmdPath;

    /**
     * 実行コマンドパス(execute command path)
     */
    private String executeCmdPath;

    /**
     * 作業フォルダパス(work folder path)
     */
    private String workFolderPath;
}
