package org.iii.ideas.catering_service.util;

public class ipConvert {
	private static final int IPV6Length = 8; // IPV6地址的分段
	private static final int IPV4Length = 4; // IPV6地址分段
	private static final int IPV4ParmLength = 2; // 一個IPV4分段占的長度
	private static final int IPV6ParmLength = 4; // 一個IPV6分段占的長

	/**
	 * IPV6、IPV4轉化为十六進制串
	 * 
	 * @param ipAddress
	 * @return
	 */
	public static String buildKey(String ipAddress) {
		String Key = "";
		// ipv4標識 。判斷是否是ipv4地址
		int dotFlag = ipAddress.indexOf(".");
		// ipv6標識 。判斷是否是ipv6地址
		int colonFlag = ipAddress.indexOf(":");
		// ipv6標識 。判斷是否是簡寫的ipv6地址
		int dColonFlag = ipAddress.indexOf("::");
		// 將v6或v4的分隔符用&代替
		ipAddress = ipAddress.replace(".", "&");
		ipAddress = ipAddress.replace(":", "&");
		// ipv4 address。將ipv4地址轉換成16進制的形式
		if (dotFlag != -1 && colonFlag == -1) {
			String[] arr = ipAddress.split("&");
			// 1、 ipv4轉ipv6，前4組數補0或f
			for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {
				// 根據v4轉v6的形式，除第4組數補ffff外，前3組數補0000
				if (i == IPV6Length - IPV4ParmLength - 1) {
					Key += "ffff";
				} else {
					Key += "0000";
				}
			}
			// 2、將ipv4地址轉成16進制
			for (int j = 0; j < IPV4Length; j++) {
				// 1)將每組ipv4地址轉換成16進制
				arr[j] = Integer.toHexString(Integer.parseInt(arr[j]));
				// 2) 位數不足補0，ipv4地址中一組可轉換成一個十六進制，兩組數即可標識ipv6中的一組，v6中的一組數不足4位補0
				for (int k = 0; k < (IPV4ParmLength - arr[j].length()); k++) {
					Key += "0";
				}
				Key += arr[j];
			}
		}
		// Mixed address with ipv4 and ipv6。將v4與v6的混合地址轉換成16進制的形式
		if (dotFlag != -1 && colonFlag != -1 && dColonFlag == -1) {
			String[] arr = ipAddress.split("&");

			for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {
				// 將ip地址中每組不足4位的補0
				for (int k = 0; k < (IPV6ParmLength - arr[i].length()); k++) {
					Key += "0";
				}
				Key += arr[i];
			}

			for (int j = 0; j < IPV4Length; j++) {
				arr[j] = Integer.toHexString(Integer.parseInt(arr[j]));
				for (int k = 0; k < (IPV4ParmLength - arr[j].length()); k++) {
					Key += "0";
				}
				Key += arr[j];
			}
		}
		// Mixed address with ipv4 and ipv6,and there are more than one
		// '0'。將v4與v6的混合地址(如::32:dc:192.168.62.174)轉換成16進制的形式
		// address param
		if (dColonFlag != -1 && dotFlag != -1) {
			String[] arr = ipAddress.split("&");
			// 存放16進制的形式
			String[] arrParams = new String[IPV6Length + IPV4ParmLength];
			int indexFlag = 0;
			int pFlag = 0;
			// 1、將簡寫的ip地址補0
			// 如果ip地址中前面部分采用簡寫，做如下處理
			if ("".equals(arr[0])) {
				// 1)如果ip地址采用簡寫形式，不足位置補0，存放到arrParams中
				for (int j = 0; j < (IPV6Length + IPV4ParmLength - (arr.length - 2)); j++) {
					arrParams[j] = "0000";
					indexFlag++;
				}
				// 2)將已有值的部分(如32:dc:192.168.62.174)存放到arrParams中
				for (int i = 2; i < arr.length; i++) {
					arrParams[indexFlag] = arr[i];
					indexFlag++;
				}
			} else {
				for (int i = 0; i < arr.length; i++) {
					if ("".equals(arr[i])) {
						for (int j = 0; j < (IPV6Length + IPV4ParmLength
								- arr.length + 1); j++) {
							arrParams[indexFlag] = "0000";
							indexFlag++;
						}
					} else {
						arrParams[indexFlag] = arr[i];
						indexFlag++;
					}
				}
			}
			// 2、ip(去除ipv4的部分)中采用4位十六進制數表示一組數，將不足4位的十六進制數補0
			for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {
				// 如果arrParams[i]組數據不足4位，前補0
				for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {
					Key += "0";
				}
				Key += arrParams[i];
				// pFlag用於標識位置，主要用來標識ipv4地址的起始位
				pFlag++;
			}
			// 3、將ipv4地址轉成16進制
			for (int j = 0; j < IPV4Length; j++) {
				// 1)將每組ipv4地址轉換成16進制
				arrParams[pFlag] = Integer.toHexString(Integer
						.parseInt(arrParams[pFlag]));
				// 2)位數不足補0，ipv4地址中一組可轉換成一個十六進制，兩組數即可標識ipv6中的一組，v6中的一組數不足4位補0
				for (int k = 0; k < (IPV4ParmLength - arrParams[pFlag].length()); k++) {
					Key += "0";
				}
				Key += arrParams[pFlag];
				pFlag++;
			}
		}
		// ipv6 address。將ipv6地址轉換成16進制
		if (dColonFlag == -1 && dotFlag == -1 && colonFlag != -1) {
			String[] arrParams = ipAddress.split("&");
			// 將v6地址轉成十六進制
			for (int i = 0; i < IPV6Length; i++) {
				// 將ipv6地址中每組不足4位的補0
				for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {
					Key += "0";
				}

				Key += arrParams[i];
			}
		}

		if (dColonFlag != -1 && dotFlag == -1) {
			String[] arr = ipAddress.split("&");
			String[] arrParams = new String[IPV6Length];
			int indexFlag = 0;
			if ("".equals(arr[0])) {
				for (int j = 0; j < (IPV6Length - (arr.length - 2)); j++) {
					arrParams[j] = "0000";
					indexFlag++;
				}
				for (int i = 2; i < arr.length; i++) {
					arrParams[indexFlag] = arr[i];
					i++;
					indexFlag++;
				}
			} else {
				for (int i = 0; i < arr.length; i++) {
					if ("".equals(arr[i])) {
						for (int j = 0; j < (IPV6Length - arr.length + 1); j++) {
							arrParams[indexFlag] = "0000";
							indexFlag++;
						}
					} else {
						arrParams[indexFlag] = arr[i];
						indexFlag++;
					}
				}
			}
			for (int i = 0; i < IPV6Length; i++) {
				for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {
					Key += "0";
				}
				Key += arrParams[i];
			}
		}
		return Key;
	}

