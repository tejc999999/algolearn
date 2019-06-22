package jp.spring.boot.algolearn.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.QuestionBean;

/**
 * 課題用リポジトリ
 * 
 * @author tejc999999
 *
 */
public interface QuestionRepository  extends JpaRepository<QuestionBean, Integer> {

}
