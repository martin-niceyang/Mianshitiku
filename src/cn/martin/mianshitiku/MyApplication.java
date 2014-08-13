package cn.martin.mianshitiku;

import org.apache.log4j.Level;
import android.app.Application;
import com.avos.avoscloud.AVOSCloud;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		AVOSCloud.initialize(this,
				"91h3klynk9n62z9ztpovt26mo3jy6iaofz15okfru2kxi65j",
				"pyvpr07nfk7mmy0t552g8an3idy955fz70z4a5xwkzd0mttx");
		
		//------ 处理全局异常变量,记录到应用目录的files目录下
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	/**
	 * log4j 的配置（暂时没有配置）
	 * 
	 * log4j的path只能创建文件，所以制定文件所在的目录一定要我们自己创建，log4j不会自己创建目录
	 * 
	 */
	private void configureLog4j(){
		LogConfigurator configurator = new LogConfigurator();
		String path = "";//you need to set this path to write log
		configurator.setFileName(path);
		configurator.setRootLevel(Level.DEBUG);
		configurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
		configurator.setMaxFileSize(1024*1024 * 1);
		configurator.setMaxBackupSize(10);
		configurator.setImmediateFlush(true);
		configurator.configure();
	}
	
}
