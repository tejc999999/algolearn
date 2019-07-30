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
 * ユーザー、課題・連関エンティティ兼コードBean(user/task : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_user_task_code")
public class UserTaskCodeBean {

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

    /**
     * 課題ID(task id).
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * 進捗状況(progress)l.
     */
    @Column(name = "status")
    private byte status;
    
    /**
     * プログラムコード(program code).
     */
    @Column(name = "code")
    private String code;
}
