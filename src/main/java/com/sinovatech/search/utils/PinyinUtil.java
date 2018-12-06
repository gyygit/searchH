package com.sinovatech.search.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

	


public class PinyinUtil {

    //字母Z使用了两个标签，这里有２７个值
    //i, u, v都不做声母, 跟随前面的字母
    private char[] chartable =
            {
                '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈',
                '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然',
                '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'
            };

    private char[] alphatable =
            {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 
                'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            };


    private int[] table = new int[27];
    //初始化
    {
        for (int i = 0; i < 27; ++i) {
            table[i] = gbValue(chartable[i]);
        }
    }

    public PinyinUtil() {
    }

    //主函数,输入字符,得到他的声母,
    //英文字母返回对应的大写字母
    //其他非简体汉字返回 '0'

    public char Char2Alpha(char ch) {

        if (ch >= 'a' && ch <= 'z')
            return (char) (ch - 'a' + 'A');
        if (ch >= 'A' && ch <= 'Z')
            return ch;
        int gb = gbValue(ch);
        if (gb < table[0])
            return '0';
        int i;
        for (i = 0; i < 26; ++i) {
            if (match(i, gb)) break;
        }
        if (i >= 26)
            return '0';
        else
            return alphatable[i];
    }

    //根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
    public String String2Alpha(String SourceStr){
        
        String Result = "";
        int StrLength = SourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                Result += Char2Alpha(SourceStr.charAt(i));
            }
        } catch (Exception e) {
            Result = "";
        }
        return Result;
    }

    private boolean match(int i, int gb) {
        
        if (gb < table[i])
            return false;
        int j = i + 1;

        //字母Z使用了两个标签
        while (j < 26 && (table[j] == table[i])) ++j;
        if (j == 26)
            return gb <= table[j];
        else
            return gb < table[j];
    }

    //取出汉字的编码
    private int gbValue(char ch) {
        
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] &
                    0xff);
        } catch (Exception e) {
            return 0;
        }
    }
    /**
	 * 获取汉字首字母，英文字符不变
	 * 
	 * @param chinese
	 *            汉字串
	 * @return 汉字首字母
	 */
    public static String cn2FirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		StringBuffer pybf1 = new StringBuffer();
		char[] arr = chinese.toCharArray();
		String index  = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					if(Character.toString(arr[i]).matches("[\\u4E00-\\u9FA5]+")){
						String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i],
								defaultFormat);
						if (_t != null) {
							pybf.append(_t[0].charAt(0));
						}
					}else{
						pybf.append(arr[i]);
					}
					 
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
			if (arr[i] > 128) {
				try {
					if(Character.toString(arr[i]).matches("[\\u4E00-\\u9FA5]+")){
						pybf1.append(PinyinHelper.toHanyuPinyinStringArray(arr[i],
								defaultFormat)[0]);
					}else{
						pybf1.append(arr[i]);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf1.append(arr[i]);
			}
		}
		if(pybf.toString().equals("z") || pybf.toString().equals("s") || pybf.toString().equals("c")){
			if(pybf.toString().length()<2)
				return pybf.toString().replaceAll("\\W", "").trim();
		//如果获得是ZSC 判断后面字母是不是H 是则追加
			String index1 = pybf1.toString();
			index = index1.substring(index1.indexOf(pybf.toString())+1, index1.indexOf(pybf.toString())+2);
	//		index = pybf1.toString().substring(pybf1.toString().indexOf(pybf.toString(), 1));
			pybf.append(index);
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}
 
	/**
	 * 获取汉字串拼音，英文字符不变
	 * 
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音
	 */
	public static String cn2Spell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					if( Character.toString(arr[i]).matches("[\\u4E00-\\u9FA5]+")){
						pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i],
								defaultFormat)[0]);
					}else{
						pybf.append(arr[i]);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}  

}

