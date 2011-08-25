/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.lang ;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;


/**
 * <p>��������Ĳ����࣬�����������ͼ����͵����顣
 * 			���ڵ���������ͷ�����
 * 		��װ��Ŀ����Ϊ����һ�㣬�����ܼ����������䶯����Ķ�ϵͳ��Ӱ�졣
 * 	�ο��б�
 * 		org.apache.commons.lang.* 
 * 
 * </p>
 * @author Zeven on 2008-5-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */
public class ArraySupport {

    /**
     * Returns true if the specified value matches one of the elements
     * in the specified array.
     *
     * @param array the array to test.
     * @param value the value to look for.
     * @return true if valid, false otherwise
     */
    public static boolean isContains(String[] array, String value) {
        boolean isIncluded = false;

        if (array == null || value == null) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            if (value.equals(array[i])) {
                isIncluded = true;
                break;
            }
        }
        //return ArrayUtils.contains(array, value);
        return isIncluded;
    }
    
    public static int indexOf( Object[] array, Object obj ){
    	return ArrayUtils.indexOf(array, obj);
    }
    

    public static int lastIndexOf( Object[] array, Object obj ){
    	return ArrayUtils.lastIndexOf(array, obj);
    }

    public static Object[] subarray( Object[] array, int startIndexInclusive, int endIndexExclusive ){
    	return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive);
    }
    
    public static Map<?, ?> toMap(Object[] array){
    	return ArrayUtils.toMap( array );
    }
    
    public static Object[] add(Object[] array,
            Object element){
    	return ArrayUtils.add( array, element);
    }
    
    public static Object[] add(Object[] array,
            int index,
            Object element){
    	return ArrayUtils.add( array, index, element);
    	
    }
    
    public static Object[] addAll(Object[] array1,
            Object[] array2){
    	return ArrayUtils.addAll( array1, array2);
    	
    }
    
    public static Object[] clone(Object[] array){
    	return ArrayUtils.clone( array );
    	
    }
    
    public static Object[] removeElement(Object[] array,
            Object element){
    	return ArrayUtils.removeElement( array, element );
    	
    }
    
}
