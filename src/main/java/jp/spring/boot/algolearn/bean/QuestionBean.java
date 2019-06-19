package jp.spring.boot.algolearn.bean;


import java.util.List;

import javax.persistence.Entity;
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
@Table(name = "t_task")
public class QuestionBean {
	
	/**
	 * 識別ID
	 */
	@Id
	private String id;
	
	/**
	 * タイトル
	 */
	private String title;
	
//	/**
//	 * 説明文
//	 */
//	private String description;
//	
//	/**
//	 * 入力値説明文
//	 */
//	private String inputDescription;
//
//	/**
//	 * 出力値説明文
//	 */
//	private String outputDescription;
//
//	/**
//	 * 課題入出力値：外部参照
//	 */
////	@OneToMany
////	private List<TaskValiableBean> taskValiableBeanList;
//
//	/**
//	 * 解答入出力値：外部参照
//	 */
////	@OneToMany
////	private List<AnswerValiableBean> answerValiableBeanList;
//
//	/**
//	 * 課題公開フラグ
//	 * falseの場合は個人用
//	 */
//	private boolean publicTaskFlg;
}
