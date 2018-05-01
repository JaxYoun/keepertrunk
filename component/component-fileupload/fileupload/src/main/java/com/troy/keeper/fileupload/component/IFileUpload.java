package com.troy.keeper.fileupload.component;

import com.troy.keeper.core.error.KeeperException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 文件上传
 * @author 
 *
 */
public interface IFileUpload {

	 /**
	  *  文件上传
	 * @param file
	 * @param newName
	 * @return
	 */
	void uploadFile(File file, String newName) ;

	void uploadFile(InputStream in, String newName);

	
	/**
	 * 文件下载
	 * @param fileName
	 * @param t
	 * @return
	 */
	<T extends OutputStream> void  downLoadFile(String fileName, T t);
	
	/**
	 * 文件部分数据下载
	 * @param fileName
	 * @param t
	 * @param start
	 * @param end
	 * @return
	 */
	<T extends OutputStream> void  downLoadFilePart(String fileName, T t, long start, long end);
	
	/**
	 * 删除文件
	 * @param fileName
	 * @return
	 */
	void delFile(String fileName);
	
	/**
	 * 通过文件名 获取文件流
	 * @param fileName
	 * @return
	 */
	InputStream getInputStream(String fileName);

	void cdRoot();

	/**
	 * 文件拷贝
	 * @param fileName
	 * @param newName
	 */
	void copyFile(String fileName,String newName);
}
