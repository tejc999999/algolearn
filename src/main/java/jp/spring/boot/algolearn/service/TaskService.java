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
    
    @Autowired
    QuestionRepository questionRepository;

    /**
     * 全ての課題を取得する
     * @param 先生ID
     * @return 全ての問題Formリスト
     */
    public List<QuestionForm> findByCreateUser(String teacherId) {
        List<QuestionForm> list = new ArrayList<>();

        for(QuestionBean questionBean : questionRepository.findByPublicFlgTrue()) {
            QuestionForm questionForm = new QuestionForm();
            BeanUtils.copyProperties(questionBean, questionForm);
            questionForm.setId(String.valueOf(questionBean.getId()));
            list.add(questionForm);
        }
        
        for(QuestionBean questionBean : questionRepository.findByCreateUser(teacherId)) {
            QuestionForm questionForm = new QuestionForm();
            BeanUtils.copyProperties(questionBean, questionForm);
            questionForm.setId(String.valueOf(questionBean.getId()));
            list.add(questionForm);
        }
        
        return list;
    }
    
    
}
