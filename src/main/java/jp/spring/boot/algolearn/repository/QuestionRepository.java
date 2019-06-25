package jp.spring.boot.algolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.QuestionBean;

/**
 * 問題用リポジトリ(question repository)
 * 
 * @author tejc999999
 *
 */
public interface QuestionRepository  extends JpaRepository<QuestionBean, Integer> {

}
