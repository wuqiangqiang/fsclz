package com.zrodo.fsclz.service;

import com.zrodo.fsclz.model.LoginModel;

/**
 * 数据缓存，留待后用
 * 
 * @author Bulantee
 */
public class CacheData {

	// private static final String TAG = "CacheData";

	public static LoginModel loginInfo = null;
	//private ArrayList<HashMap<String, Object>> shopCartTempList;

	/*public void ClearCacheData() {

		if (shopCartTempList != null)
			shopCartTempList.clear();
	}

	public void shopCartClear() {
		if (shopCartTempList != null) {
			shopCartTempList.clear();
		}
	}

	public void appendShopCartList(
			ArrayList<HashMap<String, Object>> shopCartTempLists) {
		if (shopCartTempList == null || shopCartTempList.size() == 0) {
			shopCartTempList = shopCartTempLists;
		} else {
			for (HashMap<String, Object> shopCart : shopCartTempLists) {
				if (!shopCartTempList.contains(shopCart)) {
					shopCartTempList.add(shopCart);
				}
			}
		}
	}

	public ArrayList<HashMap<String, Object>> getShopCartTempList() {
		return shopCartTempList;
	}

	public void setShopCartTempList(
			ArrayList<HashMap<String, Object>> shopCartTempList) {
		this.shopCartTempList = shopCartTempList;
	}*/
}
