package jp.spring.boot.algolearn.controller.teacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.spring.boot.algolearn.form.StudentForm;
import jp.spring.boot.algolearn.service.StudentService;

/**
 * 先生用学生Contollerクラス（teacher student Controller Class）
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/student")
public class StudentController {

    /**
     * ユーザーリポジトリ(user repository)
     */
    @Autowired
    StudentService studentService;

    /**
     * 学生一覧ページ表示(show student list page)
     * @param model 学生一覧保存用モデル(model to save student list)
     * @return 学生一覧ページビュー(student list page view)
     */
    @GetMapping
    String list(Model model) {

        List<StudentForm> studentFormList = studentService.findAll();

        model.addAttribute("students", studentFormList);

        return "teacher/student/list";
    }

    /**
     * 学生登録ページ表示(show add student page)
     * @return 学生登録ページビュー(add student page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {

        return "teacher/student/add";
    }

    /**
     * 学生登録処理(add process for student)
     * @return 学生一覧ページリダイレクト(redirect student list page)
     */
    @PostMapping(path = "add")
    public String addProcess(@Validated StudentForm form, BindingResult result,
            Model model) {

        studentService.save(form);

        return "redirect:/teacher/student";
    }

    /**
     * 学生編集ページ表示(show edit student page)
     * @return 学生編集ページビュー(edit student page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        StudentForm studentForm = studentService.findById(id);
        
        model.addAttribute("studentForm", studentForm);

        return "teacher/student/edit";
    }

    /**
     * 学生編集処理(edit process for student)
     * @return 学生一覧ページリダイレクト(student list page redirect)
     */
    @PostMapping(path = "editprocess")
    public String editProcess(StudentForm form, Model model) {
        
        studentService.save(form);

        return "redirect:/teacher/student";
    }

    /**
     * 学生削除処理(delete student for question)
     * @return 学生一覧ページリダイレクト(redirect student list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {

        studentService.delete(id);

        return "redirect:/teacher/student";
    }
}
