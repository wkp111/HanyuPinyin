package com.wkp.hanyupinyin.model.utils

import android.util.Pair
import com.github.stuxuhai.jpinyin.PinyinFormat
import com.github.stuxuhai.jpinyin.PinyinHelper
import java.util.regex.Pattern

/**
 * Created by user on 2018/3/8.
 * 汉语拼音工具
 */
class PinyinUtil {
    companion object {
        /**
         * 汉语拼音对应集合
         */
        fun convertToPinyinString(str: String): MutableList<Pair<String, String>> {
            val flag = "/*&"
            var tempStr = str
            val pair = findEnglishWords(tempStr)
            val indexList = pair.first
            val englishList = pair.second
            for (s in englishList) {
                tempStr = tempStr.replace(s, "")
            }
            val pinyinString = PinyinHelper.convertToPinyinString(tempStr, flag, PinyinFormat.WITH_TONE_MARK)
            val pinyinArray = pinyinString.split(flag)
            val pinyinList = arrayListOf<String>()
            pinyinList.addAll(pinyinArray)
            val hanyuList = arrayListOf<String>()
            for (c in tempStr) {
                hanyuList.add(c.toString())
            }
            var length = 1
            for (i in indexList.indices) {
                val index = indexList[i] - length + 1
                val s = englishList[i]
                pinyinList.add(index,s)
                hanyuList.add(index,s)
                length += s.length - 1
            }
            val pairList = arrayListOf<Pair<String,String>>()
            for (index in pinyinList.indices) {
                pairList.add(Pair.create(hanyuList[index],pinyinList[index]))
            }
            return pairList
        }

        /**
         * 找出汉语中的英文单词
         */
        private fun findEnglishWords(str: String): Pair<List<Int>,List<String>> {
            val enResult = arrayListOf<String>()
            val indexResult = arrayListOf<Int>()
            var isEnglish = false
            var sb: StringBuilder? = null
            var index = 0
            for (i in str.indices) {
                val c = str[i]
                if (Companion.isEnglish(c)) {
                    if (isEnglish) {
                        sb?.append(c)
                    } else {
                        isEnglish = true
                        index = i
                        sb = StringBuilder()
                        sb.append(c)
                    }
                } else {
                    if (isEnglish) {
                        isEnglish = false
                        enResult.add(sb.toString())
                        indexResult.add(index)
                        sb = null
                    }
                }
            }
            return Pair(indexResult,enResult)
        }

        /**
         * 判断英文字符
         */
        private fun isEnglish(c: Char): Boolean {
            return Pattern.compile("[a-zA-Z]").matcher(c.toString()).matches()
        }
    }
}