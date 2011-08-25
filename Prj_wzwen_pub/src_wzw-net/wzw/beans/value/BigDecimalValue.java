package wzw.beans.value ;

import java.math.BigDecimal;

import wzw.beans.Value;

/**
 * This class represents a BigDecimal value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class BigDecimalValue extends Value {
    private BigDecimal value;

	//��ҳ��õ��� String ����ֱ��תΪBigDecimal����
    //public BigDecimalValue(String value) {
    //    this.value = new BigDecimal(value);
    //}

    public BigDecimalValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getBigDecimal() {
        return value;
    }

    public String getString() {
        return value.toString();
    }
}