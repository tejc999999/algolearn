package jp.spring.boot.algolearn.controller;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

		String code = form.getCode();
        StringBuilder sb = new StringBuilder();
        try {
            FileWriter fw = new FileWriter("Test.java");
            fw.write(code);
            fw.close();
            
            Process p = Runtime.getRuntime().exec("javac Test.java");
            InputStream is = p.getInputStream();
            InputStream es = p.getErrorStream();
            BufferedReader br= new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }
            br= new BufferedReader(new InputStreamReader(es, "utf-8"));
	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }
            p.waitFor();    // プロセスが終了するまで待機する
            p.destroy();

            p = Runtime.getRuntime().exec("java Test");
            is = p.getInputStream();
            es = p.getErrorStream();
            br= new BufferedReader(new InputStreamReader(is, "utf-8"));
	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }
            br= new BufferedReader(new InputStreamReader(es, "utf-8"));
	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }
            p.waitFor();    // プロセスが終了するまで待機する
            p.destroy();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("code", sb.toString());

		return learn(model);
	}
}
