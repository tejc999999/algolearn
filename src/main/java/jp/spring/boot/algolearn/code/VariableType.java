package jp.spring.boot.algolearn.code;

/**
 * プログラム用変数型Enum(variable type enum for program).
 * @author tejc999999
 *
 */
public enum VariableType {

    JAVA_INT("101", "int"),
    JAVA_FLOAT("102", "float"),
    JAVA_CHAR("103", "char"),
    JAVA_BOOLEAN("103", "boolean"),
    JAVA_STRING("103", "String")
    ;

    /**
     * 変数型ID(variable type id).
     */
    private final String id;
    
    /**
     * 変数型名(variable type name).
     */
    private final String name;
    
    /**
     * コンストラクタ(constructor).
     * @param id 変数型ID(variable type id).
     * @param name 変数型名(variable type name)
     */
    private VariableType(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * 変数型ＩＤ取得(get variable type id).
     * @return 変数型ID(programing language id)
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * 変数型名取得(get variable type name).
     * @return 変数型名(variable type name)
     */
    public String getName() {
        return this.name;
    }
}
