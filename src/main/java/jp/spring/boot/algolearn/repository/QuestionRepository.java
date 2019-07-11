package jp.spring.boot.algolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.spring.boot.algolearn.bean.QuestionBean;

/**
 * 問題用リポジトリ(question repository)
 * @author tejc999999
 */
public interface QuestionRepository extends
                                    JpaRepository<QuestionBean, Long> {

    List<QuestionBean> findByPublicFlgTrue();

    @Query(value = "select q from QuestionBean q where q.userBean.id = :teacherId")
    List<QuestionBean> findByCreateUser(String teacherId);
}
