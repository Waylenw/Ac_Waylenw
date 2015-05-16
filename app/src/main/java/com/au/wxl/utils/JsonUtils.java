package com.au.wxl.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.au.wxl.bean.User;
import com.google.gson.Gson;

/**
 * JSON处理工具类
 * 
 * @author wxl
 */
public class JsonUtils {

	private static Gson gson = new Gson();

	/**
	 * 集合转成JSON数组 数据格式 {"array":[{"uid":"0","name":"Name0"},{"uid":"1","name":"Name1"}]}
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String parseList2JsonArray(ArrayList<User> list) throws Exception {
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray();
		for (User user : list) {
			array.put(new JSONObject(parseObject2JsonStr(user)));
		}
		jsonObject.put("array", array);
		return jsonObject.toString();

	}

	/**
	 * 对象转成json
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String parseObject2JsonStr(Object object) throws Exception {
		return gson.toJson(object);
	}
}
