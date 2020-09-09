/*
* This utility class is specified for
* files and folder use.
*
*@package  com.cdph.util.io
*@class  FileUtils.java
*@author  John Roy L. Calimlim (SnoopyCodeX)
*@copyright  2020
*@link  https://www.facebook.com/cdphdevs
*/


package com.cdph.util.io;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public final class FileUtils
{
	public static final String TAG = FileUtils.class.getSimpleName();
	public static boolean LOG_ERRORS = false;
	
	
	/*
	* Converts file into an array of bytes
	*
	*@method  FileUtils.toBytes(file)
	*@param	  file  -  The file to be converted
	*
	*@method  FileUtils.toBytes(filepath)
	*@param   filepath  -  The filepath of the file to be converted
	*
	*@method  FileUtils.toBytes(is)
	*@param   is  -  The corresponding InputStream of the file
	*
	*@return  byte[]  -  The bytes of the file
	*/
	public static final byte[] toBytes(File file)
	{
		byte[] data = null;
		
		try {
			InputStream is = new FileInputStream(file);
			data = new byte[is.available()];
			is.read(data);
			is.close();
		} catch(Exception e) {
			LogUtil.post(e.getCause().toString(), e.getMessage());
			e.printStackTrace();
			data = null;
		}

		return data;
	}

	public static final byte[] toBytes(String filepath)
	{
		return (toBytes(new File(filepath)));
	}

	public static final byte[] toBytes(InputStream is)
	{
		byte[] data = null;

		try {
			data = new byte[is.available()];
			is.read(data);
			is.close();
		} catch(Exception e) {
			LogUtil.post(e.getCause().toString(), e.getMessage());
			e.printStackTrace();
			data = null;
		}

		return data;
	}


	/*
	* Copies the file/folder to a specified destination
	*
	*@method  FileUtils.copy(file, dest)
	*@param   file  -  The file/folder to be copied
	*@param   dest  -  The destination of the file/folder to be copied
	*
	*@method  FileUtils.copy(file, destPath)
	*@param   file  -  The file/folder to be copied
	*@param   destPath  -  The string filepath of the destination
	*
	*@return  boolean  -  Success state of operation
	*/
	public static final boolean copy(File file, File dest)
	{
		boolean canResume = false;
		boolean copied = false;

		try {
			if(file.isFile())
			{
				canResume = file.exists() && file.canRead();
				if(!canResume)
					return copied;

				canResume = dest.exists();
				if(!canResume && !dest.getName().equals(file.getName()))
					canResume = dest.mkdir() || dest.mkdirs();

				if(canResume && dest.isDirectory())
				{
					File temp = new File(dest, file.getName());
					dest = new File(dest, getCopiedName(temp));
					dest.createNewFile();
				}

				if(!canResume && dest.isFile())
					dest.createNewFile();

				canResume = dest.canWrite();
				if(!canResume)
					return copied;

				InputStream is = new FileInputStream(file);
				OutputStream os = new FileOutputStream(dest);
				byte[] data = new byte[is.available()];

				is.read(data);
				is.close();

				os.write(data);
				os.flush();
				os.close();

				copied = true;
			}

			if(file.isDirectory())
			{
				if(file.listFiles().length < 1)
				{
					if(!dest.exists())
						canResume = dest.mkdirs() || dest.mkdir();

					if(!canResume)
						return copied;

					File temp = new File(dest, file.getName());
					dest = new File(dest, getCopiedName(temp));
					copied = dest.mkdir() || dest.mkdirs();
				}
				else
				{
					canResume = dest.exists();
					if(!canResume)
						canResume = dest.mkdirs() || dest.mkdir();

					if(!canResume)
						return copied;

					File temp = new File(dest, file.getName());
					dest = new File(dest, getCopiedName(temp));
					canResume = dest.mkdir() || dest.mkdirs();

					if(!canResume)
						return copied;

					File[] files = file.listFiles();
					for(File f : files)
						copy(f, dest);

					copied = true;
				}
			}
		} catch(Exception e) {
			LogUtil.post(e.getCause().toString(), e.getMessage());
			e.printStackTrace();
			copied = false;
		}

		return copied;
	}

	public static final boolean copy(File file, String destPath)
	{
		return (copy(file, new File(destPath)));
	}


	/*
	* Moves the file/folder to a specified destination
	*
	*@method  FileUtils.move(file, dest)
	*@param   file  -  The file/folder to be moved
	*@param   dest  -  The destination where the file/folder will be moved to
	*
	*@method  FileUtils.move(file, destPath)
	*@param   file  -  The file/folder to be moved
	*@param   destPath  -  The string filepath of the destination
	*
	*@return   boolean  -  The success state of the operation
	*/
	public static final boolean move(File file, File dest)
	{
		boolean moved = false;

		if(copy(file, dest))
			moved = delete(file);

		return moved;
	}

	public static final boolean move(File file, String destPath)
	{
		return move(file, new File(destPath));
	}


	/*
	* Deletes a file/folder
	*
	*@method  FileUtils.delete(file)
	*@param   file  -  The file/folder to be deleted
	*
	*@method  FileUtils.delete(filepath)
	*@param   filepath  -  The filepath of the file/folder to be deleted
	*
	*@return  boolean  -  The success state of the operation
	*/
	public static final boolean delete(File file)
	{
		boolean deleted = false;

		if(file.isFile() || (file.isDirectory() && file.listFiles().length < 1))
			deleted = file.delete();
		else
		{
			File[] files = file.listFiles();
			for(File f : files)
				deleted = delete(f);
			deleted = file.delete();
		}

		return deleted;
	}

	public static final boolean delete(String filepath)
	{
		return (delete(new File(filepath)));
	}


	/*
	* Renames a file/folder
	*
	*@method  FileUtils.rename(file, name)
	*@param   file  -  The file/folder to be renamed
	*@param   name  -  The new name of the file/folder
	*
	*@method  FileUtils.rename(filepath, name)
	*@param   filepath  -  The filepath of the file/folder to be renamed
	*@param   name  -  The new name of the file/folder
	*
	*@return  boolean  -  The success state of the operation
	*/
	public static final boolean rename(File file, String name)
	{
		File temp = new File(file.getParentFile(), name);
		if(temp.exists())
			return false;

		return file.renameTo(new File(file.getParentFile(), name));
	}

	public static final boolean rename(String filepath, String name)
	{
		return (rename(new File(filepath), name));
	}


	/*
	* Writes to a file
	*
	*@method  FileUtils.writeToFile(file, content, overwrite)
	*@param   file  -  The file to be written to
	*@param   content  -  The content to be written to the file (In byte array format)
	*@param   overwrite  -  Should overwrite the content of the file
	*
	*@method  FileUtils.writeToFile(file, content, overwrite)
	*@param   file  -  The file to be written to
	*@param   content  -  The content to be written to the file (In string format)
	*@param   overwrite  -  Should overwrite the content of the file
	*
	*@method  FileUtils.writeToFile(filepath, content, overwrite)
	*@param   filepath  -  The filepath of the file to be written to
	*@param   content  -  The content to be written to the file (In byte array format)
	*@param   overwrite  -  Should overwrite the content of the file
	*
	*@method  FileUtils.writeToFile(filepath, content, overwrite)
	*@param   filepath  -  The filepath of the file to be written to
	*@param   content  -  The content to be written to the file (In string format)
	*@param   overwrite  -  Should overwrite the content of the file
	*
	*@return  boolean  -  The success state of the operation
	*/
	public static final boolean writeToFile(File file, byte[] content, boolean overwrite)
	{
		OutputStream os = null;
		InputStream is = null;
		String str1 = null;
		String str2 = null;
		boolean written = false;
		byte[] data = null;

		try {
			if(!file.exists() || file.isDirectory())
				return written;
			
			os = new FileOutputStream(file);
			str2 = new String(content);

			if(file.exists())
			{
				is = new FileInputStream(file);
				data = new byte[is.available()];
				is.read(data);
				is.close();

				str1 = new String(data);
			}

			if(overwrite)
				os.write(content);
			else
				os.write((str1 + str2).getBytes());

			os.flush();
			os.close();
			written = true;
		} catch(Exception e) {
			LogUtil.post(e.getCause().toString(), e.getMessage());
			e.printStackTrace();
			written = false;
		}

		return written;
	}

	public static final boolean writeToFile(File file, String content, boolean overwrite)
	{
		return (writeToFile(file, content.getBytes(), overwrite));
	}

	public static final boolean writeToFile(String filepath, byte[] content, boolean overwrite)
	{
		return (writeToFile(new File(filepath), content, overwrite));
	}

	public static final boolean writeToFile(String filepath, String content, boolean overwrite)
	{
		return (writeToFile(filepath, content.getBytes(), overwrite));
	}


	/*
	* Reads a file
	*
	*@method  FileUtils.readFile(file)
	*@param   file  -  The file to read to
	*
	*@method  FileUtils.readFile(filepath)
	*@param   filepath  -  The filepath of the file to read to
	*
	*@method  FileUtils.readFile(is)
	*@param   is  -  The InputStream of the file
	*
	*@return  byte[]  -  The content of the file
	*@return  string  -  The content of the file
	*/
	public static final byte[] readFile(File file)
	{
		byte[] data = null;

		try {
			if(!file.exists() || file.isDirectory())
				return data;

			InputStream is = new FileInputStream(file);
			data = new byte[is.available()];
			is.read(data);
			is.close();
		} catch(Exception e) {
			LogUtil.post(e.getCause().toString(), e.getMessage());
			e.printStackTrace();
			data = null;
		}

		return data;
	}

	public static final String readFile(String filepath)
	{
		return (new String(readFile(new File(filepath))));
	}

	public static final byte[] readFile(InputStream is)
	{
		byte[] data = null;
		
		try {
			data = new byte[is.available()];
			is.read(data);
			is.close();
		} catch(Exception e) {
			LogUtil.post(e.getCause().toString(), e.getMessage());
			e.printStackTrace();
			data = null;
		}
		
		return data;
	}
	
	
	/*
	* Converts filesize into human readable format
	*
	*@method  FileUtils.filesizeToReadableFormat(file)
	*@param   file  -  The target file/folder
	*@return  string  -  The formatted filesize of the file/folder
	*/
	public static final String filesizeToReadableFormat(File file)
	{
		double B = 1;
		double KB = B * 1024;
		double MB = KB * 1024;
		double GB = MB * 1024;
		double TB = GB * 1024;
		
		List<Double> sizes = (List<Double>) Arrays.asList(
			B, KB, MB, GB, TB
		);
		
		List<String> names = (List<String>) Arrays.asList(
			"B", "KB", "MB", "GB", "TB"
		);
		
		DecimalFormat formatter = new DecimalFormat("#.##");
		double filesize = file.length();
		String size = "";

		for(int i = 0; i < sizes.size(); i++)
		{
			double cs = sizes.get(i);
			
			if(i < (sizes.size() - 1))
			{
				double ns = sizes.get(i + 1);
				
				if(filesize >= cs && filesize < ns)
				{
					double fs = filesize / cs;
					size = formatter.format(fs);
					size += names.get(i);
					break;
				}
				
				continue;
			}
			else if(i == (sizes.size() - 1) && filesize >= cs)
			{
				double fs = filesize / cs;
				size = formatter.format(fs);
				size += names.get(i);
				break;
			}
		}
		
		return size;
	}
	
	
	/*
	* Gets the file extension of the file from it's name
	*
	*@method  FileUtils.getFileExtension(file)
	*@param   file  -  The target file
	*
	*@method  FileUtils.getFileExtension(filepath)
	*@param   filepath  -  The filepath of the target file
	*
	*@return  string  -  The extension of the file
	*/
	public static final String getFileExtension(File file)
	{
		String name = file.getName();
		String ext = null;
		
		if(!file.exists() || file.isDirectory())
			return ext;
		
		name = file.getName();
		for(int i = name.length()-1; i > -1; i--)
			if(name.charAt(i) == '.')
			{
				ext = name.substring(i, name.length());
				break;
			}
		
		return ext;
	}
	
	public static final String getFileExtension(String filepath)
	{
		return (getFileExtension(new File(filepath)));
	}
	
	
	/*
	* Gets the filename of the file without it's extension
	*
	*@method  FileUtils.getFileNameWithoutExtension(file)
	*@param   file  -  The target file
	*
	*@method  FileUtils.getFileNameWithoutExtension(filepath)
	*@param   filepath  -  The filepath of the target file
	*
	*@return  string  -  The name of the file without it's extension
	*/
	public static final String getFileNameWithoutExtension(File file)
	{
		String ext = getFileExtension(file);
		String name = null;
		
		if(!file.exists() || file.isDirectory())
			return name;
		
		name = file.getName();
		name = name.substring(0, name.length() - ext.length());
		return name;
	}
	
	public static final String getFileNameWithoutExtension(String filepath)
	{
		return (getFileNameWithoutExtension(new File(filepath)));
	}
	
	

	private static final String getCopiedName(File origFile)
	{
		String copied = origFile.getName();
		String ext = getFileExtension(origFile);
		String index = "";
		int reps = -1;

		if(!origFile.exists())
			return copied;

		if(origFile.isFile())
			copied = getFileNameWithoutExtension(origFile);

		if(copied.endsWith(")"))
		{
			for(int i = copied.length()-1; i > -1; i--)
				if(copied.charAt(i) == '(')
				{
					index = copied.substring(i, copied.length());
					break;
				}

			if(index.replaceAll("[.]", "").matches("\\(\\d+\\)"))
			{
				copied = copied.substring(0, copied.length() - index.length());
				index = index.replaceAll("[\\(]", "").replaceAll("[\\)]", "");
				reps = Integer.parseInt(index);
			}
		}

		String path = origFile.getParentFile().getAbsolutePath();
		copied += String.format("(%d)%s", reps + 1, (ext.startsWith(".") ? ext : "." + ext));
		origFile = new File(path, copied);

		return (origFile.exists() ? getCopiedName(origFile) : copied);
	}

	private static final class LogUtil
	{
		public static final void post(String cause, String message)
		{
			if(LOG_ERRORS)
				Log.e(String.format("%s - %s", cause, TAG), String.format("Caused by: %s, %s", cause, message));
		}
	}
}
