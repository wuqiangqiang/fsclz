package com.zrodo.fsclz.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.zrodo.fsclz.pilingdatas.PilingData;
import com.zrodo.fsclz.utils.JSONUtil;
import com.zrodo.fsclz.utils.ZRDUtils;
import com.zrodo.fsclz.widget.SlideMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DataStatisticActivity extends BaseActivity implements OnClickListener {

	private SlideMenu slideMenu;
	private ListView mListView;
	private PieChart pieChart;
	private int title_head_rows;
	private String[] head_title;
	private String[] pie_name;
	private RelativeLayout relMenu_N,relMenu_S,relMenu_X,relMenu_N_Date,relMenu_S_Date,relMenu_X_Date;
	private ImageView menuArrow1,menuArrow2,menuArrow3;
	private TextView txtDayChart_N,txtMonthChart_N,txtZDYChart_N,txtDayChart_S,txtMonthChart_S,txtZDYChart_S,txtDayChart_X,txtMonthChart_X,txtZDYChart_X;
	private boolean nflag,sflag,xflag;
	public HorizontalScrollView mTouchView;
	//装入所有的HScrollView
	public List<CHScrollView> mHScrollViews =new ArrayList<CHScrollView>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setView("数据统计", R.layout.data_statistic_main);
		
		initView();
		initDatas();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		slideMenu.unlock();
		relMenu_N = (RelativeLayout) findViewById(R.id.rel_menu_N);
		relMenu_N.setOnClickListener(this);
		relMenu_S = (RelativeLayout) findViewById(R.id.rel_menu_S);
		relMenu_S.setOnClickListener(this);
		relMenu_X = (RelativeLayout) findViewById(R.id.rel_menu_X);
		relMenu_X.setOnClickListener(this);
		relMenu_N_Date = (RelativeLayout) findViewById(R.id.rel_menu_n_date);
		relMenu_N_Date.setOnClickListener(this);
		relMenu_S_Date = (RelativeLayout) findViewById(R.id.rel_menu_s_date);
		relMenu_S_Date.setOnClickListener(this);
		relMenu_X_Date = (RelativeLayout) findViewById(R.id.rel_menu_x_date);
		relMenu_X_Date.setOnClickListener(this);
		txtDayChart_N = (TextView) findViewById(R.id.txt_day_chart_by_N);
		txtDayChart_N.setOnClickListener(this);
		txtMonthChart_N = (TextView) findViewById(R.id.txt_month_chart_by_N);
		txtMonthChart_N.setOnClickListener(this);
		txtZDYChart_N = (TextView) findViewById(R.id.txt_zdyi_chart_by_N);
		txtZDYChart_N.setOnClickListener(this);
		txtDayChart_S = (TextView) findViewById(R.id.txt_day_chart_by_S);
		txtDayChart_S.setOnClickListener(this);
		txtMonthChart_S = (TextView) findViewById(R.id.txt_month_chart_by_S);
		txtMonthChart_S.setOnClickListener(this);
		txtZDYChart_S = (TextView) findViewById(R.id.txt_zdyi_chart_by_S);
		txtZDYChart_S.setOnClickListener(this);
		txtDayChart_X = (TextView) findViewById(R.id.txt_day_chart_by_X);
		txtDayChart_X.setOnClickListener(this);
		txtMonthChart_X = (TextView) findViewById(R.id.txt_month_chart_by_X);
		txtMonthChart_X.setOnClickListener(this);
		txtZDYChart_X = (TextView) findViewById(R.id.txt_zdyi_chart_by_X);
		txtZDYChart_X.setOnClickListener(this);
		menuArrow1 = (ImageView) findViewById(R.id.menu1_arrow);
		menuArrow2 = (ImageView) findViewById(R.id.menu2_arrow);
		menuArrow3 = (ImageView) findViewById(R.id.menu3_arrow);
		ImageView menuImg = (ImageView) findViewById(R.id.title_bar_menu_btn);
		menuImg.setVisibility(View.VISIBLE);
		menuImg.setOnClickListener(this);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		pieChart = (PieChart)findViewById(R.id.pie_chart);
		
		CHScrollView headerScroll = (CHScrollView) findViewById(R.id.item_scroll_title);
		//添加头滑动事件 
		mHScrollViews.add(headerScroll);
		mListView = (ListView)findViewById(R.id.scroll_list);
		
		loadDatasForAgricultureChart(PilingData.AgricultureChartDatas);
		loadDatasForAgriculturePieChart(PilingData.AgriculturePieChartDatas);
	}
	
	private void loadDatasForAgriculturePieChart(String objStr) {
		ZRDUtils.dismissProgressDialog();
		//String objStr = PilingData.AgriculturePieChartDatas;
		Map<String, String> pieDataMap = queryPieData2Array(objStr);
		initPieChart(pieDataMap);
		
	}

	private void initPieChart(Map<String, String> pieDataMap) {
		// TODO Auto-generated method stub
		//描述信息
        pieChart.setDescription("农产品-图示");
        //pieChart.setExtraOffsets(5, 10, 5, 5);//设置偏移量
            
        pieChart.setDragDecelerationFrictionCoef(0.95f);// 设置滑动减速摩擦系数

         /*设置饼图中心是否是空心的
           true 中间是空心的，环形图
           false 中间是实心的 饼图*/
        pieChart.setDrawHoleEnabled(true);
        /*  设置中间空心圆孔的颜色是否透明
        true 透明的
        false 非透明的*/
        
        pieChart.setHoleColorTransparent(true);
        // 设置环形图和中间空心圆之间的圆环的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        
        // 设置环形图和中间空心圆之间的圆环的透明度
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(54f);  //设置圆孔半径

        pieChart.setTransparentCircleRadius(58f); //设置空心圆半径
        pieChart.setDrawCenterText(true);  //设置饼状图中间可以添加文字
        pieChart.setRotationAngle(90); // 初始旋转角度

        pieChart.setRotationEnabled(true); // 可以手动旋转
        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.setCenterText("PieChart");  //饼状图中间的文字

        //设置数据
        PieData pieData = getPieData(pieDataMap);
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);  //最左边显示
        mLegend.setForm(Legend.LegendForm.CIRCLE);  //设置比例图的形状，默认是方形 SQUARE
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);  //设置动画
        pieChart.invalidate();
	}
	
	@SuppressLint("NewApi") 
	private PieData getPieData(Map<String, String> pieDataMap) {
		// TODO Auto-generated method stub
		ArrayList<String> xValues = new ArrayList<String>();
		ArrayList<Entry> yValues = new ArrayList<Entry>(); //xVals用来表示每个饼块上的内容
        for (int i = 0; i < pieDataMap.size(); i++) {      	
            xValues.add(pie_name[i]);  //饼块上显示成PieChart1, PieChart2, PieChart3, PieChart4，PieChart5，PieChart6
            yValues.add(new Entry(Integer.valueOf(pieDataMap.get(pie_name[i])), i));
        }
        
      //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "PieChart Revenue 2014");
         //pieDataSet.setSliceSpace(0f); 
        //设置个饼状图之间的距离
        pieDataSet.setValueTextSize(9);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setSelectionShift(5f);


        // 饼图颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));
        colors.add(Color.rgb(30, 20, 200));
        colors.add(Color.rgb(80, 60, 150));
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);
        //PieData pieData = new PieData(xValues);
        return pieData;
	}
	
	private List<Map<String, String>> queryListData2List(String result) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(result != null){
			JSONObject obj = JSONUtil.toJSONObject(result);
			try {
				
				JSONArray array = obj.getJSONArray("result");
				JSONArray nameArray = obj.getJSONArray("nameresult");
				for(int i=0;i<array.length();i++){
					Map<String,String> map = new HashMap<String, String>();
					JSONArray jsonArray = (JSONArray) array.get(i);
					title_head_rows = nameArray.length();
					head_title = new String[title_head_rows];	
					for(int j=0;j<title_head_rows;j++){
					       map.put(nameArray.getString(j), jsonArray.getString(j));
					       head_title[j] = nameArray.getString(j);
					}
					list.add(map);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private Map<String,String> queryPieData2Array(String pieResult) {
		Map<String,String> pieResultMap = new HashMap<String, String>();
        if (pieResult != null) {
            JSONObject obj = JSONUtil.toJSONObject(pieResult);
            try {
            	JSONArray nameArray = obj.getJSONArray("nameresult");
            	JSONArray pieChartArray = obj.getJSONArray("result");
            	pie_name = new String[nameArray.length()];
            	for(int i=0 ;i<nameArray.length(); i++){
            		pieResultMap.put(nameArray.getString(i), pieChartArray.getString(i));
            		pie_name[i] = nameArray.getString(i);
            	}
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return pieResultMap;
    }
	
	@SuppressLint("NewApi") 
	public void addHViews(final CHScrollView hScrollView) {
		if(!mHScrollViews.isEmpty()) {
			int size = mHScrollViews.size();
			CHScrollView scrollView = mHScrollViews.get(size - 1);
			final int scrollX = scrollView.getScrollX();
			//第一次满屏后，向下滑动，有一条数据在开始时未加入
			if(scrollX != 0) {
				mListView.post(new Runnable() {
					@SuppressLint("NewApi") @Override
					public void run() {
						//当listView刷新完成之后，把该条移动到最终位置
						hScrollView.scrollTo(scrollX, 0);
					}
				});
			}
		}
		
		mHScrollViews.add(hScrollView);
	}
	
	private void loadDatasForAgricultureChart(String objStr) {
		ZRDUtils.dismissProgressDialog();
		//String objStr = PilingData.AgricultureChartDatas;
		List<Map<String, String>> list = queryListData2List(objStr);
		LinearLayout ll = (LinearLayout)findViewById(R.id.title_head);
		for(int i=0 ;i< title_head_rows; i++){
			TextView tv = new TextView(this);
			tv.setWidth(70);
			tv.setText(head_title[i]);
			tv.setTextSize((float) 18.0);
			tv.setTextColor(Color.parseColor("#ffffff"));
			tv.setLines(2);
			tv.setGravity(Gravity.CENTER);
			ll.addView(tv);
		}
		createViewAndBinderValues(list);
	}

	private void createViewAndBinderValues(List<Map<String, String>> list) {
		// TODO Auto-generated method stub
		ScrollAdapter adapter = new ScrollAdapter(this, list, R.layout.item);
        mListView.setAdapter(adapter);
	}
	
	public class ScrollAdapter extends BaseAdapter {

		private List<Map<String,String>> datas;
		private int res;
		private Context context;
				
		public ScrollAdapter(Context context,List<Map<String,String>> data, int resource) {
		
			this.context = context;
			this.datas = data;
			this.res = resource;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
				
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ViewHolder holder = null;
			if(v == null) {
				synchronized (DataStatisticActivity.this) {
					v = LayoutInflater.from(context).inflate(res, null);
					holder = new ViewHolder();
					//第一次初始化的时候装进来
					addHViews((CHScrollView) v.findViewById(R.id.item_scroll));
					LinearLayout ll = (LinearLayout) v.findViewById(R.id.table_item);
					for(int i=0;i<title_head_rows;i++){
						holder.tv = new TextView(context);
						holder.tv.setWidth(70);
						holder.tv.setText(datas.get(position).get(head_title[i]));
						holder.tv.setTextColor(Color.parseColor("#000000"));
						holder.tv.setGravity(Gravity.CENTER);
	        			ll.addView(holder.tv);
	                }
				}				
			} else {
				holder = (ViewHolder) v.getTag();
			}
			//View[] holders = (View[]) v.getTag();
			return v;
		}
	
		class ViewHolder {
			TextView tv;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.title_bar_menu_btn:
				if (slideMenu.isMainScreenShowing()) {
					slideMenu.openMenu();
				} else {
					slideMenu.closeMenu();
				}
				break;
			case R.id.rel_menu_N:
				if(nflag){		
					relMenu_N_Date.setVisibility(View.VISIBLE);
					menuArrow1.setBackgroundResource(R.drawable.arrow_close_proinfo);
				}  else {
					relMenu_N_Date.setVisibility(View.GONE);
					menuArrow1.setBackgroundResource(R.drawable.arrow_open_proinfo);
				}
				nflag = !nflag;
				break;
			case R.id.rel_menu_S:
				if(sflag){	
					relMenu_S_Date.setVisibility(View.VISIBLE);
					menuArrow2.setBackgroundResource(R.drawable.arrow_close_proinfo);
				} else {
					relMenu_S_Date.setVisibility(View.GONE);
					menuArrow2.setBackgroundResource(R.drawable.arrow_open_proinfo);
				}
				sflag = !sflag;
				break;
			case R.id.rel_menu_X:
				if(xflag){
					relMenu_X_Date.setVisibility(View.VISIBLE);
					menuArrow3.setBackgroundResource(R.drawable.arrow_close_proinfo);
				} else {
					relMenu_X_Date.setVisibility(View.GONE);
					menuArrow3.setBackgroundResource(R.drawable.arrow_open_proinfo);
				}
			    xflag = !xflag;
				break;
			case R.id.txt_day_chart_by_N:
				//添加不同条件
				loadDatasForAgricultureChart(PilingData.AgricultureChartDatas);
				loadDatasForAgriculturePieChart(PilingData.AgriculturePieChartDatas);
				slideMenu.closeMenu();
				break;
			case R.id.txt_month_chart_by_N:
				slideMenu.closeMenu();
				break;
			case R.id.txt_zdyi_chart_by_N:
				slideMenu.closeMenu();
				break;
			case R.id.txt_day_chart_by_S:
				loadDatasForAgricultureChart(PilingData.AquaticChartDatas);
				loadDatasForAgriculturePieChart(PilingData.AquaticPieChartDatas);
				slideMenu.closeMenu();
				break;
			case R.id.txt_month_chart_by_S:
				slideMenu.closeMenu();
				break;
			case R.id.txt_zdyi_chart_by_S:
				slideMenu.closeMenu();
				break;
			case R.id.txt_day_chart_by_X:
				loadDatasForAgricultureChart(PilingData.AnimalChartDatas);
				loadDatasForAgriculturePieChart(PilingData.AnimalPieChartDatas);
				slideMenu.closeMenu();
				break;
			case R.id.txt_month_chart_by_X:
				slideMenu.closeMenu();
				break;
			case R.id.txt_zdyi_chart_by_X:
				slideMenu.closeMenu();
				break;
		}
	}

	@SuppressLint("NewApi") 
	public void onScrollChanged(int l, int t, int oldl, int oldt){
		for(CHScrollView scrollView : mHScrollViews) {
			//防止重复滑动
			if(mTouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
	}
}
