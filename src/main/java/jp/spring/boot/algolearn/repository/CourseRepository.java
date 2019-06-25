package jp.spring.boot.algolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.CourseBean;

/**
 * コース用リポジトリ(course repository)
 * 
 * @author tejc999999
 *
 */
public interface CourseRepository  extends JpaRepository<CourseBean, Integer> {

}
