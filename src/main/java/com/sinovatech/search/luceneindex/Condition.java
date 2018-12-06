package com.sinovatech.search.luceneindex;

import java.util.ArrayList;
import java.util.List;


public class Condition {
	 private ArrayList<Condition> clauses = new ArrayList<Condition>();
	 private String queryStr;
	 private String type;//
	 private String field;
	 private String value;
	 private Condition.StringOrInt stringOrInt;
	 private List<Condition> values;
	 private Condition.Logic logic;
	 private Condition.Opt opt;
	 private static final String rs ="00000000000.000";
	 private static final String rs1 ="00000000000";
	 private static final String rs2 ="000";
	 private final static  String And="And";
	 private final static  String Or="Or";//用于 BETWEEN_SPLIT_STR 等
	 private final static  String List="List";//用于一个括号((a>1)or (b>1)) AND (a>b)
	 public final static  String BETWEEN_SPLIT_STR="_to_";
	 
	 public static enum Logic {
		    AND     { @Override public String toString() { return "AND"; } },

		    OR   { @Override public String toString() { return "OR";  } },

		    NOT { @Override public String toString() { return "NOT"; } };
	 }
	 public static enum Opt {
		    EQ     { @Override public String toString() { return "~0"; } },

		    LIKE   { @Override public String toString() { return "*";  } },
		    
		    LIKE_ANALYZER   { @Override public String toString() { return "";  } },//分词查询
		    
		    BETWEEN_OPEN { @Override public String toString() { return "{}"; } },
		    
		    BETWEEN_CLOSE { @Override public String toString() { return "[]"; } };
	 }
	 public static enum StringOrInt {
		    STRING { @Override public String toString() { return "STRING"; } },

		    INT   { @Override public String toString() { return "INT";  } },

	 }
	 public Condition(){}
	 public Condition(String queryStr){
		 this.queryStr=queryStr;
	 }
	 
	 /**
	  * 添加一个查询条件
	  * @param field 字段名
	  * @param value 字段值
	  * @param opt   操作符 Eq Like ...
	  * @param logic 链接符 AND OR
	  * @param stringOrInt  字段值是否是String还是Int
	  */
	 public  Condition(String field,String value,Condition.Opt opt,Condition.Logic logic,Condition.StringOrInt stringOrInt){
		 this.field=field;
		 this.value=escape(value);
		 this.logic=logic;
		 this.opt=opt;
		 this.type=And;
		 this.stringOrInt=stringOrInt;
	 }
	 /**
	  * 添加一个查询条件(字段类型是String)
	  * @param field 字段名
	  * @param value 字段值
	  * @param opt   操作符 Eq Like ...
	  * @param logic 链接符 AND OR
	  * @param logic
	  */
	 public  Condition(String field,String value,Condition.Opt opt,Condition.Logic logic){
		 this.field=field;
		 this.value=escape(value);
		 this.logic=logic;
		 this.opt=opt;
		 this.type=And;
		 this.stringOrInt= StringOrInt.STRING;
	 }
	 /**
	  * 添加一个查询条件用于//用于一个括号((a>1)or (b>1)) 
	  * @param field
	  * @param values
	  * @param logic
	  */
	 public  Condition(List<Condition> values,Condition.Logic logic){
		 this.values=values;
		 this.logic=logic;
		 this.type=List;
		 for(Condition condition:values)
		 {
			 condition.setValue(escape(condition.value));
		 }
	 }
	 
	 /**
	  * 添加一个查询条件用于price:(value or value)
	  * @param values
	  * @param logic
	  */
	 public  Condition(String field, List<Condition> values,Condition.Logic logic){
		 this.field=field;
		 this.values=values;
		 this.logic=logic;
		 this.type=Or;
		 for(Condition condition:values)
		 {
			 condition.setValue(escape(condition.value));
		 }
	 }
	 /**
	  * 
	  * 快速添加一个查询条件返回本身
	  * @param field 字段名
	  * @param value 字段值
	  * @param opt   操作符 Eq Like ...
	  * @param logic 链接符 AND OR
	  * @param stringOrInt  字段值是否是String还是Int
	  * @return Condition
	  */
	 public Condition addCondition(String field,String value,Condition.Opt opt,Condition.Logic logic,StringOrInt stringOrInt)
	 {
		 Condition a = new Condition(  field,  value,  opt ,   logic ,stringOrInt) ;
		 clauses.add(a);
		 return this;
	 }
	 /**
	  * 
	  * @param field 字段名
	  * @param value 字段值
	  * @param opt   操作符 Eq Like ...
	  * @param logic 链接符 AND OR
	  * @return Condition
	  */
	public Condition addCondition(String field,String value,Condition.Opt opt,Condition.Logic logic)
	 {
		 Condition a = new Condition(  field,  value,  opt ,   logic ) ;
		 clauses.add(a);
		 return this;
	 }
	/**
	 * 
	  * 快速添加一个查询条件返回本身 用于price:(value or value)
	  * @param field
	  * @param values
	  * @param logic
	 * @return
	 */
	 public Condition addCondition(String field,List<Condition> values,Condition.Logic logic)
	 {
		 Condition a = new Condition(  field,  values,logic); 
		 clauses.add(a);
		 return this;
	 }
	 /**
	  * 
	  * 快速添加一个查询条件返回本身 用于//用于一个括号((a>1)or (b>1)) 
	  * @param field
	  * @param values
	  * @param logic
	  * @return 
	  */
	 public Condition addCondition(List<Condition> values,Condition.Logic logic)
	 {
		 Condition a = new Condition(values,logic); 
		 clauses.add(a);
		 return this;
	 }
	 
