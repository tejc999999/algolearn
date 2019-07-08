package jp.spring.boot.algolearn.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザーBean(user Bean)
 * @author tejc999999
 */
@Setter
@Getter
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
     * コンストラクタ
     */
    public UserBean() {
        userClassBeans = new HashSet<>();
        userCourseBean = new HashSet<>();
    }

    /**
     * ユーザー所属クラス：相互参照オブジェクト(user belonging class：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="user_id")
    private Set<UserClassBean> userClassBeans;

    /**
     * ユーザー所属コース：相互参照オブジェクト(user belonging course：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="user_id")
    private Set<UserCourseBean> userCourseBean;
}
