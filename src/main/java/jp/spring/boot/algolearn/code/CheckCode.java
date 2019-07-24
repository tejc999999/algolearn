package jp.spring.boot.algolearn.code;

/**
 * プログラム確認コード用インターフェース.
 * @author tejc999999
 */
public interface CheckCode {
    
    public static final String LINE_FEED_CODE = System.getProperty("line.separator");
    
    /**
     * コード前部を取得する.
     * @return コード前部
     */
    public abstract String getFrontCode();

    /**
     * コード中部を取得する.
     * @return コード中部
     */
    public abstract String getMiddleCode();

    /**
     * コード後部を取得する.
     * @return コード後部
     */
    public abstract String getBackCode();
}
