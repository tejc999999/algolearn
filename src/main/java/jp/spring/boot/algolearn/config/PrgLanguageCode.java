package jp.spring.boot.algolearn.config;

/**
 * プログラミング言語種別Enum(Programming language type enum)
 * @author tejc999999
 */
public enum PrgLanguageCode {

    CCPP("001", "℃／Ｃ＋＋"),
    JAVA("002", "Ｊａｖａ"),
    PYTHON("003", "Ｐｙｔｈｏｎ")
    ;
    
    /**
     * プログラム言語ID(programing language)
     */
    private final String id;
    
    /**
     * プログラム言語名(programing language name)
     */
    private final String name;
    
    /**
     * コンストラクタ(constructor)
     * @param id プログラム言語ID(proguraming language id)
     * @param name プログラム言語名(programing language name)
     */
    private PrgLanguageCode(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * プログラム言語ＩＤ取得(get programing language id)
     * @return プログラム言語ID(programing language id)
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * プログラム言語名取得(get programing language name)
     * @return プログラム言語名(programing language name)
     */
    public String getName() {
        return this.name;
    }
}
