package jp.spring.boot.algolearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.spring.boot.algolearn.form.StudentLearnForm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 学生用学習Contollerクラス
 * 
 * @author tejc999999
 *
 */
@Controller
@RequestMapping("/student/learn")
public class StudentLearnController {
	
	/**
	 * 学習ページ表示
	 * 
	 * @return 学習ページパス
	 */
	@GetMapping
	public String learn(Model model) {
		
		return "student/learn/learn";
	}
	
	@PostMapping(path="execute")
	public String execute(StudentLearnForm form, Model model) {
		model.addAttribute("code", form.getCode());
		return learn(model);
	}
}
