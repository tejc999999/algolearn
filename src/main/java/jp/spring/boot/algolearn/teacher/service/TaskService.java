package jp.spring.boot.algolearn.teacher.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.bean.TaskBean;
import jp.spring.boot.algolearn.config.PrgLanguageCode;
import jp.spring.boot.algolearn.config.PrgLanguageProperties;
import jp.spring.boot.algolearn.config.PrgLanguagePropertiesDetail;
import jp.spring.boot.algolearn.config.ServerProperties;
import jp.spring.boot.algolearn.repository.QuestionRepository;
import jp.spring.boot.algolearn.repository.TaskRepository;
import jp.spring.boot.algolearn.teacher.form.QuestionForm;
import jp.spring.boot.algolearn.teacher.form.TaskAddCodeForm;
import jp.spring.boot.algolearn.teacher.form.TaskForm;

/**
 * 先生用課題Serviceクラス（teacher task Service Class）
 * @author tejc999999
 *
 */
@Service
public class TaskService {

    /**
     * 課題リポジトリ(task repository)
     */
    @Autowired
    TaskRepository taskRepository;
    
    @Autowired
    QuestionRepository questionRepository;
    
    
    /**
     * 
     */
    @Autowired
    PrgLanguageProperties prgLanguageProperties;

    /**
     * 
     */
    @Autowired
    ServerProperties serverProperties;
    
    /**
     * 全ての問題を取得する
     * @return 全ての問題Formリスト
     */
    public List<QuestionForm> findAll() {
        List<QuestionForm> list = new ArrayList<>();

        for(QuestionBean questionBean : questionRepository.findAll()) {
            QuestionForm questionForm = new QuestionForm();
            questionForm.setId(String.valueOf(questionBean.getId()));
            questionForm.setTitle(questionBean.getTitle());
            questionForm.setDescription(questionBean.getDescription());
            questionForm.setInputNum(questionBean.getInputNum());
            list.add(questionForm);
        }
        
        return list;
    }
    
    
    /**
     * 検索文字列をタイトルか説明文に含む問題を取得する
     * @param searchStr 検索文字列
     * @return 合致する問題Formリスト
     */
    public List<QuestionForm> findByTitleLikeOrDescriptionLike(String searchStr) {
        List<QuestionForm> list = new ArrayList<>();
        

        for(QuestionBean questionBean : questionRepository.findByTitleLikeOrDescriptionLike(searchStr, searchStr)) {
            QuestionForm questionForm = new QuestionForm();
            questionForm.setId(String.valueOf(questionBean.getId()));
            questionForm.setTitle(questionBean.getTitle());
            questionForm.setDescription(questionBean.getDescription());
            questionForm.setInputNum(questionBean.getInputNum());
            list.add(questionForm);
        }
        
        return list;
    }
    
