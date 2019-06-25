package jp.spring.boot.algolearn.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * サーバー関連設定ファイル読み込み
 * 
 * @author tejc999999
 *
 */
@Configuration
//@PropertySource(value = {"classpath:server.properties"})
public class PrgLanguageConfig {

	/**
	 * Java ビルドコマンドパス
	 */
	@Value("${prglanguage.java.buildCmdPath}")
    private String javaBuildCmdPath;

	/**
	 * Java 実行コマンドパス
	 */
	@Value("${prglanguage.java.executeCmdPath}")
    private String javaExecuteCmdPath;

	/**
	 * Java 作業フォルダパス
	 */
	@Value("${prglanguage.java.workFolderPath}")
    private String javaWorkFolderPath;

	/**
	 * Java ファイル名
	 */
	@Value("${prglanguage.java.fileName}")
    private String javaFileName;

	/**
	 * Java ビルド後ファイル名
	 */
	@Value("${prglanguage.java.buildFileName}")
    private String javaBuildFileName;

	/**
	 * C_CPP ビルドコマンドパス
	 */
	@Value("${prglanguage.ccpp.buildCmdPath}")
    private String cCppBuildCmdPath;

	/**
	 * C_CPP 実行コマンドパス
	 */
	@Value("${prglanguage.ccpp.executeCmdPath}")
    private String cCppExecuteCmdPath;

	/**
	 * C_CPP 作業フォルダパス
	 */
	@Value("${prglanguage.ccpp.workFolderPath}")
    private String cCppWorkFolderPath;

	/**
	 * C_CPP ファイル名
	 */
	@Value("${prglanguage.ccpp.fileName}")
    private String cCppFileName;

	/**
	 * C_CPP ビルド後ファイル名
	 */
	@Value("${prglanguage.ccpp.buildFileName}")
    private String cCppBuildFileName;
	
	/**
	 * Python ビルドコマンドパス
	 */
	@Value("${prglanguage.python.buildCmdPath}")
    private String pythonBuildCmdPath;

	/**
	 * Python 実行コマンドパス
	 */
	@Value("${prglanguage.python.executeCmdPath}")
    private String pythonExecuteCmdPath;

	/**
	 * Python 作業フォルダパス
	 */
	@Value("${prglanguage.python.workFolderPath}")
    private String pythonWorkFolderPath;

	/**
	 * Python ファイル名
	 */
	@Value("${prglanguage.python.fileName}")
    private String pythonFileName;

	/**
	 * Python ビルド後ファイル名
	 */
	@Value("${prglanguage.python.buildFileName}")
    private String pythonBuildFileName;
	
	/**
	 * プロパティを取得する
	 * 
	 * @return プロパティ
	 */
    @Bean
    public PrgLanguageProperties prgLanguageProperties() {
    	
    	Map<String, PrgLanguagePropertiesDetail> map = new HashMap<String, PrgLanguagePropertiesDetail>();
    	PrgLanguagePropertiesDetail java = new PrgLanguagePropertiesDetail();
    	java.setName(PrgLanguage.JAVA.toString());
    	java.setFileName(javaFileName);
    	java.setBuildFileName(javaBuildFileName);
    	java.setBuildCmdPath(javaBuildCmdPath);
    	java.setExecuteCmdPath(javaExecuteCmdPath);
    	java.setWorkFolderPath(javaWorkFolderPath);
    	
    	PrgLanguagePropertiesDetail ccpp = new PrgLanguagePropertiesDetail();
    	ccpp.setName(PrgLanguage.CCPP.toString());
    	ccpp.setFileName(cCppFileName);
    	ccpp.setBuildFileName(cCppBuildFileName);
    	ccpp.setBuildCmdPath(cCppBuildCmdPath);
    	ccpp.setExecuteCmdPath(cCppExecuteCmdPath);
    	ccpp.setWorkFolderPath(cCppWorkFolderPath);
    	
    	PrgLanguagePropertiesDetail python = new PrgLanguagePropertiesDetail();
    	python.setName(PrgLanguage.PYTHON.toString());
    	python.setFileName(pythonFileName);
    	python.setBuildFileName(pythonBuildFileName);
    	python.setBuildCmdPath(pythonBuildCmdPath);
    	python.setExecuteCmdPath(pythonExecuteCmdPath);
    	python.setWorkFolderPath(pythonWorkFolderPath);

    	map.put(java.getName(),  java);
    	map.put(ccpp.getName(), ccpp);
    	map.put(python.getName(), python);
    	
        return new PrgLanguageProperties(map);
    }
    
	/**
	 * サーバー文字コード
	 */
	@Value("${server.charactercode}")
    private String characterCode;

	/**
	 * プロパティを取得する
	 * 
	 * @return プロパティ
	 */
    @Bean
    public ServerProperties serverProperties() {
    	    	
        return new ServerProperties(characterCode);
    }
}
