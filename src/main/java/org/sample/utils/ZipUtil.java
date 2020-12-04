package org.sample.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具类
 */
public class ZipUtil {
	private static final int BUFFER_SIZE = 2 * 1024;
	/**
	 * 是否保留原来的目录结构 true: 保留目录结构; false: 所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 */
	private static final boolean KeepDirStructure = true;
	private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);

	public static void main(String[] args) {
		try {
//			toZip("E:/app1", "E:/app.zip",true);
			unZipFiles("E:\\suning\\1\\phoebus-web-20200217115525.war", "E:\\suning\\1\\old");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩成ZIP
	 * 
	 * @param srcDir       压缩 文件/文件夹 路径
	 * @param outPathFile  压缩 文件/文件夹 输出路径+文件名 D:/xx.zip
	 * @param isDelSrcFile 是否删除原文件: 压缩前文件
	 */
	public static void toZip(String srcDir, String outPathFile, boolean isDelSrcFile) throws Exception {
		long start = System.currentTimeMillis();
		FileOutputStream out = null;
		ZipOutputStream zos = null;
		try {
			out = new FileOutputStream(new File(outPathFile));
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			if (!sourceFile.exists()) {
				throw new Exception("需压缩文件或者文件夹不存在");
			}
			compress(sourceFile, zos, sourceFile.getName());
			if (isDelSrcFile) {
				delDir(srcDir);
			}
			log.info("原文件:{}. 压缩到:{}完成. 是否删除原文件:{}. 耗时:{}ms. ", srcDir, outPathFile, isDelSrcFile,
					System.currentTimeMillis() - start);
		} catch (Exception e) {
			log.error("zip error from ZipUtils: {}. ", e.getMessage());
			throw new Exception("zip error from ZipUtils");
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 递归压缩方法
	 * 
	 * @param sourceFile 源文件
	 * @param zos        zip输出流
	 * @param name       压缩后的名称
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			zos.putNextEntry(new ZipEntry(name));
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				if (KeepDirStructure) {
					zos.putNextEntry(new ZipEntry(name + "/"));
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					if (KeepDirStructure) {
						compress(file, zos, name + "/" + file.getName());
					} else {
						compress(file, zos, file.getName());
					}
				}
			}
		}
	}

	/**
	 * 解压文件到指定目录
	 * @param zipPath
	 * @param descDir
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	public static void unZipFiles(String zipPath, String descDir) throws IOException {
		log.info("文件:{}. 解压路径:{}. 解压开始.", zipPath, descDir);
		long start = System.currentTimeMillis();
		try {
			File zipFile = new File(zipPath);
			System.err.println(zipFile.getName());
			if (!zipFile.exists()) {
				throw new IOException("需解压文件不存在.");
			}
			File pathFile = new File(descDir);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			ZipFile zip = new ZipFile(zipFile, Charset.forName("UTF-8"));
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
//				System.err.println(zipEntryName);
				InputStream in = zip.getInputStream(entry);
				String outPath = (descDir + File.separator + zipEntryName).replaceAll("\\\\", "/");
//				System.err.println(outPath);
				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}
				// 输出文件路径信息
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			log.info("文件:{}. 解压路径:{}. 解压完成. 耗时:{}ms. ", zipPath, descDir, System.currentTimeMillis() - start);
		} catch (Exception e) {
			log.info("文件:{}. 解压路径:{}. 解压异常:{}. 耗时:{}ms. ", zipPath, descDir, e, System.currentTimeMillis() - start);
			throw new IOException(e);
		}
	}

	// 删除文件或文件夹以及文件夹下所有文件
	public static void delDir(String dirPath) throws IOException {
		log.info("删除文件开始:{}.", dirPath);
		long start = System.currentTimeMillis();
		try {
			File dirFile = new File(dirPath);
			if (!dirFile.exists()) {
				return;
			}
			if (dirFile.isFile()) {
				dirFile.delete();
				return;
			}
			File[] files = dirFile.listFiles();
			if (files == null) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				delDir(files[i].toString());
			}
			dirFile.delete();
			log.info("删除文件:{}. 耗时:{}ms. ", dirPath, System.currentTimeMillis() - start);
		} catch (Exception e) {
			log.info("删除文件:{}. 异常:{}. 耗时:{}ms. ", dirPath, e, System.currentTimeMillis() - start);
			throw new IOException("删除文件异常.");
		}
	}

	
	/**
	 * .gz压缩文件解压
	 * @param sourcedir
	 */
	public static void unGzipFile(String sourcedir) {
		String ouputfile = "";
		try {
			// 建立gzip压缩文件输入流
			FileInputStream fin = new FileInputStream(sourcedir);
			// 建立gzip解压工作流
			GZIPInputStream gzin = new GZIPInputStream(fin);
			// 建立解压文件输出流
			ouputfile = sourcedir.substring(0, sourcedir.lastIndexOf('.'));
			ouputfile = ouputfile.substring(0, ouputfile.lastIndexOf('.'));
			FileOutputStream fout = new FileOutputStream(ouputfile);

			int num;
			byte[] buf = new byte[1024];

			while ((num = gzin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, num);
			}

			gzin.close();
			fout.close();
			fin.close();
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return;
	}

	/**
	 * 解压tar.gz 文件
	 * 
	 * @param file      要解压的tar.gz文件对象
	 * @param outputDir 要解压到某个指定的目录下
	 * @throws IOException
	 */
	public static void unTarGz(File file, String outputDir) throws IOException {
		TarInputStream tarIn = null;
		try {
			tarIn = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file))),
					1024 * 2);

			createDirectory(outputDir, null);// 创建输出目录

			TarEntry entry = null;
			while ((entry = tarIn.getNextEntry()) != null) {

				if (entry.isDirectory()) {// 是目录
					entry.getName();
					createDirectory(outputDir, entry.getName());// 创建空目录
				} else {// 是文件
					File tmpFile = new File(outputDir + "/" + entry.getName());
					createDirectory(tmpFile.getParent() + "/", null);// 创建输出目录
					OutputStream out = null;
					try {
						out = new FileOutputStream(tmpFile);
						int length = 0;

						byte[] b = new byte[2048];

						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}

					} catch (IOException ex) {
						throw ex;
					} finally {

						if (out != null)
							out.close();
					}
				}
			}
		} catch (IOException ex) {
			throw new IOException("解压归档文件出现异常", ex);
		} finally {
			try {
				if (tarIn != null) {
					tarIn.close();
				}
			} catch (IOException ex) {
				throw new IOException("关闭tarFile出现异常", ex);
			}
		}
	}

	/**
	 * 构建目录
	 * 
	 * @param outputDir
	 * @param subDir
	 */
	public static void createDirectory(String outputDir, String subDir) {
		File file = new File(outputDir);
		if (!(subDir == null || subDir.trim().equals(""))) {// 子目录不为空
			file = new File(outputDir + "/" + subDir);
		}
		if (!file.exists()) {
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			file.mkdirs();
		}
	}
	
	/**
	 * 压缩tar文件
	 * @param file
	 * @param targetPath
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void tarFile(File file, String targetPath) throws Exception {
		
		TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(new File(targetPath)));
		
        TarArchiveEntry tae = new TarArchiveEntry(file);
        tae.setSize(file.length());//大小
        tae.setName(new String(file.getName().getBytes("gbk"), "ISO-8859-1"));//不设置会默认全路径
        taos.putArchiveEntry(tae);
 
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int count;
        byte data[] = new byte[1024];
        while ((count = bis.read(data, 0, 1024)) != -1) {
            taos.write(data, 0, count);
        }
        bis.close();
 
        taos.closeArchiveEntry();
    }

	/**
	 * 解压tar文件
	 * @param destPath
	 * @param sourcePath
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void deTarFile(String destPath, String sourcePath) throws Exception {
		
		TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(new File(sourcePath)));
		
        TarArchiveEntry tae = null;
        while ((tae = tais.getNextTarEntry()) != null) {
 
            String dir = destPath + File.separator + tae.getName();//tar档中文件
            File dirFile = new File(dir);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dirFile));
 
            int count;
            byte data[] = new byte[1024];
            while ((count = tais.read(data, 0, 1024)) != -1) {
                bos.write(data, 0, count);
            }
 
            bos.close();
        }
    }

	
}
