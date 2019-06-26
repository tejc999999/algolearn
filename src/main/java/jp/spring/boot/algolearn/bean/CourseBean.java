package jp.spring.boot.algolearn.bean;

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
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * コースBean(course Bean)
 * 
 * @author tejc999999
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@ToString(exclude = "userBeans")
@EqualsAndHashCode(exclude = "userBeans")
@Table(name = "t_course")
public class CourseBean {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;
	
	/**
	 * ユーザー所属コース：相互参照オブジェクト(user belonging course：cross reference object)
	 */
	@ManyToMany(cascade = {CascadeType.REFRESH}, fetch=FetchType.EAGER)
	@JoinTable(
			name = "t_user_course",
			joinColumns = {@JoinColumn(name = "course_id", referencedColumnName="id")},
			inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName="id")}
			)
	Set<UserBean> userBeans;
}
