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
 * 課題、コース・連関エンティティBean(task/course : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Entity
@Table(name = "t_task_course")
@Setter
@Getter
public class TaskCourseBean {

    /**
     * サロゲートキー(surrogate key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * 課題ID(task id).
     */
    @Column(name = "task_id")
    private int taskId;

    /**
     * コースID(course id).
     */
    @Column(name = "course_id")
    private int courseId;

}
