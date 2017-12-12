package com.apestech.framework.esb.processor.mapping.config;

import com.apestech.framework.util.DateUtil;

public class Field {

    private String attrname = null;
    private String fieldtype = null;
    private String width = null;
    private String decimals = null;
    private String target = null;
    private String defaultValue = null;
    private String allownull = null;
    private String name = null;
    private String format = null;
    private String check = null;
    private String key = null;
    private String SJ=null;  //税金
    private String JE=null;  //金额
    private String ZSJ=null;  //总税金
    private String ZJE=null;  //总金额
    private String UNABS=null;  //不论正负，取负数
    private String ABS=null;   //不论正负,取正数
    private String qx = null; //权限
    private String length = null; //长度限制
    
    public String getQx() {
        return qx;
    }
    
    public void setQx(String qx) {
        this.qx = qx;
    }
    
	public String getUNABS() {
		return UNABS;
	}

	public void setUNABS(String uNABS) {
		UNABS = uNABS;
	}

	public String getABS() {
		return ABS;
	}

	public void setABS(String aBS) {
		ABS = aBS;
	}

	public String getZSJ() {
		return ZSJ;
	}

	public void setZSJ(String zSJ) {
		ZSJ = zSJ;
	}

	public String getZJE() {
		return ZJE;
	}

	public void setZJE(String zJE) {
		ZJE = zJE;
	}

	public String getSJ() {
		return SJ;
	}

	public void setSJ(String sJ) {
		SJ = sJ;
	}

	public String getJE() {
		return JE;
	}

	public void setJE(String jE) {
		JE = jE;
	}

	public String getAttrname() {
        return attrname;
    }

    public String getFieldtype() {
        return fieldtype;
    }

    public String getWidth() {
        return width;
    }

    public String getDecimals() {
        return decimals;
    }

    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDefaultValue() {
        return defaultValue;
    }

    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getAllownull() {
        return allownull;
    }

    public void setAllownull(String allownull) {
        this.allownull = allownull;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Object conver(Object value) throws Exception {
        if (fieldtype == null) {
            return value;
        }
        Object result;
        String s = String.valueOf(value);
        if (fieldtype.equalsIgnoreCase("string")) {
            result = s;
        } else if (fieldtype.equalsIgnoreCase("int")) {
            if (s.length() == 0) {
                result = 0;
            } else {
                result = Integer.parseInt(s);
            }
        }  else if (fieldtype.equalsIgnoreCase("long")) {
            if (s.length() == 0) {
                result = 0;
            } else {
                result = Long.parseLong(s);
            }
        }else if (fieldtype.equalsIgnoreCase("double")) {
            if (s.length() == 0) {
                result = 0;
            } else {
                result = Double.parseDouble(s);
            }
        } else if (fieldtype.equalsIgnoreCase("date") || fieldtype.equalsIgnoreCase("dateTime")) {
            if (s.length() == 0) {
                result = String.valueOf("");
            } else {
                if (s.length() < 11) {
                    result = DateUtil.parseDate(s);
                } else {
                    result = DateUtil.parseTimestamp(s);
                }
            }
        } else {
            result = value;
        }
        return result;
    }

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
