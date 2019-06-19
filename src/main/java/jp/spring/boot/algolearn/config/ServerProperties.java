package jp.spring.boot.algolearn.config;

import lombok.Data;

@Data
public class ServerProperties {

	private String characterCode;
	
	public ServerProperties(String characterCode) {
		this.characterCode = characterCode;
	}
}