     /**
      * 快速插入本身一个条件
      * @param a
      * @return
      */
	 public Condition addCondition(Condition a)
	 {
		 clauses.add(a);
		 return this;
	 }
	 public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	 /**
	  * 生成查询语句
	  * @return
	  */
	 public String  builder()
	 {
		 String rstr="";
		 if(queryStr!=null)
		 {
			 rstr = queryStr;
		 }else{
			rstr =makeLuceneQuery();
		 }
		 return rstr;
	 }
	 //构造查询语句生成
	 private String makeLuceneQuery()
	 {
		 StringBuffer sb = new StringBuffer();
		 Condition c  =null;
		 for( int i=0;i<clauses.size();i++)
			{
			 	c=clauses.get(i);
			 	
				if(And.equals(c.type))
				{
				  if(i!=clauses.size()-1){
					 // String.format("(%s:%s) %s ", c.field, makeOpt(c), c.logic);
					  sb.append("(").append(c.field).append(":").append(makeOpt(c)).append(")").append(" ").append(c.logic).append(" ");
				  	}else{
				  	  sb.append("(").append(c.field).append(":").append(makeOpt(c)).append(")").append(" ");	
				  	}
				  }
				if(Or.equals(c.type))
				{
					StringBuffer sb_tmp = new StringBuffer();
					Condition conditionOrTerm = null ;
					
					for(int j=0;j<c.values.size();j++)
					{
						conditionOrTerm=c.values.get(j);
						if(j!=c.values.size()-1){
							sb_tmp.append(makeOpt(conditionOrTerm)).append(" ").append(conditionOrTerm.logic).append(" ");
						}else{
							sb_tmp.append(makeOpt(conditionOrTerm)).append(" ");
						}
					}
					 if(i!=clauses.size()-1){
						  sb.append("(").append(c.field).append(":(").append(sb_tmp).append("))").append(" ").append(c.logic).append(" ");	
					 }else{
						 sb.append("(").append(c.field).append(":(").append(sb_tmp).append("))").append(" "); 
					  	}
					
					
					//area:( 北京  OR 天津   NOT 上海)  ");
				}
				if(List.equals(c.type))
				{
					StringBuffer sb_tmp = new StringBuffer();
					Condition conditionOrTerm = null ;
					
					for(int j=0;j<c.values.size();j++)
					{
						conditionOrTerm=c.values.get(j);
						if(j!=c.values.size()-1){
							sb_tmp.append("(").append(conditionOrTerm.field).append(":").append(makeOpt(conditionOrTerm)).append(")").append(" ")
							  .append(conditionOrTerm.logic).append(" ");

						}else{
							sb_tmp.append("(").append(conditionOrTerm.field).append(":").append(makeOpt(conditionOrTerm)).append(")").append(" ");
						}
					}
					 if(i!=clauses.size()-1){
						  sb.append("(").append(sb_tmp).append(")").append(" ").append(c.logic).append(" ");	
					 }else{
						 sb.append("(").append(sb_tmp).append(")").append(" "); 
				     }
					
					
					//area:( 北京  OR 天津   NOT 上海)  ");
				}
			}
		 return sb.toString();
	 }
	 private String makeOpt(Condition c)
	 {
		 String rstr="";
		 
		 if(Opt.EQ.equals(c.opt))
		 {
			 if(StringOrInt.INT.equals(c.stringOrInt))
			 {
				 rstr=buzero(c.value)+Opt.EQ;
			 }else{
				 rstr=c.value+Opt.EQ;
			 }
			
		 }
		 if(Opt.LIKE_ANALYZER.equals(c.opt))
		 {
			 if(StringOrInt.INT.equals(c.stringOrInt))
			 {
				 rstr=buzero(c.value)+Opt.LIKE_ANALYZER;
			 }else{
				 rstr=c.value+Opt.LIKE_ANALYZER;
			 }
			
		 }
		 if(Opt.LIKE.equals(c.opt))
		 {
			 if(StringOrInt.INT.equals(c.stringOrInt))
			 {
				 rstr=buzero(c.value)+Opt.LIKE;
			 }
			 else{
				 rstr=c.value+Opt.LIKE;
			 }
		 }
		  
		 if(Opt.BETWEEN_OPEN.equals(c.opt))
		 {
			 String[] ss = c.value.split(BETWEEN_SPLIT_STR);
			 if(StringOrInt.INT.equals(c.stringOrInt))
			 {
				 rstr="{"+buzero(ss[0])+" TO "+buzero(ss[1])+"}";
			 } else{
				 rstr="{"+ss[0]+" TO "+ss[1]+"}";
			 }
			 //{2012-12-15 TO 2012-12-19}
		 }
		 if(Opt.BETWEEN_CLOSE.equals(c.opt))
		 {
			 
			 String[] ss = c.value.split(BETWEEN_SPLIT_STR);
			 if(StringOrInt.INT.equals(c.stringOrInt))
			 {
				 rstr="["+buzero(ss[0])+" TO "+buzero(ss[1])+"]";
			 }else{
				 rstr="["+ss[0]+" TO "+ss[1]+"]";
			 }
			 //[2012-12-15 TO 2012-12-19]
		 }
		 return rstr;
	 }
	 public Condition.StringOrInt getStringOrInt() {
			return stringOrInt;
		}
		public void setStringOrInt(Condition.StringOrInt stringOrInt) {
			this.stringOrInt = stringOrInt;
		}

	 
	 public static String escape(String s) {
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < s.length(); i++) {
		      char c = s.charAt(i);
		      // These characters are part of the query syntax and must be escaped
		      if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
		        || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
		        || c == '*' || c == '?' || c == '|' || c == '&' || c == '/') {
		        sb.append('\\');
		      }
		      sb.append(c);
		    }
		    return sb.toString();//.toLowerCase();
		  }
	 
	 public  static String buzero(String s)
		{
		 try{
		 	Double.valueOf(s);
		 }catch(Exception e)
		 {
			 return    s;
		 }
			 String rstr=null;
			if(s==null || "".equals(s.trim()) || "0".equals(s.trim())){
				return rs;
			}
	         String	ss[]=s.split("\\.");
	         if(ss.length==1)
	         {
	        	 rstr =rs1.substring(ss[0].length())+ss[0]+"."+rs2;
	        	 
	         }else{
	        	 rstr =rs1.substring(ss[0].length())+ss[0]+"."+ss[1]+rs2.substring(ss[1].length());
	         }
	         
			return rstr;
		}
	 
