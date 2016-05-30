package com.zrodo.fsclz.service;
/**
 * Created by grandry.xu on 15-12-29.
 */
public class ZrodoProviderImp implements Provider {

	private static Provider mProvider;
	
	public static Provider getmProvider() {
        if(mProvider==null){
            mProvider=new ZrodoProviderImp();
        }
        return mProvider;
    }
	
    @Override
    public StringBuilder login(String username, String passwd)throws Exception{
        StringBuilder loginUrl = new StringBuilder(getRequestUrl(ZRDApplication.getInstance().getRequestUrl(), "doLogin.do"));
        loginUrl.append("?").append("username=").append(username).append("&password=").append(passwd);
        return loginUrl;
    }
    
    @Override
    public StringBuilder postSamplePic() throws Exception {
    	StringBuilder postUrl=new StringBuilder(getRequestUrl(ZRDApplication.getInstance().getRequestUrl(),"addSamplePicAndVideo.do"));  
        return postUrl;
    }
    
    @Override
    public StringBuilder changePWD() throws Exception {
    	StringBuilder postUrl=new StringBuilder(getRequestUrl(ZRDApplication.getInstance().getRequestUrl(),"modifypassword.do"));  
        return postUrl;
    }
    
    @Override
    public StringBuilder getAllBasedata() throws Exception {
    	StringBuilder getUrl=new StringBuilder(getRequestUrl(ZRDApplication.getInstance().getRequestUrl(),"getAllBasedata.do"));  
    	return getUrl;
    }
    
    @Override
	public StringBuilder postSignIn() throws Exception {
    	StringBuilder postUrl=new StringBuilder(getRequestUrl(ZRDApplication.getInstance().getRequestUrl(),"signIn.do"));  
        return postUrl;
	}
    
    @Override
	public StringBuilder getInfoByCardid(String cardId) throws Exception {
    	StringBuilder postUrl=new StringBuilder(getRequestUrl(ZRDApplication.getInstance().getRequestUrl(),"getInfoByCardid.do"));
    	postUrl.append("?").append("cardid=").append(cardId);
        return postUrl;
	}

    @Override
	public StringBuilder getVersionInfo(String syscode) throws Exception {
    	  StringBuilder versionUrl = new StringBuilder(getRequestUrl(ZRDApplication.getInstance().getRequestUrl(), "getVersion.do"));
    	  versionUrl.append("?").append("syscode=").append(syscode);
		return versionUrl;
	}
    
    private String getRequestUrl(String serverUrl, String... appendUrls) {
        StringBuilder sb = new StringBuilder(serverUrl.trim());
        if (appendUrls.length <= 0) {
            return sb.toString();
        }
        for (String appendUrl : appendUrls) {
            sb.append(appendUrl);
        }
        return sb.toString();
    }

	
    
}
