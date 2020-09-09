/*
* TimeUtils
*
* A simple library inspired from a groupmate's
* problem. I just kind of "improved" his work
* and made it more simple.
*
* If you want to add more timeline(don't know what
* it's called, sorry). Just add the appropriate time
* in milliseconds inside of TIME_IN_MILLIS array. The
* position of the added timeline must correspond to it's
* name inside of WORD_EXTENSIONS array. (See current code).
*
* Supports: 
*   -Future & Past Relative Times from seconds to centuries
*
*@author  John Roy L. Calimlim
*@credits  Programming PH (GC)
*@copyright  2020
*@link  https://www.facebook.com/cdphdevs
*/

package com.cdph.util.time;

import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.List;
import java.math.BigInteger;

public final class TimeUtils
{
	private static final List<BigInteger> TIME_IN_MILLIS = (List<BigInteger>) Arrays.asList(
		BigInteger.valueOf(TimeUnit.SECONDS.toMillis(1)),		//1 Second
		BigInteger.valueOf(TimeUnit.MINUTES.toMillis(1)),		//1 Minute
		BigInteger.valueOf(TimeUnit.HOURS.toMillis(1)),			//1 Hour
		BigInteger.valueOf(TimeUnit.DAYS.toMillis(1)),			//1 Day
		BigInteger.valueOf(TimeUnit.DAYS.toMillis(1) * 7),		//1 Week
		BigInteger.valueOf(TimeUnit.DAYS.toMillis(1) * 30),		//1 Month
		BigInteger.valueOf(TimeUnit.DAYS.toMillis(365)),		//1 Year
		BigInteger.valueOf(TimeUnit.DAYS.toMillis(365) * 10),	//1 Decade
		BigInteger.valueOf(TimeUnit.DAYS.toMillis(365) * 100)	//1 Century
	);

	private static final List<String> WORD_EXTENSIONS = (List<String>) Arrays.asList(
		"second",
		"minute",
		"hour",
		"day",
		"week",
		"month",
		"year",
		"decade",
		"century"
	);

	
	/*
	* Converts the time(in milliseconds) into 
	* relative time string.
	*
	* Example:
	* 	• 1 minute ago
	* 	• 2 hours ago
	*	• 6 years from now
	*
	*@method  TimeUtils.toRelativeTime(timeInMS)
	*@param   timeInMS  -  Time / Date that is specified in milliseconds
	*@return  string
	*/
	public static final String toRelativeTime(long timeInMS)
	{
		long now = System.currentTimeMillis();
		long diff = now - timeInMS;

		if(diff < 0)
			return computeFutureRelativeTime(diff);
		return computePastRelativeTime(diff);
	}

	private static final String computeFutureRelativeTime(long timeInMS)
	{
		String ret = "Just now";

		for(int i = 0; i < TIME_IN_MILLIS.size(); i++)
		{
			StringBuilder str = new StringBuilder();
			String ext = WORD_EXTENSIONS.get(i);
			long num = TIME_IN_MILLIS.get(i).longValue();
			long quot = -timeInMS / num;

			if(i < TIME_IN_MILLIS.size()-1)
			{
				long nxt = TIME_IN_MILLIS.get(i + 1).longValue();

				if(timeInMS <= -num && timeInMS > -nxt)
				{
					str.append(quot)
						.append(" ")
						.append((quot > 1) ? toPluralForm(ext) : ext)
						.append(" from now");

					ret = str.toString();
					break;
				}
			}
			else
			{
				if(timeInMS <= -num)
				{
					str.append(quot)
						.append(" ")
						.append((quot > 1) ? toPluralForm(ext) : ext)
						.append(" from now");

					ret = str.toString();
					break;
				}
			}
		}

		return ret;
	}

	private static final String computePastRelativeTime(long timeInMS)
	{
		String ret = "Just now";

		for(int i = 0; i < TIME_IN_MILLIS.size(); i++)
		{
			StringBuilder str = new StringBuilder();
			String ext = WORD_EXTENSIONS.get(i);
			long num = TIME_IN_MILLIS.get(i).longValue();
			long quot = timeInMS / num;

			if(i < TIME_IN_MILLIS.size()-1)
			{
				long nxt = TIME_IN_MILLIS.get(i + 1).longValue();

				if(timeInMS >= num && timeInMS < nxt)
				{
					str.append(quot)
						.append(" ")
						.append((quot > 1) ? toPluralForm(ext) : ext)
						.append(" ago");

					ret = str.toString();
					break;
				}
			}
			else
			{
				if(timeInMS >= num)
				{
					str.append(quot)
						.append(" ")
						.append((quot > 1) ? toPluralForm(ext) : ext)
						.append(" ago");

					ret = str.toString();
					break;
				}
			}
		}

		return ret;
	}

	private static final String toPluralForm(String word)
	{
		String[] consonant1 = {"s", "sh", "ch", "x", "o"};
		char[] vowels = {'a', 'e', 'i', 'o', 'u'};
		String wrd = word.toLowerCase();

		for(String consonant : consonant1)
			if(wrd.endsWith(consonant))
				return word + "es";

		if(wrd.endsWith("y"))
		{
			for(char vowel : vowels)
				if(wrd.charAt(word.length()-2) == vowel)
					return (word + "s");
				else
					continue;

			return (word.substring(0, word.length()-1) + "ies");
		}

		if(wrd.endsWith("f"))
			return (word.substring(0, word.length()-1) + "ves");

		return (word + "s");
	}
}
