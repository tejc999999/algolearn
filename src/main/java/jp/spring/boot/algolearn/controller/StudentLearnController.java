package jp.spring.boot.algolearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.GetMapping;

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
	public String learn() {
		
		return "student/learn/learn";
	}
}
