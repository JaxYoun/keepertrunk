package com.troy.keeper.fileupload.component.impl;

import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.fileupload.component.IFileUpload;
import com.troy.keeper.fileupload.config.UploadPropertiesBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * 支持断点续传的FTP实用类
 * 
 * @author BenZhou
 * @version 0.1 实现基本断点上传下载
 * @version 0.2 实现上传下载进度汇报
 * @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持
 */
public class FtpFileUpload implements IFileUpload {
	private static final Log log = LogFactory.getLog(FtpFileUpload.class);
	private static String ENCODING = "UTF-8";
    private FTPClient ftpClient;
	private UploadPropertiesBean configBean;
	public enum UploadStatus {
		Create_Directory_Fail, // 远程服务器相应目录创建失败
		Create_Directory_Success, // 远程服务器闯将目录成功
		Upload_New_File_Success, // 上传新文件成功
		Upload_New_File_Failed, // 上传新文件失败
		File_Exits, // 文件已经存在
		Remote_Bigger_Local, // 远程文件大于本地文件
		Upload_From_Break_Success, // 断点续传成功
		Upload_From_Break_Failed, // 断点续传失败
		Delete_Remote_Faild; // 删除远程文件失败
	}

	public enum DownloadStatus {
		Remote_File_Noexist, // 远程文件不存在
		Local_Bigger_Remote, // 本地文件大于远程文件
		Download_From_Break_Success, // 断点下载文件成功
		Download_From_Break_Failed, // 断点下载文件失败
		Download_New_Success, // 全新下载文件成功
		Download_New_Failed; // 全新下载文件失败
	}

