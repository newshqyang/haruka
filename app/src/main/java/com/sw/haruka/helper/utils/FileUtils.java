package com.sw.haruka.helper.utils;

import android.os.Environment;
import android.os.StatFs;

import com.sw.haruka.R;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.dal.Namespace;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileUtils {

    /*
    获取当前所在文件夹的名称
     */
    public static String getNameFromPath(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path.split("/")[path.split("/").length - 1];
    }

    /*
    创建文件
     */
    public static File createFile(String pathAndName) {
        File file = new File(pathAndName);
        if (file.exists()) {
            if (!file.delete()) {
                return null;
            }
        }
        try {
            if (file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    移动文件或者文件夹
     */
    public static boolean moveFile(String movedFilePath, String moveToPath, Set<String> paths) {
        File file = new File(movedFilePath);
        if (!file.exists()) {
            return false;
        }
        paths.add(file.getAbsolutePath());
        if (file.isDirectory()) {
            File[] fileArray = file.listFiles();
            if (fileArray != null) {
                for (File f : fileArray) {
                    moveFile(f.getAbsolutePath(), moveToPath + "/" + file.getName(), paths);
                }
            }
        }
        paths.add(moveToPath + "/" + file.getName());
        return file.renameTo(new File(moveToPath + "/" + file.getName()));
    }

    /*
    检测文件的新名字是否在当前目录下没有重名
     */
    public static boolean isRepeatName(String directoryPath, String fileName) {
        File[] sameDirectoryFileArray = new File(directoryPath).listFiles();
        boolean sameName = false;
        if (sameDirectoryFileArray != null) {
            for (File f : sameDirectoryFileArray) {
                if (f.getName().equals(fileName)) {
                    sameName = true;
                }
            }
        }
        return sameName;
    }

    /*
    删除文件或文件夹
     */
    public static boolean deleteFile(String path, Set<String> paths) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        paths.add(file.getAbsolutePath());
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    deleteFile(f.getAbsolutePath(), paths);
                }
            }
        }
        HarukaConfig.PATH_MAP.remove(file.getAbsolutePath());
        return file.delete();
    }

    /*
    获取文件路径中最后一个文件夹或者文件的名称
     */
    public static String getLastFolderOrFileName(String path) {
        String[] fileList = path.split("/");
        return fileList[fileList.length - 1];
    }

    /*
    获取文件后缀名
     */
    public static String getSuffix(String fileName) {
        String[] arr = fileName.split("\\.");
        return arr[arr.length - 1];
    }

    /*
    创建文件夹
     */
    public static boolean createNewFolder(String folderPath) {
        boolean r = false;
        File file = new File(folderPath);
        if (!file.exists()) {
            r = file.mkdirs();
        }
        return r;
    }

    /*
    获取格式化后的大小，字符串
     */
    private static final long GB_SIZE = 1073741824L;
    private static final long MB_SIZE = 1048576L;
    private static final long KB_SIZE = 1024L;
    public static String getFormatSize(long size) {
        if (size > GB_SIZE) {      // 如果超过 1 G
            return (getNumberWithDecimalPoint((double)size / GB_SIZE, 1)) + "GB";
        } else if (size > MB_SIZE) {
            return (getNumberWithDecimalPoint((double)size / MB_SIZE, 1)) + "MB";
        } else if (size > KB_SIZE) {
            return (getNumberWithDecimalPoint((double)size / KB_SIZE, 1)) + "KB";
        } else {
            return size + "B";
        }
    }

    /*
    获取小数点后几位
     */
    public static String getNumberWithDecimalPoint(double number, int theLastFew) {
        return ("" + number).substring(0, ("" + number).indexOf(".") + theLastFew + 1);
    }

    public static boolean isImg(String type) {
        boolean r = false;
        switch (type.toLowerCase()) {
            case "jpg":
            case "png":
            case "jpeg":
                r = true;
                break;
            default:
        }
        return r;
    }

    /*
    判断文件类型，返回对应的图片
     */
    public static int getTypeImage(String type) {
        switch (type.toLowerCase()) {
            case "access":
                return R.drawable.access;
            case "apk":
                return R.drawable.apk;
            case "avi":
                return R.drawable.avi;
            case "bmp":
                return R.drawable.bmp;
            case "docx":
                return R.drawable.docx;
            case "excel":
                return R.drawable.excel;
            case "gif":
                return R.drawable.gif;
            case "jpeg":
                return R.drawable.jpeg;
            case "jpg":
                return R.drawable.jpg;
            case "mov":
                return R.drawable.mov;
            case "mp3":
                return R.drawable.mp3;
            case "pdf":
                return R.drawable.pdf;
            case "png":
                return R.drawable.png;
            case "ppt":
                return R.drawable.ppt;
            case "pptx":
                return R.drawable.pptx;
            case "psd":
                return R.drawable.psd;
            case "rar":
                return R.drawable.rar;
            case "md":
            case "readme":
                return R.drawable.readme;
            case "txt":
                return R.drawable.txt;
            case "mp4":
            case "rmvb":
            case "mpeg":
            case "mpg":
            case "ram":
            case "rm":
            case "qt":
            case "asf":
                return R.drawable.video;
            case "wmv":
                return R.drawable.wmv;
            case "doc":
                return R.drawable.word;
            case "xlsx":
                return R.drawable.xlsx;
            case "zip":
                return R.drawable.zip;
            default:
                    return R.drawable.file_icon;
        }
    }

    /*
    获取当前可用空间，单位byte
     */
    public static long getUsefulSize() {
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return sf.getBlockSizeLong() * sf.getAvailableBlocksLong();
    }

    /*
    获取内部存储设备总共空间，单位byte
     */
    public static long getTotalSize() {
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return sf.getBlockSizeLong() * sf.getBlockCountLong();
    }

    /*
    文件排序
     */
    public static void sortFileByName(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());    // 无视大小写差别排序
            }
        });
    }


    /*
    获取路径下的所有文件
     */
    public static List<File> listAllFiles(String path) {
        File[] files = new File(path).listFiles();
        if (files == null) {
            return null;
        }
        return new ArrayList<>(Arrays.asList(files));
    }

    public static String getFileUpdateTime(File f) {
        Calendar calendar = Calendar.getInstance(); // 获取文件修改日期
        long time = f.lastModified();
        calendar.setTimeInMillis(time);
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return dateFormat.format(calendar.getTime());
    }

    /*
    搜索文件或文件夹
     */
    public static boolean isSearching = false;  // 正在搜索的标志
    public static List<File> searchFile(String fileName, HashSet<String> pathMap) {
        isSearching = true;
        List<File> foundList = new ArrayList<>();
        for (String path : pathMap) {
            if (!isSearching) {
                System.out.println("嗯否");
                break;
            }
            if (path.split("/")[path.split("/").length - 1].toLowerCase()
                    .contains(fileName.toLowerCase())) {
                foundList.add(new File(path));
            }
        }
        isSearching = false;
        return foundList;
    }

    public static void searchFileOrFolder(String path, HashSet<String> foundList) {
        File fileDir = new File(path);
        File[] files = fileDir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!SPUtils.getBooleanValueDefaultFalse(Namespace.SHOW_HIDDEN_FILE_OFFSET)) {
                    if (f.getName().startsWith(".")) {
                        continue;
                    }
                }
                foundList.add(f.getAbsolutePath());
                // 如果是文件夹 继续读取
                if (f.isDirectory()) {
                    searchFileOrFolder(f.getAbsolutePath(), foundList);
                }
            }
        }
    }

    public static Thread scanningThread;    // 根据扫描线程是否为空，判断扫描是否完成
    public static void scan() {
        if (scanningThread != null) {
            return;
        }
        scanningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                isSearching = false;
                searchFileOrFolder(HarukaConfig.ROOT_PATH, HarukaConfig.PATH_MAP);
                scanningThread = null;
            }
        });
        scanningThread.start();
    }
}
