package com.zrodo.fsclz.service;

/**
 * Created by grandry.xu on 15-12-29.
 */
public interface Provider {
    //登录接口
    public StringBuilder login(String username, String passwd)throws Exception;
    //上传图片接口
    public StringBuilder postSamplePic()throws Exception;
    //修改密码接口
    public StringBuilder changePWD()throws Exception;
    //获取样本采集基础信息数据
    public StringBuilder getAllBasedata()throws Exception;
    //上传定位签到信息
    public StringBuilder postSignIn()throws Exception;
    //获取更新信息
    public StringBuilder getVersionInfo(String syscode)throws Exception;
    //获取溯源查询信息
    public StringBuilder getInfoByCardid(String cardId)throws Exception;
  
}
