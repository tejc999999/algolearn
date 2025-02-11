package jp.spring.boot.algolearn.teacher.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import jp.spring.boot.algolearn.bean.QuestionBean;
import jp.spring.boot.algolearn.bean.TaskBean;
import jp.spring.boot.algolearn.code.CcppCheckCodeFactory;
import jp.spring.boot.algolearn.code.CheckCode;
import jp.spring.boot.algolearn.code.CheckCodeFactory;
import jp.spring.boot.algolearn.code.ExecuteResult;
import jp.spring.boot.algolearn.code.JavaCheckCodeFactory;
import jp.spring.boot.algolearn.code.PythonCheckCodeFactory;
import jp.spring.boot.algolearn.config.PrgLanguageCode;
import jp.spring.boot.algolearn.config.PrgLanguageProperties;
import jp.spring.boot.algolearn.config.PrgLanguagePropertiesDetail;
import jp.spring.boot.algolearn.config.ServerProperties;
import jp.spring.boot.algolearn.repository.QuestionRepository;
import jp.spring.boot.algolearn.repository.TaskRepository;
import jp.spring.boot.algolearn.teacher.form.QuestionForm;
import jp.spring.boot.algolearn.teacher.form.TaskAddCodeForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 先生用課題Serviceクラス（teacher task Service Class）.
 * @author tejc999999
 */
@Service
public class TaskService {

    /**
     * 課題リポジトリ(task repository).
     */
    @Autowired
    TaskRepository taskRepository;

    /**
     * 問題Repository(question repository).
     */
    @Autowired
    QuestionRepository questionRepository;

    /**
     * プログラム言語プロパティ.
     */
    @Autowired
    PrgLanguageProperties prgLanguageProperties;

    /**
     * サーバー設定プロパティ.
     */
    @Autowired
    ServerProperties serverProperties;

