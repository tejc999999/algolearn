package jp.spring.boot.algolearn.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * サーバー関連設定ファイル読み込み(Load server related configuration file)
 * @author tejc999999
 */
@Configuration
// @PropertySource(value = {"classpath:server.properties"})
public class PrgLanguageConfig {

    /**
     * Java ビルドコマンドパス(java build command path)
     */
    @Value("${prglanguage.java.buildCmdPath}")
    private String javaBuildCmdPath;

    /**
     * Java 実行コマンドパス(java execute command path)
     */
    @Value("${prglanguage.java.executeCmdPath}")
    private String javaExecuteCmdPath;

    /**
     * Java 作業フォルダパス(java work folder path)
     */
    @Value("${prglanguage.java.workFolderPath}")
    private String javaWorkFolderPath;

    /**
     * Java ファイル名(java file name)
     */
    @Value("${prglanguage.java.fileName}")
    private String javaFileName;

    /**
     * Java ビルド後ファイル名(Java file name after build)
     */
    @Value("${prglanguage.java.buildFileName}")
    private String javaBuildFileName;

    /**
     * C/C++ ビルドコマンドパス(c/c++ build command path)
     */
    @Value("${prglanguage.ccpp.buildCmdPath}")
    private String cCppBuildCmdPath;

    /**
     * C/C++ 実行コマンドパス(c/c++ execute command path)
     */
    @Value("${prglanguage.ccpp.executeCmdPath}")
    private String cCppExecuteCmdPath;

    /**
     * C/C++ 作業フォルダパス(c/c++ work folder path)
     */
    @Value("${prglanguage.ccpp.workFolderPath}")
    private String cCppWorkFolderPath;

    /**
     * C/C++ ファイル名(C/C++ file name)
     */
    @Value("${prglanguage.ccpp.fileName}")
    private String cCppFileName;

    /**
     * C/C++ ビルド後ファイル名(c,/c++ file name after build)
     */
    @Value("${prglanguage.ccpp.buildFileName}")
    private String cCppBuildFileName;

    /**
     * Python ビルドコマンドパス(Python build command path)
     */
    @Value("${prglanguage.python.buildCmdPath}")
    private String pythonBuildCmdPath;

    /**
     * Python 実行コマンドパス(Python execute command path)
     */
    @Value("${prglanguage.python.executeCmdPath}")
    private String pythonExecuteCmdPath;

    /**
     * Python 作業フォルダパス(Python work folder path)
     */
    @Value("${prglanguage.python.workFolderPath}")
    private String pythonWorkFolderPath;

    /**
     * Python ファイル名(Python file name)
     */
    @Value("${prglanguage.python.fileName}")
    private String pythonFileName;

    /**
     * Python ビルド後ファイル名(Python file name after build)
     */
    @Value("${prglanguage.python.buildFileName}")
    private String pythonBuildFileName;

    /**
     * プロパティを取得する(get the property)
     * @return プロパティ(property)
     */
    @Bean
    public PrgLanguageProperties prgLanguageProperties() {

        Map<String, PrgLanguagePropertiesDetail> map = new HashMap<String, PrgLanguagePropertiesDetail>();
        PrgLanguagePropertiesDetail java = new PrgLanguagePropertiesDetail();
        java.setName(PrgLanguageCode.JAVA.toString());
        java.setFileName(javaFileName);
        java.setBuildFileName(javaBuildFileName);
        java.setBuildCmdPath(javaBuildCmdPath);
        java.setExecuteCmdPath(javaExecuteCmdPath);
        java.setWorkFolderPath(javaWorkFolderPath);

        PrgLanguagePropertiesDetail ccpp = new PrgLanguagePropertiesDetail();
        ccpp.setName(PrgLanguageCode.CCPP.toString());
        ccpp.setFileName(cCppFileName);
        ccpp.setBuildFileName(cCppBuildFileName);
        ccpp.setBuildCmdPath(cCppBuildCmdPath);
        ccpp.setExecuteCmdPath(cCppExecuteCmdPath);
        ccpp.setWorkFolderPath(cCppWorkFolderPath);

        PrgLanguagePropertiesDetail python = new PrgLanguagePropertiesDetail();
        python.setName(PrgLanguageCode.PYTHON.toString());
        python.setFileName(pythonFileName);
        python.setBuildFileName(pythonBuildFileName);
        python.setBuildCmdPath(pythonBuildCmdPath);
        python.setExecuteCmdPath(pythonExecuteCmdPath);
        python.setWorkFolderPath(pythonWorkFolderPath);

        map.put(java.getName(), java);
        map.put(ccpp.getName(), ccpp);
        map.put(python.getName(), python);

        return new PrgLanguageProperties(map);
    }

    /**
     * サーバー文字コード(server character code)
     */
    @Value("${server.charactercode}")
    private String characterCode;

    /**
     * プロパティを取得する(get the property)
     * @return プロパティ(property)
     */
    @Bean
    public ServerProperties serverProperties() {

        return new ServerProperties(characterCode);
    }
}
