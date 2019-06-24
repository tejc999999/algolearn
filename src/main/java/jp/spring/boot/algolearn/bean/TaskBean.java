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
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "t_task")
public class TaskBean {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	int id;
	
	@Column(name="language_id")
	String languageId;
	
	@Column(name="title")
	String title;
	
	@Column(name="description")
	String description;
	
	@Column(name="question_id")
	int questionId;
	
	String selfMadeCheckCode;
	
	@OneToMany(mappedBy="taskBean", cascade= {CascadeType.ALL})
//    @MapsId("taskBean")
	List<TaskValiableBean> taskValiableBeans;

	@OneToMany(mappedBy="taskBean", cascade= {CascadeType.ALL})
//    @MapsId("taskBean")
	List<TaskValiableAnswerBean> taskValiableAnswerBeans;
	
	@ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id", insertable = false, updatable = false)
//    @MapsId("questionId")
	QuestionBean questionBean;
}
