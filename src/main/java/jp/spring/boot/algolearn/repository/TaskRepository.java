package jp.spring.boot.algolearn.repository;

import jp.spring.boot.algolearn.bean.TaskBean;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 課題用リポジトリ(taks repository).
 * @author tejc999999
 */
public interface TaskRepository extends JpaRepository<TaskBean, Integer> {

}
