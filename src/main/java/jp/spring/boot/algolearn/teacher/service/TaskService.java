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
     * プログラム採点用コードをFormにセットする.
     * @param form 先生用課題登録時コード作成Form
     * @return 先生用課題登録時コード作成Form
     */
    public TaskAddCodeForm getTaskCode(TaskAddCodeForm form) throws Exception {

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

        TaskAddCodeForm resultForm = new TaskAddCodeForm();
        resultForm.setCode(checkCode.getCheckCode());

        return resultForm;
    }

    /**
     * プログラムコード登録.
     * @param form 課題追加コードForm
     * @return 実行結果
     */
    public String save(TaskAddCodeForm form) {
        // TaskAddCodeForm resultForm = new TaskAddCodeForm();

        // TODO: DB保存
        TaskBean taskBean = new TaskBean();
        String taskId = form.getId();
        if (taskId != null && !taskId.equals("")) {
            taskBean.setId(Long.parseLong(taskId));
        }
        taskBean.setTitle(form.getTitle());
        taskBean.setDescription(form.getPrgLanguageId());
        taskBean.setQuestionId(Long.parseLong(form.getQuestionId()));
        taskBean.setCodeMethod(form.getCodeMethod());
        taskBean.setCodeReturn(form.getCodeReturn());

        taskBean = taskRepository.save(taskBean);
        taskId = String.valueOf(taskBean.getId());

        // TODO: 課題チェックプログラムファイル出力
        StringBuilder sb = null;
        if (fileOutputCheckCode(taskId, form.getCode(), form.getPrgLanguageId())
                // TODO: 課題ダミープログラムファイル出力
                && fileOutputTaskCode(taskId, form.getCodeMethod(), form
                        .getCodeReturn(), form.getPrgLanguageId())) {
            // TODO: コンパイル実行
            sb = codeCompileCheck(form.getPrgLanguageId(), taskId);

        } else {
            sb = new StringBuilder();
        }
        // TODO: 戻り値を返す

        
        // TaskBean taskBean = new TaskBean();
        // if (form.getId() != null && !form.getId().contentEquals("")) {
        // taskBean.setId(Long.valueOf(form.getId()));
        // }
        // taskBean.setTitle(form.getTitle());
        // taskBean.setDescription(form.getDescription());
        // taskBean.setLanguageId(form.getLanguageId());
        // taskBean.setFrontCode(form.getFrontCode());
        // taskBean.setMiddleCode(form.getMiddleCode());
        // taskBean.setBackCode(form.getBackCode());
        //
        // taskBean = taskRepository.save(taskBean);
        //
        // resultForm.setId(String.valueOf(taskBean.getId()));
        // resultForm.setTitle(taskBean.getTitle());
        // resultForm.setDescription(taskBean.getDescription());
        // resultForm.setLanguageId(taskBean.getLanguageId());
        // resultForm.setFrontCode(taskBean.getFrontCode());
        // resultForm.setMiddleCode(taskBean.getMiddleCode());
        // resultForm.setBackCode(taskBean.getBackCode());

        return sb.toString();
    }

    /**
     * 課題登録時の初期データを設定する.
     * @param id 問題ID(questionid).
     * @return 課題登録コードForm(task add code form)
     */
    public TaskAddCodeForm initAutoCodeData(String id) {

        TaskAddCodeForm taskAddCodeForm = new TaskAddCodeForm();

        taskAddCodeForm.setQuestionId(id);

        Optional<QuestionBean> opt = questionRepository.findById(Long.valueOf(
                id));
        opt.ifPresent(questionBean -> {
            taskAddCodeForm.setQuestionId(String.valueOf(questionBean.getId()));
            taskAddCodeForm.setQuestionDescription(questionBean
                    .getDescription());
        });

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
     * @return 課題登録コードForm(task add code form)
     */
    public TaskAddCodeForm setDefaultCode(TaskAddCodeForm taskAddCodeForm) {

        String prgLanguageId = taskAddCodeForm.getPrgLanguageId();
        CheckCode checkCode = null;
        if (PrgLanguageCode.CCPP.getId().equals(prgLanguageId)) {

            checkCode = new CcppCheckCodeFactory().getInstance();
        } else if (PrgLanguageCode.JAVA.getId().equals(prgLanguageId)) {

            checkCode = new JavaCheckCodeFactory().getInstance();
        } else if (PrgLanguageCode.PYTHON.getId().equals(prgLanguageId)) {

            checkCode = new PythonCheckCodeFactory().getInstance();
        }

        if (checkCode != null) {

            taskAddCodeForm.setCode(checkCode.getCheckCode());
        }

        return taskAddCodeForm;
    }

    /**
     * 課題チェック用プログラムコードファイル出力.
     * @param taskId 課題ID
     * @param code 課題チェック用コード
     * @param prgCodeId プログラミング言語ID
     * @return 実行結果（true:成功、false:失敗）
     */
    private boolean fileOutputCheckCode(String taskId, String code,
            String prgCodeId) {

        boolean resultFlg = false;
        ;

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

            try {
                BufferedReader reader = new BufferedReader(new StringReader(code));

                // ファイル書き込み処理を文字コード指定に変更
                // close処理を正しくする
                FileOutputStream fos = new FileOutputStream(prgLanguagePropertiesDetail
                        .getWorkFolderPath() + java.io.File.separator + taskId
                        + java.io.File.separator + prgLanguagePropertiesDetail
                                .getCheckFileName());
                OutputStreamWriter osw = new OutputStreamWriter(fos, serverProperties
                        .getCharacterCode());
                String line2 = null;
                while ((line2 = reader.readLine()) != null) {
                    osw.write(line2 + "\n");
                }
                osw.close();

                resultFlg = true;

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else if (PrgLanguageCode.CCPP.toString().equals(prgCodeId)) {
            // Cの場合

        } else if (PrgLanguageCode.PYTHON.toString().equals(prgCodeId)) {
            // Pythonの場合
        }

        return resultFlg;
    }

    /**
     * 課題用ダミープログラムコードファイル出力.
     * @param taskId 課題ID
     * @param codeMethod メソッド名
     * @param codeRetrun 戻り値
     * @param prgLanguageId プログラミング言語ID
     * @return 実行結果（true:成功、false:失敗）
     */
    private boolean fileOutputTaskCode(String taskId, String codeMethod,
            String codeReturn, String prgLanguageId) {

        boolean resultFlg = false;

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

                resultFlg = true;

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

        return resultFlg;
    }

    /**
     * プログラムコードのコンパイルエラーチェック.
     * @param prgLanguageId プログラミング言語ID
     * @param taskId 課題ID
     * @return
     */
    private StringBuilder codeCompileCheck(String prgLanguageId,
            String taskId) {

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

                    // 実行処理
                    p = Runtime.getRuntime().exec("\""
                            + prgLanguagePropertiesDetail.getExecuteCmdPath()
                            + "\" -cp  \"" + prgLanguagePropertiesDetail
                                    .getWorkFolderPath() + java.io.File.separator
                            + taskId
                            + "\" "
                            + prgLanguagePropertiesDetail
                                    .getBuildCheckFileName());
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

        return sb;
    }

}