	/**
	 * 十六進制串轉化为IP地址
	 * 
	 * @param key
	 * @return
	 */
	public static String splitKey(String key) {
		String IPV6Address = "";
		String IPAddress = "";
		String strKey = "";
		String ip1 = key.substring(0, 24);
		String tIP1 = ip1.replace("0000", "").trim();
		if (!"".equals(tIP1) && !"FFFF".equals(tIP1)) {
			// 將ip按：分隔
			while (!"".equals(key)) {
				strKey = key.substring(0, 4);
				key = key.substring(4);
				if ("".equals(IPV6Address)) {
					IPV6Address = strKey;
				} else {
					IPV6Address += ":" + strKey;
				}
			}
			IPAddress = IPV6Address;
		}
		return IPAddress;
	}

	/**
	 * 將ip地址都轉成16個字節的數組。先將v6地址存以":"分隔存放到數組中，再將數組中的每兩位取存到長度为16的字符串數組中，
	 * 再將這兩位十六進制數轉成十進制，再轉成byte類型存放到16個字的數組中。
	 * 
	 * @param ip
	 * @return
	 */
	public static byte[] toByte(String ip) {
		// 將ip地址轉換成16進制
		String Key = buildKey(ip);
		// 將16進制轉換成ip地址
		String ip6 = splitKey(Key);

		// 將v6f地址存以":"分隔存放到數組中
		String[] ip6Str = ip6.split(":");
		String[] ipStr = new String[16];
		byte[] ip6Byte = new byte[16];

		// 將數組中的每兩位取存到長度为16的字符串數組中
		for (int j = 0, i = 0; i < ip6Str.length; j = j + 2, i++) {
			ipStr[j] = ip6Str[i].substring(0, 2);
			ipStr[j + 1] = ip6Str[i].substring(2, 4);
		}
		


		// 將ipStr中的十六進制數轉成十進制，再轉成byte類型存放到16個字的數組中
		for (int i = 0; i < ip6Byte.length; i++) {
			ip6Byte[i] = (byte) Integer.parseInt(ipStr[i], 16);
		}
		return ip6Byte;
	}

}
