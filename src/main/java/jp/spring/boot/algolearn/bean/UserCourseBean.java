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
 * ユーザー、コース・連関エンティティBean(user/course : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_user_course")
public class UserCourseBean {

    /**
     * サロゲートキー(surrogate key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ユーザーID(user id).
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * コースID(course id).
     */
    @Column(name = "course_id")
    private Long courseId;
}
