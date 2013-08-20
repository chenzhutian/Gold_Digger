package com.billme.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.futurePayment.http.MyHttpClient;
import com.futurePayment.model.Friend;

public class FileUtil
{
	private MyHttpClient client = null;
	private String ROOTPATH;
	private String APP_NAME = "BillMe";
	private String USER_NAME = null;

	public FileUtil(String name)
	{
		USER_NAME = name;
		ROOTPATH = Environment.getExternalStorageDirectory() + File.separator + APP_NAME + File.separator + USER_NAME + File.separator;
		client = new MyHttpClient(name);
		createSDRootDirectory();
		//����һϵ���ļ���
		createSDDirectory("Friend");
	}

	/**
	 * ����sd���Ƿ����
	 * 
	 * @return ���
	 */
	public boolean isSDCardAvailable()
	{
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * �õ��ļ���С
	 * @param path �ļ�����·��
	 * @return �ļ���С
	 */
	public int getFileSize(String path)
	{
		FileInputStream fis = null;
		File file = new File(ROOTPATH + path);
		try
		{
			if (file.exists())
			{
				fis = new FileInputStream(file);
				return fis.available();
			}
			else
				return 0;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	/**
	 * �õ��ļ���С
	 * 
	 * @param dir �ļ�Ŀ¼·��
	 * @param name �ļ���
	 * @return �ļ���С
	 */
	public int getFileSize(String dir, String name)
	{
		FileInputStream fis = null;
		File file = new File(ROOTPATH + dir, name);
		try
		{
			if (file.exists())
			{
				fis = new FileInputStream(file);
				Log.i("error", fis.available() + "");
				return fis.available();
			}
			else
				return 0;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * �����ļ�
	 * 
	 * @param path �ļ�����·��
	 * @return �������ļ�������������null
	 * @throws IOException
	 */
	public File createSDFile(String path)
	{
		File file = null;
		try
		{
			file = new File(path);
			file.createNewFile();
			if (!file.isFile())
			{
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return file;
	}
	/**
	 * �����ļ�
	 * 
	 * @param dir
	 *            Ŀ¼·��
	 * @param name
	 *            �ļ���
	 * @return �������ļ�������������null
	 * @throws IOException
	 */
	public File createSDFile(String dir, String name)
	{
		File file = null;
		try
		{
			file = new File(ROOTPATH + dir, name);
			file.createNewFile();
			if (!file.isFile())
			{
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return file;
	}
	/**
	 * ������Ŀ¼
	 * 
	 * @param path
	 *            Ŀ¼·��
	 * @return ������Ŀ¼,����������null
	 */
	public File createSDRootDirectory()
	{
		File dir = new File(ROOTPATH);
		dir.mkdirs();
		if (!dir.isDirectory())
		{
			return null;
		}
		return dir;
	}
	/**
	 * ����Ŀ¼
	 * 
	 * @param path
	 *            Ŀ¼·��
	 * @return ������Ŀ¼,����������null
	 */
	public File createSDDirectory(String path)
	{
		File dir = new File(ROOTPATH + path);
		dir.mkdirs();
		if (!dir.isDirectory())
		{
			return null;
		}
		return dir;
	}
	/**
	 * ����ļ��Ƿ����
	 * 
	 * @param path �ļ�����·��
	 * @return �Ƿ�����ļ�
	 */
	public boolean isFileExists(String path)
	{
		File file = new File(path);
		return file.exists();
	}
	/**
	 * ����ļ��Ƿ����
	 * 
	 * @param dir
	 *            Ŀ¼·��
	 * @param name
	 *            �ļ���
	 * @return �Ƿ�����ļ�
	 */
	public boolean isFileExists(String dir, String name)
	{
		File file = new File(ROOTPATH + dir, name);
		return file.exists();
	}

	/**
	 * ����ļ��������ļ��Ƿ�Ϊͬһ�ļ���ͨ���Ƚϴ�Сȷ��
	 * 
	 * @param path �ļ�����·��
	 * @param size
	 *            �����ļ���С
	 * @return �Ƿ�Ϊͬһ�ļ�
	 */
	public boolean isSameFile(String path, int size)
	{
		if(getFileSize(path) == size)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * ����ļ��������ļ��Ƿ�Ϊͬһ�ļ���ͨ���Ƚϴ�Сȷ��
	 * 
	 * @param dir
	 *            Ŀ¼·��
	 * @param name
	 *            �ļ���
	 * @param size
	 *            �����ļ���С
	 * @return �Ƿ�Ϊͬһ�ļ�
	 */
	public boolean isSameFile(String dir, String name, int size)
	{
		if(getFileSize(dir, name) == size)
		{
			return true;
		}
		return false;
	}

	/**
	 * ͨ��inputStreamд�����ݵ�sd��
	 * 
	 * @param dir
	 *            �ļ�Ŀ¼·��
	 * @param fileName
	 *            �ļ���
	 * @param input
	 *            ������
	 * @param cover
	 *            ���ļ����ڣ��Ƿ񸲸�
	 * @return ���ɵ��ļ�
	 */
	public File writeToSDFromInputStream(String dir, String fileName,
			InputStream input, boolean cover)
	{
		File file = null;
		FileOutputStream output = null;
		try
		{
			createSDDirectory(dir);
			file = createSDFile(dir, fileName);
			if (isFileExists(dir, fileName) && cover == false)
			{
			}
			else
			{
				output = new FileOutputStream(file);
				byte buffer[] = new byte[1024];
				while (input.read(buffer) != -1)
				{
					output.write(buffer);
				}
				output.flush();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				output.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * ��ȡsd�����ͼƬ
	 * @param path �ļ�����·��
	 * @return ͼƬdrawable
	 */
	public Drawable readImageFromSD(String path)
	{
		Drawable image = null;
		FileInputStream inputStream = null;
		File file = new File(path);
		if (isSDCardAvailable())
		{
			try
			{
				inputStream = new FileInputStream(file);
				image = Drawable.createFromStream(inputStream, file.getName());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (inputStream != null)
				{
					try
					{
						inputStream.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return image;
	}
	/**
	 * ��ȡsd�����ͼƬ
	 * @param dir �ļ�Ŀ¼·��
	 * @param name �ļ���
	 * @return ͼƬdrawable
	 */
	public Drawable readImageFromSD(String dir, String name)
	{
		Drawable image = null;
		FileInputStream inputStream = null;
		File file = new File(ROOTPATH + dir, name);
		if (isSDCardAvailable())
		{
			try
			{
				inputStream = new FileInputStream(file);
				image = Drawable.createFromStream(inputStream, file.getName());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (inputStream != null)
				{
					try
					{
						inputStream.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return image;
	}
	
	/**
	 * 
	 * @param urlStr �ļ�url��ַ
	 * @param path �洢��sdĿ¼·��
	 * @param fileName �洢���ļ���
	 * @param cover ���ļ��Ѵ��ڣ��Ƿ񸲸�
	 * @return �����Ƿ�ɹ�
	 */
	public boolean downloadFile(String urlStr, String path, String fileName,
			boolean cover)
	{
		InputStream inputStream = null;
		try
		{
			inputStream = client.getInputStreamFromUrl(urlStr);
			File file = writeToSDFromInputStream(path, fileName,
					inputStream, cover);
			if (file == null)
			{
				return false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				inputStream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	/**
	 * ��ʵ��ת���ɴ洢��ַ
	 * 
	 * @param urlStr
	 * @return
	 */
	public String modelToAddress(Object model) {
		String result = null;
		if (model instanceof Friend) {
			Friend f = (Friend) model;
			String dir = "Friend";
			String name = f.getName();
			result = dir + File.separator + name;
		}
		return ROOTPATH + result;
	}
	
}
