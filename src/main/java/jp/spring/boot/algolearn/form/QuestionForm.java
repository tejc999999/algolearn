package jp.spring.boot.algolearn.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 教員用問題Form
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
	private String code;
	
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
	 * 入力値説明文
	 */
	private List<String> inputDescriptionList;

	/**
	 * 出力値説明文
	 */
	private String outputDescription;
	
	/**
	 * 問題公開フラグ
	 */
	private boolean publicTaskFlg;
}
