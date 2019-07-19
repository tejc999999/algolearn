package jp.spring.boot.algolearn.teacher.form;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 先生用課題Form(question task for teacher)
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
public class TaskForm {

	/**
	 * 課題ID(task id)
	 */
	private String id;
	
	/**
	 * タイトル(title)
	 */
	private String title;

	/**
	 * 説明文(description)
	 */
	private String description;

	/**
	 * プログラム言語種別名称
	 */
	private String languageName;
}
