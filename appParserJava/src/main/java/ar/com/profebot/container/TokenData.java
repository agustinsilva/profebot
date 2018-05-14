package ar.com.profebot.container;

import ar.com.profebot.services.ScannerService.TOKEN;

public class TokenData {
	
	private TOKEN token;
	private String value;
	public TokenData(TOKEN token, String value) {
		super();
		this.token = token;
		this.value = value;
	}
	public TOKEN getToken() {
		return token;
	}
	public void setToken(TOKEN token) {
		this.token = token;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "TokenData [token=" + token + ", value=" + value + "]";
	}
}
