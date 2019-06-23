package jp.spring.boot.algolearn.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 問題Bean(question Bean)
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_question")
public class QuestionBean {
	
	/**
	 * 問題コード(question code)
	 */
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	/**
	 * タイトル(title)
	 */
	private String title;
	
	/**
	 * 説明文(description)
	 */
	private String description;
	
	/**
	 * 入力値個数(number of input)
	 */
	private int inputNum;
	
	/**
	 * 問題公開フラグ(question public flag)
	 * falseの場合は個人用(false is private)
	 */
	@Column(nullable = false)
	private boolean publicFlg;
}
