package jp.spring.boot.algolearn.controller.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.form.QuestionForm;
import jp.spring.boot.algolearn.repository.QuestionRepository;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

/**
 * 先生用問題Contollerクラス（teacher question Controller Class）
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/question")
public class QuestionController {

    /**
     * 問題リポジトリ(question repository)
     */
    @Autowired
    QuestionRepository questionRepository;

    /**
     * 問題一覧ページ表示(show question list page)
     * @param model 問題一覧保存用モデル(model to save question list)
     * @return 問題一覧ページビュー(question list page view)
     */
    @GetMapping
    String list(Model model) {

        List<QuestionForm> questionFormList = new ArrayList<QuestionForm>();

        for (QuestionBean questionBean : questionRepository.findAll()) {
            QuestionForm questionForm = new QuestionForm();
            BeanUtils.copyProperties(questionBean, questionForm);
            questionForm.setId(String.valueOf(questionBean.getId()));
            questionFormList.add(questionForm);
        }

        model.addAttribute("questions", questionFormList);

        return "teacher/question/list";
    }

    /**
     * 問題登録ページ表示(show add question page)
     * @return 問題登録ページビュー(add question page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {

        return "teacher/question/add";
    }

    /**
     * 問題登録処理(add process for question)
     * @return 問題一覧ページリダイレクト(redirect question list page)
     */
    @PostMapping(path = "add")
    public String addProcess(@Validated QuestionForm form, BindingResult result,
            Model model) {

        QuestionBean questionBean = new QuestionBean();
        BeanUtils.copyProperties(form, questionBean);
        questionRepository.save(questionBean);

        return "redirect:/teacher/question";
    }

    /**
     * 問題編集ページ表示(show edit question page)
     * @return 問題編集ページビュー(edit question page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        Optional<QuestionBean> optQuestion = questionRepository.findById(Integer
                .parseInt(id));
        optQuestion.ifPresent(questionBean -> {
            QuestionForm form = new QuestionForm();
            BeanUtils.copyProperties(questionBean, form);
            form.setId(String.valueOf(questionBean.getId()));

            model.addAttribute("questionForm", form);
        });

        return "teacher/question/edit";
    }

    /**
     * 問題編集処理(edit process for question)
     * @return 問題一覧ページリダイレクト(question list page redirect)
     */
    @PostMapping(path = "editprocess")
    public String editProcess(QuestionForm form, Model model) {

        QuestionBean questionBean = new QuestionBean();
        BeanUtils.copyProperties(form, questionBean);
        questionBean.setId(Integer.parseInt(form.getId()));

        questionRepository.save(questionBean);

        return "redirect:/teacher/question";
    }

    /**
     * 問題削除処理(delete process for question)
     * @return 問題一覧ページリダイレクト(redirect question list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {

        QuestionBean questionBean = new QuestionBean();
        questionBean.setId(Integer.parseInt(id));

        questionRepository.delete(questionBean);

        return "redirect:/teacher/question";
    }
}
