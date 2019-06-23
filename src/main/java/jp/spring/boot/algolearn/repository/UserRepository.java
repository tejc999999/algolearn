package jp.spring.boot.algolearn.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.algolearn.bean.UserBean;

/**
 * ユーザー用リポジトリ(user repository)
 * 
 * @author tejc999999
 *
 */
public interface UserRepository  extends JpaRepository<UserBean,String> {

}
