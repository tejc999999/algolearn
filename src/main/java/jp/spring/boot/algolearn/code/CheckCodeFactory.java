package jp.spring.boot.algolearn.code;

/**
 * プログラム採点用コードFactoryクラス.
 * @author tejc999999
 *
 */
public interface CheckCodeFactory {

    /**
     * プログラム採点コード生成オブジェクトを取得する.
     * @return プログラム採点コード生成オブジェクト
     */
    public abstract CheckCode getInstance();
}
