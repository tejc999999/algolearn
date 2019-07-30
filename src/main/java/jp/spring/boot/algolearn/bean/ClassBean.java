package jp.spring.boot.algolearn.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * クラスBean(class Bean).
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_class")
public class ClassBean {

    /**
     * クラスコード(class code).
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * クラス名(class name).
     */
    @Column(name = "name")
    private String name;
    
    /**
     * コンストラクタ（constructor）.
     */
    public ClassBean() {
        userClassBeans = new HashSet<>();
        classCourseBeans = new HashSet<>();
    }

    /**
     * ユーザー所属クラス：相互参照オブジェクト(user belonging class：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id")
    private Set<UserClassBean> userClassBeans;
    
    /**
     * コース所属クラス：相互参照オブジェクト(class belonging course：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id")
    private Set<ClassCourseBean> classCourseBeans;
    
    /**
     * ユーザー所属クラス：相互参照オブジェクトを追加する(add an object(class belonging course)).
     * @param userClassBean ユーザ所属クラスBean
     */
    public void addUserClassBean(UserClassBean userClassBean) {
        userClassBeans.add(userClassBean);
    }

    /**
     * ユーザー所属クラス：相互参照オブジェクトを削除する(delete an object(class belonging course)).
     */
    public void clearUserClassBean() {
        userClassBeans.clear();
    }
    
    /**
     * ユーザIDリストを取得する(get user id list).
     * @return ユーザIDリスト(user id list)
     */
    public List<String> getUserIdList() {
        List<String> list = new ArrayList<>();
        userClassBeans.forEach(userClassBean -> {
            list.add(userClassBean.getUserId());
        });
        return list;
    }

    /**
     * コース所属クラス：相互参照オブジェクトを追加する(add an object(course affiliation class)).
     * @param classCourseBean コース所属クラスBean(course affiliation class bean)
     */
    public void addClassCourseBean(ClassCourseBean classCourseBean) {
        classCourseBeans.add(classCourseBean);
    }

    /**
     * コース所属クラス：相互参照オブジェクトを削除する(delete an object(course affiliation class)).
     */
    public void clearClassCourseBean() {
        classCourseBeans.clear();
    }

    /**
     * コースIDリストを取得する(get course id list).
     * @return コースIDリスト(course id list)
     */
    public List<String> getCourseIdList() {
        List<String> list = new ArrayList<>();
        classCourseBeans.forEach(classCourseBean -> {
            list.add(String.valueOf(classCourseBean.getCourseId()));
        });
        return list;
    }
}
