package jp.spring.boot.algolearn.controller.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.form.ClassForm;
import jp.spring.boot.algolearn.repository.ClassRepository;
import jp.spring.boot.algolearn.repository.UserRepository;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

/**
 * 先生用クラスContollerクラス（teacher class Controller Class）
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/class")
public class ClassController {

    /**
     * クラス用リポジトリ(class repository)
     */
    @Autowired
    ClassRepository classRepository;

    /**
     * ユーザー用リポジトリ(user repository)
     */
    @Autowired
    UserRepository userRepository;

    /**
     * モデルにフォームをセット(set form the model)
     * @return クラスForm(class form)
     */
    @ModelAttribute
    ClassForm setupForm() {
        return new ClassForm();
    }

    /**
     * クラス一覧ページ表示(show class list page)
     * @param model クラス一覧保存用モデル(model to save class list)
     * @return クラス一覧ページビュー(class list page view)
     */
    @GetMapping
    String list(ClassForm form, Model model) {

        List<ClassForm> classFormList = new ArrayList<ClassForm>();

        for (ClassBean classBean : classRepository.findAll()) {
            ClassForm classForm = new ClassForm();
            BeanUtils.copyProperties(classBean, classForm);
            classForm.setId(String.valueOf(classBean.getId()));
            classFormList.add(classForm);
        }

        model.addAttribute("classes", classFormList);

        return "teacher/class/list";
    }

    /**
     * クラス登録ページ表示(show add class page)
     * @return クラス登録ページビュー(add class page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {
        Map<String, String> userMap = new HashMap<>();
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString());
        if (userBeanList != null)
            userBeanList.forEach(userBean -> {
                userMap.put(userBean.getId(), userBean.getName());
            });

        model.addAttribute("userCheckItems", userMap);

        return "teacher/class/add";
    }

    /**
     * クラス登録処理(add process for class)
     * @return クラス一覧ページリダイレクト(redirect class list page)
     */
    @PostMapping(path = "add")
    public String addProcess(@Validated ClassForm form, BindingResult result,
            Model model) {

        ClassBean classBean = new ClassBean();
        classBean.setName(form.getName());

        if (form.getUserCheckedList() != null) {
            Set<UserBean> userBeanSet = new HashSet<>();
            List<String> userIdList = form.getUserCheckedList();
            if (userIdList != null)
                userIdList.forEach(userId -> {
                    UserBean userBean = new UserBean();
                    userBean.setId(userId);
                    userBeanSet.add(userBean);
                });
            classBean.setUserBeans(userBeanSet);
        }
        classRepository.save(classBean);

        return "redirect:/teacher/class";
    }

    /**
     * クラス編集ページ表示(show edit class page)
     * @return クラス編集ページビュー(edit class page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        // チェックボックスのユーザー一覧
        Map<String, String> userMap = new HashMap<>();
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString());
        if (userBeanList != null)
            userBeanList.forEach(bean -> {
                userMap.put(bean.getId(), bean.getName());
            });
        model.addAttribute("userCheckItems", userMap);

        // クラスの既存情報
        ClassForm classForm = new ClassForm();
        List<String> userCheckedList = new ArrayList<>();
        Optional<ClassBean> optClass = classRepository.findById(Integer.parseInt(
                id));
        optClass.ifPresent(classBean -> {
            classForm.setId(String.valueOf(classBean.getId()));
            classForm.setName(classBean.getName());
            Set<UserBean> userSet = classBean.getUserBeans();
            if (userSet != null)
                userSet.forEach(userBean -> {
                    userCheckedList.add(String.valueOf(userBean.getId()));
                });
        });
        classForm.setUserCheckedList(userCheckedList);
        model.addAttribute("classForm", classForm);

        return "teacher/class/edit";
    }

    /**
     * クラス編集処理(edit process for class)
     * @return クラス一覧ページリダイレクト(class list page redirect)
     */
    @PostMapping(path = "editprocess")
    public String editProcess(ClassForm form, Model model) {

        ClassBean classBean = new ClassBean();
        classBean.setId(Integer.parseInt(form.getId()));
        classBean.setName(form.getName());

        if (form.getUserCheckedList() != null) {
            Set<UserBean> userBeanSet = new HashSet<>();
            List<String> userIdList = form.getUserCheckedList();
            if (userIdList != null)
                userIdList.forEach(userId -> {
                    UserBean userBean = new UserBean();
                    userBean.setId(userId);
                    userBeanSet.add(userBean);
                });
            classBean.setUserBeans(userBeanSet);
        }
        classRepository.save(classBean);

        return "redirect:/teacher/class";
    }

    /**
     * クラス削除処理(delete process for class)
     * @return クラス一覧ページリダイレクト(redirect class list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {

        ClassBean classBean = new ClassBean();
        classBean.setId(Integer.parseInt(id));
        classRepository.delete(classBean);

        return "redirect:/teacher/class";
    }
}
