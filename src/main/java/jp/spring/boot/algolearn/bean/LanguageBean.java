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
 * プログラミング言語Bean(programing language Bean)
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_language")
public class LanguageBean {

    /**
     * 課題タイトル
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 課題タイトル
     */
    @Column(name = "name")
    private String name;
}
