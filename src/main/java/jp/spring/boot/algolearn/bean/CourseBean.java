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
 * コースBean(course Bean)
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_course")
public class CourseBean {

    /**
     * コースコード(course code)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * コース名(course name)
     */
    @Column(name = "name")
    private String name;
    
    /**
     * コンストラクタ
     */
    public CourseBean() {
        userCourseBeans = new HashSet<>();
        classCourseBeans = new HashSet<>();
        taskCourseBeans = new HashSet<>();
        userTaskCodeBeans = new HashSet<>();
    }

    /**
     * ユーザ所属コース：相互参照オブジェクト(user belonging course：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="course_id")
    Set<UserCourseBean> userCourseBeans;
    
    /**
     * クラス所属コース：相互参照オブジェクト(class belonging course：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="course_id")
    Set<ClassCourseBean> classCourseBeans;

    /**
     * 課題・コース：相互参照オブジェクト(task・course：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="course_id")
    Set<TaskCourseBean> taskCourseBeans;

    /**
     * ユーザー・課題：相互参照オブジェクト(user・task：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="course_id")
    Set<UserTaskCodeBean> userTaskCodeBeans;

    /**
     * ユーザ・コース情報クリア
     */
    public void clearUserCourseBean() {
        userCourseBeans.clear();
    }
    
    /**
     * クラス・コース情報クリア
     */
    public void clearClassCourseBean() {
        classCourseBeans.clear();
    }

    /**
     * ユーザー・コース情報を追加する
     * @param userCourseBean ユーザー・コースBean
     */
    public void addUserCourseBean(UserCourseBean userCourseBean) {
        userCourseBeans.add(userCourseBean);
    }
    
    /**
     * クラス・コース情報を追加する
     * @param classCourseBean クラス・コースBean
     */
    public void addClassCourseBean(ClassCourseBean classCourseBean) {
        classCourseBeans.add(classCourseBean);
    }
    
    /**
     * コースに紐づくユーザーIDリストを取得する
     * @return ユーザーIDリスト
     */
    public List<String> getUserIdList() {
        List<String> list = new ArrayList<>();
        userCourseBeans.forEach(userCourseBean -> {
            list.add(String.valueOf(userCourseBean.getUserId()));
        });
        return list;
    }
    
    /**
     * コースに紐づくクラスIDリストを取得する
     * @return クラスIDリスト
     */
    public List<String> getClassIdList() {
        List<String> list = new ArrayList<>();
        classCourseBeans.forEach(classCourseBean -> {
            list.add(String.valueOf(classCourseBean.getClassId()));
        });
        return list;
    }
}
