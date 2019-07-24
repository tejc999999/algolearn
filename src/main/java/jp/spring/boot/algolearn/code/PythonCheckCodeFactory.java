package jp.spring.boot.algolearn.code;

/**
 * Pythonプログラム採点コード生成Factoryクラス.
 * @author tejc999999
 *
 */
public class PythonCheckCodeFactory implements CheckCodeFactory {

    /**
     * Pythonプログラム採点コード生成オブジェクトを取得する.
     * @return Pythonプログラム採点コード生成オブジェクト
     */
    @Override
    public CheckCode getInstance() {
        return new PythonCheckCode();
    }
}
