package jp.spring.boot.algolearn.bean;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jp.spring.boot.algolearn.bean.embedded.TaskValiableId;
import lombok.Data;

/**
 * 課題入出力値
 * @author tejc999999
 */
@Data
@Entity
@Table(name = "t_task_valiable")
public class TaskValiableBean {

    @EmbeddedId
    private TaskValiableId taskValiableId;

    @Column(name = "date_type")
    private String dataType;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", insertable = false, updatable = false)
    // @MapsId("taskId")
    private TaskBean taskBean;
}
