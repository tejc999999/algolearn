package jp.spring.boot.algolearn.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * ユーザー、課題・連関エンティティ兼コードBean
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name="t_user_task_code")
public class UserTaskCodeBean {

    /**
     * サロゲートキー
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "status")
    private byte status;
    
    @Column(name = "front_code")
    private String frontCode;
    
    @Column(name = "back_code")
    private String backCode;
    
    @Column(name = "code")
    private String code;
}
