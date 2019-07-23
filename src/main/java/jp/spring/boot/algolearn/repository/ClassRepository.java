
package jp.spring.boot.algolearn.repository;

import jp.spring.boot.algolearn.bean.ClassBean;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 問題用リポジトリ(question repository).
 * 
 * @author tejc999999
 *
 */
public interface ClassRepository  extends JpaRepository<ClassBean, Long> {
    
}
