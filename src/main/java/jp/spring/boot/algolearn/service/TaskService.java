package jp.spring.boot.algolearn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.bean.TaskBean;
import jp.spring.boot.algolearn.form.QuestionForm;
import jp.spring.boot.algolearn.form.TaskForm;
import jp.spring.boot.algolearn.repository.QuestionRepository;
import jp.spring.boot.algolearn.repository.TaskRepository;

/**
 * 先生用課題Serviceクラス（teacher task Service Class）
 * @author tejc999999
 *
 */
@Service
public class TaskService {

    /**
     * 課題リポジトリ(task repository)
     */
    @Autowired
    TaskRepository taskRepository;

    /**
     * 全ての課題を取得する
     * @return 全ての問題Formリスト
     */
    public List<TaskForm> findAll() {
        List<TaskForm> list = new ArrayList<TaskForm>();

        for (TaskBean taskBean : taskRepository.findAll()) {
            TaskForm taskForm = new TaskForm();
            BeanUtils.copyProperties(taskBean, taskForm);
            taskForm.setId(String.valueOf(taskBean.getId()));
            list.add(taskForm);
        }
        
        return list;
    }
    
    
}
