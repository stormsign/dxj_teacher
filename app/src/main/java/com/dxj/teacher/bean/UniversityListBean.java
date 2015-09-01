package com.dxj.teacher.bean;


import java.util.ArrayList;

public class UniversityListBean extends BaseBean{

	private ArrayList<UniversityBean> list = new ArrayList<>();

	@Override
	public void setCode(Long code) {
		super.setCode(code);
	}



	public void setList(ArrayList<UniversityBean> list) {
		this.list = list;
	}

	public ArrayList<UniversityBean> getList() {
		return list;
	}

	@Override
	public void setMsg(String msg) {
		super.setMsg(msg);
	}

	@Override
	public Long getCode() {
		return super.getCode();
	}

	@Override
	public String getMsg() {
		return super.getMsg();
	}
}
