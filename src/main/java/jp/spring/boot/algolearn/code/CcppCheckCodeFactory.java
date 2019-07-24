package jp.spring.boot.algolearn.code;

/**
 * C/C++プログラム採点コード生成Factoryクラス.
 * @author tejc999999
 *
 */
public class CcppCheckCodeFactory implements CheckCodeFactory {

    /**
     * C/C++プログラム採点コード生成オブジェクトを取得する.
     * @return C/C++プログラム採点コード生成オブジェクト
     */
    @Override
    public CheckCode getInstance() {
        return new CcppCheckCode();
    }
}
