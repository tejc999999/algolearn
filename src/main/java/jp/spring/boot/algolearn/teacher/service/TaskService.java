package jp.spring.boot.algolearn.teacher.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.bean.TaskBean;
import jp.spring.boot.algolearn.repository.QuestionRepository;
import jp.spring.boot.algolearn.repository.TaskRepository;
import jp.spring.boot.algolearn.teacher.form.QuestionForm;
import jp.spring.boot.algolearn.teacher.form.TaskForm;

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
     * @return 全ての問題Formリスト
     */
    public List<QuestionForm> findAll() {
        List<QuestionForm> list = new ArrayList<>();

        for(QuestionBean questionBean : questionRepository.findAll()) {
            QuestionForm questionForm = new QuestionForm();
            questionForm.setId(String.valueOf(questionBean.getId()));
            questionForm.setTitle(questionBean.getTitle());
            questionForm.setDescription(questionBean.getDescription());
            questionForm.setInputNum(questionBean.getInputNum());
            list.add(questionForm);
        }
        
        return list;
    }
    
    
    /**
     * 全ての課題を取得する
     * @param 先生ID
     * @return 全ての問題Formリスト
     */
    public List<QuestionForm> findByTitleLikeOrDescriptionLike(String searchStr) {
        List<QuestionForm> list = new ArrayList<>();
        

        for(QuestionBean questionBean : questionRepository.findByTitleLikeOrDescriptionLike(searchStr, searchStr)) {
            QuestionForm questionForm = new QuestionForm();
            questionForm.setId(String.valueOf(questionBean.getId()));
            questionForm.setTitle(questionBean.getTitle());
            questionForm.setDescription(questionBean.getDescription());
            questionForm.setInputNum(questionBean.getInputNum());
            list.add(questionForm);
        }
        
        return list;
    }
    
    
}
