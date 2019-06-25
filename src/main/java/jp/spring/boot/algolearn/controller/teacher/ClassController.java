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
import jp.spring.boot.algolearn.form.ClassForm;
import jp.spring.boot.algolearn.repository.ClassRepository;
import jp.spring.boot.algolearn.repository.UserRepository;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

/**
 * 先生用クラスContollerクラス（teacher class Controller Class）
 * 
 * @author tejc999999
 *
 */
@Controller
@RequestMapping("/teacher/class")
public class ClassController {

	@Autowired
	ClassRepository classRepository;

	@Autowired
	UserRepository userRepository;

	@ModelAttribute
	ClassForm setupForm() {
	    return new ClassForm();
	}
	
	/**
	 * クラス一覧ページ表示(show class list page)
	 * 
	 * @param model クラス一覧保存用モデル(model to save class list)
	 * @return クラス一覧ページビュー(class list page view)
	 */
	@GetMapping
	String list(ClassForm form, Model model) {
		
		List<ClassForm> list = new ArrayList<ClassForm>();

		for (ClassBean classBean : classRepository.findAll()) {
			ClassForm classForm = new ClassForm();
			BeanUtils.copyProperties(classBean, classForm);
			classForm.setId(String.valueOf(classBean.getId()));
			list.add(classForm);
		}			

		model.addAttribute("classes", list);

		return "teacher/class/list";
	}
	
	/**
	 * クラス登録ページ表示(show add class page)
	 * 
	 * @return クラス登録ページビュー(add class page view)
	 */
	@GetMapping(path="add")
	public String add(Model model) {
		Map<String, String> userMap = new HashMap<>();
		List<UserBean> userBeanList = userRepository.findAll();
		for(UserBean bean : userBeanList) {
			userMap.put(bean.getId(), bean.getName());
		}
		
		model.addAttribute("userCheckItems", userMap);
		
		return "teacher/class/add";
	}
	
	/**
	 * クラス登録処理(add process for class)
	 * 
	 * @return クラス一覧ページリダイレクト(redirect class list page)
	 */
	@PostMapping(path="add")
	public String addProcess(@Validated ClassForm form, BindingResult result, Model model) {
		
		ClassBean bean = new ClassBean();
		bean.setName(form.getName());

		if(form.getUserCheckedList() != null) {
			Set<UserBean> set = new HashSet<>();
			for(String userId : form.getUserCheckedList()) {
				UserBean userBean = new UserBean();
				userBean.setId(userId);
				set.add(userBean);
			}
			bean.setUserBeans(set);
		}
		classRepository.save(bean);
				
		return "redirect:/teacher/class";
	}

	/**
	 * クラス編集ページ表示(show edit class page)
	 * 
	 * @return クラス編集ページビュー(edit class page view)
	 */
	@PostMapping(path="edit")
	public String edit(@RequestParam String id, Model model) {
		
		// チェックボックスのユーザー一覧
		Map<String, String> userMap = new HashMap<>();
		List<UserBean> userBeanList = userRepository.findAll();
		for(UserBean bean : userBeanList) {
			userMap.put(bean.getId(), bean.getName());
		}
		model.addAttribute("userCheckItems", userMap);

		// クラスの既存情報
		ClassForm form = new ClassForm();
		List<String> list = new ArrayList<>();
		Optional<ClassBean> opt = classRepository.findById(Integer.parseInt(id));
		opt.ifPresent(bean -> {
			form.setId(String.valueOf(bean.getId()));
			form.setName(bean.getName());
			Set<UserBean> userSet = bean.getUserBeans();
			for(UserBean userBean : userSet) {
				list.add(String.valueOf(userBean.getId()));
			}
		});
		form.setUserCheckedList(list);
		model.addAttribute("classForm", form);

		return "teacher/class/edit";
	}
	
	/**
	 * クラス編集処理(edit process for class)
	 * 
	 * @return クラス一覧ページリダイレクト(class list page redirect)
	 */
	@PostMapping(path="editprocess")
	public String editProcess(ClassForm form, Model model) {

		ClassBean bean = new ClassBean();
		bean.setId(Integer.parseInt(form.getId()));
		bean.setName(form.getName());
		
		if(form.getUserCheckedList() != null) {
		Set<UserBean> set = new HashSet<>();
			for(String userId : form.getUserCheckedList()) {
				UserBean userBean = new UserBean();
				userBean.setId(userId);
				set.add(userBean);
			}
			bean.setUserBeans(set);
		}
		classRepository.save(bean);

		return "redirect:/teacher/class";
	}
	
	/**
	 * クラス削除処理(delete process for class)
	 * 
	 * @return クラス一覧ページリダイレクト(redirect class list page)
	 */
	@PostMapping(path="delete")
	public String delete(@RequestParam String id, Model model) {

		ClassBean classBean = new ClassBean();
		classBean.setId(Integer.parseInt(id));
		classRepository.delete(classBean);
		
		return "redirect:/teacher/class";
	}	
}
