package com.gf.entity;

import java.math.BigDecimal;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class Regulations extends DataModel{
	private int serial_number; //序号
	private String name; //条例名称
	private String context; //内容
	private BigDecimal score; //总分值
	private BigDecimal self_evaluation; //自评分数
	private BigDecimal expert_scoreOne; //专家1打分
	private BigDecimal expert_scoreTwo; //专家2打分
	private BigDecimal expert_scoreThree; //专家3打分
	private BigDecimal expert_scoreFour; //专家4打分
	private BigDecimal expert_scoreFive; //专家5打分
	private Integer number_returns; //退回次数
	private String type; //类型 招生 就业
	private String date; //年份
	private String description; //评分说明
	private String faculty_id; //院系ID
	
	
	
	@PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();

	@NotMap
	private static final long serialVersionUID = 1L;

	
	
	public int getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(int serial_number) {
		this.serial_number = serial_number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	

	

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public BigDecimal getSelf_evaluation() {
		return self_evaluation;
	}

	public void setSelf_evaluation(BigDecimal self_evaluation) {
		this.self_evaluation = self_evaluation;
	}

	

	public BigDecimal getExpert_scoreOne() {
		return expert_scoreOne;
	}

	public void setExpert_scoreOne(BigDecimal expert_scoreOne) {
		this.expert_scoreOne = expert_scoreOne;
	}

	public BigDecimal getExpert_scoreTwo() {
		return expert_scoreTwo;
	}

	public void setExpert_scoreTwo(BigDecimal expert_scoreTwo) {
		this.expert_scoreTwo = expert_scoreTwo;
	}

	public BigDecimal getExpert_scoreThree() {
		return expert_scoreThree;
	}

	public void setExpert_scoreThree(BigDecimal expert_scoreThree) {
		this.expert_scoreThree = expert_scoreThree;
	}

	
	public BigDecimal getExpert_scoreFour() {
		return expert_scoreFour;
	}

	public void setExpert_scoreFour(BigDecimal expert_scoreFour) {
		this.expert_scoreFour = expert_scoreFour;
	}

	public BigDecimal getExpert_scoreFive() {
		return expert_scoreFive;
	}

	public void setExpert_scoreFive(BigDecimal expert_scoreFive) {
		this.expert_scoreFive = expert_scoreFive;
	}

	public Integer getNumber_returns() {
		return number_returns;
	}

	public void setNumber_returns(Integer number_returns) {
		this.number_returns = number_returns;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getFaculty_id() {
		return faculty_id;
	}

	public void setFaculty_id(String faculty_id) {
		this.faculty_id = faculty_id;
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
		return "Regulations [serial_number=" + serial_number + ", name=" + name + ", context=" + context + ", score="
				+ score + ", self_evaluation=" + self_evaluation + ", expert_scoreOne=" + expert_scoreOne
				+ ", expert_scoreTwo=" + expert_scoreTwo + ", expert_scoreThree=" + expert_scoreThree
				+ ", expert_scoreFour=" + expert_scoreFour + ", expert_scoreFive=" + expert_scoreFive
				+ ", number_returns=" + number_returns + ", type=" + type + ", date=" + date + ", description="
				+ description + ", faculty_id=" + faculty_id + ", kid=" + kid + ", status=" + status + ", created="
				+ created + "]";
	}

	

	

	

	
	

	

	
	
}
