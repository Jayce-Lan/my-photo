package com.photo.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class AdmdvsToPTUtil {
    /**
     * 区划转拼音
     * @param admdvsName
     * @return
     */
    public static String admdvsToPT(String admdvsName) {
        String admdvsPt = "";
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        // 控制大小写（小写）
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 音标：用数字代替
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        // 使用v表示
        hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] chars = admdvsName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            try {
                String[] s = PinyinHelper.toHanyuPinyinStringArray(chars[i], hanyuPinyinOutputFormat);
                for (String item : s) {
                    admdvsPt += item;
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                return badHanyuPinyinOutputFormatCombination.getMessage();
            }
        }
        return admdvsPt;
    }
}
