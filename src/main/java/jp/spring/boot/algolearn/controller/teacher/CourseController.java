package jp.spring.boot.algolearn.controller.teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
 * 
 * @author tejc999999
 *
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
	 * 
	 * @param model コース一覧保存用モデル(model to save class list)
	 * @return コース一覧ページビュー(class list page view)
	 */
	@GetMapping
	String list(CourseForm form, Model model) {
		
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
	 * 
	 * @return コース登録ページビュー(add course page view)
	 */
	@GetMapping(path="add")
	public String add(Model model) {

		Map<String, String> classMap = new HashMap<>();
		List<ClassBean> classBeanList = classRepository.findAll();
		for(ClassBean bean : classBeanList) {
			classMap.put(String.valueOf(bean.getId()), bean.getName());
		}
		
		model.addAttribute("classCheckItems", classMap);
		
		
		Map<String, String> userMap = new HashMap<>();
		List<UserBean> userBeanList = userRepository.findAll();
		for(UserBean bean : userBeanList) {
			userMap.put(bean.getId(), bean.getName());
		}
		
		model.addAttribute("userCheckItems", userMap);
		
		return "teacher/course/add";
	}
	
	/**
	 * コース登録処理(add process for course)
	 * 
	 * @return コース一覧ページリダイレクト(redirect course list page)
	 */
	@PostMapping(path="add")
	public String addProcess(@Validated CourseForm form, BindingResult result, Model model) {
		
//		ClassBean bean = new ClassBean();
//		bean.setName(form.getName());
//
//		if(form.getUserCheckedList() != null) {
//			List<UserBean> list = new ArrayList<>();
//			for(String userId : form.getUserCheckedList()) {
//				UserBean userBean = new UserBean();
//				userBean.setId(userId);
//				list.add(userBean);
//			}
//			bean.setUserBeans(list);
//		}
//		classRepository.save(bean);
				
		return "redirect:/teacher/course";
	}
	
	/**
	 * コース登録処理(add process for course)
	 * 
	 * @return コース一覧ページリダイレクト(redirect course list page)
	 */
	@PostMapping(path="add", params = "userExclusionBtn")
	public String addUserExclusion(@Validated CourseForm form, BindingResult result, Model model) {
		
		Map<String, String> classMap = new HashMap<>();
		List<ClassBean> classBeanList = classRepository.findAll();
		for(ClassBean bean : classBeanList) {
			classMap.put(String.valueOf(bean.getId()), bean.getName());
		}
		
		model.addAttribute("classCheckItems", classMap);
		
		Map<String, String> userMap = new HashMap<>();
		List<UserBean> userBeanList = userRepository.findAll();
		for(UserBean bean : userBeanList) {
			userMap.put(bean.getId(), bean.getName());
		}
		
		if(form.getClassChecks() != null) {
			List<String> list = new ArrayList<>();
			for(String classId : form.getClassChecks()) {
				Optional<ClassBean> opt = classRepository.findById(Integer.parseInt(classId));
				opt.ifPresent(bean -> {
					if(bean.getUserBeans() != null) {
						for(UserBean userBean : bean.getUserBeans()) {
							list.add(userBean.getId());
						}
					}
				});
			}

			if(list != null) {
				for(String userId : list) {
					userMap.remove(userId);
				}
			}
		}
		model.addAttribute("userCheckItems", userMap);
				
		return "/teacher/course/add";
	}

	/**
	 * コース編集ページ表示(show edit course page)
	 * 
	 * @return コース編集ページビュー(edit course page view)
	 */
	@PostMapping(path="edit")
	public String edit(@RequestParam String id, Model model) {
		
//		// チェックボックスのユーザー一覧
//		Map<String, String> userMap = new HashMap<>();
//		List<UserBean> userBeanList = userRepository.findAll();
//		for(UserBean bean : userBeanList) {
//			userMap.put(bean.getId(), bean.getName());
//		}
//		model.addAttribute("userCheckItems", userMap);
//
//		// クラスの既存情報
//		ClassForm form = new ClassForm();
//		List<String> list = new ArrayList<>();
//		Optional<ClassBean> opt = classRepository.findById(Integer.parseInt(id));
//		opt.ifPresent(bean -> {
//			form.setId(String.valueOf(bean.getId()));
//			form.setName(bean.getName());
//			List<UserBean> userList = bean.getUserBeans();
//			for(UserBean userBean : userList) {
//				list.add(String.valueOf(userBean.getId()));
//			}
//		});
//		form.setUserCheckedList(list);
//		model.addAttribute("classForm", form);

		return "teacher/course/edit";
	}
	
	/**
	 * コース編集処理(edit process for course)
	 * 
	 * @return コース一覧ページリダイレクト(course list page redirect)
	 */
	@PostMapping(path="editprocess")
	public String editProcess(CourseForm form, Model model) {

//		ClassBean bean = new ClassBean();
//		bean.setId(Integer.parseInt(form.getId()));
//		bean.setName(form.getName());
//		
//		if(form.getUserCheckedList() != null) {
//		List<UserBean> list = new ArrayList<>();
//			for(String userId : form.getUserCheckedList()) {
//				UserBean userBean = new UserBean();
//				userBean.setId(userId);
//				list.add(userBean);
//			}
//			bean.setUserBeans(list);
//		}
//		classRepository.save(bean);

		return "redirect:/course/class";
	}
	
	/**
	 * コース削除処理(delete process for course)
	 * 
	 * @return コース一覧ページリダイレクト(redirect course list page)
	 */
	@PostMapping(path="delete")
	public String delete(@RequestParam String id, Model model) {

//		ClassBean classBean = new ClassBean();
//		classBean.setId(Integer.parseInt(id));
//		classRepository.delete(classBean);
		
		return "redirect:/course/class";
	}	
}
