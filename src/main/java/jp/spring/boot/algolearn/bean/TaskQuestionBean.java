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
 * 課題、問題・連関エンティティBean.
 * @author tejc999999
 *
 */
@Entity
@Setter
@Getter
@Table(name = "t_task_question")
public class TaskQuestionBean {

    /**
     * サロゲートキー.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 課題ID.
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * 問題ID.
     */
    @Column(name = "question_id")
    private Long questionId;
}
