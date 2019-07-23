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
 * ユーザー、コース・連関エンティティBean.
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_user_course")
public class UserCourseBean {

    /**
     * サロゲートキー.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ユーザーID.
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * コースID.
     */
    @Column(name = "course_id")
    private Long courseId;
}
