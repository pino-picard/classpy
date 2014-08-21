package com.github.zxh.classpy.common;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 *
 * @author zxh
 */
public class FileComponentHelper {
    
    // todo
    public static void setNameForFileComponentFields(FileComponent ccObj)
            throws ReflectiveOperationException {
        
        for (Class<?> ccClass = ccObj.getClass(); ccClass != null; ccClass = ccClass.getSuperclass()) {
            for (Field field : ccClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (isFileComponentType(field)) {
                    // field is FileComponent
                    FileComponent ccFieldVal = (FileComponent) field.get(ccObj);
                    if (ccFieldVal != null) {
                        ccFieldVal.setName(field.getName());
                        setNameForFileComponentFields(ccFieldVal);
                    }
                } else if (isFileComponentArrayType(field)) {
                    // field is FileComponent[]
                    Object arrFieldVal = field.get(ccObj);
                    if (arrFieldVal != null) {
                        int length = Array.getLength(arrFieldVal);
                        for (int i = 0; i < length; i++) {
                            FileComponent arrItem = (FileComponent) Array.get(arrFieldVal, i);
                            if (arrItem != null) {
                                setNameForFileComponentFields(arrItem);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static boolean isFileComponentType(Field field) {
        return FileComponent.class.isAssignableFrom(field.getType());
    }
    
    private static boolean isFileComponentArrayType(Field field) {
        if (!field.getType().isArray()) {
            return false;
        }
        
        return FileComponent.class.isAssignableFrom(
                field.getType().getComponentType());
    }
    
}
