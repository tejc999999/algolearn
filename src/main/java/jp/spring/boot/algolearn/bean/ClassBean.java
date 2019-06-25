package jp.spring.boot.algolearn.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * クラスBean(class Bean)
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_class")
public class ClassBean {
	
	/**
	 * クラスコード(class code)
	 */
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	/**
	 * クラス名(class name)
	 */
	@Column(name="name")
	private String name;

	/**
	 * ユーザー所属クラス：相互参照オブジェクト(user belonging class：cross reference object)
	 */
	// CascadeType.ALL:全ての操作
	// CascadeType.MERGE:非管理状態のエンティティを永続化コンテキストに登録
	// CascadeType.PERSIST:新規にエンティティを永続化コンテキストに登録
	// CascadeType.REFRESH:エンティティをDBの内容で更新
	// CascadeType.REMOVE:永続化コンテキストからエンティティを削除
	@ManyToMany(cascade = {CascadeType.REFRESH}, fetch=FetchType.EAGER)
	@JoinTable(
			name = "t_user_class",
			joinColumns = {@JoinColumn(name = "class_id")},
			inverseJoinColumns = {@JoinColumn(name = "user_id")}
			)
	private List<UserBean> userBeans = new ArrayList<>();

}
