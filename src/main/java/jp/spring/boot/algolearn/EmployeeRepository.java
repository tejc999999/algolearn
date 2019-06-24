package jp.spring.boot.algolearn;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.UserBean;

public interface EmployeeRepository  extends JpaRepository<Employee,Integer> {

}