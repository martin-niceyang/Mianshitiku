package cn.martin.mianshitiku.bean;

import com.avos.avoscloud.AVObject;

public class Question {

	private String id;
	private String title;// 问题题目
	private String answer;// 问题答案
	private AVObject user;// 问题所属者
	private boolean needVerify;// 是否需要审核
	private String createTime;// 创建时间
	private String updateTime;// 最后一次更新时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public AVObject getUser() {
		return user;
	}

	public void setUser(AVObject user) {
		this.user = user;
	}

	public boolean isNeedVerify() {
		return needVerify;
	}

	public void setNeedVerify(boolean needVerify) {
		this.needVerify = needVerify;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