    /**
     * プログラムコードのコンパイルエラーチェック
     * @param code
     * @return
     */
    private boolean codeCompileCheck(String code, String plCode) {
        
        StringBuilder sb = new StringBuilder();

        String lineFeedCode = System.getProperty("line.separator");

        if (PrgLanguageCode.JAVA.toString().equals(plCode)) {
            // Javaの場合

            // List<PrgLanguagePropertyDetail> plList = prgBuildProperties.getList();
            PrgLanguagePropertiesDetail prgLanguagePropertiesDetail = prgLanguageProperties
                    .getMap().get(PrgLanguageCode.JAVA.toString());

            try {
                BufferedReader reader = new BufferedReader(new StringReader(code));

                // ファイル書き込み処理を文字コード指定に変更
                // close処理を正しくする
                FileOutputStream fos = new FileOutputStream(prgLanguagePropertiesDetail
                        .getWorkFolderPath() + java.io.File.separator
                        + prgLanguagePropertiesDetail.getFileName());
                OutputStreamWriter osw = new OutputStreamWriter(fos, serverProperties
                        .getCharacterCode());
                String line2 = null;
                while ((line2 = reader.readLine()) != null) {
                    osw.write(line2 + "\n");
                }
                osw.close();
                // FileWriter fw = new FileWriter(prgLanguagePropertiesDetail.getWorkFolderPath() + java.io.File.separator +
                // prgLanguagePropertiesDetail.getFileName());
                // fw.write(code);
                // fw.close();

                Process p = Runtime.getRuntime().exec("\""
                        + prgLanguagePropertiesDetail.getBuildCmdPath()
                        + "\" \"" + prgLanguagePropertiesDetail
                                .getWorkFolderPath() + java.io.File.separator
                        + prgLanguagePropertiesDetail.getFileName() + "\"");
                InputStream is = p.getInputStream();
                InputStream es = p.getErrorStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, serverProperties
                        .getCharacterCode()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(lineFeedCode);
                }
                br = new BufferedReader(new InputStreamReader(es, serverProperties
                        .getCharacterCode()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(lineFeedCode);
                }

                p.waitFor(); // プロセスが終了するまで待機する
                p.destroy();

                if (sb.toString() == null || sb.toString().equals("")) {
                    // エラーが発生していない場合のみ、実行

                    p = Runtime.getRuntime().exec("\""
                            + prgLanguagePropertiesDetail.getExecuteCmdPath()
                            + "\" -cp  \"" + prgLanguagePropertiesDetail
                                    .getWorkFolderPath() + "\" "
                            + prgLanguagePropertiesDetail.getBuildFileName());
                    is = p.getInputStream();
                    es = p.getErrorStream();
                    br = new BufferedReader(new InputStreamReader(is, serverProperties
                            .getCharacterCode()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append(lineFeedCode);
                    }
                    br = new BufferedReader(new InputStreamReader(es, serverProperties
                            .getCharacterCode()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append(lineFeedCode);
                    }
                    p.waitFor(); // プロセスが終了するまで待機する
                    p.destroy();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (PrgLanguageCode.CCPP.toString().equals(plCode)) {
            // Cの場合

        } else if (PrgLanguageCode.PYTHON.toString().equals(plCode)) {
            // Pythonの場合

        } else {
            // 登録されていないプログラミング言語の場合
            sb.append("予期せぬエラーが発生しました。");
        }
        
        
        
        
        
        
        
        return false;
    }

    /**
     * プログラムコード登録
     * @param form
     * @return
     */
    public TaskAddCodeForm save(TaskAddCodeForm form) {
        TaskAddCodeForm resultForm = new TaskAddCodeForm();
        String lineFeedCode = System.getProperty("line.separator");
        if (codeCompileCheck(form.getFrontCode() + lineFeedCode + form
                .getMiddleCode() + lineFeedCode + form.getBackCode(), form
                        .getLanguageId())) {

            TaskBean taskBean = new TaskBean();
            if (form.getId() != null && !form.getId().contentEquals("")) {
                taskBean.setId(Long.valueOf(form.getId()));
            }
            taskBean.setTitle(form.getTitle());
            taskBean.setDescription(form.getDescription());
            taskBean.setLanguageId(form.getLanguageId());
            taskBean.setFrontCode(form.getFrontCode());
            taskBean.setMiddleCode(form.getMiddleCode());
            taskBean.setBackCode(form.getBackCode());
            
            taskBean = taskRepository.save(taskBean);
            
            resultForm.setId(String.valueOf(taskBean.getId()));
            resultForm.setTitle(taskBean.getTitle());
            resultForm.setDescription(taskBean.getDescription());
            resultForm.setLanguageId(taskBean.getLanguageId());
            resultForm.setFrontCode(taskBean.getFrontCode());
            resultForm.setMiddleCode(taskBean.getMiddleCode());
            resultForm.setBackCode(taskBean.getBackCode());
        }

        return resultForm;
    }
}