//	 public static class Mouse{
//		
//		private int age;//几个月
//		
//		public int getAge() {
//			return age;
//		}
//
//		public void setAge(int age) {
//			this.age = age;
//		}
//		public Mouse(int age){
//			this.age = age;
//		}
//	 }
//	 
//	 public static int suanfa(int startNum, int month){
//		 List<Mouse> list = new ArrayList<Mouse>();
//		 List<Mouse> remore = new ArrayList<Mouse>();//死亡的老鼠
//		 for(int i =1; i <= (startNum/2); i++){//初始化
//			 list.add(new Mouse(0));
//		 }
//		 for(int m = 0; m <= month; m ++){ //循环月
//			 for(int i = 0;i < list.size();i++){
//				 Mouse mou = list.get(i);
//				 if( mou.getAge()<=14 && mou.getAge() > 2){ //性成熟期是俩月,第三个月有第一窝
//					 for(int c = 0; c < 10/2 ; c++){ //在list里面添加10/2个初始年龄为0的对象
//						 list.add(new Mouse(0));
//					 }
//				 }else if(mou.age >24){//大于24个月，删除对象
//					 remore.add(list.get(i));
//				 }
//				 mou.setAge(mou.getAge()+1);
//			 }
//			 for(int i =0; i< remore.size(); i++){
//				 list.remove(remore.get(i));
//			 }
//		 }
//		 
//		 return list.size();
//	 }

	 public static void main(String args[]){
		 Condition con = new Condition();
		 List<Condition> orWhere= new ArrayList<Condition>();
		 orWhere.add(new Condition("","1000.11 "+Condition.BETWEEN_SPLIT_STR+"2000.78",Condition.Opt.BETWEEN_CLOSE,Condition.Logic.OR,Condition.StringOrInt.INT));
		 orWhere.add(new Condition("","3000.11 "+Condition.BETWEEN_SPLIT_STR+"4000.78",Condition.Opt.BETWEEN_OPEN,Condition.Logic.OR,Condition.StringOrInt.INT));
			
		 con.addCondition("pinpai", "三星",  Condition.Opt.EQ,Condition.Logic.AND).
		     addCondition("area", "深圳", Condition.Opt.LIKE,Condition.Logic.AND).
		     addCondition("price", orWhere, Condition.Logic.AND).
		     addCondition("salenum", "100.89",Condition.Opt.EQ, Condition.Logic.AND,Condition.StringOrInt.INT);
		 System.out.println(con.builder());
		 
	 }
}
