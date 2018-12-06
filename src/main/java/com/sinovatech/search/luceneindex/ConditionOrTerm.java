package com.sinovatech.search.luceneindex;
/**
 * 
 * @author lzq
 * @date 2014-12-15
 * 该类为了构造单字段or查询用
 */
public class ConditionOrTerm {

	private String value;
	
	private Condition.Opt opt;
	
	private Condition.Logic logic;
	
	public ConditionOrTerm(String value,Condition.Opt opt,Condition.Logic logic){
		this.value=value;
		this.opt=opt;
		this.logic=logic;
	}
	public Condition.Logic getLogic() {
		return logic;
	}
	public void setLogic(Condition.Logic logic) {
		this.logic = logic;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Condition.Opt getOpt() {
		return opt;
	}
	public void setOpt(Condition.Opt opt) {
		this.opt = opt;
	}
}
