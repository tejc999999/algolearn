package jp.spring.boot.algolearn.controller.teacher;

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
import jp.spring.boot.algolearn.service.TaskService;

/**
 * 先生用課題Contollerクラス（teacher task Controller Class）
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    /**
     * 学生一覧ページ表示(show student list page)
     * @param model 学生一覧保存用モデル(model to save student list)
     * @return 学生一覧ページビュー(student list page view)
     */
    @GetMapping
    String list(Model model) {

        // List<StudentForm> list = new ArrayList<StudentForm>();
        //
        // for (UserBean userBean : userRepository.findAll()) {
        // StudentForm userForm = new StudentForm();
        // BeanUtils.copyProperties(userBean, userForm);
        // list.add(userForm);
        // }
        //
        // model.addAttribute("students", list);

        return "teacher/task/list";
    }

    /**
     * 学生登録ページ表示(show add student page)
     * @return 学生登録ページビュー(add student page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {

        return "teacher/task/add";
    }

    /**
     * 学生登録処理(add process for student)
     * @return 学生一覧ページリダイレクト(redirect student list page)
     */
    @PostMapping(path = "add")
    public String addProcess(@Validated StudentForm form, BindingResult result,
            Model model) {

        // UserBean bean = new UserBean();
        // BeanUtils.copyProperties(form, bean);
        // userRepository.save(bean);

        return "redirect:/teacher/task";
    }

    /**
     * 学生編集ページ表示(show edit student page)
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
     * 学生編集処理(edit process for student)
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
     * 学生削除処理(delete student for question)
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
