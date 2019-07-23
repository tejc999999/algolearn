package jp.spring.boot.algolearn.teacher.form;

import jp.spring.boot.algolearn.config.RoleCode;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ユーザForm(user form).
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
public class StudentForm {

    /**
     * ユーザID(user id).
     */
    String id;

    /**
     * パスワード(password).
     */
    String password;

    /**
     * ユーザ名(user name).
     */
    String name;

    /**
     * 権限ID(role id).
     */
    @Setter(AccessLevel.PRIVATE)
    String roleId = RoleCode.ROLE_STUDENT.getId();
}
