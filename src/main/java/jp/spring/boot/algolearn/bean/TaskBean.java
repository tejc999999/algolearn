package jp.spring.boot.algolearn.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_task")
public class TaskBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "language_id")
    private String languageId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "question_id")
    private int questionId;

    private String selfMadeCheckCode;

    @OneToMany(mappedBy = "taskBean", cascade = { CascadeType.ALL })
    // @MapsId("taskBean")
    private List<TaskValiableBean> taskValiableBeans;

    @OneToMany(mappedBy = "taskBean", cascade = { CascadeType.ALL })
    // @MapsId("taskBean")
    private List<TaskValiableAnswerBean> taskValiableAnswerBeans;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id", insertable = false, updatable = false)
    // @MapsId("questionId")
    private QuestionBean questionBean;
}
