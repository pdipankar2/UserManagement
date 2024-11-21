package com.jtc.dto;

public class ResetPwdFromDTO {

	
	private String email;
	private String OldPwd;
	
	private String newPwd;
	private String confirmPwd;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOldPwd() {
		return OldPwd;
	}
	public void setOldPwd(String oldPwd) {
		OldPwd = oldPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	public String getConfirmPwd() {
		return confirmPwd;
	}
	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}
	
	
}
