package jp.spring.boot.algolearn.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * クラス、コース・連関エンティティBean.
 * @author tejc999999
 *
 */
@Entity
@Table(name = "t_class_course")
@Setter
@Getter
public class ClassCourseBean {
    
    /**
     * サロゲートキー.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * クラスID.
     */
    @Column(name = "class_id")
    private Long classId;

    /**
     * コースID.
     */
    @Column(name = "course_id")
    private Long courseId;    
}
