package com.mailboxplus;

import java.util.Date;


public class Stamp {
	public String stamp_code;
	public String sType;
	public Date expiresAt;
	public boolean valid;
	
	public Stamp(String sType, String code, Date expiresAt, boolean valid) {
		this.stamp_code = code;
		this.sType = sType;
		this.expiresAt = expiresAt;
		this.valid = valid;
	}
}

