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
*/

package com.cdph.util.font;

import java.io.File;
import static com.cdph.util.io.FileUtils.toBytes;

public final class FontUtils
{
	private static final byte[] TTF_MAGIC = {0x00, 0x01, 0x00, 0x00, 0x00};
	private static final byte[] OTF_MAGIC = {0x4F, 0x54, 0x54, 0x4F, 0x00};

	
	/*
	* Verifies a .ttf file if it's real or not by checking
	* it's magic numbers: 00 01 00 00 00
	*
	*@method  FontUtils.isRealTTF(ttf)
	*@param   ttf  -  The ttf file to be verified
	*@return  boolean
	*/
	public static final boolean isRealTTF(File ttf)
	{
		boolean real = false;
		byte[] data = toBytes(ttf);

		real = ttf.getName().toLowerCase().endsWith("ttf");
		real = (real) ? (data != null) : real;
		real = (real) ? (
			inBytes(data[0], TTF_MAGIC) &&
			inBytes(data[1], TTF_MAGIC) &&
			inBytes(data[2], TTF_MAGIC) &&
			inBytes(data[3], TTF_MAGIC) &&
			inBytes(data[4], TTF_MAGIC)
			) : real;

		return real;
	}

	
	/*
	* Verifies a .otf file if it's real or not by checking
	* it's magic numbers: 4F 54 54 4F 00
	*
	*@method  FontUtils.isRealOTF(otf)
	*@param   otf  -  The otf file to be verified
	*@return  boolean
	*/
	public static final boolean isRealOTF(File otf)
	{
		boolean real = false;
		byte[] data = toBytes(otf);

		real = otf.getName().toLowerCase().endsWith("otf");
		real = (real) ? (data != null) : real;
		real = (real) ? (
			inBytes(data[0], OTF_MAGIC) &&
			inBytes(data[1], OTF_MAGIC) &&
			inBytes(data[2], OTF_MAGIC) &&
			inBytes(data[3], OTF_MAGIC) &&
			inBytes(data[4], OTF_MAGIC)
			) : real;

		return real;
	}

	private static boolean inBytes(byte target, byte[] data)
	{
		for(byte bit : data)
			if(target == bit)
				return true;
		
		return false;
	}
}
