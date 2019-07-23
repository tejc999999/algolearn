package jp.spring.boot.algolearn.config;

/**
 * 権限コードEnum(role code enum).
 * @author tejc999999
 */
public enum RoleCode {
    ROLE_ADMIN("001", "管理者"),
    ROLE_TEACHER("002", "先生"),
    ROLE_STUDENT("003", "学生");

    /**
     * 権限ID(Role id).
     */
    private final String id;

    /**
     * 権限名(Role name).
     */
    private final String name;

    /**
     * コンストラクタ(Constructor).
     * @param id 権限ID
     * @param name 権限名(Role name)
     */
    private RoleCode(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 権限ID取得(Get role id).
     * @return 権限ID(Role id)
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * 権限名取得(Get role name).
     * @return 権限名(Role name)
     */
    public String getName() {
        return this.name;
    }
}
