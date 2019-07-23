package jp.spring.boot.algolearn.teacher.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 先生用クラスForm(class form for teacher).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class ClassForm {

    /**
     * クラスID(class id).
     */
    private String id;

    /**
     * クラス名(class name).
     */
    private String name;

    /**
     * 所属ユーザIDリスト(affiliated user list).
     */
    private List<String> userCheckedList;
}
