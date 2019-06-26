package jp.spring.boot.algolearn.form;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生用学習Form(learn form for student)
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
	 * プログラミング言語種別(programing language type)
	 */
	private String programmingLanguage;

	/**
	 *  プログラムコード(program code)
	 */
	private String code;
	
	/**
	 * プログラムコード（冒頭）(program code(beginning))
	 */
	private String codeHead;

	/**
	 * プログラムコード（チェック用）(program code for check)
	 */
	private String codeCheck;

	/**
	 * 問題情報(question information)
	 */
	private QuestionForm questionForm;

}