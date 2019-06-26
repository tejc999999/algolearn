package jp.spring.boot.algolearn.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 問題Bean(question Bean)
 * 
 * @author tejc999999
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@ToString(exclude = "taskBeans")
@EqualsAndHashCode(exclude = "taskBeans")
@Table(name = "t_question")
public class QuestionBean {
	
	/**
	 * 問題コード(question code)
	 */
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
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
	

	@OneToMany(mappedBy="questionBean", cascade= {CascadeType.ALL})
//    @MapsId("questionBean")
	private List<TaskBean> taskBeans;
}
