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
 * ユーザー、クラス結合テーブルBean
 * @author tejc999999
 *
 */
@Entity
@Setter
@Getter
@Table(name="t_user_class")
public class UserClassBean {
    
    /**
     * サロゲートキー
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    /**
     * ユーザーID
     */
    @Column(name = "user_id")
    private String userId;
    
    /**
     * クラスID
     */
    @Column(name = "class_id")
    private Long classId;
    
}