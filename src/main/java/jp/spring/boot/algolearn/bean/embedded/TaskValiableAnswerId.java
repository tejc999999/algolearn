package jp.spring.boot.algolearn.bean.embedded;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskValiableAnswerId  implements Serializable {

	@Column(name="task_id")
	int taskId;

	@Column(name="val_number")
	int valNumber;

}
