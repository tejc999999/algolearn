package jp.spring.boot.algolearn.code;

/**
 * Java用チェックコード.
 * @author tejc999999
 *
 */
public class JavaCheckCode implements CheckCode {

    /**
     * チェックコードを取得する.
     * @return チェックコード前部
     */
    @Override
    public String getCheckCode() {
        
        StringBuffer buff = new StringBuffer();
        buff.append("public class CheckClass {" + LINE_FEED_CODE);
        buff.append("    public static void main(String[] args) {" + LINE_FEED_CODE);
        buff.append("        System.out.println(\"TEST CHECK!!\");" + LINE_FEED_CODE);
        buff.append("    }" + LINE_FEED_CODE);
        buff.append("}" + LINE_FEED_CODE);
        
        return buff.toString();
    }

    /**
     * 課題用ダミーコードを取得する.
     * @param codeMethod メソッド名
     * @param codeReturn 戻り値
     * @return コード前部
     */
    @Override
    public String getDummyCode(String codeMethod, String codeReturn) {
        
        StringBuffer buff = new StringBuffer();
        buff.append("public class TaskClass {" + LINE_FEED_CODE);
        buff.append("    " + codeMethod + " {" + LINE_FEED_CODE);
        buff.append("        return " + codeReturn + ";" + LINE_FEED_CODE);
        buff.append("    }" + LINE_FEED_CODE);
        buff.append("}" + LINE_FEED_CODE);
        
        return buff.toString();
    }

}
