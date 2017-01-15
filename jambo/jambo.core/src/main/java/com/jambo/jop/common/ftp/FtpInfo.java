package com.jambo.jop.common.ftp;

import com.jambo.jop.infrastructure.config.CoreConfigInfo;

public class FtpInfo {

	public String ftpServer; // ftp服务器ip

	public int ftpPort; // ftp服务器端口

	public String ftpUser; // ftp登陆用户名

	public String ftpPwd; // ftp登陆密码

	public String curSerPath; // ftp服务器当前目录

	public int timeout; // 超时时长

	private static FtpInfo info = null;

	public static FtpInfo getInstance() throws Exception {
		if (info == null) {
			info = new FtpInfo();
			info.ftpServer = CoreConfigInfo.FTP_ADDRESS;
			info.ftpPort = Integer.parseInt(CoreConfigInfo.FTP_PORT);
			info.ftpUser = CoreConfigInfo.FTP_USER;
			info.ftpPwd = CoreConfigInfo.FTP_PASSWORD;
			info.curSerPath = CoreConfigInfo.FTP_WORK_DIR;

			info.timeout = 3000;
		}

		return info;
	}

	public static String getUpload() throws Exception {
		return CoreConfigInfo.UPLOAD_LOCATION;
	}

	public static String getDownload() throws Exception {
		return CoreConfigInfo.DOWNLOAD_LOCATION;
	}
}
