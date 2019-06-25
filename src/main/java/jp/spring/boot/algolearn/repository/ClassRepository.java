package jp.spring.boot.algolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.ClassBean;

/**
 * 問題用リポジトリ(question repository)
 * 
 * @author tejc999999
 *
 */
public interface ClassRepository  extends JpaRepository<ClassBean, Integer> {

}
