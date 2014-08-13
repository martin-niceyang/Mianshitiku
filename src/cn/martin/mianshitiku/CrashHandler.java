package cn.martin.mianshitiku;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类 来接管程序,并记录 发送错误报告.
 */
public class CrashHandler implements UncaughtExceptionHandler
{

	private static Logger log = Logger.getLogger(CrashHandler.class);
	
	
    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /** CrashHandler实例 */
    private static CrashHandler INSTANCE;

    /** 程序的Context对象 */
    private Context mContext;

    /** 使用Properties来保存设备的信息和错误堆栈信息 */
    private final Properties mDeviceCrashInfo = new Properties();

    private static final String VERSION_NAME = "versionName";

    private static final String VERSION_CODE = "versionCode";

    private static final String STACK_TRACE = "STACK_TRACE";

    /** 错误报告文件的扩展名 */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler()
    {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     * 
     * @param ctx
     */
    public void init(Context ctx)
    {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
        else
        {
        	if(mDefaultHandler != null){
        		mDefaultHandler.uncaughtException(thread, ex);
        	}
             //Sleep一会后结束程序
//             try {
//             Thread.sleep(3000);
//             } catch (InterruptedException e) {
//             e.printStackTrace();
//             }
//             android.os.Process.killProcess(android.os.Process.myPid());
//             System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     * 
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex)
    {
        if (ex == null)
        {
            return true;
        }

        ex.printStackTrace();

        final String msg = ex.getLocalizedMessage();

        // 收集设备信息
        collectCrashDeviceInfo(mContext);

        // 保存错误报告文件
        String crashFileName = saveCrashInfoToFile(ex);

        return true;
    }

    /**
     * 获取错误报告文件名
     * 
     * @param ctx
     * @return
     */
    private String[] getCrashReportFiles(Context ctx)
    {
    	File filesDir = ctx.getFilesDir();
    	
        FilenameFilter filter = new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return filesDir.list(filter);
    }

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

    /**
     * 保存错误信息到文件中
     * 
     * @param ex
     * @return
     */
    private String saveCrashInfoToFile(Throwable ex)
    {
        removeCache();

        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null)
        {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        mDeviceCrashInfo.put(STACK_TRACE, result);

        try
        {
            // long timestamp = System.currentTimeMillis();
            String fileName = "crash-" + format.format(new Date()) + CRASH_REPORTER_EXTENSION;

            FileOutputStream trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);

            mDeviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
            return fileName;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 收集程序崩溃的设备信息
     * 
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx)
    {
        try
        {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null)
            {
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode + "");
            }
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }

        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 具体信息请参考后面的截图
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null) + "");

                log.debug(field.getName() + " : " + field.get(null));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static final int FILE_NUM = 10;

    /**
     * 删除日志文件，控制在10个
     * 
     * @param dirPath
     */
    private void removeCache()
    {

        String[] crFiles = getCrashReportFiles(mContext);

        if (crFiles == null || crFiles.length == 0)
        {
            return;
        }

        if (crFiles.length > FILE_NUM)
        {

            int removeFactor = crFiles.length - FILE_NUM;

            File[] files = new File[crFiles.length];
            for (int i = 0; i < crFiles.length; i++)
            {
                files[i] = new File(mContext.getFilesDir(), crFiles[i]);
            }

            Arrays.sort(files, new FileLastModifSort());

            for (int i = 0; i < removeFactor; i++)
            {
                if (files[i].isFile())
                {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 根据文件的最后修改时间进行排序 *
     */
    private class FileLastModifSort implements Comparator<File>
    {
        @Override
        public int compare(File arg0, File arg1)
        {
            if (arg0.lastModified() > arg1.lastModified())
            {
                return 1;
            }
            else if (arg0.lastModified() == arg1.lastModified())
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
    }

}
