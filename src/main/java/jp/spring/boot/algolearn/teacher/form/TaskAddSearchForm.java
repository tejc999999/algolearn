package jp.spring.boot.algolearn.teacher.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 先生用クラス検索Form(class search form for teacher)
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
