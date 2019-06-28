package jp.spring.boot.algolearn.bean;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * クラスBean(class Bean)
 * @author tejc999999
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@ToString(exclude = "userBeans")
@EqualsAndHashCode(exclude = "userBeans")
@Table(name = "t_class")
public class ClassBean {

    /**
     * クラスコード(class code)
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * クラス名(class name)
     */
    @Column(name = "name")
    private String name;

    /**
     * ユーザー所属クラス：相互参照オブジェクト(user belonging class：cross reference object)
     */
    // CascadeType.ALL:全ての操作
    // CascadeType.MERGE:非管理状態のエンティティを永続化コンテキストに登録
    // CascadeType.PERSIST:新規にエンティティを永続化コンテキストに登録
    // CascadeType.REFRESH:エンティティをDBの内容で更新
    // CascadeType.REMOVE:永続化コンテキストからエンティティを削除
    @ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_class", joinColumns = {
            @JoinColumn(name = "class_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id") })
    Set<UserBean> userBeans;
    
    /**
     * コース所属クラス：相互参照オブジェクト(class belonging course：cross reference object)
     */
    @ManyToMany(mappedBy = "classBeans", fetch = FetchType.EAGER)
    private Set<CourseBean> courseBeans;

    /**
     * クラス関連削除
     * @param classBean クラスBean
     */
    public void removeFromCourse(CourseBean courseBean) {
        courseBean.getClassBeans().remove(this);
        courseBeans.remove(courseBean);
    }


}
