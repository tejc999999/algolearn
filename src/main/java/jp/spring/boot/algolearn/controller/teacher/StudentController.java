package jp.spring.boot.algolearn.controller.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.bean.UserBean;
import jp.spring.boot.algolearn.config.RoleCode;
import jp.spring.boot.algolearn.form.QuestionForm;
import jp.spring.boot.algolearn.form.StudentForm;
import jp.spring.boot.algolearn.repository.UserRepository;

/**
 * 先生用学生Contollerクラス（teacher student Controller Class）
 * 
 * @author tejc999999
 *
 */
@Controller
@RequestMapping("/teacher/student")
public class StudentController {

	@Autowired
	UserRepository userRepository;
	
	/**
	 * 学生一覧ページ表示(show student list page)
	 * 
	 * @param model 学生一覧保存用モデル(model to save student list)
	 * @return 学生一覧ページビュー(student list page view)
	 */
	@GetMapping
	String list(Model model) {
		
		List<StudentForm> list = new ArrayList<StudentForm>();

		for (UserBean userBean : userRepository.findAll()) {
			StudentForm userForm = new StudentForm();
			BeanUtils.copyProperties(userBean, userForm);
			list.add(userForm);
		}
		
		model.addAttribute("students", list);

		return "teacher/student/list";
	}
	
	/**
	 * 学生登録ページ表示(show add student page)
	 * 
	 * @return 学生登録ページビュー(add student page view)
	 */
	@GetMapping(path="add")
	public String add(Model model) {
		
		return "teacher/student/add";
	}
	
	/**
	 * 学生登録処理(add process for student)
	 * 
	 * @return 学生一覧ページリダイレクト(redirect student list page)
	 */
	@PostMapping(path="add")
	public String addProcess(@Validated StudentForm form, BindingResult result, Model model) {
		
		UserBean bean = new UserBean();
		BeanUtils.copyProperties(form, bean);
		userRepository.save(bean);
		
		return "redirect:/teacher/student";
	}

	/**
	 * 学生編集ページ表示(show edit student page)
	 * 
	 * @return 学生編集ページビュー(edit student page view)
	 */
	@PostMapping(path="edit")
	public String edit(@RequestParam String userId, Model model) {

		Optional<UserBean> opt = userRepository.findById(userId);
		opt.ifPresent(bean -> {
			StudentForm form = new StudentForm();
			BeanUtils.copyProperties(bean, form);
			
			model.addAttribute("studentForm", form);
		});

		return "teacher/student/edit";
	}
	
	/**
	 * 学生編集処理(edit process for student)
	 * 
	 * @return 学生一覧ページリダイレクト(student list page redirect)
	 */
	@PostMapping(path="editprocess")
	public String editProcess(StudentForm form, Model model) {
		
		UserBean bean = new UserBean();
		BeanUtils.copyProperties(form, bean);

		userRepository.save(bean);
		
		return "redirect:/teacher/student";
	}
	
	/**
	 * 学生削除処理(delete student for question)
	 * 
	 * @return 学生一覧ページリダイレクト(redirect student list page)
	 */
	@PostMapping(path="delete")
	public String delete(@RequestParam String userId, Model model) {
		
		UserBean bean = new UserBean();
		bean.setId(userId);
		
		userRepository.delete(bean);
		
		return "redirect:/teacher/student";
	}	
}
