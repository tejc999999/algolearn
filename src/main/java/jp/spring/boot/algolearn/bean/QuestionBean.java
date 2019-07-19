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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 問題Bean(question Bean)
 * 
 * @author tejc999999
 *
 */
@Entity
@Setter
@Getter
@Table(name = "t_question")
public class QuestionBean {
	
	/**
	 * 問題コード(question code)
	 */
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	/**
	 * タイトル(title)
	 */
	@Column(name="title")
	private String title;
	
	/**
	 * 説明文(description)
	 */
	@Column(name="description")
	private String description;
	
	/**
	 * 入力値個数(number of input)
	 */
	@Column(name="input_num")
	private byte inputNum;

	/**
	 * コンストラクタ
	 */
	public QuestionBean() {
	    taskQuestionBeans = new HashSet<>();
	}
	
    /**
     * 課題・問題：相互参照オブジェクト(task・question：cross reference object)
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL )
    @JoinColumn(name="question_id")
	private Set<TaskQuestionBean> taskQuestionBeans;
}
