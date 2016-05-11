package com.kevin.iesutdio.kfgis.web.framework.validator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.kevin.iesutdio.kfgis.web.framework.annotation.EnumValid;
import com.kevin.iesutdio.kfgis.web.framework.annotation.CustomValid;
import com.kevin.iesutdio.kfgis.web.framework.annotation.Ignore;
import com.kevin.iesutdio.kfgis.web.framework.annotation.LengthValid;
import org.springframework.validation.FieldError;

import com.kevin.iesutdio.kfgis.web.framework.annotation.ICustomValidator;
import com.kevin.iesutdio.kfgis.web.framework.annotation.NotNullValid;
import com.kevin.iesutdio.kfgis.web.framework.annotation.NumberValid;
import com.kevin.iesutdio.kfgis.web.framework.annotation.RegexValid;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkErrorMsgContants;
import com.kevin.iesutdio.kfgis.web.framework.util.ObjUtil;
import com.kevin.iesutdio.kfgis.util.ObjectUtil;

/**
 * 注解校验
 * 
 * @author Kevin
 * 
 */
public class AnnotationValidator {

    /**
     * 是否忽略对象
     * 
     * @param field
     * @return
     */
    public boolean isIgnore(Field field) {
        Ignore ignore = field.getAnnotation(Ignore.class);
        if (ignore != null) {
            return true;
        }
        return false;
    }

