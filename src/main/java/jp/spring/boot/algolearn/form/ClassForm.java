package jp.spring.boot.algolearn.form;

import java.util.List;
import java.util.Map;

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
	 * クラス所属ユーザID
	 */
	private Map<String, String> users;
	
	/**
	 * チェックボックス用所属ユーザID
	 */
	private String[] userChecks;
	
	/**
	 * 既存所属ユーザID
	 */
	private List<String> userCheckedList;
}
