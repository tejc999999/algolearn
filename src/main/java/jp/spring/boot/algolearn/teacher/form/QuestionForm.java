package jp.spring.boot.algolearn.teacher.form;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 先生用問題Form(question form for teacher).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class QuestionForm {

    /**
     * 問題ID(question id).
     */
    private String id;

    /**
     * タイトル(title).
     */
    private String title;

    /**
     * 説明文(description).
     */
    private String description;

    /**
     * 入力値個数(number of input param).
     */
    private int inputNum;
}
