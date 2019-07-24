package jp.spring.boot.algolearn.code;

/**
 * Javaプログラム採点コード生成Factoryクラス.
 * @author tejc999999
 *
 */
public class JavaCheckCodeFactory implements CheckCodeFactory {

    /**
     * Javaプログラム採点コード生成オブジェクトを取得する.
     * @return Javaプログラム採点コード生成オブジェクト
     */
    @Override
    public CheckCode getInstance() {
        return new JavaCheckCode();
    }
}
