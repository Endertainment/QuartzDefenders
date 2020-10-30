package ua.endertainment.quartzdefenders.utils;

public class Replacer {
		
	private String s = null;
	private String what;
	private String to;
		
	public Replacer(String what, Object to) {
		this.what = what;
		this.to = to.toString();
	}
	public Replacer addString(String s) {
		this.s = s;
		this.s = this.s.replace(what, to);
		return this;
	}
	public String getReplaced() {
		return s;
	}
	public String what() {
		return what;
	}
	public String to() {
		return to;
	}	
	
	
}
