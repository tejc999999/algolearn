package jp.spring.boot.algolearn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import jp.spring.boot.algolearn.bean.ClassBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;

@Data
@Entity
@Table(name = "employee")
@NoArgsConstructor
@AllArgsConstructor
public class Employee { 
  
	@Id
	@Column(name="employee_id")
	private int employeeId;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "employee_project", 
        joinColumns = { @JoinColumn(name = "employee_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    List<Project> projects = new ArrayList<>();
    
//    private List<Project> getProjects() {
//    	return this.projects;
//    }
//    
//    public List<Project> getProjectSet() {
//    	return this.projects;
//    }
    
    // standard constructor/getters/setters
}