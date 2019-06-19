package jp.spring.boot.algolearn.bean;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 課題入出力値
 * 
 * @author tejc999999
 *
 */
@Data
//@Table(name = "t_task_valiable")
public class TaskValiableBean {

	/**
	 * 識別ID
	 */
//	@Id
	private String id;

	/**
	 * データ型
	 */
	private String dataType;

	/**
	 * 値
	 */
	private String parametor;
	
	/**
	 * 課題：外部参照
	 */
	private QuestionBean taskBean;

	/**
	 * 入力値フラグ
	 * falseの場合は出力値
	 */
	private boolean inputFlg;

}
