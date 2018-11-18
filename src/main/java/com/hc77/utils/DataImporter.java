package com.hc77.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc77.utils.INewObjectObserver;
import com.thinkgem.jeesite.modules.wx.listenner.MyContextRefreshedListener;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class DataImporter<T> {
    private String strImportPath, strNamePrefix;
    private INewObjectObserver<T> objObserver;
    // 目录扫描间隔：10秒
    private final long interval = 10000;
    private Timer scanTimer;
    private final Class<T> classOfT;

    //	Logger logger = LoggerFactory.getLogger(com.hc77.utils.DataImporter.class);
    private Logger logger = Logger.getLogger(DataImporter.class);

    public DataImporter(String strImportPath, String strNamePrefix, INewObjectObserver<T> objObserver, Class<T> classOfT) {
        this.strImportPath = strImportPath;
        this.strNamePrefix = strNamePrefix;
        this.objObserver = objObserver;
        this.classOfT = classOfT;
    }

    public void init() {

        scanTimer = new Timer();
        scanTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                WatchImportDir();
            }
        }, interval, interval);
        logger.info("DataImporter开始扫描，文件夹：" + strImportPath);
    }

    public void deinit() {
        scanTimer.cancel();
    }

    public void WatchImportDir() {
        File fileImportDir = new File(strImportPath);
        FileOutputStream out = null;

        try {
            logger.info("扫描文件夹：" + strImportPath + "……");
            File[] watchedFiles = fileImportDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String fileName) {
                    if (fileName.startsWith(strNamePrefix + "-"))
                        return true;
                    else
                        return false;
                }

			});

			if (watchedFiles != null ) {
				// 首先处理修改时间最早的文件
				Arrays.sort(watchedFiles, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

                for (File watchedFile : watchedFiles) {
                    // 先尝试lock文件，lock成功后再继续处理、防止文件写入中途误处理
                    out = new FileOutputStream(watchedFile, true/*必须用append模式打开，不然文件会被清空*/);
                    out.getChannel().tryLock().close();
                    out.close();
                    // 在history和error文件夹查找文件是否已处理过（error文件夹存在的文件，也视作处理过；如果一个文件处理失败、
                    // 移至error文件夹，应设法修正错误后挪出来重新处理（挪出来之前不能重新处理））
                    String nameWatchedFile = watchedFile.getName();

                    Path pathInHistory = Paths.get(strImportPath, "history", nameWatchedFile),
                            pathInError = Paths.get(strImportPath, "error", nameWatchedFile);
                    if (pathInHistory.toFile().exists() // 文件在history文件夹存在
                            || pathInError.toFile().exists()) { // 文件在error文件夹存在
                        // 打印日志，跳过当前文件
                        logger.warn("文件" + nameWatchedFile + "之前已处理过，此次不会处理、将移至repeat文件夹");
                        File dirRepeat = Paths.get(strImportPath, "repeat").toFile();
                        if (!dirRepeat.exists())
                            dirRepeat.mkdirs();
                        File[] filesInRepeat = dirRepeat.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String fileName) {
                                if (fileName.startsWith(nameWatchedFile))
                                    return true;
                                else
                                    return false;
                            }
                        });
                        File fileInRepeat = null;
                        if (filesInRepeat.length > 0) // repeat文件夹也有同名文件，则增加后缀
                            fileInRepeat = Paths.get(dirRepeat.getAbsolutePath(), nameWatchedFile + "-" + filesInRepeat.length).toFile();
                        else
                            fileInRepeat = Paths.get(dirRepeat.getAbsolutePath(), nameWatchedFile).toFile();
                        if (!watchedFile.renameTo(fileInRepeat)) {
                            logger.error("将文件" + nameWatchedFile + "移动至repeat目录时发生错误");
                        }

                        continue;
                    }

                    ObjectMapper objMapper = new ObjectMapper();
                    boolean bSuccess = false;
                    try {
                        T obj = objMapper.readValue(watchedFile, classOfT);
                        bSuccess = objObserver.onNewObjectArrived(obj);
                    } catch (Exception e) {
                        logger.error("dataimport", e);
                        e.printStackTrace();
                    } finally {
                        if (bSuccess) {
                            // 处理成功，将文件移至history目录
                            if (!pathInHistory.getParent().toFile().exists())
                                pathInHistory.getParent().toFile().mkdirs();
                            if (!watchedFile.renameTo(pathInHistory.toFile())) {
                                logger.error("将文件" + nameWatchedFile + "移动至history目录时发生错误");
                            }
                        } else {
                            // 处理失败，将文件移至error目录
                            if (!pathInError.getParent().toFile().exists())
                                pathInError.getParent().toFile().mkdirs();
                            if (!watchedFile.renameTo(pathInError.toFile())) {
                                logger.error("将文件" + nameWatchedFile + "移动至error目录时发生错误");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}