package jp.spring.boot.algolearn.code;

import lombok.Data;

/**
 * コマンド実行結果.
 * @author tejc999999
 */
@Data
public class ExecuteResult {

    /**
     * コマンド実行結果コード：成功.
     */
    public static int RETURN_CODE_SUCCESS = 0;

    /**
     * コマンド実行結果コード：失敗.
     */
    public static int RETURN_CODE_ERROR = -1;

    /**
     * コマンド実行結果コード.
     */
    private int returnCode;

    /**
     * 標準出力文字列.
     */
    private String outputString;
    
    /**
     * 標準エラー出力文字列.
     */
    private String errorOutputString;
}
