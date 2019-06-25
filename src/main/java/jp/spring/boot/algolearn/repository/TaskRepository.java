package jp.spring.boot.algolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.TaskBean;

/**
 * 
 * @author tejc999999
 *
 */
public interface TaskRepository  extends JpaRepository<TaskBean, Integer> {

}