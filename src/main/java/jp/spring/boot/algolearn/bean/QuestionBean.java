package jp.spring.boot.algolearn.bean;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 課題Bean
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
	 * 問題コード
	 */
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	/**
	 * タイトル
	 */
	private String title;
	
	/**
	 * 説明文
	 */
	private String description;
	
	/**
	 * 入力値個数
	 */
	private int inputNum;
	
	/**
	 * 課題公開フラグ
	 * falseの場合は個人用
	 */
	private boolean publicFlg;
}
