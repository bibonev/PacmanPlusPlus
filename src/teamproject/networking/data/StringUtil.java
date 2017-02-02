package teamproject.networking.data;

import java.util.ArrayList;

public class StringUtil {
	/**
	 * Implementation of a non regex-based string splitting utility. Returns
	 * an array of each substring within {@code input} that is delimited
	 * (separated) by the string {@code delimiter}, optionally removing empty
	 * substrings. For example,
	 * <pre><code>
	 * split("1,2,,3,4,5,", ",", true) = ["1", "2", "3", "4", "5"]
	 * split("1,2,,3,4,5,", ",", false) = ["1", "2", "", "3", "4", "5", ""]
	 * </code></pre>
	 * This non regex-based implementation is used for speed.
	 * 
	 * @param input The input string to split.
	 * @param delimiter The splitting delimiter.
	 * @param removeEmpty Whether to remove empty substrings from the result.
	 * @return An array of substrings separated by the given delimiter.
	 */
	public static String[] split(String input, String delimiter, boolean removeEmpty) {
		if(input.length() > 0) {
			ArrayList<String> parts = new ArrayList<String>();
			int beginIndex = 0, endIndex;
			while((endIndex = input.indexOf(delimiter, beginIndex)) != -1) {
				parts.add(input.substring(beginIndex, endIndex));
				beginIndex = endIndex + delimiter.length();
			}
			
			parts.add(input.substring(beginIndex));
			
			String[] partsArray = new String[parts.size()];
			for(int i = 0; i < partsArray.length; i++) {
				partsArray[i] = parts.get(i);
			}
			return parts.toArray(partsArray);
		} else {
			return new String[0];
		}
}
}
