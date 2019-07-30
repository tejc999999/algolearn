package jp.spring.boot.algolearn.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * ユーザー、クラス・連関エンティティBean(user/class : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Entity
@Setter
@Getter
@Table(name = "t_user_class")
public class UserClassBean {
    
    /**
     * サロゲートキー(surrogate key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * ユーザーID(user id).
     */
    @Column(name = "user_id")
    private String userId;
    
    /**
     * クラスID(class id).
     */
    @Column(name = "class_id")
    private Long classId;
    
}
