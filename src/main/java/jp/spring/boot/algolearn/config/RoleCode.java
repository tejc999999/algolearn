package jp.spring.boot.algolearn.config;

/**
 * 権限コードEnum(role code enum)
 * @author tejc999999
 */
public enum RoleCode {
    ROLE_ADMIN("001"), ROLE_TEACHER("002"), ROLE_STUDENT("003");

    /**
     * 権限コード文字列(Role code string)
     */
    private final String text;

    /**
     * コンストラクタ(Constructor)
     * @param text 権限コード文字列(Role code string)
     */
    private RoleCode(final String text) {
        this.text = text;
    }

    /**
     * 権限コード文字列取得(Get role code string)
     * @return 権限コード文字列(Role code string)
     */
    public String getString() {
        return this.text;
    }
}
