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

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

/**
 * クラスBean(class Bean)
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_class")
public class ClassBean {

    /**
     * クラスコード(class code)
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * クラス名(class name)
     */
    @Column(name = "name")
    private String name;
    
    /**
     * コンストラクタ
     */
    public ClassBean() {
        userClassBeans = new HashSet<>();
        classCourseBeans = new HashSet<>();
    }

    /**
     * ユーザー所属クラス：相互参照オブジェクト(user belonging class：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="class_id")
    private Set<UserClassBean> userClassBeans;
    
    /**
     * コース所属クラス：相互参照オブジェクト(class belonging course：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="class_id")
    private Set<ClassCourseBean> classCourseBeans;
    
    
    public void addUserClassBean(UserClassBean userClassBean) {
        userClassBeans.add(userClassBean);
    }

    public void clearUserClassBean() {
        userClassBeans.clear();
    }
    
    public List<String> getUserIdList() {
        List<String> list = new ArrayList<>();
        userClassBeans.forEach(userClassBean -> {
            list.add(userClassBean.getUserId());
        });
        return list;
    }
    
    public void addClassCourseBean(ClassCourseBean classCourseBean) {
        classCourseBeans.add(classCourseBean);
    }

    public void clearClassCourseBean() {
        classCourseBeans.clear();
    }

}
