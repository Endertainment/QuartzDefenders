package ua.endertainment.quartzdefenders.utils;

public class Replacer {
		
	private String s = null;
	private String what;
	private String to;
		
	public Replacer(String what, String to) {
		this.what = what;
		this.to = to;
	}
	public void addString(String s) {
		this.s = s;
		this.s = this.s.replace(what, to);
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
