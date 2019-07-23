package jp.spring.boot.algolearn.repository;

import java.util.List;

import jp.spring.boot.algolearn.bean.UserBean;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ユーザー用リポジトリ(user repository).
 * @author tejc999999
 */
public interface UserRepository extends JpaRepository<UserBean, String> {

    /**
     * 権限IDを条件とした取得.
     * 
     * @param roleId 権限ID
     * @return ユーザーBean
     */
    List<UserBean> findByRoleId(String roleId);   
}
