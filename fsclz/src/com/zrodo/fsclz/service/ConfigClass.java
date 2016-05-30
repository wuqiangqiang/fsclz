package com.zrodo.fsclz.service;


public class ConfigClass {
	
	//private static String serverUrl = "http://115.29.106.147:8080/clzDetectList/";
    //private static String serverUrl = "http://192.168.1.110:8080/sjDetectList/";
	/*向充URL*/
	private static String serverUrl = "http://192.168.1.104:8080/clzDetectList/"; 
	
	public static void init(){
		
		String _serverUrl = serverUrl;
		//String[] config = read(); //后面可采用从SD卡中读取配置的方式进行URL读取
		ZRDApplication.getInstance().setRequestUrl(_serverUrl);
	}

	//读取SD卡上的URL配置	
	/*private static String[] read() {
		String[] config = new String[4];
		
		try {

			File file = new File("/data/data/com.zrodo.fsclzUrl/files/url4.txt");
			int ch;
			StringBuffer strContent = new StringBuffer("");
			FileInputStream fin = null;
			fin = new FileInputStream(file);

			while ((ch = fin.read()) != -1)
				strContent.append((char) ch);
			fin.close();
			String text = DESEncryption.decrypturl(strContent.toString());
			String[] all = text.split("#");
			if (all[0].equals("1")) {
				urlname= all[1];
				if (!all[2].trim().equals("")) {
					config[0] = all[2];
				}
				 if (!all[3].trim().equals("")) {
					 config[3] = all[3];
					 strDebugger = all[3];
				 }
				 if(!all[4].trim().equals(""))
				 {
					 config[1] = all[4];
				 }
				 if(!all[5].trim().equals(""))
				 {
					 config[2] = all[5];
				 }				 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}*/
}
