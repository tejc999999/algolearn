package jp.spring.boot.algolearn.code;

/**
 * プログラム確認コード用インターフェース.
 * @author tejc999999
 */
public interface CheckCode {
    
    public static final String LINE_FEED_CODE = System.getProperty("line.separator");
    
    /**
     * 確認用コードを取得する.
     * @return コード前部
     */
    public abstract String getCheckCode();

    /**
     * 課題用ダミーコードを取得する.
     * @param codeMethod メソッド名
     * @param codeReturn 戻り値
     * @return コード前部
     */
    public abstract String getDummyCode(String codeMethod, String codeReturn);
}
