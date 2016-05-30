package com.zrodo.fsclz.service;

import org.json.JSONObject;

public interface OnResponseListener {

	void success(JSONObject result);

    void failure(String errorMessage);
}