    /**
     * 可以为空
     * 
     * @param field
     * @param fieldValue
     * @return
     * @deprecated
     */
    @Deprecated
    private boolean canEmpty(Field field, Object fieldValue) {
        NotNullValid notNull = field.getAnnotation(NotNullValid.class);
        if (notNull == null || notNull.value()) {
            if (fieldValue == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 可以为空
     * 
     * @param field
     * @param fieldValue
     * @return
     */
    public FieldError fieldEmpty(Field field, Object fieldValue) {
        NotNullValid notNull = field.getAnnotation(NotNullValid.class);
        if (notNull == null || notNull.value()) {

            if (fieldValue == null) { // || ((fieldValue instanceof
                                      // java.lang.String) ?
                                      // "".equalsIgnoreCase((String)
                                      // fieldValue) : false)
                                      // //去掉这句话是为了，当参数是可选参数时，如果输入的是字符串，并且字符串无验证的情况下，可以通过校验
                return new FieldError(this.getClass().getName(), field.getName(), FrameworkErrorMsgContants.ERROR_MISSINGPARAM);
            }
        }
        return null;
    }

    /**
     * 如果字段是String类型，可以验证长度
     * 
     * @param field
     * @param fieldValue
     * @return
     */
    private boolean isOutOfLength(Field field, Object fieldValue) {
        /**
         * 如果字段是String类型，可以验证长度
         */
        LengthValid lengthValidator = field.getAnnotation(LengthValid.class);
        if (String.class.equals(field.getType()) && lengthValidator != null) {
            String str = (String) fieldValue;
            if (str != null) {
                if (lengthValidator.numberMaxExclusive() != LengthValid.IGNORE_VALUE && str.length() >= lengthValidator.numberMaxExclusive()) {
                    return true;
                }
                if (lengthValidator.numberMaxInclusive() != LengthValid.IGNORE_VALUE && str.length() > lengthValidator.numberMaxInclusive()) {
                    return true;
                }
                if (lengthValidator.numberMinExclusive() != LengthValid.IGNORE_VALUE && str.length() <= lengthValidator.numberMinExclusive()) {
                    return true;
                }
                if (lengthValidator.numberMinInclusive() != LengthValid.IGNORE_VALUE && str.length() < lengthValidator.numberMinInclusive()) {
                    return true;
                }
                if (lengthValidator.numberMinInclusive() != LengthValid.IGNORE_VALUE && str.length() == lengthValidator.numberEquals()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 正则表达式
     * 
     * @param field
     * @param fieldValue
     * @return
     */
    private boolean regexMatcher(Field field, Object fieldValue) {
        /**
         * 正则表达式
         */
        RegexValid regexValidator = field.getAnnotation(RegexValid.class);
        if (regexValidator != null) {
            Pattern pattern = Pattern.compile(regexValidator.value());
            if (!pattern.matcher(String.valueOf(fieldValue)).find()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param number
     * @param numberValidator
     * @return true 未通过校验
     */
    private boolean numberVaild(Double number, NumberValid numberValidator) {
        if (number != null) {
            if (numberValidator.numberMaxExclusive() != NumberValid.IGNORE_VALUE && number >= numberValidator.numberMaxExclusive()) {
                return true;
            }
            if (numberValidator.numberMaxInclusive() != NumberValid.IGNORE_VALUE && number > numberValidator.numberMaxInclusive()) {
                return true;
            }
            if (numberValidator.numberMinExclusive() != NumberValid.IGNORE_VALUE && number <= numberValidator.numberMinExclusive()) {
                return true;
            }
            if (numberValidator.numberMinInclusive() != NumberValid.IGNORE_VALUE && number < numberValidator.numberMinInclusive()) {
                return true;
            }
            if (numberValidator.numberMinInclusive() != LengthValid.IGNORE_VALUE && number == numberValidator.numberEquals()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果字段是String类型，可以验证长度
     * 
     * @param field
     * @param fieldValue
     * @return
     */
    private boolean isNumberLimit(Field field, Object fieldValue) {
        /**
         * 如果字段是String类型，可以验证长度
         */
        NumberValid numberValidator = field.getAnnotation(NumberValid.class);
        if (ObjectUtil.isParentClass(field.getType(), Number.class) && numberValidator != null) {
            Double number = Double.valueOf(String.valueOf(fieldValue));
            return this.numberVaild(number, numberValidator);
        } else if (ObjectUtil.isParentClass(field.getType(), String.class) && fieldValue != null && numberValidator != null) {
            String strValue = (String) fieldValue;
            if ("".equalsIgnoreCase(numberValidator.split())) {
                if (!ObjUtil.isNumber(strValue)) {
                    return true;
                }
                double tempDou = Double.valueOf(strValue);
                if (this.numberVaild(tempDou, numberValidator)) {
                    return true;
                }
            } else {
                String[] enumValues = strValue.split(numberValidator.split());
                for (String tempEnumValue : enumValues) {
                    if (!ObjUtil.isNumber(tempEnumValue)) {
                        return true;
                    }
                    double tempDou = Double.valueOf(tempEnumValue);
                    if (this.numberVaild(tempDou, numberValidator)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 需要判断枚举型
     * 
     * @param field
     * @param fieldValue
     * @return
     */
    private boolean enumHit(Field field, Object fieldValue) {
        /**
         * 需要判断枚举型
         */
        EnumValid enumValidator = field.getAnnotation(EnumValid.class);
        if (enumValidator != null && enumValidator.value().length > 0 && field.getType().getName().startsWith("java.lang.")) {
            String enumValue = String.valueOf(fieldValue);
            boolean checked = false;
            if ("".equalsIgnoreCase(enumValidator.split())) {
                for (String s : enumValidator.value()) {
                    if (s.equalsIgnoreCase(enumValue)) {
                        checked = true;
                    }
                }
            } else {
                String[] enumValues = enumValue.split(enumValidator.split());
                for (String tempEnumValue : enumValues) {
                    for (String s : enumValidator.value()) {
                        if (s.equalsIgnoreCase(tempEnumValue)) {
                            checked = true;
                        }
                    }
                }
            }

            if (!checked) {
                return false;
            }
        }

        return true;
    }

    private static Map<String, ICustomValidator> validatorMap = null;

    /**
     * @param field
     * @param fieldValue
     * @return true 验证通过
     */
    private boolean isCustomValid(Field field, Object fieldValue) {
        CustomValid customVaild = field.getAnnotation(CustomValid.class);
        if (customVaild != null) {
            if (validatorMap == null) {
                validatorMap = new HashMap<String, ICustomValidator>();
            }
            ICustomValidator cv = validatorMap.get(customVaild.validator().getName());
            if (cv == null) {
                try {
                    cv = customVaild.validator().newInstance();
                    validatorMap.put(customVaild.validator().getName(), cv);
                } catch (InstantiationException e) {
                    //					e.printStackTrace();
                    return false;
                } catch (IllegalAccessException e) {
                    //					e.printStackTrace();
                    return false;
                }
            }
            return cv.validate(fieldValue);
        }
        return true;
    }

    public FieldError validateField(Field field, Object obj) {
        if (obj != null && (((obj instanceof java.lang.String) && !"".equalsIgnoreCase((String) obj)) || obj instanceof java.lang.Number|| obj instanceof java.lang.Boolean)) {
            String msg = null;
            // if (((obj instanceof java.lang.String) ?
            // "".equalsIgnoreCase((String) obj) : false)) {
            // return new FieldError(this.getClass().getName(), field.getName(),
            // FrameworkErrorMsgContants.ERROR_NULLPARAM);
            // } else
            String errorCode="";
            
            if (this.isOutOfLength(field, obj)) {
//                msg = FrameworkErrorMsgContants.ERROR_OUTOFLENGTH;
                LengthValid lengthValidator = field.getAnnotation(LengthValid.class);
                if(lengthValidator!=null){
                    errorCode=lengthValidator.errCode();
                    if("".equalsIgnoreCase(lengthValidator.errMsg())){
                        msg = FrameworkErrorMsgContants.ERROR_OUTOFLENGTH;
                    }else{
                        msg=lengthValidator.errMsg();
                    }
                }
            } else if (!this.regexMatcher(field, obj)) {
                RegexValid regexValidator = field.getAnnotation(RegexValid.class);
                if(regexValidator!=null){
                    errorCode=regexValidator.errCode();
                    if("".equalsIgnoreCase(regexValidator.errMsg())){
                        msg = FrameworkErrorMsgContants.ERROR_REGEX;
                    }else{
                        msg=regexValidator.errMsg();
                    }
                }
//                msg = FrameworkErrorMsgContants.ERROR_REGEX;
            } else if (this.isNumberLimit(field, obj)) {
                NumberValid numberValidator = field.getAnnotation(NumberValid.class);
                if(numberValidator!=null){
                    errorCode=numberValidator.errCode();
                    if("".equalsIgnoreCase(numberValidator.errMsg())){
                        msg = FrameworkErrorMsgContants.ERROR_NUMBERLIMIT;
                    }else{
                        msg=numberValidator.errMsg();
                    }
                }
//                msg = FrameworkErrorMsgContants.ERROR_NUMBERLIMIT;
            } else if (!this.enumHit(field, obj)) {
                EnumValid enumValidator = field.getAnnotation(EnumValid.class);
//                msg = FrameworkErrorMsgContants.ERROR_ENUMHIT;
                if(enumValidator!=null){
                    errorCode=enumValidator.errCode();
                    if("".equalsIgnoreCase(enumValidator.errMsg())){
                        msg = FrameworkErrorMsgContants.ERROR_ENUMHIT;
                    }else{
                        msg=enumValidator.errMsg();
                    }
                }
            } else if (!isCustomValid(field, obj)) {
                CustomValid customVaild = field.getAnnotation(CustomValid.class);
                if(customVaild!=null){
                    errorCode=customVaild.errCode();
                    if("".equalsIgnoreCase(customVaild.errMsg())){
                        msg = FrameworkErrorMsgContants.ERROR_CUSTOMPARAM;
                    }else{
                        msg=customVaild.errMsg();
                    }
                }
//                msg = FrameworkErrorMsgContants.ERROR_CUSTOMPARAM;
            }
            if (msg != null) {
                /**
                 * ###以ObjectName代表ErrorCode
                 */
                if("".equalsIgnoreCase(errorCode)){
                    return new FieldError("", field.getName(), msg);
                }else{
                    return new FieldError(errorCode, field.getName(), msg);
                }
                
            }
        }
        return null;
    }
    

}
