package jp.spring.boot.algolearn.form;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生用学習Form.
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
public class StudentLearnForm {

	/**
	 *  開発エディタテーマ
	 */
	private String theme;
	
	/**
	 * 開発エディタモード
	 */
	private String mode;
}