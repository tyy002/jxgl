package com.gf.entity;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class Result extends DataModel{
	
	private String faculty_kid; //院系kid
	private int first_trial; //管理员初审结果 0 未通过/1已通过
	private int expert_statusOne; //专家一评审状态0未评审/1已评审
	private int expert_statusTwo; //专家二评审状态0未评审/1已评审
	private int expert_statusThree; //专家三评审状态0未评审/1已评审
	private int expert_statusFour; //专家三评审状态0未评审/1已评审
	private int expert_statusFive; //专家三评审状态0未评审/1已评审
	private String type; //招生/就业
	
	
	
	@PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();

	@NotMap
	private static final long serialVersionUID = 1L;

	public String getFaculty_kid() {
		return faculty_kid;
	}

	public void setFaculty_kid(String faculty_kid) {
		this.faculty_kid = faculty_kid;
	}

	public int getFirst_trial() {
		return first_trial;
	}

	public void setFirst_trial(int first_trial) {
		this.first_trial = first_trial;
	}

	public int getExpert_statusOne() {
		return expert_statusOne;
	}

	public void setExpert_statusOne(int expert_statusOne) {
		this.expert_statusOne = expert_statusOne;
	}

	public int getExpert_statusTwo() {
		return expert_statusTwo;
	}

	public void setExpert_statusTwo(int expert_statusTwo) {
		this.expert_statusTwo = expert_statusTwo;
	}

	public int getExpert_statusThree() {
		return expert_statusThree;
	}

	public void setExpert_statusThree(int expert_statusThree) {
		this.expert_statusThree = expert_statusThree;
	}

	public int getExpert_statusFour() {
		return expert_statusFour;
	}

	public void setExpert_statusFour(int expert_statusFour) {
		this.expert_statusFour = expert_statusFour;
	}

	public int getExpert_statusFive() {
		return expert_statusFive;
	}

	public void setExpert_statusFive(int expert_statusFive) {
		this.expert_statusFive = expert_statusFive;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Result [faculty_kid=" + faculty_kid + ", first_trial=" + first_trial + ", expert_statusOne="
				+ expert_statusOne + ", expert_statusTwo=" + expert_statusTwo + ", expert_statusThree="
				+ expert_statusThree + ", expert_statusFour=" + expert_statusFour + ", expert_statusFive="
				+ expert_statusFive + ", type=" + type + ", kid=" + kid + ", status=" + status + ", created=" + created
				+ "]";
	}

	

	
	
	

}
