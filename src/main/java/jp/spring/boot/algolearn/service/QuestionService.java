package jp.spring.boot.algolearn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.form.QuestionForm;
import jp.spring.boot.algolearn.repository.QuestionRepository;

/**
 * 先生用問題Serviceクラス（teacher question Service Class）
 * @author tejc999999
 *
 */
@Service
public class QuestionService {

    /**
     * 問題リポジトリ(question repository)
     */
    @Autowired
    QuestionRepository questionRepository;
    
    /**
     * 全ての問題を取得する
     * @return 全ての問題Formリスト
     */
    public List<QuestionForm> findAll() {
        List<QuestionForm> list = new ArrayList<QuestionForm>();

        for (QuestionBean questionBean : questionRepository.findAll()) {
            QuestionForm questionForm = new QuestionForm();
            BeanUtils.copyProperties(questionBean, questionForm);
            questionForm.setId(String.valueOf(questionBean.getId()));
            list.add(questionForm);
        }
        
        return list;
    }
    
    /**
     * 問題を取得する
     * @param id 問題ID
     * @return 問題Form
     */
    public QuestionForm findById(String id) {

        QuestionForm form = new QuestionForm();
        Optional<QuestionBean> optQuestion = questionRepository.findById(Long
                .parseLong(id));
        optQuestion.ifPresent(questionBean -> {
            BeanUtils.copyProperties(questionBean, form);
            form.setId(String.valueOf(questionBean.getId()));
            
        });
        
        return form;
    }
    
    /**
     * 問題を登録する
     * @param form 問題Form
     * @return 登録済み問題Form
     */
    public QuestionForm save(QuestionForm form) {

        QuestionBean questionBean = new QuestionBean();
        BeanUtils.copyProperties(form, questionBean);
        String questionId = form.getId();
        if(questionId != null && !questionId.equals("")) {
            questionBean.setId(Long.parseLong(questionId));
        }
        
        questionBean = questionRepository.save(questionBean);
        
        QuestionForm resultQuestionForm = new QuestionForm();
        BeanUtils.copyProperties(questionBean, resultQuestionForm);
        resultQuestionForm.setId(String.valueOf(questionId));
        
        return resultQuestionForm;
    }
    
    /**
     * 問題削除
     * @param id 問題ID
     */
    public void delete(String id) {

        QuestionBean questionBean = new QuestionBean();
        questionBean.setId(Long.parseLong(id));

        questionRepository.delete(questionBean);
    }
}
