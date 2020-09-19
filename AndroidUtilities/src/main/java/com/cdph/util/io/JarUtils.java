/*
* Copyright 2020 SnoopyCodeX | Cyber Droid Developers PH
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* This utility class is for compressing
* files and folders into a JAR(Java Archive) format
*
*@package  com.cdph.util.io
*@class  JarUtils.java
*@author  John Roy L. Calimlim (SnoopyCodeX)
*@copyright  2020
*@link  https://www.facebook.com/cdphdevs
*/

package com.cdph.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public final class JarUtils implements Cloneable
{
	/*
	* Compressess the target files into a JAR file and
	* stores to the specified directory
	*
	*@method  JarUtils.compress(target, directory)
	*@param  target  -  The target file/folder to be compressed
	*@param  directory  -  The directory of the output JAR file
	*@return  java.io.File  -  The output JAR file
	*/
	public static final File compress(File target, File directory)
	{
		JarOutputStream jos = null;
		Manifest manifest = null;
		File output = null;

		if(!directory.exists())
			directory.mkdirs();

		if(!target.exists())
			return output;

		try {
			manifest = new Manifest();
			output = new File(directory, target.getName() + ".jar");

			manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
			jos = new JarOutputStream(new FileOutputStream(output), manifest);
			compress(target, target, jos);
			jos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return output;
	}

	/*
	* Compressess the target files into a JAR file and
	* stores to the specified directory
	*
	*@method  JarUtils.compress(target, directory, outputName)
	*@param  target  -  The target file/folder to be compressed
	*@param  directory  -  The directory of the output JAR file
	*@param  outputName  -  The name of the output JAR file
	*@return  java.io.File  -  The output JAR file
	*/
	public static final File compress(File target, File directory, String outputName)
	{
		JarOutputStream jos = null;
		Manifest manifest = null;
		File output = null;

		if(!directory.exists())
			directory.mkdirs();

		if(!outputName.endsWith(".jar"))
			outputName += ".jar";

		if(!target.exists())
			return output;

		try {
			manifest = new Manifest();
			output = new File(directory, outputName);

			manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
			jos = new JarOutputStream(new FileOutputStream(output), manifest);
			compress(target, target, jos);
			jos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return output;
	}



	private static void compress(File parentFile, File source, JarOutputStream jos)
	{
		try {
			String path = source.getPath();
			path = path.replaceAll(parentFile.getParent(), "");
			path = path.replace("\\", "/");

			if(source.isDirectory())
			{
				File[] files = source.listFiles();
				for(File file : files)
					compress(parentFile, file, jos);

				if(!path.isEmpty())
				{
					if(!path.endsWith("/"))
						path += "/";

					JarEntry entry = new JarEntry(path);
					entry.setTime(source.lastModified());
					jos.putNextEntry(entry);
					jos.closeEntry();
				}
			}

			if(source.isFile())
			{
				FileInputStream fis = new FileInputStream(source);
				byte[] buffer = new byte[1024];
				int len = 1;

				JarEntry entry = new JarEntry(path);
				entry.setTime(source.lastModified());
				jos.putNextEntry(entry);

				while((len = fis.read(buffer)) != -1)
					jos.write(buffer, 0, len);
				jos.closeEntry();
				fis.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected JarUtils clone() throws CloneNotSupportedException
	{
		return this;
	}
}
