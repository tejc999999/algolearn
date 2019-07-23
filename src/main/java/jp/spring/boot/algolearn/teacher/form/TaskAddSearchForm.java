package jp.spring.boot.algolearn.teacher.form;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 先生用課題登録時問題検索Form(question search form when task regist for teacher)
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
public class TaskAddSearchForm {

    /**
     * 検索ワード
     */
    public String searchWord;
}
