package jp.spring.boot.algolearn.code;

/**
 * Java用チェックコード.
 * @author tejc999999
 *
 */
public class JavaCheckCode implements CheckCode {

    /**
     * チェックコード前部取得.
     * @return チェックコード前部
     */
    @Override
    public String getFrontCode() {
        
        StringBuffer buff = new StringBuffer();
        buff.append("public class CheckClass {" + LINE_FEED_CODE);
        buff.append("    public void taskMethod() {" + LINE_FEED_CODE);
        
        return buff.toString();
    }

    /**
     * チェックコード中部取得.
     * @return チェックコード中部.
     */
    @Override
    public String getMiddleCode() {
        
        return "";
    }

    /**
     * チェックコード後部取得.
     * @return チェックコード後部
     */
    @Override
    public String getBackCode() {
        StringBuffer buff = new StringBuffer();
        buff.append("    }" + LINE_FEED_CODE);
        buff.append("    public static void main(String[] args) {" + LINE_FEED_CODE);
        buff.append("        System.out.println(\"TEST CHECK!!\");" + LINE_FEED_CODE);
        buff.append("    }" + LINE_FEED_CODE);
        buff.append("}" + LINE_FEED_CODE);
        
        return buff.toString();
    }

}
