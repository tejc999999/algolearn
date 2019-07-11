package jp.spring.boot.algolearn.form;

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
public class TaskSearchForm {

    /**
     * 検索ワード
     */
    public String searchWord;
    
    /**
     * 公開フラグ（TRUE:公開、FALSE:個人)
     */
    public boolean publicFlg;
    
}
