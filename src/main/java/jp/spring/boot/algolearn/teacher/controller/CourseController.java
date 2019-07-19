package jp.spring.boot.algolearn.teacher.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.spring.boot.algolearn.teacher.form.CourseForm;
import jp.spring.boot.algolearn.teacher.service.CourseService;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

/**
 * 先生用コースContollerクラス（teacher class Controller Class）
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/course")
public class CourseController {

    /**
     * コースサービス
     */
    @Autowired
    CourseService courseService;
    
    /**
     * モデルにフォームをセットする(set form the model)
     * @return コースForm(course form)
     */
    @ModelAttribute
    CourseForm setupForm() {
        return new CourseForm();
    }

    /**
     * コース一覧ページ表示(show class list page)
     * @param model コース一覧保存用モデル(model to save class list)
     * @return コース一覧ページビュー(class list page view)
     */
    @GetMapping
    String list(CourseForm form, Model model) {

        // 全コース取得
        List<CourseForm> courseFormList = courseService.findAll();
        model.addAttribute("courses", courseFormList);

        return "teacher/course/list";
    }

    /**
     * コース登録ページ表示(show add course page)
     * @return コース登録ページビュー(add course page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {

        // 全クラス取得
        Map<String, String> classMap = courseService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全学生取得
        Map<String, String> userMap = courseService.findAllStudent();
        model.addAttribute("userCheckItems", userMap);

        return "teacher/course/add";
    }

    /**
     * コース登録処理(add process for course)
     * @return コース一覧ページリダイレクト(redirect course list page)
     */
    @PostMapping(path = "add")
    public String addProcess(@Validated CourseForm form, BindingResult result,
            Model model) {

        // コース情報をDBに保存する
        courseService.save(form);
        
        return "redirect:/teacher/course";
    }

    /**
     * クラス所属ユーザ除外処理(exclude user  belonging course)
     * @return コース登録ページビュー(course add page view)
     */
    @PostMapping(path = "add", params = "userExclusionBtn")
    public String addUserExclusion(@Validated CourseForm form,
            BindingResult result, Model model) {

        // 全クラス取得
        Map<String, String> classMap = courseService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得(クラス所属ユーザを除外する）
        Map<String, String> userMap = courseService.findAllStudent(form.getClassCheckedList());
        model.addAttribute("userCheckItems", userMap);
        
        // コース名の入力状態保持のため
        model.addAttribute("courseForm", form);

        return "teacher/course/add";
    }

    /**
     * コース編集ページ表示(show edit course page)
     * @return コース編集ページビュー(edit course page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        // 全クラス取得
        Map<String, String> classMap = courseService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得
        Map<String, String> userMap = courseService.findAllStudent();
        model.addAttribute("userCheckItems", userMap);
        
        // コースの既存情報
        CourseForm courseForm = courseService.checkClassAndUser(id);
         model.addAttribute("courseForm", courseForm);

        return "teacher/course/edit";
    }

    /**
     * コース編集処理(edit process for course)
     * @return コース一覧ページリダイレクト(course list page redirect)
     */
    @PostMapping(path = "editprocess")
    public String editProcess(CourseForm form, Model model) {

        courseService.save(form);
        
        return "redirect:/teacher/course";
    }

    /**
     * クラス所属ユーザ除外処理(exclude user  belonging course)
     * @return コース編集ページビュー(course edit page view)
     */
    @PostMapping(path = "editprocess", params = "userExclusionBtn")
    public String editUserExclusion(@Validated CourseForm form,
            BindingResult result, Model model) {

        // 全クラス取得
        Map<String, String> classMap = courseService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得(クラス所属ユーザを除外する）
        Map<String, String> userMap = courseService.findAllStudent(form.getClassCheckedList());
        model.addAttribute("userCheckItems", userMap);

        // コース名の入力状態保持のため
        model.addAttribute("courseForm", form);

        return "teacher/course/edit";
    }
    
    /**
     * コース削除処理(delete process for course)
     * @return コース一覧ページリダイレクト(redirect course list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {
        
        courseService.delete(id);

        return "redirect:/teacher/course";
    }
}
