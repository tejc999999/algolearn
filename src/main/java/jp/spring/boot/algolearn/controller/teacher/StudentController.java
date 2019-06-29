package jp.spring.boot.algolearn.controller.teacher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.form.StudentForm;
import jp.spring.boot.algolearn.repository.UserRepository;

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
    UserRepository userRepository;

    /**
     * 学生一覧ページ表示(show student list page)
     * @param model 学生一覧保存用モデル(model to save student list)
     * @return 学生一覧ページビュー(student list page view)
     */
    @GetMapping
    String list(Model model) {

        List<StudentForm> studentFormList = new ArrayList<StudentForm>();

        for (UserBean userBean : userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString())) {
            StudentForm userForm = new StudentForm();
            BeanUtils.copyProperties(userBean, userForm);
            studentFormList.add(userForm);
        }

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

        UserBean userBean = new UserBean();
        BeanUtils.copyProperties(form, userBean);
        userRepository.save(userBean);

        return "redirect:/teacher/student";
    }

    /**
     * 学生編集ページ表示(show edit student page)
     * @return 学生編集ページビュー(edit student page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> {
            StudentForm form = new StudentForm();
            BeanUtils.copyProperties(userBean, form);

            model.addAttribute("studentForm", form);
        });

        return "teacher/student/edit";
    }

    /**
     * 学生編集処理(edit process for student)
     * @return 学生一覧ページリダイレクト(student list page redirect)
     */
    @PostMapping(path = "editprocess")
    public String editProcess(StudentForm form, Model model) {

        UserBean userBean = new UserBean();
        BeanUtils.copyProperties(form, userBean);

        userRepository.save(userBean);

        return "redirect:/teacher/student";
    }

    /**
     * 学生削除処理(delete student for question)
     * @return 学生一覧ページリダイレクト(redirect student list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {

        Set<ClassBean> deleteClassSet = new HashSet<>();

        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> {
            Set<ClassBean> classBeanSet = userBean.getClassBeans();
            // java.util.ConcurrentModificationExceptionが発生するので一旦削除用のSetを使用
            if (classBeanSet != null)
                classBeanSet.forEach(classBean -> {
                    deleteClassSet.add(classBean);
                });
            ClassBean[] classBeanArray = new ClassBean[deleteClassSet.size()];
            deleteClassSet.toArray(classBeanArray);
            for (int i = 0; i < classBeanArray.length; i++) {
                userBean.removeFromClass(classBeanArray[i]);
            }

            userRepository.delete(userBean);
        });

        return "redirect:/teacher/student";
    }
}
