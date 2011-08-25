package com.kingcore.framework.bean.value ;

import com.kingcore.framework.bean.Value;
/**
 * This class represents a double value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class DoubleValue extends Value {
    private double value;

	//��ҳ��õ���String��������
    public DoubleValue(String value) {
        this.value = Double.parseDouble(value) ;
    }
    
    //
    public DoubleValue(double value) {
        this.value = value;
    }

    public double getDouble() {
        return value;
    }

    public String getString() {
        return String.valueOf(value);
    }
}
