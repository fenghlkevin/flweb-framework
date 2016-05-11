package com.kevin.iesutdio.kfgis.web.framework.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ObjUtil {

	public static boolean isEmpty(String args) {
		// if (null==args || "".equalsIgnoreCase(args)) {
		// return true;
		// }
		// return false;
		return !(args != null && args.length() > 0);
	}

	public static boolean isEmpty(String args, boolean trimBlank) {
		if (trimBlank) {
			if (isEmpty(args) || "".equalsIgnoreCase(args.trim())) {
				return true;
			}
		} else {
			return isEmpty(args);
		}

		return false;
	}

	public static boolean isEmpty(Map map) {
		return map == null || map.isEmpty();
	}

	public static boolean isEmpty(Collection collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}

		if (object instanceof Collection) {
			return isEmpty((Collection) object);
		} else if (object instanceof String) {
			return isEmpty((String) object);
		} else if (object.getClass().isArray()) {
			if (object instanceof String[]) {
				String[] strObj = (String[]) object;
				if (strObj.length == 1 && isEmpty(strObj[0])) {
					return true;
				}
				return false;
			} else if (object instanceof Number[]) {
				Number[] strObj = (Number[]) object;
				if (strObj.length == 1 && isEmpty(strObj[0])) {
					return true;
				}
				return false;
			} else if (object instanceof byte[]) {
				byte[] strObj = (byte[]) object;
				if (strObj.length == 1 && isEmpty(strObj[0])) {
					return true;
				}
				return false;
			} else if (object instanceof boolean[]) {
				boolean[] strObj = (boolean[]) object;
				if (strObj.length == 1 && isEmpty(strObj[0])) {
					return true;
				}
				return false;
			} else {
				return false;
			}

		} else if (object instanceof Map) {
			return isEmpty((Map) object);
		}
		return false;

	}

	public static boolean isNumber(String args) {
		boolean isnumber = true;
		if (ObjUtil.isEmpty(args)) {
			return false;
		}
		for (int i = 0; i < args.length(); i++) {
			if (Character.isDigit(args.charAt(i)) || args.charAt(i) == '.'
					|| args.charAt(i) == '-') {
				continue;
			} else {
				isnumber = false;
			}
		}
		return isnumber;
	}

//	public static void main(String[] args) {
//		System.out.println(ObjUtil.numberReplace("0000000", "9"));
//		System.out.println(ObjUtil.numberReplaceForAdmin("000000", "9"));
//		System.out.println(ObjUtil.numberReplaceForAdmin("500000", "9"));
//		System.out.println(ObjUtil.numberReplaceForAdmin("501000", "9"));
//		System.out.println(ObjUtil.numberReplaceForAdmin("510000", "9"));
//		System.out.println(ObjUtil.numberReplaceForAdmin("500100", "9"));
//		System.out.println(ObjUtil.numberReplaceForAdmin("500010", "9"));
//	}

	public static String numberReplace(String number, String arg) {
		StringBuffer str = new StringBuffer(number);
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.charAt(i) == '0') {
				str.replace(i, i + 1, arg);
			} else {
				break;
			}
		}
		return str.toString();
	}
	
	/**
	 * 字符替换--for行政区检索，按照省市区进行划分的，使得取得当前行政区的最大code更为准确，
	 * 解决500000--最大值599999的问题，真正的最大值应该是509999
	 * @param number 
	 * @param arg
	 * @return
	 */
	public static String numberReplaceForAdmin(String number, String arg) {
        StringBuffer str = new StringBuffer(number);
        for (int i = str.length() - 1; i >= 0; i--) {
            if (i % 2 == 0) {
                if (str.charAt(i) == '0') {
                    str.replace(i, i + 1, arg);
                } else {
                    break;
                }  
            } else {
                if (str.charAt(i) == '0' && str.charAt(i-1) == '0') {
                    str.replace(i, i + 1, arg);
                } else {
                    break;
                }
            }
        }
        return str.toString();
    }

	public static String numberDigit(String source, int digit) {
		String temp = source;
		if (ObjUtil.isEmpty(temp) || !isNumber(temp) || digit < 0) {
			return temp;
		}
		if (temp.indexOf(".") != -1) {
			String[] numParts = temp.split("[.]");
			String digitPart = numParts[1];
			int digitLength = digitPart.length();
			if (digitLength > digit) {
				// return CalculateUtil.divideByBigDecimal(temp, "1", digit);
				return new BigDecimal(temp).divide(new BigDecimal(1), digit,
						BigDecimal.ROUND_HALF_UP).toString();
			} else if (digitLength < digit) {
				StringBuffer strtemp = new StringBuffer();
				strtemp.append(temp);
				for (int i = 1; i <= -(digitLength - digit); i++) {
					strtemp.append("0");
				}
				return strtemp.toString();
			} else {
				return temp;
			}
		} else {
			StringBuffer strtemp = new StringBuffer();
			strtemp.append(temp);
			if (digit != 0) {
				strtemp.append(".");
			}
			for (int i = 0; i < digit; i++) {
				strtemp.append("0");
			}
			return strtemp.toString();
		}
	}

	public static List<String> arrayToList(String[] args) {
		return Arrays.asList(args);
//		List<String> l = new ArrayList<String>();
//		for (int i = 0; i < args.length; i++) {
//			l.add(args[i]);
//		}
//		return l;
	}

	public static String arrayToString(String[] a) {
		if (a == null) {
			return null;
		}
		int iMax = a.length - 1;
		if (iMax == -1) {
			return "";
		}

		StringBuilder b = new StringBuilder();
		for (int i = 0;; i++) {
			b.append(String.valueOf(a[i]));
			if (i == iMax) {
				return b.toString();
			}
			b.append(", ");
		}
	}

	/**
	 * <p>
	 * Discription:[判断字符是否为中文]
	 * </p>
	 * 
	 * @param c
	 * @return
	 * @author:fengheliang
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	public static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * <p>
	 * Discription:[判断字符串中有中文]
	 * </p>
	 * 
	 * @param strName
	 * @return
	 * @author:fengheliang
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	public static final boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Discription:[判断字符串是否为中文字符串]
	 * </p>
	 * 
	 * @param chineseStr
	 * @return
	 * @author:fengheliang
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	public static final boolean isChineseCharacter(String chineseStr) {
		char[] charArray = chineseStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {

				// Java判断一个字符串是否有中文是利用Unicode编码来判断，

				// 因为中文的编码区间为：0x4e00--0x9fbb
				return true;
			}
		}
		return false;
	}
	
	public static final boolean isAllChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if(c=='/'||c=='.'||c=='-'||c=='\''||c==' '||c=='\t'||Character.isDigit(c)){
                continue;
            }
            
            
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }
	
	public static void main(String[] args){
	    System.out.println(ObjUtil.isAllChinese("中华"));
	    System.out.println(ObjUtil.isAllChinese("中华 这个"));
	    System.out.println(ObjUtil.isAllChinese("中华 这个  仨"));
	    System.out.println(ObjUtil.isAllChinese("中华-"));
	    System.out.println(ObjUtil.isAllChinese("中华.单独"));
	    System.out.println(ObjUtil.isAllChinese("中华/单独"));
	    System.out.println(ObjUtil.isAllChinese("中华'单独"));
	    System.out.println(ObjUtil.isAllChinese("中华    单独"));
	    System.out.println(ObjUtil.isAllChinese("中华    单独 12345"));
	}

}
