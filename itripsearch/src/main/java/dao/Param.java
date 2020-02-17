package dao;

import java.io.Serializable;

public class Param implements Serializable {
    private String key;
    private Object value;
    private String operator;

    public static class OPERATOR { //判断语句；OR；或者 AND 并且 in 子查询
        public static String OR = "OR";
        public static String AND = "AND";
        public static String IN = "IN";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Param(String key, Object value) { //构造方法
        this.key = key;
        this.value = value;
        this.operator = OPERATOR.AND;
    }

    public Param(String key, Object value, String operator) {
        this.key = key;
        this.value = value;
        this.operator = operator;
    }
}
