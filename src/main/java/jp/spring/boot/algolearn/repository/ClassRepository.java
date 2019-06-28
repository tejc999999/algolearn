package jp.spring.boot.algolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.UserBean;

/**
 * 問題用リポジトリ(question repository)
 * 
 * @author tejc999999
 *
 */
public interface ClassRepository  extends JpaRepository<ClassBean, Integer> {
    
}
