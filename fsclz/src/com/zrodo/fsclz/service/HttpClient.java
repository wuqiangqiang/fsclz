package com.zrodo.fsclz.service;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zrodo.fsclz.utils.NetworkUtil;

public class HttpClient {

	public static RequestQueue queue = Volley.newRequestQueue(ZRDApplication.getContext());

  //{"code":"0","message":"没有更好的建议","result":"{"字段1":"值1","字段2":"值2","字段3":"值3"}"}
	public static final String RESULT_CODE = "code"; // 状态码，后台返回成功或者失败
	public static final String RETURN_MESSAGE = "message";// 消息，后台返回的文本消息，提示或者错误信息
	public static final String RESULT = "result";// 成功的返回结果
	public static final String SUCCESS = "1"; // 成功
	public static final String FAILURE = "0";// 失败

	// volley post请求(统一格式后可用，暂时未启用)
	/*public static void postRequest(Context context, String url,final Class clazz, final Map<String, String> map,final OnResponseListener listener) {
		    StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
					@SuppressWarnings("unchecked")
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);// 将json数据转化为json对象
							if (!jsonObject.isNull(RESULT_CODE)) {// 判断code字段是否存在
								String code = jsonObject.getString(RESULT_CODE);// 获得code的值
								if (code.equals(SUCCESS)) { // 成功则继续往下判断
									if (!jsonObject.isNull(RESULT)) {
										String result = jsonObject.getString(RESULT);
										Object o = null;
										if (result.charAt(0) == '{') {
											// 解析对象
											o = new Gson().fromJson(result,clazz);
										} else {
											// 这个方法是在stackoverflow中找到的可将json转换为list，普通的通过type去解析是不行的
											o = new Gson().fromJson(result,com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null,ArrayList.class,clazz));
										}
										listener.success(o);
									}

								} else if (code.equals(FAILURE)) {// 失败返回错误信息
									if (!jsonObject.isNull(RETURN_MESSAGE)) {// 判断message字段是否存在
										listener.failure(jsonObject.getString(RETURN_MESSAGE));// 返回后台失败信息
									}

								} else {
									listener.failure("无效的状态码");// 返回状态码无效的错误信息
								}

							} else {
								listener.failure("状态码不存在");// 返回状态码不存在的错误信息
							}
						} catch (JSONException e) {
							e.printStackTrace();
							listener.failure(e.getLocalizedMessage());// 返回json异常的错误信息
						}
						listener.success(response);
					}
			}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						listener.failure(error.getLocalizedMessage());// 返回Volley异常的错误信息
					}
			}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return map;
			}
		};

		// 判断网络是否连接
		if (NetworkUtil.isAvailable(ZRDApplication.getContext())) {
			addRequest(request, context);// 将请求添加到队列
		} else {
			listener.failure("网络未连接");// 返回网络未连接的错误信息
		}
	}*/

	// volley get请求
	public static void getRequest(Context context, String url,final OnResponseListener listener) {
		    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if(response != null){
								if(!response.isNull(RESULT_CODE)){
									String code = response.getString(RESULT_CODE);
									if(code.equals(SUCCESS)){
										    listener.success(response);
										    return;
									} 
									if (code.equals(FAILURE)) {// 失败返回错误信息
										if (!response.isNull(RETURN_MESSAGE)) {// 判断message字段是否存在
											listener.failure(response.getString(RETURN_MESSAGE));// 返回后台失败信息
										}
									} else {
										listener.failure("无效的状态码");// 返回状态码无效的错误信息
									} 								
								} else {
									listener.failure("状态码不存在");// 返回状态码不存在的错误信息
								}
							}												
						} catch (JSONException e) {
							e.printStackTrace();
						}	
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						listener.failure(error.getLocalizedMessage());// 返回Volley异常的错误信息
					}
				});

		// 判断网络是否连接
		if (NetworkUtil.isAvailable(ZRDApplication.getContext())) {
			addRequest(request, context);// 将请求添加到队列
		} else {
			listener.failure("网络未连接");// 返回网络未连接的错误信息
		}

	}

	/**
	 * 添加请求到请求队列中
	 * @param request请求
	 * @param tag  目标对象，Activity或者Fragment
	 */
	private static void addRequest(Request<?> request, Object tag) {
		if (tag != null) {
			request.setTag(tag);
		}
		queue.add(request);
	}

	/**
	 * 取消目标对象中的所有请求，在Activity或者Fragment销毁的时候调用
	 * @param tag目标对象，Activity或者Fragment
	 */
	public static void cancelRequest(Object tag) {
		queue.cancelAll(tag);
	}

}