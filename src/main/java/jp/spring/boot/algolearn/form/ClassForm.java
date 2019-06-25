package jp.spring.boot.algolearn.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 先生用クラスForm
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
public class ClassForm {

	/**
	 * クラスID
	 */
	private String id;
	
	/**
	 * クラス名
	 */
	private String name;
	
	/**
	 * チェックボックス入力情報（所属ユーザID）用
	 */
	private String[] userChecks;
	
	/**
	 * 既存所属ユーザID
	 */
	private List<String> userCheckedList;
}
