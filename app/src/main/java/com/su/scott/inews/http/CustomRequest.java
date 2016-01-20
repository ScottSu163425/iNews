package com.su.scott.inews.http;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.su.scott.inews.util.L;

/**
 * @类名 CustomRequest
 * @描述 基于Volley的http请求封装
 * @作者 Su
 * @时间 2015年11月18日
 */
public class CustomRequest {
    private Context context;
    private static CustomRequest mInstance;
    private static final String DEFAULT_EMPTY_VALUE = "";
    private static final String KEY_DATA_ARRAY = "newslist";

    private CustomRequest(Context context) {
        this.context = context;
    }

    public static CustomRequest getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new CustomRequest(context);
        }
        return mInstance;
    }

    public void postString(RequestQueue requestQueue,String tag,String url, final CustomParam header, final CustomParam param, final SimpleListener listener) {
        StringRequest request = new StringRequest(Method.POST, url, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
//                    Toast.makeText(context, "请求失败，请检查网络", 0).show();
                }
                listener.onFailed(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (null != param) {
                    return param.getMap();
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (null != header) {
                    return header.getMap();
                }
                return super.getHeaders();
            }
        };
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void getString(RequestQueue requestQueue,String tag,String url, final CustomParam header, final CustomParam param, final SimpleListener listener) {
        String getUrl = url + "?" + getParamUrlString(param);

        L.e("request:" + getUrl);
        StringRequest request = new StringRequest(Method.GET, getUrl, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
                L.e("getString response:" + response);
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
//                    Toast.makeText(context, "请求失败，请检查网络", 0).show();
                }
                listener.onFailed(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (null != header) {
                    return header.getMap();
                }
                return super.getHeaders();
            }
        };
        request.setTag(tag);
        requestQueue.add(request);
    }

    public <T> void getStringBeanList(RequestQueue requestQueue,String tag,String url, final CustomParam header, final CustomParam param, final Class<T> classOfT, final BeanListener<T> listener) {
        String getUrl = url + "?" + getParamUrlString(param);

        L.e("request:" + getUrl);
        StringRequest request = new StringRequest(Method.GET, getUrl, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray data = new JSONObject(response).getJSONArray(KEY_DATA_ARRAY);
                    L.e("response:" + response);
                    listener.onSuccess(jsonToList(data.toString(), classOfT));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
//                    Toast.makeText(context, "请求失败，请检查网络", 0).show();
                }
                listener.onFailed(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (null != header) {
                    return header.getMap();
                }
                return super.getHeaders();
            }
        };
        request.setTag(tag);
        requestQueue.add(request);
    }


    /**
     * 请求并返回指定bean列表
     *
     * @param url
     * @param params
     * @param classOfT 返回bean类型
     * @param listener
     */
    public <T> void postStringBeanList(RequestQueue requestQueue,String tag,String url, final CustomParam params, final Class<T> classOfT, final BeanListener<T> listener) {
        StringRequest request = new StringRequest(Method.POST, url, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("onResponse===>", response);
                try {
                    JSONArray data = new JSONObject(response).getJSONArray(KEY_DATA_ARRAY);
                    listener.onSuccess(jsonToList(data.toString(), classOfT));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
//                    Toast.makeText(context, "请检查网络", 0).show();
                }
                listener.onFailed(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (null != params) {
                    return params.getMap();
                }
                return super.getParams();
            }
        };
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void postJson(RequestQueue requestQueue,String tag,String url, final CustomParam param, final SimpleListener listener) {
        JSONObject jsonRequest = new JSONObject(param.getMap());
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jsonRequest, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                listener.onSuccess(response.toString());
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
//                    Toast.makeText(context, "请求失败，请检查网络", 0).show();
                }
                listener.onFailed(error.getMessage());
            }
        });
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void getJson(RequestQueue requestQueue,String tag,String url, final CustomParam param, final SimpleListener listener) {
        JSONObject jsonRequest = new JSONObject(param.getMap());
        L.e("get json:" + jsonRequest.toString());

        JsonObjectRequest request = new JsonObjectRequest(Method.GET, url, jsonRequest, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                listener.onSuccess(response.toString());
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError || error instanceof TimeoutError) {
//                    Toast.makeText(context, "请求失败，请检查网络", 0).show();
                }
                listener.onFailed(error.getMessage());
            }
        });
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 自定义请求回调(原始返回)
     *
     * @author Administrator
     */
    public static abstract class SimpleListener {

        public abstract void onSuccess(String response);

        public abstract void onFailed(String errorMsg);
    }

    /**
     * 自定义请求回调(bean返回)
     *
     * @param <T>
     * @author Administrator
     */
    public static abstract class BeanListener<T> {

        public abstract void onSuccess(List<T> response);

        public abstract void onFailed(String errorMsg);
    }

    /**
     * 自定义请求参数类
     */
    public static class CustomParam {
        private Map<String, String> paramsMap;

        public CustomParam() {
            paramsMap = new HashMap<String, String>();
        }

        /**
         * @param keyAndValues "key1","value1","key2","value2"...
         */
//        public CustomParam(String... keyAndValues) {
//            paramsMap = new HashMap<String, String>();
//            fillKeyAndValue(paramsMap, keyAndValues);
//        }
        public CustomParam put(String key, String value) {
            paramsMap.put(key, value);
            return this;
        }

        public String get(String key) {
            return this.paramsMap.get(key);
        }

        public Map<String, String> getMap() {
            return this.paramsMap;
        }

        /**
         * 填充参数map
         *
         * @param map
         * @param keyAndValues
         */
        private void fillKeyAndValue(Map<String, String> map, String[] keyAndValues) {
            int size = keyAndValues.length;
            int lastVauleIndex = -1;

            if (0 == size) {
                return;
            }

            for (int i = 0; i < size - 1; i += 2) {
                String key = keyAndValues[i];
                String value = keyAndValues[i + 1];
                map.put(key, value);
                lastVauleIndex = i + 1;
            }
            // 奇数，默认未添加value为空
            if (size - 1 == lastVauleIndex) {
                map.put(keyAndValues[lastVauleIndex], DEFAULT_EMPTY_VALUE);
            }
        }


    }

    /**
     * 拼装Get参数url
     *
     * @param param
     * @return
     */
    private String getParamUrlString(CustomParam param) {
        StringBuilder result = new StringBuilder();

        Iterator<String> iterator = param.getMap().keySet().iterator();

        boolean isFirstKey = true;
        while (iterator.hasNext()) {
            if (!isFirstKey) {
                result.append("&");
            } else {
                isFirstKey = false;
            }

            String key = iterator.next();
            String value = param.get(key);
            result.append(key + "=" + value);
        }
        return result.toString();
    }


    /**
     * json、beanList转换
     *
     * @param jsonArr
     * @param classOfT
     * @return
     */
    public static <T> ArrayList<T> jsonToList(String jsonArr, Class<T> classOfT) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjs = new Gson().fromJson(jsonArr, type);
            ArrayList<T> listOfT = new ArrayList<T>();
            for (JsonObject jsonObj : jsonObjs) {
                listOfT.add(new Gson().fromJson(jsonObj, classOfT));
            }
            return listOfT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
