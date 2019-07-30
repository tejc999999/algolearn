package jp.spring.boot.algolearn.teacher.form;

import java.util.List;
import java.util.Map;

import jp.spring.boot.algolearn.code.VariableType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 先生用課題登録時コード作成Form(code create form when task regist for teacher).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class TaskAddCodeForm {

    /**
     * 問題ID(question id).
     */
    private String questionId;

    /**
     * 問題タイトル(question title).
     */
    private String questionTitle;
    
    /**
     * 問題説明文(question description).
     */
    private String questionDescription;

    /**
     * 課題ID(task id).
     */
    private String id;
    
    /**
     * 課題タイトル(task title).
     */
    private String title;
    
    /**
     * 課題説明文(task description).
     */
    private String description;

    /**
     * プログラム言語(programing language).
     */
    private String prgLanguageId;
    
    /**
     * プログラム言語マップ（programing language map）.
     */
    private Map<String, String> prgLanguageMap;
    
    /**
     * プログラムコード(program code).
     */
    private String code;

    /**
     * 課題コードメソッド部(Task code method name part).
     */
    private String codeMethod;

    /**
     * 課題コード戻り値部(Task code return value part).
     */
    private String codeReturn;

    /**
     * コード自動生成フラグ(code auto generate flg).
     */
    private boolean autoCodeFlg;
    
    /**
     * プログラム採点コード用正解個数.
     */
    private int answerCnt;
    
    /**
     * プログラム採点コード用正解型リスト.
     */
    private List<VariableType> answerCodeTypeList;

    /**
     * プログラム採点コード用引数個数.
     */
    private int inputCnt;
    
    /**
     * プログラム採点コード用引数型リスト.
     */
    private List<VariableType> inputCodeTypeList;
    
    /**
     * 検索ワード.
     */
    private String searchWord;
}
