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
 * 課題入出力値
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_task_valtype")
public class TaskValTypeBean {

    /**
     * 識別ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name="task_id")
    private Long taskId;
    
    @Column(name="val_number")
    private byte valNumber;

    @Column(name="data_type")
    private String dataType;

}
