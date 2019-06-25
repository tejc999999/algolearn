package jp.spring.boot.algolearn.controller.student;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.spring.boot.algolearn.config.PrgLanguage;
import jp.spring.boot.algolearn.config.PrgLanguageProperties;
import jp.spring.boot.algolearn.config.PrgLanguagePropertiesDetail;
import jp.spring.boot.algolearn.config.ServerProperties;
import jp.spring.boot.algolearn.form.LearnForm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 学生用学習Contollerクラス
 * 
 * @author tejc999999
 *
 */
@Controller
@RequestMapping("/student")
public class LearnController {
	
		
	@Autowired
	PrgLanguageProperties prgLanguageProperties;
	
	@Autowired
	ServerProperties serverProperties;
	
	/**
	 * 学習ページ表示
	 * 
	 * @return 学習ページパス
	 */
	@GetMapping(path="learn")
	public String learn(Model model) {
		
		if(!model.containsAttribute("code")) {
			
			String lineFeedCode = System.getProperty("line.separator");
			
			model.addAttribute("code", "public class Test {" + lineFeedCode
				    + "    public static void main(String[] args) {" + lineFeedCode
				    + "        System.out.println(\"TEST\");" + lineFeedCode
				    + "    }" + lineFeedCode
				    + "}" + lineFeedCode
				    );
		}
		
		return "student/learn";
	}
	
	/**
	 * ビルド・実行処理
	 * 
	 * @param form 学生用学習ページForm
	 * @param model モデル
	 * @return 遷移先view
	 */
	@PostMapping(path="learn")
	public String learnExecute(LearnForm form, Model model) {
		
		String code = form.getCode();
        StringBuilder sb = new StringBuilder();
        
        String plCode = form.getProgrammingLanguage();
        
		String lineFeedCode = System.getProperty("line.separator");
        
        if(PrgLanguage.JAVA.toString().equals(plCode)) {
        	// Javaの場合
        	
//        	List<PrgLanguagePropertyDetail> plList = prgBuildProperties.getList();
        	PrgLanguagePropertiesDetail prgLanguagePropertiesDetail = prgLanguageProperties.getMap().get(PrgLanguage.JAVA.toString());
        	
            try {
            	BufferedReader reader = new BufferedReader(new StringReader(code));

            	// ファイル書き込み処理を文字コード指定に変更
            	// close処理を正しくする
           	    FileOutputStream fos =  new FileOutputStream(prgLanguagePropertiesDetail.getWorkFolderPath() + java.io.File.separator + prgLanguagePropertiesDetail.getFileName());
           	    OutputStreamWriter  osw = new OutputStreamWriter(fos, serverProperties.getCharacterCode());
            	String line2 = null;
                while ((line2 = reader.readLine()) != null) {
                	osw.write(line2 + "\n");
                }
                osw.close();
//                FileWriter fw = new FileWriter(prgLanguagePropertiesDetail.getWorkFolderPath() + java.io.File.separator + prgLanguagePropertiesDetail.getFileName());
//                fw.write(code);
//                fw.close();
            	
                Process p = Runtime.getRuntime().exec("\"" + prgLanguagePropertiesDetail.getBuildCmdPath() + "\" \"" + prgLanguagePropertiesDetail.getWorkFolderPath() + java.io.File.separator + prgLanguagePropertiesDetail.getFileName() + "\"");
                InputStream is = p.getInputStream();
                InputStream es = p.getErrorStream();
                BufferedReader br= new BufferedReader(new InputStreamReader(is, serverProperties.getCharacterCode()));
                String line;
    	        while ((line = br.readLine()) != null) {
    	            sb.append(line);
    	            sb.append(lineFeedCode);
    	        }
                br= new BufferedReader(new InputStreamReader(es, serverProperties.getCharacterCode()));
    	        while ((line = br.readLine()) != null) {
    	            sb.append(line);
    	            sb.append(lineFeedCode);
    	        }
    	        
                p.waitFor();    // プロセスが終了するまで待機する
                p.destroy();
                
                if(sb.toString() == null || sb.toString().equals("")) {
	                // エラーが発生していない場合のみ、実行
	                
	
	                p = Runtime.getRuntime().exec("\"" + prgLanguagePropertiesDetail.getExecuteCmdPath() + "\" -cp  \""+ prgLanguagePropertiesDetail.getWorkFolderPath() + "\" " + prgLanguagePropertiesDetail.getBuildFileName());
	                is = p.getInputStream();
	                es = p.getErrorStream();
	                br= new BufferedReader(new InputStreamReader(is, serverProperties.getCharacterCode()));
	    	        while ((line = br.readLine()) != null) {
	    	            sb.append(line);
	    	            sb.append(lineFeedCode);
	    	        }
	                br= new BufferedReader(new InputStreamReader(es, serverProperties.getCharacterCode()));
	    	        while ((line = br.readLine()) != null) {
	    	            sb.append(line);
	    	            sb.append(lineFeedCode);
	    	        }
	                p.waitFor();    // プロセスが終了するまで待機する
	                p.destroy();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	
        } else if(PrgLanguage.CCPP.toString().equals(plCode)) {
        	// Cの場合
        	
        } else if(PrgLanguage.PYTHON.toString().equals(plCode)) {
        	// Pythonの場合
        	
        } else {
        	// 登録されていないプログラミング言語の場合
        	sb.append("予期せぬエラーが発生しました。");
        }
		
		model.addAttribute("result", sb.toString());
		model.addAttribute("code", code);

		return learn(model);
	}
}
