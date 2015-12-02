package bean;

import java.util.ArrayList;

public class Info {
	private String code;
	private String message;
	private ArrayList<Msg> content;
	private String totalPage;   //总页数
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<Msg> getContent() {
		return content;
	}
	public void setContent(ArrayList<Msg> content) {
		this.content = content;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
}
