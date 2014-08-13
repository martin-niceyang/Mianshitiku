package cn.martin.mianshitiku.util;

public class U_DateUtil {

	/**
	 * long型数据转换成 时 分 秒
	 * 
	 * @param mss
	 * @return
	 */
	public static String formatDuring(long mss) {
		long hours = mss / (60 * 60);
		long minutes = mss / 60;
		if (minutes > 60) {
			minutes = minutes % 60;
		}
		long seconds = (mss % 60);
		if (hours > 0) {
			return hours + "	小时	" + minutes + "	分钟	" + seconds + "	秒	";
		} else {
			return minutes + "	分钟	" + seconds + "	秒	";
		}
	}
	
}
