package com.troy.keeper.fileupload.component.impl;

import com.jcraft.jsch.*;
import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.fileupload.component.IFileUpload;
import com.troy.keeper.fileupload.config.UploadPropertiesBean;
import com.troy.keeper.fileupload.util.SshUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Properties;


public class SftpFileUpload  implements IFileUpload {
	private static final Log log = LogFactory.getLog(SftpFileUpload.class);
    private UploadPropertiesBean configBean;
	private ChannelSftp sftp;

	public SftpFileUpload(UploadPropertiesBean configBean)  {
		this.configBean = configBean;
		try{
			JSch jsch = new JSch();

			Session sshSession =jsch.getSession(configBean.getUserName(),configBean.getIp(),configBean.getPort());
			log.debug("Session created.");
			sshSession.setPassword(configBean.getPassword());
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			log.debug("Session connected.");
			log.debug("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			sftp.cd("/");
		}catch (Exception e){
			log.error("连接初始化失败！！");
			throw new KeeperException(e);
		}

	}
	/**
	 * 文件下载
	 * 
	 * @param fileName
	 * @param out
	 */
	public void download(String fileName, OutputStream out)  {
		fileName = (configBean.getPath()+ fileName).replaceAll("^\\/", "");
		try {
			int i = fileName.lastIndexOf("/");
			// mod by liux at 20140804 ，如果以/开头，要去掉，使用相对路径进入目录
			if (fileName.startsWith("/")) {
				sftp.cd(fileName.substring(1, i));
			} else {
				sftp.cd(fileName.substring(0, i));
			}
			sftp.get(fileName.substring(i + 1), out);
		} catch (Exception e) {
			log.error("sftp文件下载失败!!");
			throw new KeeperException(e);
		}
	}

	/**
	 * 下载文件的部分数据 add by liux at 20130529
	 * 
	 * @param fileName
	 * @param out
	 * @param start
	 * @param end
	 * @throws Exception
	 */
	public void downloadPart(String fileName, OutputStream out, long start, long end)  {
         if(fileName == null )
         	return ;
		fileName = (configBean.getPath()+ fileName).replaceAll("^\\/", "");
		InputStream in = null;
		long length = end - start;
		try {
			int i = fileName.lastIndexOf("/");
			sftp.cd(fileName.substring(0, i));
			in = sftp.get(fileName.substring(i + 1), null, start);
			byte[] bytes = new byte[1024];
			int c;
			long curPosition = 0L;
			while ((c = in.read(bytes)) != -1 && curPosition < length) {
				out.write(bytes, 0, c);
				curPosition += c;
			}
			log.info("sftp文件下载完成");
		} catch (Exception e) {
			log.error("sftp文件下载失败!!");
			throw new KeeperException(e);
		} finally {
			try {
				out.close();
			} catch (IOException ioe) {
				log.error(ioe.getMessage());
			}
		}
	}

	/**
	 * 通过文件名 获取文件流
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public InputStream getInputStream(String fileName)  {
		if(fileName == null)
			return null;
		fileName = (configBean.getPath()+ fileName).replaceAll("^\\/", "");
		try {
			int i = fileName.lastIndexOf("/");
			sftp.cd(fileName.substring(0, i));
			return sftp.get(fileName.substring(i + 1));
		} catch (Exception e) {
			log.error("sftp文件读取失败!!");
			throw new KeeperException(e);
		}
	}

	@Override
	public void cdRoot() {
		try {
			sftp.cd("/");
		} catch (SftpException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void copyFile(String fileName, String newName) {
		StringBuilder sb = new StringBuilder();
		sb.append("cp ");
		sb.append(configBean.getPath());
		sb.append(fileName);
		sb.append(" ");
		sb.append(configBean.getPath());
		sb.append(newName);
		SshUtil.exec(configBean.getIp(), configBean.getUserName(), configBean.getPassword(),configBean.getPort(), sb.toString());
	}

	/**
	 * 文件上传
	 */
	public void upload(File file, String newName)  {

		try {
			if (file.isFile()) {
				newName = configBean.getPath() + newName;
				log.debug("localFile : " + file.getAbsolutePath());
				String remoteFile = newName.replaceAll("^\\/", "");
				int i = remoteFile.lastIndexOf("/");
				log.debug("remotePath:" + remoteFile);
				String rpath = remoteFile.substring(0, i);
				try {
					createDirs(rpath,sftp);
				} catch (Exception e) {
					log.error("*******create path failed" + rpath);
				}

				sftp.put(new FileInputStream(file), remoteFile.substring(i + 1));
				log.debug("=========upload down for " + file.getName());
			}

		} catch (Exception e) {
			log.error("文件上传失败!");
			throw new KeeperException(e);
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param filepath
	 * @throws SftpException 
	 */
	private void createDirs(String filepath,ChannelSftp sft) throws SftpException {
		if (null == filepath || filepath.trim().length() == 0)
			return;
		String[] paths = filepath.split("\\/");
		for (String path : paths) {
			createDir(path,sft);
		}
	}

	/**
	 * 创建目录 create Directory
	 * 
	 * @param filepath
	 * @param sftp
	 * @throws SftpException 
	 */
	private void createDir(String filepath,ChannelSftp sftp) throws SftpException {
		try {
			sftp.cd(filepath);
			log.debug("cd success :" + filepath);
			return;
		} catch (SftpException e1) {
			log.error("cd failed :" + filepath);
		}
		try {
			sftp.mkdir(filepath);
			log.debug("mkdir success :" + filepath);
			sftp.cd(filepath);
		} catch (SftpException e) {
			log.error("mkdir failed :" + filepath);
			throw e;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 */
	public void delete(String fileName)  {
        if(fileName == null)
        	return;
		try {
			fileName = (configBean.getPath()+ fileName).replaceAll("^\\/", "");
			int i = fileName.lastIndexOf("/");
			sftp.cd(fileName.substring(0, i));
			sftp.rm(fileName.substring(i + 1));
		} catch (Exception e) {
			log.error("文件删除失败!!");
		}
	}

	
	public void uploadFile(File file, String newName)  {
		this.upload(file, newName);

	}

	public void delFile(String fileName)  {
		this.delete(fileName);

	}

	public void downLoadFile(String fileName, OutputStream out)  {
		this.download(fileName, out);

	}

	public void downLoadFilePart(String fileName, OutputStream out, long start, long end)  {
		this.downloadPart(fileName, out, start, end);
	}

	@Override
	public void uploadFile(InputStream in, String newName)  {

		try {
			if(newName == null )
				return;
			newName =configBean.getPath() + newName;
			String remoteFile = newName.replaceAll("^\\/", "");
			int i = remoteFile.lastIndexOf("/");
			log.debug("remotePath:" + remoteFile);
			String rpath = remoteFile.substring(0, i);
			createDirs(rpath,sftp);
			sftp.put(in, remoteFile.substring(i + 1));

		} catch (Exception e) {
			log.error("文件上传失败!");
			throw new KeeperException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				throw new KeeperException(e);
			}
		}
	}

}
