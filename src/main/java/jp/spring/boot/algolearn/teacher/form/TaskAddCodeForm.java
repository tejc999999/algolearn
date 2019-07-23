package jp.spring.boot.algolearn.teacher.form;

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
     * 課題ID(task id).
     */
    public String id;

    /**
     * タイトル(title).
     */
    public String title;
    
    /**
     * 説明文(description).
     */
    public String description;

    /**
     * プログラム言語(programing language).
     */
    public String languageId;
    
    /**
     * コード前部(front code).
     */
    public String frontCode;

    /**
     * コード中部(middle code).
     */
    public String middleCode;

    /**
     * コード後部(back code).
     */
    public String backCode;
    
}
