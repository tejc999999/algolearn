package jp.spring.boot.algolearn.controller.teacher;

import java.util.ArrayList;
import java.util.Arrays;
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

import jp.spring.boot.algolearn.bean.CourseBean;
import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.bean.ClassBean;
import jp.spring.boot.algolearn.bean.CourseBean;
import jp.spring.boot.algolearn.form.ClassForm;
import jp.spring.boot.algolearn.form.CourseForm;
import jp.spring.boot.algolearn.repository.ClassRepository;
import jp.spring.boot.algolearn.repository.CourseRepository;
import jp.spring.boot.algolearn.repository.UserRepository;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

/**
 * 先生用コースContollerクラス（teacher class Controller Class）
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/course")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    UserRepository userRepository;

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
        List<CourseForm> list = new ArrayList<CourseForm>();
        for (CourseBean courseBean : courseRepository.findAll()) {
            CourseForm courseForm = new CourseForm();
            BeanUtils.copyProperties(courseBean, courseForm);
            courseForm.setId(String.valueOf(courseBean.getId()));
            list.add(courseForm);
        }
        model.addAttribute("courses", list);

        return "teacher/course/list";
    }

    /**
     * コース登録ページ表示(show add course page)
     * @return コース登録ページビュー(add course page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {

        // 全クラス取得
        Map<String, String> classMap = new HashMap<>();
        List<ClassBean> classBeanList = classRepository.findAll();
        if (classBeanList != null)
            classBeanList.forEach(bean -> {
                classMap.put(String.valueOf(bean.getId()), bean.getName());
            });
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得
        Map<String, String> userMap = new HashMap<>();
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString());
        if (userBeanList != null)
            userBeanList.forEach(bean -> {
                userMap.put(bean.getId(), bean.getName());
            });
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

        CourseBean courseBean = new CourseBean();
        courseBean.setName(form.getName());
        
        Set<ClassBean> classBeanSet = new HashSet<>();        
        List<String> classIdList = form.getClassCheckedList();
        if(classIdList != null) classIdList.forEach(classId -> {
            Optional<ClassBean> opt = classRepository.findById(Integer.parseInt(classId));
            opt.ifPresent(classBean -> {
                classBeanSet.add(classBean);
            });
        });
        courseBean.setClassBeans(classBeanSet);
        
        Set<UserBean> userBeanSet = new HashSet<>();
        List<String> userIdList = form.getUserCheckedList();
        if(userIdList != null) userIdList.forEach(userId -> {
            Optional<UserBean> opt = userRepository.findById(userId);
            opt.ifPresent(userBean -> {
                userBeanSet.add(userBean);
            });
        });

        courseBean.setUserBeans(userBeanSet);
        courseRepository.save(courseBean);
        
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
        Map<String, String> classMap = new HashMap<>();
        List<ClassBean> classBeanList = classRepository.findAll();
        if (classBeanList != null)
            classBeanList.forEach(bean -> {
                classMap.put(String.valueOf(bean.getId()), bean.getName());
            });
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得後、選択済みクラスに所属するユーザを除外
        Map<String, String> userMap = new HashMap<>();
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString());
        if (userBeanList != null)
            userBeanList.forEach(bean -> {
                userMap.put(bean.getId(), bean.getName());
            });

        if (form.getClassCheckedList() != null) {
            List<String> userLlist = new ArrayList<>();
            List<String> classList = form.getClassCheckedList();
            if (classList != null)
                classList.forEach(classId -> {
                    Optional<ClassBean> opt = classRepository.findById(Integer
                            .parseInt(classId));
                    opt.ifPresent(classBean -> {
                        Set<UserBean> userBeanSet = classBean.getUserBeans();
                        if (userBeanSet != null)
                            userBeanSet.forEach(userBean -> {
                                userLlist.add(userBean.getId());
                            });
                    });
                });
            // 選択済みクラス所属ユーザを除外
            if (userLlist != null)
                userLlist.forEach(userId -> {
                    userMap.remove(userId);
                });
        }
        model.addAttribute("userCheckItems", userMap);

        return "teacher/course/add";
    }

    /**
     * コース編集ページ表示(show edit course page)
     * @return コース編集ページビュー(edit course page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        // 全クラス取得
        Map<String, String> classMap = new HashMap<>();
        List<ClassBean> classBeanList = classRepository.findAll();
        if (classBeanList != null)
            classBeanList.forEach(bean -> {
                classMap.put(String.valueOf(bean.getId()), bean.getName());
            });
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得
        Map<String, String> userMap = new HashMap<>();
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString());
        if (userBeanList != null)
            userBeanList.forEach(bean -> {
                userMap.put(bean.getId(), bean.getName());
            });
        model.addAttribute("userCheckItems", userMap);
        
        // コースの既存情報
        CourseForm courseForm = new CourseForm();
        Optional<CourseBean> optBean = courseRepository.findById(Integer.parseInt(id));
        optBean.ifPresent(courseBean -> {
            BeanUtils.copyProperties(courseBean, courseForm);
            courseForm.setId(String.valueOf(courseBean.getId()));
            Set<ClassBean> classBeanSet = courseBean.getClassBeans();
            List<String> classIdList = new ArrayList<>();
            if(classBeanSet != null) classBeanSet.forEach(classBean -> {
                classIdList.add(String.valueOf(classBean.getId()));
            });
            courseForm.setClassCheckedList(classIdList);
            Set<UserBean> userBeanSet = courseBean.getUserBeans();
            List<String> userIdList = new ArrayList<>();
            if(userBeanSet != null) userBeanSet.forEach(userBean -> {
                userIdList.add(userBean.getId());
            });
            courseForm.setUserCheckedList(userIdList);
        });

         model.addAttribute("courseForm", courseForm);

        return "teacher/course/edit";
    }

    /**
     * コース編集処理(edit process for course)
     * @return コース一覧ページリダイレクト(course list page redirect)
     */
    @PostMapping(path = "editprocess")
    public String editProcess(CourseForm form, Model model) {

        CourseBean courseBean = new CourseBean();
        courseBean.setId(Integer.parseInt(form.getId()));
        courseBean.setName(form.getName());
        // 所属クラス登録
        Set<ClassBean> classBeanSet = new HashSet<>();
        List<String> classIdList = form.getClassCheckedList();
        if(classIdList != null) classIdList.forEach(classId -> {
            Optional<ClassBean> optClass = classRepository.findById(Integer.parseInt(classId));
            optClass.ifPresent(classBean -> {
                classBeanSet.add(classBean);
            });
        });
        courseBean.setClassBeans(classBeanSet);
        // 所属ユーザー登録
        Set<UserBean> userBeanSet = new HashSet<>();
        List<String> userIdList = form.getUserCheckedList();
        if(userIdList != null) userIdList.forEach(userId -> {
            Optional<UserBean> optUser = userRepository.findById(userId);
            optUser.ifPresent(userBean -> {
                userBeanSet.add(userBean);
            });
        });
        courseBean.setUserBeans(userBeanSet);

        courseRepository.save(courseBean);
        
        return "redirect:/teacher/course";
    }

    /**
     * クラス所属ユーザ除外処理(exclude user  belonging course)
     * @return コース編集ページビュー(course edit page view)
     */
    @PostMapping(path = "edit", params = "userExclusionBtn")
    public String editUserExclusion(@Validated CourseForm form,
            BindingResult result, Model model) {

        // 全クラス取得
        Map<String, String> classMap = new HashMap<>();
        List<ClassBean> classBeanList = classRepository.findAll();
        if (classBeanList != null)
            classBeanList.forEach(bean -> {
                classMap.put(String.valueOf(bean.getId()), bean.getName());
            });
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得後、選択済みクラスに所属するユーザを除外
        Map<String, String> userMap = new HashMap<>();
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getString());
        if (userBeanList != null)
            userBeanList.forEach(bean -> {
                userMap.put(bean.getId(), bean.getName());
            });

        if (form.getClassCheckedList() != null) {
            List<String> userLlist = new ArrayList<>();
            List<String> classList = form.getClassCheckedList();
            if (classList != null)
                classList.forEach(classId -> {
                    Optional<ClassBean> opt = classRepository.findById(Integer
                            .parseInt(classId));
                    opt.ifPresent(classBean -> {
                        Set<UserBean> userBeanSet = classBean.getUserBeans();
                        if (userBeanSet != null)
                            userBeanSet.forEach(userBean -> {
                                userLlist.add(userBean.getId());
                            });
                    });
                });
            // 選択済みクラス所属ユーザを除外
            if (userLlist != null)
                userLlist.forEach(userId -> {
                    userMap.remove(userId);
                });
        }
        model.addAttribute("userCheckItems", userMap);

        return "teacher/course/edit";
    }
    
    /**
     * コース削除処理(delete process for course)
     * @return コース一覧ページリダイレクト(redirect course list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {
        
        CourseBean courseBean = new CourseBean();
        courseBean.setId(Integer.parseInt(id));
        courseRepository.delete(courseBean);

        return "redirect:/teacher/course";
    }
}
