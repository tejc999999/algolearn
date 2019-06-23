package jp.spring.boot.algolearn.form;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 先生用問題Form
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
public class QuestionForm {

	/**
	 * 問題ID
	 */
	private String id;
	
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
	 * 問題公開フラグ
	 */
	private boolean publicFlg;
}
