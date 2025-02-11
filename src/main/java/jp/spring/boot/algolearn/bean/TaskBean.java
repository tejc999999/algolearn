package jp.spring.boot.algolearn.bean;

import java.util.HashSet;
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
 * 課題Bean(task Bean).
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_task")
public class TaskBean {

    /**
     * 課題コード(task code).
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 課題タイトル(task title).
     */
    @Column(name = "title")
    private String title;

    /**
     * 課題説明文(task description).
     */
    @Column(name = "description")
    private String description;

    /**
     * プログラム言語種別.
     */
    @Column(name = "language_id")
    private String languageId;

    /**
     * 問題ID(question id).
     */
    @Column(name = "question_id")
    private Long questionId;

    /**
     * メソッド部コード(method part code).
     */
    @Column(name = "code_method")
    private String codeMethod;

    /**
     * 戻り値部コード(return part code).
     */
    @Column(name = "code_return")
    private String codeReturn;
    
    /**
     * コンストラクタ(constructor).
     */
    public TaskBean() {
        taskCourseBeans = new HashSet<>();
        taskQuestionBeans = new HashSet<>();
        userTaskCodeBeans = new HashSet<>();
    }
    
    /**
     * 課題・コース：相互参照オブジェクト(task・course：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Set<TaskCourseBean> taskCourseBeans;

    /**
     * 課題・問題：相互参照オブジェクト(task・question：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Set<TaskQuestionBean> taskQuestionBeans;
    
    /**
     * 課題・コース：相互参照オブジェクト(task・course：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Set<UserTaskCodeBean> userTaskCodeBeans;

}
