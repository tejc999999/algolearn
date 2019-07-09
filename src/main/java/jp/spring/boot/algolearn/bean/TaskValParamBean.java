package jp.spring.boot.algolearn.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name="t_task_valparam")
public class TaskValParamBean {

    /**
     * 識別ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 課題ID(task id)
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * 入出力番号（0:出力値、1以上:第N引数)
     */
    @Column(name = "val_number")
    private byte valNumber;

    /**
     * データ型(data type)
     */
    @Column(name = "date_type")
    private String dataType;
    
    /**
     * グループ番号(group number)
     */
    @Column(name = "group_id")
    private byte groupId;


}
