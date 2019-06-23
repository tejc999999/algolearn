package jp.spring.boot.algolearn.config;

public enum RoleCode {
	ROLE_ADMIN("001"),
	ROLE_TEACHER("002"),
	ROLE_STUDENT("003");
	
    private final String text;

    private RoleCode(final String text) {
        this.text = text;
    }

    public String getString() {
        return this.text;
    }
}
