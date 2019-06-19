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
public class LearnForm {

//	/**
//	 * プログラミング言語コード：Java
//	 */
//	public static String PL_CODE_JAVA = "java";
//
//	/**
//	 * プログラミング言語コード：C
//	 */
//	public static String PL_CODE_C = "c_cpp";
//
//	/**
//	 * プログラミング言語コード：Python
//	 */
//	public static String PL_CODE_PYTHON = "python";
	
	/**
	 * プログラミング言語種別
	 */
	private String programmingLanguage;

	/**
	 *  プログラムコード
	 */
	private String code;
	
	/**
	 * プログラムコード（冒頭）
	 */
	private String codeHead;

	/**
	 * プログラムコード（チェック用）
	 */
	private String codeCheck;
	/**
	 * 課題情報
	 */
	private QuestionForm questionForm;

}