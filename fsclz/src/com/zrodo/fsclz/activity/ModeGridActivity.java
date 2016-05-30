package com.zrodo.fsclz.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.zrodo.fsclz.model.RoleModel;
import com.zrodo.fsclz.service.Constant;
import com.zrodo.fsclz.sqlite.RoleDAOImpl;
import com.zrodo.fsclz.utils.ActivityUtil.MsgDuration;
import com.zrodo.fsclz.utils.ZRDUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016-02-26.
 * 页面九宫图布局
 */

public class ModeGridActivity extends BaseActivity implements GridView.OnItemClickListener{
    private GridView gridView;
    private List<Map<String,Object>>list;
    private RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(getString(R.string.apptitle),R.layout.mode_grid);
        back = (RelativeLayout) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        initView();
    }
    
    @Override
	protected void onResume() {
		super.onResume();
	}
    
	protected void initView() {
		RoleDAOImpl roleDaoImpl = new RoleDAOImpl(this);
		ArrayList<RoleModel> roleList = roleDaoImpl.getRoleList("0");
		ArrayList<String> roleSubIdList = new ArrayList<String>();
		for(int i =0 ;i< roleList.size() ;i++){
			String roleSubId = roleList.get(i).getSubId();
			roleSubIdList.add(roleSubId);			
		}
		
        list= Constant.handMap(roleSubIdList);
        gridView=(GridView) findViewById(R.id.gridView);
        SimpleAdapter adapter=new SimpleAdapter(this,list,R.layout.mode_grid_item,new String[]{"itemimag","itemtext"},new int[]{R.id.ItemImage,R.id.ItemText});
        gridView.setAdapter(adapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,Object> map=list.get(position);
        Object drawableId=map.get("itemimag");
        boolean role = (Boolean) map.get("role");
        if(Constant.classmap.containsKey(drawableId)){
        	if(role){
        		ZRDUtils.CommIntent(this, null, Constant.classmap.get(drawableId));
        	} else {
        		ZRDUtils.alert(this, MsgDuration.SHORT, "未获取该功能权限");
        	}
        	
        }else{
        	ZRDUtils.alert(this, MsgDuration.SHORT, R.string.FIATO);
        }
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitGameAlert();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
	    }
	}
    
    public void showExitGameAlert() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.exit_dialog);
		Button cofirm = (Button) window.findViewById(R.id.btn_cofirm);
		cofirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				shutdonw (); 
			}
		});

		Button cancel = (Button) window.findViewById(R.id.btn_cancle);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}
    
    public void shutdonw () {
        ZRDUtils.setLaunched(this, false);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
