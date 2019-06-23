package jp.spring.boot.algolearn.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザーBean(user Bean)
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user")
public class UserBean {

	/**
	 * ユーザーID(user id)
	 */
	@Id
	String userId;
	
	/**
	 * パスワード(password)
	 */
	@Column(nullable = false)
	String password;

	/**
	 * ユーザー名(user name)
	 */
	String name;
	
	/**
	 * 権限ID(role id)
	 */
	@Column(nullable = false, length = 3)
	String roleId;
}
