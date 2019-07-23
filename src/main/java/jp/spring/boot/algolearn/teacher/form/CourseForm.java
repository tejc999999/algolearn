package jp.spring.boot.algolearn.teacher.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 先生用コースForm(course form for teacher).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class CourseForm {

    /**
     * コースID(course id).
     */
    private String id;

    /**
     * コース名(course name).
     */
    private String name;

    /**
     * 所属クラスIDリスト(affiliated class list).
     */
    private List<String> classCheckedList;

    /**
     * 所属ユーザIDリスト(affiliated user list).
     */
    private List<String> userCheckedList;
}
