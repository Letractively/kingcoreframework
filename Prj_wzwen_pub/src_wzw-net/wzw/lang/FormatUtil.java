package wzw.lang;

/**
 * 格式化工具类
 * 
 * @author gembler
 * @version 2008-12-3 下午03:01:50
 */
public class FormatUtil {

	/**
	 * 格式化二进制。默认取8位，超过则截取，不足则补零。
	 * 格式：“00000000”，与NumberFormat的pattern：“########”类似。
	 * 
	 * @author gembler
	 * @version 2008-12-3 下午03:15:06
	 * 
	 * @param binary
	 *            需要格式化的字节。
	 * 
	 * @return 格式化后的字符串。
	 */
	public static String formatBinary(byte binary) {

		return formatBinary(binary, null).toString();

	}

	/**
	 * 格式化二进制，超过则截取，不足则补零。格式：“00000000”，与NumberFormat的pattern：“########”类似。
	 * 
	 * @author gembler
	 * @version 2008-12-3 下午03:15:09
	 * 
	 * @param binary
	 *            需要格式化的字节。
	 * @param bitCount
	 *            需要格式化的位数。
	 * 
	 * @return 格式化后的字符串。
	 */
	public static String formatBinary(byte binary, int bitCount) {

		return formatBinary(binary, null, bitCount).toString();

	}

	/**
	 * 格式化二进制，超过则截取，不足则补零。格式：“00000000”，与NumberFormat的pattern：“########”类似。
	 * 
	 * @author gembler
	 * @version 2008-12-3 下午03:15:12
	 * 
	 * @param binary
	 *            需要格式化的字节。
	 * @param toAppendTo
	 *            追加到的Builder。
	 * 
	 * @return 格式化后的StringBuffer。
	 */
	public static StringBuffer formatBinary(byte binary,
			StringBuffer toAppendTo) {

		return formatBinary(binary, toAppendTo, Base64Smarted.EIGHT_BIT);
	}

	/**
	 * 格式化二进制，超过则截取，不足则补零。格式：“00000000”，与NumberFormat的pattern：“########”类似。
	 * 
	 * @author gembler
	 * @version 2008-12-3 下午03:15:16
	 * 
	 * @param binary
	 *            需要格式化的字节。
	 * @param toAppendTo
	 *            追加到的Builder。
	 * @param bitCount
	 *            需要格式化的位数。
	 * 
	 * @return 格式化后的StringBuffer。
	 */
	public static StringBuffer formatBinary(byte binary,
			StringBuffer toAppendTo, int bitCount) {

		String strBinary = Integer.toBinaryString(binary);

		return formatBinary(strBinary, toAppendTo, bitCount);
	}

	/**
	 * 格式化二进制，超过则截取，不足则补零。格式：“00000000”，与NumberFormat的pattern：“########”类似。
	 * 
	 * @author gembler
	 * @version 2008-12-3 下午03:15:20
	 * 
	 * @param binary
	 *            需要格式化的字节。
	 * 
	 * @return 格式化后的字符串。
	 */
	public static String formatBinary(String binary) {

		return formatBinary(binary, null).toString();

	}

	/**
	 * 格式化二进制，超过则截取，不足则补零。格式：“00000000”，与NumberFormat的pattern：“########”类似。
	 * 
	 * @author gembler
	 * @version 2008-12-3 下午03:15:24
	 * 
	 * @param binary
	 *            需要格式化的字节。
	 * @param bitCount
	 *            需要格式化的位数。
	 * 
	 * @return 格式化后的字符串。
	 */
	public static String formatBinary(String binary, int bitCount) {

		return formatBinary(binary, null, bitCount).toString();

	}

	/**
	 * 格式化二进制，超过则截取，不足则补零。格式：“00000000”，与NumberFormat的pattern：“########”类似。
	 * 
	 * @author gembler
	 * @version 2008-12-3 下午03:15:27
	 * 
	 * @param binary
	 *            需要格式化的字节。
	 * @param toAppendTo
	 *            追加到的Builder。
	 * 
	 * @return 格式化后的StringBuffer。
	 */
	public static StringBuffer formatBinary(String binary,
			StringBuffer toAppendTo) {

		return formatBinary(binary, toAppendTo, Base64Smarted.EIGHT_BIT);

	}

	/**
	 * 格式化二进制，超过则截取，不足则补零。格式：“00000000”，与NumberFormat的pattern：“########”类似。
	 * 
	 * @author gembler
	 * @version 2008-12-3 下午03:15:31
	 * 
	 * @param binary
	 *            需要格式化的字节。
	 * @param toAppendTo
	 *            追加到的Builder。
	 * @param bitCount
	 *            追加到的Builder。
	 * 
	 * @return 格式化后的StringBuffer。
	 */
	public static StringBuffer formatBinary(String binary,
			StringBuffer toAppendTo, int bitCount) {

		if (binary == null || binary.length() < 1) {

			return toAppendTo;

		}

		if (toAppendTo == null) {

			toAppendTo = new StringBuffer();

		}

		if (binary.length() == bitCount) {

			toAppendTo.append(binary);

			return toAppendTo;

		}

		/*
		 * 前补0， 如： "100011" to "00100011"
		 */
		if (binary.length() < bitCount) {

			StringBuffer formatted = new StringBuffer();

			formatted.append(binary);

			do {

				formatted.insert(0, "0");

			} while (formatted.length() < bitCount);

			toAppendTo.append(formatted);

			return toAppendTo;

		}

		/*
		 * 截取， 如： "11111111111111111111111110100011" to "10100011"
		 */
		if (binary.length() > bitCount) {

			String intercepted = binary.substring(binary.length() - bitCount);

			toAppendTo.append(intercepted);

			return toAppendTo;

		}

		return toAppendTo;
	}

}
