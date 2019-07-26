package jp.spring.boot.algolearn.teacher.controller;

import java.util.List;

import jp.spring.boot.algolearn.teacher.form.QuestionForm;
import jp.spring.boot.algolearn.teacher.form.StudentForm;
import jp.spring.boot.algolearn.teacher.form.TaskAddCodeForm;
import jp.spring.boot.algolearn.teacher.form.TaskAddSearchForm;
import jp.spring.boot.algolearn.teacher.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 先生用課題Contollerクラス（teacher task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    /**
     * 課題登録用問題一覧ページ表示(show question list page for task add).
     * @param model 問題一覧保存用モデル(model to save question list)
     * @return 課題登録用問題一覧ページビュー(question list page view for task add)
     */
    @GetMapping(path = "addlist")
    String addlist(Model model) {

        List<QuestionForm> list = taskService.findAll();

        model.addAttribute("questions", list);

        return "teacher/task/addlist";
    }
    
    /**
     * 課題登録用問題検索(question search for task add).
     * @param form 検索Form(serach form)
     * @param result エラーチェック結果(error validate result)
     * @param model 問題一覧保存用モデル(model to save question list)
     * @return 課題登録用問題一覧ページビュー(question list page view for task add)
     */
    @PostMapping(path = "addsearch")
    public String addSearch(@Validated TaskAddSearchForm form, BindingResult result,
            Model model) {

        // TODO:あとで作成者ごとに登録        
        List<QuestionForm> list = taskService
                .findByTitleLikeOrDescriptionLike("%" + form.getSearchWord() + "%");
       
        model.addAttribute("questions", list);

        return "/teacher/task/addlist";
    }

    /**
     * 課題自動作成画面表示(question list page view for task add).
     * @return 課題自動作成ページビュー(auto create task page view)
     */
    @PostMapping(path = "addProcess")
    public String addProcess(@Validated TaskAddCodeForm form, BindingResult result,
            Model model) {

        taskService.save(form);
        

        return addlist(model);
    }

    /**
     * 課題登録コード画面表示.
     * @param taskAddCodeForm 課題登録コードForm
     * @param result 
     * @param model 
     * @return 課題コード登録ページビュー
     */
    @PostMapping(path = "addcode")
    public String addCode(@Validated TaskAddCodeForm taskAddCodeForm, BindingResult result,
            Model model) {
        
        try {
        
            taskAddCodeForm = taskService.getTaskCode(taskAddCodeForm);
            
            model.addAttribute("taskAddCodeForm", taskAddCodeForm);
            
            return "/teacher/task/addcode";
            
        } catch (Exception e) {
            
            return addAuto(taskAddCodeForm.getQuestionId(), model);
        }
    }


    /**
     * 課題自動コード生成画面表示.
     * @param id 問題ID
     * @param model モデル(model)
     * @return 課題自動コード生成ページビュー
     */
    @PostMapping(path = "addauto")
    public String addAuto(@RequestParam String id, Model model) {
        
        TaskAddCodeForm taskAddCodeForm = taskService.initAutoCodeData(id);
        model.addAttribute("taskAddCodeForm", taskAddCodeForm);
        
        return "/teacher/task/addauto";
    }

    /**
     * 学生編集ページ表示(show edit student page).
     * @return 学生編集ページビュー(edit student page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        // Optional<UserBean> opt = userRepository.findById(userId);
        // opt.ifPresent(bean -> {
        // StudentForm form = new StudentForm();
        // BeanUtils.copyProperties(bean, form);
        //
        // model.addAttribute("studentForm", form);
        // });

        return "teacher/task/edit";
    }

    /**
     * 学生編集処理(edit process for student).
     * @return 学生一覧ページリダイレクト(student list page redirect)
     */
    @PostMapping(path = "editprocess")
    public String editProcess(StudentForm form, Model model) {

        // UserBean bean = new UserBean();
        // BeanUtils.copyProperties(form, bean);
        //
        // userRepository.save(bean);

        return "redirect:/teacher/task";
    }

    /**
     * 学生削除処理(delete student for question).
     * @return 学生一覧ページリダイレクト(redirect student list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String userId, Model model) {

        // UserBean bean = new UserBean();
        // bean.setUserId(userId);
        //
        // userRepository.delete(bean);

        return "redirect:/teacher/task";
    }
}
