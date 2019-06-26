package jp.spring.boot.algolearn.config;

import lombok.Data;

/**
 * サーバー設定情報(Server configuration infomation)
 * 
 * @author tejc999999
 *
 */
@Data
public class ServerProperties {

	/**
	 * 文字コード(Character code)
	 */
	private String characterCode;
	
	/**
	 * コンストラクタ(Constructor)
	 * 
	 * @param characterCode 文字コード(Character code)
	 */
	public ServerProperties(String characterCode) {
		this.characterCode = characterCode;
	}
}
