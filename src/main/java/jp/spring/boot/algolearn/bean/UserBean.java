package jp.spring.boot.algolearn.bean;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ユーザーBean(user Bean)
 * @author tejc999999
 */
@Data
@ToString(exclude = { "classBeans", "courseBeans" })
@EqualsAndHashCode(exclude = { "classBeans", "courseBeans" })
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user")
public class UserBean {

    /**
     * ユーザーID(user id)
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * パスワード(password)
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * ユーザー名(user name)
     */
    @Column(name = "name")
    private String name;

    /**
     * 権限ID(role id)
     */
    @Column(name = "role_id", nullable = false, length = 3)
    private String roleId;

    /**
     * ユーザー所属クラス：相互参照オブジェクト(user belonging class：cross reference object)
     */
    @ManyToMany(mappedBy = "userBeans", fetch = FetchType.EAGER)
    private Set<ClassBean> classBeans;

    /**
     * ユーザー所属コース：相互参照オブジェクト(user belonging course：cross reference object)
     */
    @ManyToMany(mappedBy = "userBeans", fetch = FetchType.EAGER)
    private Set<CourseBean> courseBeans;

    /**
     * クラス関連削除
     * @param classBean クラスBean
     */
    public void removeFromClass(ClassBean classBean) {
        classBean.getUserBeans().remove(this);
        classBeans.remove(classBean);
    }

    /**
     * コース関連削除
     * @param classBean コースBean
     */
    public void removeFromCourse(CourseBean courseBean) {
        courseBean.getUserBeans().remove(this);
        courseBeans.remove(courseBean);
    }

}