	public FtpFileUpload( UploadPropertiesBean configBean) throws KeeperException {
        this.configBean = configBean;
		 ftpClient = new FTPClient();
		 try {
			 ftpClient.connect(configBean.getIp(), configBean.getPort());
			 ftpClient.setControlEncoding(ENCODING);
			 if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				 ftpClient.login(configBean.getUserName(), configBean.getPassword());
			 }
		 }catch (Exception e){
		 	 throw  new KeeperException(e);
		 }

	}
	/**
	 * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
	 * 
	 * @param remote
	 *            远程文件路径
	 * @return 上传的状态
	 * @throws IOException
	 */
	public DownloadStatus download(String remote, OutputStream out) throws KeeperException {

		try {

			remote = configBean.getPath() + remote;
			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			ftpClient.getReplyCode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			DownloadStatus result;

			// 检查远程文件是否存在
			FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes(ENCODING), "iso-8859-1"));
			if (files.length != 1) {
				log.info("远程文件不存在");
				return DownloadStatus.Remote_File_Noexist;
			}
			long lRemoteSize = files[0].getSize();
			InputStream in = ftpClient
					.retrieveFileStream(new String(remote.getBytes(ENCODING), "iso-8859-1"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize / 100;
			// 20130711 王波修改 当文件小于100字节时 step = 0会报错
			if (lRemoteSize < 100L) {
				step = 1L;
			}
			long process = 0;
			long localSize = 0L;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0)
						log.info("下载进度：" + process);
					// TODO 更新文件下载进度,值存放在process变量中
				}
			}
			in.close();
			out.close();
			boolean upNewStatus = ftpClient.completePendingCommand();
			if (upNewStatus) {
				result = DownloadStatus.Download_New_Success;
			} else {
				result = DownloadStatus.Download_New_Failed;
			}
			return result;
		} catch (Exception e) {
			throw new KeeperException(e);
		}
	}

	public void downLoadFilePart(String remote, OutputStream out, long start, long end) throws KeeperException {
		this.downloadPart(remote, out, start, end);
	}

	/**
	 * 下载文件部分数据 add by liux at 20130529
	 * 
	 * @param remote
	 * @param out
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public DownloadStatus downloadPart(String remote, OutputStream out, long start, long end) throws KeeperException {

		try {
			remote =configBean.getPath() + remote;
			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			ftpClient.getReplyCode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			DownloadStatus result;

			// 检查远程文件是否存在
			FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes(ENCODING), "iso-8859-1"));
			if (files.length != 1) {
				log.info("远程文件不存在");
				return DownloadStatus.Remote_File_Noexist;
			}
			long lRemoteSize = end - start;
			// 首先调到开始位置
			ftpClient.rest(String.valueOf(start));
			InputStream in = ftpClient
					.retrieveFileStream(new String(remote.getBytes(ENCODING), "iso-8859-1"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize / 100;
			// 20130711 王波修改 当文件小于100字节时 step = 0会报错
			if (lRemoteSize < 100L) {
				step = 1L;
			}
			long process = 0;
			long localSize = 0L;
			int c;
			// 首先调到开始位置
			// in.skip(start);
			while ((c = in.read(bytes)) != -1 && localSize < lRemoteSize) {
				// mod by liux at 20130619 只下载指定长度的文件，所以这里要判断一下
				if (localSize + c <= lRemoteSize) {
					out.write(bytes, 0, c);
					localSize += c;
				} else {
					out.write(bytes, 0, Long.valueOf((lRemoteSize - localSize)).intValue());
					localSize = lRemoteSize;
				}
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0)
						log.info("下载进度：" + process);
					// TODO 更新文件下载进度,值存放在process变量中
				}
			}
			in.close();
			out.close();
			boolean upNewStatus = ftpClient.completePendingCommand();
			if (upNewStatus) {
				result = DownloadStatus.Download_New_Success;
			} else {
				result = DownloadStatus.Download_New_Failed;
			}
			// 关闭连接

			return result;
		} catch (Exception e) {
			throw new KeeperException(e);
		}

	}

	/**
	 * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
	 * 
	 * @param remote
	 *            远程文件路径
	 * @param local
	 *            本地文件路径
	 * @return 上传的状态
	 * @throws IOException
	 */
	public DownloadStatus download(String remote, String local) throws KeeperException {

		try {
			remote = configBean.getPath() + remote;
			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			DownloadStatus result;

			// 检查远程文件是否存在
			FTPFile[] files =ftpClient.listFiles(new String(remote.getBytes(ENCODING), "iso-8859-1"));
			if (files.length != 1) {
				log.info("远程文件不存在");
				return DownloadStatus.Remote_File_Noexist;
			}

			long lRemoteSize = files[0].getSize();
			File f = new File(local);
			// 本地存在文件，进行断点下载
			if (f.exists()) {
				long localSize = f.length();
				// 判断本地文件大小是否大于远程文件大小
				if (localSize >= lRemoteSize) {
					log.info("本地文件大于远程文件，下载中止");
					return DownloadStatus.Local_Bigger_Remote;
				}

				// 进行断点续传，并记录状态
				FileOutputStream out = new FileOutputStream(f, true);
				ftpClient.setRestartOffset(localSize);
				InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes(ENCODING), "iso-8859-1"));
				byte[] bytes = new byte[1024];
				long step = lRemoteSize / 100;
				long process = localSize / step;
				int c;
				while ((c = in.read(bytes)) != -1) {
					out.write(bytes, 0, c);
					localSize += c;
					long nowProcess = localSize / step;
					if (nowProcess > process) {
						process = nowProcess;
						if (process % 10 == 0)
							log.info("下载进度：" + process);
						// TODO 更新文件下载进度,值存放在process变量中
					}
				}
				in.close();
				out.close();
				boolean isDo = ftpClient.completePendingCommand();
				if (isDo) {
					result = DownloadStatus.Download_From_Break_Success;
				} else {
					result = DownloadStatus.Download_From_Break_Failed;
				}
			} else {
				if(f == null)
					return null;
				OutputStream out = new FileOutputStream(f);
				InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes(ENCODING), "iso-8859-1"));
				byte[] bytes = new byte[1024];
				long step = lRemoteSize / 100;
				long process = 0;
				long localSize = 0L;
				int c;
				while ((c = in.read(bytes)) != -1) {
					out.write(bytes, 0, c);
					localSize += c;
					long nowProcess = localSize / step;
					if (nowProcess > process) {
						process = nowProcess;
						if (process % 10 == 0)
							log.info("下载进度：" + process);
						// TODO 更新文件下载进度,值存放在process变量中
					}
				}
				in.close();
				out.close();
				boolean upNewStatus = ftpClient.completePendingCommand();
				if (upNewStatus) {
					result = DownloadStatus.Download_New_Success;
				} else {
					result = DownloadStatus.Download_New_Failed;
				}
			}
			return result;
		} catch (Exception e) {
			log.error("文件上传失败！");
			return  null;
		}
		
	}

	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param local
	 *            本地文件名称，绝对路径
	 * @param remote
	 *            远程文件路径，使用/home/directory1/subdirectory/file.ext
	 *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadStatus upload(String local, String remote) throws KeeperException {

		try {
			// 设置PassiveMode传输
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding(ENCODING);
			UploadStatus result;
			// 对远程目录的处理
			remote = configBean.getPath() + remote;
			String remoteFileName = remote;
			if (remote.contains("/")) {
				remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
				// 创建服务器远程目录结构，创建失败直接返回
				if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
					return UploadStatus.Create_Directory_Fail;
				}
			}

			// 检查远程是否存在文件
			FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes(ENCODING), "iso-8859-1"));
			if (files.length == 1) {
				long remoteSize = files[0].getSize();
				File f = new File(local);
				long localSize = f.length();
				if (remoteSize == localSize) {
					return UploadStatus.File_Exits;
				} else if (remoteSize > localSize) {
					return UploadStatus.Remote_Bigger_Local;
				}

				// 尝试移动文件内读取指针,实现断点续传
				result = uploadFile(remoteFileName, f, ftpClient, remoteSize);

				// 如果断点续传没有成功，则删除服务器上文件，重新上传
				if (result == UploadStatus.Upload_From_Break_Failed) {
					if (!ftpClient.deleteFile(remoteFileName)) {
						return UploadStatus.Delete_Remote_Faild;
					}
					result = uploadFile(remoteFileName, f, ftpClient, 0);
				}
			} else {
				result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
			}
			return result;
		} catch (Exception e) {
			throw new KeeperException(e);
		}
		
	}

	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param file
	 *            本地文件名称，绝对路径
	 * @param remote
	 *            远程文件路径，使用/home/directory1/subdirectory/file.ext
	 *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadStatus upload(File file, String remote) throws KeeperException {

		try {
			// 设置PassiveMode传输
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding(ENCODING);
			UploadStatus result;
			// 对远程目录的处理
			remote = configBean.getPath() + remote;
			String remoteFileName = remote;
			if (remote.contains("/")) {
				remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
				// 创建服务器远程目录结构，创建失败直接返回
				if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
					return UploadStatus.Create_Directory_Fail;
				}
			}

			// 检查远程是否存在文件
			FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes(ENCODING), "iso-8859-1"));
			if (files.length == 1) {
				long remoteSize = files[0].getSize();
				File f = file;
				long localSize = f.length();
				if (remoteSize == localSize) {
					return UploadStatus.File_Exits;
				} else if (remoteSize > localSize) {
					return UploadStatus.Remote_Bigger_Local;
				}

				// 尝试移动文件内读取指针,实现断点续传
				result = uploadFile(remoteFileName, f, ftpClient, remoteSize);

				// 如果断点续传没有成功，则删除服务器上文件，重新上传
				if (result == UploadStatus.Upload_From_Break_Failed) {
					if (!ftpClient.deleteFile(remoteFileName)) {
						return UploadStatus.Delete_Remote_Faild;
					}
					result = uploadFile(remoteFileName, f, ftpClient, 0);
				}
			} else {
				result = uploadFile(remoteFileName, file, ftpClient, 0);
			}
			return result;
		} catch (Exception e) {
			throw new KeeperException(e);
		}
		
	}

	/**
	 * 删除文件
	 * 
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	public boolean del(String remote) throws KeeperException {

		try {
			remote = configBean.getPath() + remote;
			return ftpClient.deleteFile(remote);
		} catch (Exception e) {
			throw new KeeperException(e);
		}
		
	}

	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remote
	 *            远程服务器文件绝对路径
	 * @param ftpClient
	 *            FTPClient对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public UploadStatus CreateDirecroty(String remote, FTPClient ftpClient) throws KeeperException {
		String directory = null;
		if(remote!= null)
		  directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		try{
			if (!directory.equalsIgnoreCase("/")
					&& !ftpClient.changeWorkingDirectory(new String(directory.getBytes(ENCODING), "iso-8859-1"))) {
				// 如果远程目录不存在，则递归创建远程服务器目录
				int start = 0;
				int end = 0;
				if (directory.startsWith("/")) {
					start = 1;
				} else {
					start = 0;
				}
				end = directory.indexOf("/", start);
				while (true) {
					String subDirectory = new String(remote.substring(start, end).getBytes(ENCODING), "iso-8859-1");
					if (subDirectory.trim().length() == 0)
						break;
					if (!ftpClient.changeWorkingDirectory(subDirectory)) {
						if (ftpClient.makeDirectory(subDirectory)) {
							ftpClient.changeWorkingDirectory(subDirectory);
						} else {
							log.info("创建目录失败");
							return UploadStatus.Create_Directory_Fail;
						}
					}

					start = end + 1;
					end = directory.indexOf("/", start);

					// 检查所有目录是否创建完毕
					if (end <= start) {
						break;
					}
				}
			}
			return UploadStatus.Create_Directory_Success;
		}catch (Exception e){
			throw new KeeperException(e);
		}

	}

	/**
	 * 上传文件到服务器,新上传和断点续传
	 * 
	 * @param remoteFile
	 *            远程文件名，在上传之前已经将服务器工作目录做了改变
	 * @param localFile
	 *            本地文件File句柄，绝对路径

	 * @param ftpClient
	 *            FTPClient引用
	 * @return
	 * @throws IOException
	 */
	public UploadStatus uploadFile(String remoteFile, File localFile, FTPClient ftpClient, long remoteSize)
			throws KeeperException {
		try {
			UploadStatus status;
			// 显示进度的上传
			long step = localFile.length() / 100;
			// mod by liux at 20140428 修复文件小于100字节时的除0错误
			if (localFile.length() < 100L) {
				step = 1L;
			}
			long process = 0;
			long localreadbytes = 0L;
			RandomAccessFile raf = new RandomAccessFile(localFile, "r");
			OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes(ENCODING), "iso-8859-1"));
			// 断点续传
			if (remoteSize > 0) {
				ftpClient.setRestartOffset(remoteSize);
				process = remoteSize / step;
				raf.seek(remoteSize);
				localreadbytes = remoteSize;
			}
			byte[] bytes = new byte[1024];
			int c;
			while ((c = raf.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localreadbytes += c;
				if (localreadbytes / step != process) {
					process = localreadbytes / step;
					log.info("上传进度:" + process);
					// TODO 汇报上传状态
				}
			}
			out.flush();
			raf.close();
			out.close();
			boolean result = ftpClient.completePendingCommand();
			if (remoteSize > 0) {
				status = result ? UploadStatus.Upload_From_Break_Success : UploadStatus.Upload_From_Break_Failed;
			} else {
				status = result ? UploadStatus.Upload_New_File_Success : UploadStatus.Upload_New_File_Failed;
			}
			return status;
		}catch (Exception e){
			throw new KeeperException(e);
		}

	}

	public void delFile(String fileName) throws KeeperException {
		del(fileName);
	}

	public void downLoadFile(String fileName, String newName) throws KeeperException {
		this.download(fileName, newName);

	}

	public void downLoadFile(String fileName, OutputStream out) throws KeeperException {
		this.download(fileName, out);

	}


	public void uploadFile(File file, String newName) throws KeeperException {
		this.upload(file, newName);

	}

	

	/**
	 * 通过文件名 获取文件流
	 * 
	 * @param remote
	 *            远程文件路径

	 *            本地文件路径
	 * @return 上传的状态
	 * @throws IOException
	 */
	public InputStream getInputStream(String remote) throws KeeperException {

		try {
			remote = configBean.getPath() + remote;
			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// 检查远程文件是否存在
			FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes(ENCODING), "iso-8859-1"));
			if (files.length != 1) {
				log.info("远程文件不存在");
				return null;
			}
			return ftpClient.retrieveFileStream(new String(remote.getBytes(ENCODING), "iso-8859-1"));
		} catch (Exception e) {
			throw new KeeperException(e);
		}
		
	}

	@Override
	public void cdRoot() {
//	/	ftpClient.
	}

	@Override
	public void copyFile(String fileName, String newName) {

	}

	@Override
	public void uploadFile(InputStream in, String newName) throws KeeperException {
		// TODO Auto-generated method stub

	}

}