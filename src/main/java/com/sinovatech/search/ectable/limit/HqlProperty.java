package com.sinovatech.search.ectable.limit;

import java.util.Collection;

public class HqlProperty {
	String ASC;
	String DESC;
	private String propertyName;
	private Object max;
	private Object min;
	private Object compareValue;
	private int chkType;
	private boolean useRefQuery;
	private boolean reverseCompare;
	public static int TYPE_EQ = 1;
	public static int TYPE_LIKE = 2;
	public static int TYPE_COMPARE = 3;
	public static int TYPE_COMPARE_EQ_MAX = 5;
	public static int TYPE_COMPARE_EQ_MIN = 6;
	public static int TYPE_COMPARE_EQ = 7;
	public static int TYPE_COMPARE_RE = 8;
	public static int TYPE_COMPARE_EQ_MIN_RE = 9;
	public static int TYPE_COMPARE_EQ_MAX_RE = 10;
	public static int TYPE_COMPARE_EQ_RE = 11;
	public static int TYPE_IN = 4;
	public static int TYPE_NOTEQ = 12;
	public static int TYPE_COMPARE_EQ_LT = 13;
	public static int TYPE_COMPARE_EQ_GT = 14;

	public HqlProperty(String propertyName, Object max, Object min,
			int chkType, boolean useRefQuery, boolean reverseCompare) {
		ASC = "asc";
		DESC = "desc";
		this.propertyName = propertyName;
		this.max = max;
		this.min = min;
		this.chkType = chkType;
		this.useRefQuery = useRefQuery;
		this.reverseCompare = reverseCompare;
	}

	public HqlProperty(String propertyName, Object max, Object min,
			int chkType, boolean useRefQuery) {
		ASC = "asc";
		DESC = "desc";
		this.propertyName = propertyName;
		this.max = max;
		this.min = min;
		this.chkType = chkType;
		this.useRefQuery = useRefQuery;
	}

	public HqlProperty(String propertyName, Object compareValue, int chkType,
			boolean useRefQuery) {
		this.propertyName = propertyName;
		this.compareValue = compareValue;
		this.chkType = chkType;
		this.useRefQuery = useRefQuery;
	}

	public static HqlProperty getEq(String name, Object value) {
		return new HqlProperty(name, null, value, TYPE_EQ, true);
	}

	public static HqlProperty getLike(String name, String value) {
		return new HqlProperty(name, null, value, TYPE_LIKE, true);
	}

	public static HqlProperty getCompare(String name, Object max, Object min) {
		return new HqlProperty(name, max, min, TYPE_COMPARE, true);
	}

	public static HqlProperty getCompareLT(String name, Object compareValue) {
		return new HqlProperty(name, compareValue, TYPE_COMPARE_EQ_LT, true);
	}

	public static HqlProperty getCompareGT(String name, Object compareValue) {
		return new HqlProperty(name, compareValue, TYPE_COMPARE_EQ_GT, true);
	}

	public static HqlProperty getEqCompare(String name, Object max, Object min) {
		return new HqlProperty(name, max, min, TYPE_COMPARE_EQ, true);
	}

	public static HqlProperty getEqMinCompare(String name, Object max,
			Object min) {
		return new HqlProperty(name, max, min, TYPE_COMPARE_EQ_MIN, true);
	}

	public static HqlProperty getEqMaxCompare(String name, Object max,
			Object min) {
		return new HqlProperty(name, max, min, TYPE_COMPARE_EQ_MAX, true);
	}

	public static HqlProperty getReCompare(String name, Object max, Object min) {
		return new HqlProperty(name, max, min, TYPE_COMPARE_RE, true);
	}

	public static HqlProperty getReEqCompare(String name, Object max, Object min) {
		return new HqlProperty(name, max, min, TYPE_COMPARE_EQ_RE, true);
	}

	public static HqlProperty getReEqMinCompare(String name, Object max,
			Object min) {
		return new HqlProperty(name, max, min, TYPE_COMPARE_EQ_MIN_RE, true);
	}

	public static HqlProperty getReEqMaxCompare(String name, Object max,
			Object min) {
		return new HqlProperty(name, max, min, TYPE_COMPARE_EQ_MAX_RE, true);
	}

	public static HqlProperty getIn(String name, Collection values) {
		return new HqlProperty(name, null, values, TYPE_IN, true);
	}

	public static HqlProperty getIn(String name, Object values[]) {
		return new HqlProperty(name, null, ((Object) (values)), TYPE_IN, true);
	}

	public static HqlProperty getNotEq(String name, Object value) {
		return new HqlProperty(name, null, value, TYPE_NOTEQ, true);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getMax() {
		return max;
	}

	public void setMax(Object max) {
		this.max = max;
	}

	public Object getMin() {
		return min;
	}

	public void setMin(Object min) {
		this.min = min;
	}

	public int getChkType() {
		return chkType;
	}

	public void setChkType(int chkType) {
		this.chkType = chkType;
	}

	public boolean isUseRefQuery() {
		return useRefQuery;
	}

	public void setUseRefQuery(boolean useRefQuery) {
		this.useRefQuery = useRefQuery;
	}

	public Object getCompareValue() {
		return this.compareValue;
	}
}
