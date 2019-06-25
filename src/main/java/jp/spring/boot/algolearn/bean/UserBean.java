package jp.spring.boot.algolearn.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@Column(name="id")
	private String id;
	
	/**
	 * パスワード(password)
	 */
	@Column(name="password", nullable = false)
	private String password;

	/**
	 * ユーザー名(user name)
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 権限ID(role id)
	 */
	@Column(name="role_id", nullable = false, length = 3)
	private String roleId;
	
	/**
	 * ユーザー所属クラス：相互参照オブジェクト(user belonging class：cross reference object)
	 */
	@ManyToMany(mappedBy = "userBeans", fetch=FetchType.EAGER)
	private List<ClassBean> classBeans = new ArrayList<>();
}
