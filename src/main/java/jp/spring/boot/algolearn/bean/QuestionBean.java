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
	private int inputNum;
	
	/**
	 * 問題公開フラグ(question public flag)
	 * falseの場合は個人用(false is private)
	 */
	@Column(name="public_flg", nullable = false)
	private boolean publicFlg;

//	/**
//	 * コンストラクタ
//	 */
//	public QuestionBean() {
////	    taskQuestionBeans = new HashSet<>();
//	}
	
	/**
	 * 問題を使用した課題：相互参照オブジェクト(task belonging question：cross reference object)
	 */
////	@OneToMany(mappedBy="questionBean", cascade= {CascadeType.ALL})
////    @MapsId("questionBean")
//	@OneToMany(mappedBy="questionBean", fetch = FetchType.EAGER)
//	private Set<TaskQuestionBean> taskQuestionBeans;
}
