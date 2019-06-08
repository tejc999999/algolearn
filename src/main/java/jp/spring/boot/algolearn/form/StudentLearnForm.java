package jp.spring.boot.algolearn.form;

import java.sql.Timestamp;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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