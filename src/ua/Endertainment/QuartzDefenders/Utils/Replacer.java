package ua.Endertainment.QuartzDefenders.Utils;

public class Replacer {
		
	private String s = null;
	private String what;
	private String to;
		
	public Replacer(String what, String to) {
		this.what = what;
		this.to = to;
	}
	Replacer(String s, String what, String to) {
		this.s = s;
		this.what = what;
		this.to = to;
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
