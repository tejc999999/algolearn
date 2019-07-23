package jp.spring.boot.algolearn.repository;

import jp.spring.boot.algolearn.bean.CourseBean;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * コース用リポジトリ(course repository).
 * @author tejc999999
 */
public interface CourseRepository extends JpaRepository<CourseBean, Long> {

}