    /**
     * 全ての問題を取得する.
     * @return 全ての問題Formリスト
     */
    public List<QuestionForm> findAll() {
        List<QuestionForm> list = new ArrayList<>();

        for (QuestionBean questionBean : questionRepository.findAll()) {
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
     * 検索文字列をタイトルか説明文に含む問題を取得する.
     * @param searchStr 検索文字列
     * @return 合致する問題Formリスト
     */
    public List<QuestionForm> findByTitleLikeOrDescriptionLike(
            String searchStr) {
        List<QuestionForm> list = new ArrayList<>();

        for (QuestionBean questionBean : questionRepository
                .findByTitleLikeOrDescriptionLike(searchStr, searchStr)) {
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
     * 課題確認用コードを取得する.
     * @param form 先生用課題登録時コード作成Form
     * @return 課題確認用コード
     */
    public String getCheckCode(TaskAddCodeForm form) throws Exception {

        CheckCodeFactory checkCodeFactory = null;
        if (form.getPrgLanguageId().equals(PrgLanguageCode.CCPP.getId())) {

            checkCodeFactory = new CcppCheckCodeFactory();
        } else if (form.getPrgLanguageId().equals(PrgLanguageCode.JAVA
                .getId())) {

            checkCodeFactory = new JavaCheckCodeFactory();
        } else if (form.getPrgLanguageId().equals(PrgLanguageCode.PYTHON
                .getId())) {

            checkCodeFactory = new PythonCheckCodeFactory();
        } else {

            throw new Exception("プログラム言語コード取得失敗.");
        }
        CheckCode checkCode = checkCodeFactory.getInstance();

        return checkCode.getCheckCode();
    }

    /**
     * プログラムコード登録.
     * @param form 課題追加コードForm
     * @return コマンド実行結果
     * @throws IOException ファイル入出力例外
     */
    public ExecuteResult save(TaskAddCodeForm form) throws IOException {
        // TaskAddCodeForm resultForm = new TaskAddCodeForm();

        // TODO: DB保存
        TaskBean taskBean = new TaskBean();
        String taskId = form.getId();
        if (taskId != null && !taskId.equals("")) {
            taskBean.setId(Long.parseLong(taskId));
        }
        taskBean.setTitle(form.getTitle());
        taskBean.setDescription(form.getDescription());
        taskBean.setLanguageId(form.getPrgLanguageId());
        taskBean.setQuestionId(Long.parseLong(form.getQuestionId()));
        taskBean.setCodeMethod(form.getCodeMethod());
        taskBean.setCodeReturn(form.getCodeReturn());

        taskBean = taskRepository.save(taskBean);
        taskId = String.valueOf(taskBean.getId());

        ExecuteResult executeResult = new ExecuteResult();
        try {
            // TODO: 課題チェックプログラムファイル出力
            fileOutputCheckCode(taskId, form.getCode(), form.getPrgLanguageId());
            
            // TODO: 課題ダミープログラムファイル出力
            fileOutputTaskCode(taskId, form.getCodeMethod(), form.getCodeReturn(),
                    form.getPrgLanguageId());
       
            // TODO: コンパイル実行
            executeResult = codeBuildCheck(form.getPrgLanguageId(), taskId);
            if (executeResult.getReturnCode() == ExecuteResult.RETURN_CODE_SUCCESS) {
                
                executeResult = codeExecuteCheck(form.getPrgLanguageId(), taskId);
            }        
            
            // TODO: 戻り値を返す
        } catch (IOException e) {
            executeResult.setReturnCode(ExecuteResult.RETURN_CODE_ERROR);
            throw e;
        } finally {
            if (executeResult.getReturnCode() == ExecuteResult.RETURN_CODE_ERROR) {
                taskRepository.delete(taskBean);
            }
        }

        return executeResult;
    }

    /**
     * 課題登録時の初期データを設定する.
     * @param id 問題ID
     * @return 課題登録コードForm(task add code form)
     */
    public TaskAddCodeForm setQuestionData(String id) {

        TaskAddCodeForm taskAddCodeForm = new TaskAddCodeForm();

        taskAddCodeForm.setQuestionId(id);

        Optional<QuestionBean> opt = questionRepository.findById(Long.valueOf(
                id));
        opt.ifPresent(questionBean -> {
            taskAddCodeForm.setQuestionId(String.valueOf(questionBean.getId()));
            taskAddCodeForm.setQuestionDescription(questionBean
                    .getDescription());
        });
        
        return taskAddCodeForm;
    }
    
    /**
     * プログラミング言語マップを設定する.
     * @param taskAddCodeForm 課題登録コードForm(task add code form)
     * @return 課題登録コードFOrm
     */
    public TaskAddCodeForm setPrgLanguageMap(TaskAddCodeForm taskAddCodeForm) {

        HashMap<String, String> prgLanguageMap = new HashMap<String, String>();
        prgLanguageMap.put(PrgLanguageCode.CCPP.getId(), PrgLanguageCode.CCPP
                .getName());
        prgLanguageMap.put(PrgLanguageCode.JAVA.getId(), PrgLanguageCode.JAVA
                .getName());
        prgLanguageMap.put(PrgLanguageCode.PYTHON.getId(),
                PrgLanguageCode.PYTHON.getName());

        taskAddCodeForm.setPrgLanguageMap(prgLanguageMap);

        return taskAddCodeForm;
    }

    /**
     * 課題登録時の初期データを設定する.
     * @param taskAddCodeForm 課題登録コードForm(task add code form)
     * @return 課題確認用コード
     */
    public String setDefaultCode(TaskAddCodeForm taskAddCodeForm) {

        String prgLanguageId = taskAddCodeForm.getPrgLanguageId();
        CheckCode checkCode = null;
        if (PrgLanguageCode.CCPP.getId().equals(prgLanguageId)) {

            checkCode = new CcppCheckCodeFactory().getInstance();
        } else if (PrgLanguageCode.JAVA.getId().equals(prgLanguageId)) {

            checkCode = new JavaCheckCodeFactory().getInstance();
        } else if (PrgLanguageCode.PYTHON.getId().equals(prgLanguageId)) {

            checkCode = new PythonCheckCodeFactory().getInstance();
        }

        String resultStr = null;;
        if (checkCode != null) {

            resultStr = checkCode.getCheckCode();
        }
        return resultStr;
    }

    /**
     * 課題チェック用プログラムコードファイル出力.
     * @param taskId 課題ID
     * @param code 課題チェック用コード
     * @param prgCodeId プログラミング言語ID
     * @throws IOException ファイル入出力例外
     */
    private void fileOutputCheckCode(String taskId, String code,
            String prgCodeId) throws IOException {


        if (PrgLanguageCode.JAVA.getId().equals(prgCodeId)) {
            // Javaの場合

            PrgLanguagePropertiesDetail prgLanguagePropertiesDetail = prgLanguageProperties
                    .getMap().get(PrgLanguageCode.JAVA.toString());

            // ディレクトリ作成
            File dir = new File(prgLanguagePropertiesDetail.getWorkFolderPath()
                    + java.io.File.separator + taskId);
            if (!dir.exists()) {
                dir.mkdir();
            }

            FileOutputStream fos = null;
            OutputStreamWriter osw = null;
            try {
                BufferedReader reader = new BufferedReader(new StringReader(code));

                // ファイル書き込み処理を文字コード指定に変更
                // close処理を正しくする
                fos = new FileOutputStream(prgLanguagePropertiesDetail
                        .getWorkFolderPath() + java.io.File.separator + taskId
                        + java.io.File.separator + prgLanguagePropertiesDetail
                                .getCheckFileName());
                osw = new OutputStreamWriter(fos, serverProperties
                        .getCharacterCode());
                String line2 = null;
                while ((line2 = reader.readLine()) != null) {
                    osw.write(line2 + "\n");
                }
            } finally {
                if (osw != null) {
                    try {
                        osw.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        } else if (PrgLanguageCode.CCPP.toString().equals(prgCodeId)) {
            // Cの場合

        } else if (PrgLanguageCode.PYTHON.toString().equals(prgCodeId)) {
            // Pythonの場合
        }
    }

    /**
     * 課題用ダミープログラムコードファイル出力.
     * @param taskId 課題ID
     * @param codeMethod メソッド名
     * @param codeRetrun 戻り値
     * @param prgLanguageId プログラミング言語ID
     */
    private void fileOutputTaskCode(String taskId, String codeMethod,
            String codeReturn, String prgLanguageId) {

        CheckCodeFactory checkCodeFactory = null;
        if (PrgLanguageCode.JAVA.getId().equals(prgLanguageId)) {
            // Javaの場合

            checkCodeFactory = new JavaCheckCodeFactory();
            CheckCode checkCode = checkCodeFactory.getInstance();

            String code = checkCode.getDummyCode(codeMethod, codeReturn);

            PrgLanguagePropertiesDetail prgLanguagePropertiesDetail = prgLanguageProperties
                    .getMap().get(PrgLanguageCode.JAVA.toString());

            // ディレクトリ作成
            File dir = new File(prgLanguagePropertiesDetail.getWorkFolderPath()
                    + java.io.File.separator + taskId);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {

                BufferedReader reader = new BufferedReader(new StringReader(code));

                // ファイル書き込み処理を文字コード指定に変更
                // close処理を正しくする
                FileOutputStream fos = new FileOutputStream(prgLanguagePropertiesDetail
                        .getWorkFolderPath() + java.io.File.separator + taskId
                        + java.io.File.separator + prgLanguagePropertiesDetail
                                .getFileName());
                OutputStreamWriter osw = new OutputStreamWriter(fos, serverProperties
                        .getCharacterCode());
                String line2 = null;
                while ((line2 = reader.readLine()) != null) {
                    osw.write(line2 + "\n");
                }
                osw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else if (PrgLanguageCode.CCPP.toString().equals(prgLanguageId)) {
            // Cの場合

            // checkCodeFactory = new CcppCheckCodeFactory();
            // CheckCode checkCode = checkCodeFactory.getInstance();
            // String code = checkCode.getDummyCode(codeMethod, codeReturn);

        } else if (PrgLanguageCode.PYTHON.toString().equals(prgLanguageId)) {
            // Pythonの場合

            // checkCodeFactory = new PythonCheckCodeFactory();
            // CheckCode checkCode = checkCodeFactory.getInstance();
            // String code = checkCode.getDummyCode(codeMethod, codeReturn);
        }
    }

    /**
     * プログラムコードのコンパイルエラーチェック.
     * @param prgLanguageId プログラミング言語ID
     * @param taskId 課題ID
     * @return
     */
    private ExecuteResult codeBuildCheck(String prgLanguageId,
            String taskId) {

        ExecuteResult executeResult = new ExecuteResult();
        
        StringBuilder sb = new StringBuilder();

        String lineFeedCode = System.getProperty("line.separator");

        if (PrgLanguageCode.JAVA.getId().equals(prgLanguageId)) {
            // Javaの場合

            PrgLanguagePropertiesDetail prgLanguagePropertiesDetail = prgLanguageProperties
                    .getMap().get(PrgLanguageCode.JAVA.toString());

            try {

                // ビルド処理
                Process p = Runtime.getRuntime().exec("\""
                        + prgLanguagePropertiesDetail.getBuildCmdPath()
                        + "\" \"" + prgLanguagePropertiesDetail
                                .getWorkFolderPath() + java.io.File.separator
                        + taskId + java.io.File.separator
                        + prgLanguagePropertiesDetail.getFileName()
                        + "\" "
                        + "\"" + prgLanguagePropertiesDetail
                                .getWorkFolderPath() + java.io.File.separator
                        + taskId + java.io.File.separator
                        + prgLanguagePropertiesDetail.getCheckFileName()
                        + "\"");

                // 標準出力を取得する
                InputStream is = p.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, serverProperties
                        .getCharacterCode()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(lineFeedCode);
                }
                executeResult.setOutputString(sb.toString());
                sb.setLength(0);
                
                // 標準エラー出力を取得する
                InputStream es = p.getErrorStream();
                br = new BufferedReader(new InputStreamReader(es, serverProperties
                        .getCharacterCode()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(lineFeedCode);
                }
                executeResult.setErrorOutputString(sb.toString());

                executeResult.setReturnCode(p.waitFor()); // プロセスが終了するまで待機する
                p.destroy();

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        } else if (PrgLanguageCode.CCPP.toString().equals(prgLanguageId)) {
            // Cの場合

        } else if (PrgLanguageCode.PYTHON.toString().equals(prgLanguageId)) {
            // Pythonの場合

        } else {
            // 登録されていないプログラミング言語の場合
            sb.append("予期せぬエラーが発生しました。");
        }

        return executeResult;
    }
    
    /**
     * プログラムコードの実行エラーチェック.
     * @param prgLanguageId プログラミング言語ID
     * @param taskId 課題ID
     * @return
     */
    private ExecuteResult codeExecuteCheck(String prgLanguageId,
            String taskId) {
        
        ExecuteResult executeResult = new ExecuteResult();

        StringBuilder sb = new StringBuilder();

        String lineFeedCode = System.getProperty("line.separator");

        if (PrgLanguageCode.JAVA.getId().equals(prgLanguageId)) {
            // Javaの場合

            PrgLanguagePropertiesDetail prgLanguagePropertiesDetail = prgLanguageProperties
                    .getMap().get(PrgLanguageCode.JAVA.toString());

            try {
                // エラーが発生していない場合のみ、実行

                // 実行処理
                Process p = Runtime.getRuntime().exec("\"" + prgLanguagePropertiesDetail
                        .getExecuteCmdPath() + "\" -cp  \""
                        + prgLanguagePropertiesDetail.getWorkFolderPath()
                        + java.io.File.separator + taskId + "\" "
                        + prgLanguagePropertiesDetail.getBuildCheckFileName());
                InputStream is = p.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, serverProperties
                        .getCharacterCode()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(lineFeedCode);
                }
                executeResult.setOutputString(sb.toString());
                
                sb.setLength(0);
                InputStream es = p.getErrorStream();
                br = new BufferedReader(new InputStreamReader(es, serverProperties
                        .getCharacterCode()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(lineFeedCode);
                }
                executeResult.setErrorOutputString(sb.toString());

                executeResult.setReturnCode(p.waitFor()); // プロセスが終了するまで待機する
                p.destroy();

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        } else if (PrgLanguageCode.CCPP.toString().equals(prgLanguageId)) {
            // Cの場合

        } else if (PrgLanguageCode.PYTHON.toString().equals(prgLanguageId)) {
            // Pythonの場合

        } else {
            // 登録されていないプログラミング言語の場合
            sb.append("予期せぬエラーが発生しました。");
        }

        return executeResult;
    }

}
