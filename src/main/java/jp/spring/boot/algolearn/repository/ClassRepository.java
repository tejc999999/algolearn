package jp.spring.boot.algolearn.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.QuestionBean;

/**
 * 問題用リポジトリ(question repository)
 * 
 * @author tejc999999
 *
 */
public interface ClassRepository  extends JpaRepository<ClassBean, Integer> {

}
