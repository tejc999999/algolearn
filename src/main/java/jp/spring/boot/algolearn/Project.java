package jp.spring.boot.algolearn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import jp.spring.boot.algolearn.bean.ClassBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class Project {    
	
	@Id
	@Column(name="project_id")
	private int projectId;

	@Column(name="title")
	private String title;
  
    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees = new ArrayList<>();
     
    private List<Employee> getEmployees() {
    	return this.employees;
    }
    
    public List<Employee> getEmployeeSet() {
    	return this.employees;
    }

    
    // standard constructors/getters/setters   
}