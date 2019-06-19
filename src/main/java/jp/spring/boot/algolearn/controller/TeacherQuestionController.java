package jp.spring.boot.algolearn.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.spring.boot.algolearn.config.PrgLanguage;
import jp.spring.boot.algolearn.form.QuestionForm;

/**
 * 教員用問題Contollerクラス
 * 
 * @author tejc999999
 *
 */
@Controller
@RequestMapping("/teacher/question")
public class TeacherQuestionController {

	/**
	 * 課題登録ページ表示
	 * 
	 * @return 課題登録ページパス
	 */
	@GetMapping(path="add")
	public String add(Model model) {
		
		List<String> list = new ArrayList<String>();
		list.add(PrgLanguage.JAVA.toString());
		list.add(PrgLanguage.CCPP.toString());
		list.add(PrgLanguage.PYTHON.toString());
		model.addAttribute("prgLanguageList", list);
		
		return "teacher/question/add";
	}
	
	/**
	 * 課題登録
	 * 
	 * @return 課題登録ページパス
	 */
	@PostMapping(path="add")
	public String questionAdd(Model model, QuestionForm form) {
		
		return add(model);
	}

	/**
	 * 課題編集ページ表示
	 * 
	 * @return 課題登録ページパス
	 */
	@PostMapping(path="edit")
	public String questionEdit() {
		
		
		return "teacher/question/edit";
	}

	/**
	 * 個人用課題一覧ページ表示
	 * 
	 * @return 課題登録ページパス
	 */
	@GetMapping(path="privatelist")
	public String personalList() {
		
		
		return "teacher/question/personallist";
	}
	
	/**
	 * 個人用課題一覧ページ表示
	 * 
	 * @return 課題登録ページパス
	 */
	@GetMapping(path="publiclist")
	public String publicList() {
		
		
		return "teacher/question/publiclist";
	}
}
