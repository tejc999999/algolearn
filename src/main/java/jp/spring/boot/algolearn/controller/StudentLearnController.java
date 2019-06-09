package jp.spring.boot.algolearn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.spring.boot.algolearn.config.AceProperties;
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
	 * セキュリティ情報
	 */
	@Autowired
	AceProperties aceProperties;
//	@Autowired
//	AcePropertiesBean acePropertiesBean;
	
	/**
	 * 学習ページ表示
	 * 
	 * @return 学習ページパス
	 */
	@GetMapping
	public String learn(Model model) {
		
		List<String> themeList = aceProperties.getThemeList();
		List<String> modeList = aceProperties.getModeList();
//		List<String> themeList = acePropertiesBean.getThemeList();
//		List<String> modeList = acePropertiesBean.getModeList();
		
        model.addAttribute("themeList",themeList);
        model.addAttribute("modeList",modeList);
        
		return "student/learn/learn";
	}
	
	/**
	 * 開発エディタ設定変更
	 * 
	 * @return 学習ページパス
	 */
	@PostMapping(path = "editorsetting")
	public String editorSetting(@Validated StudentLearnForm form, BindingResult result , Model model) {
		
		String activeTheme = form.getTheme();
		String activeMode = form.getMode();
		
        model.addAttribute("activeTheme",activeTheme);
        model.addAttribute("activeMode",activeMode);
        
        return learn(model);
	}
}
