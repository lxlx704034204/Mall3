package com.hxqc.mall.reactnative.model;

import com.hxqc.util.DebugLog;

import java.io.File;

/**
 * Author:  wh
 * Date:  2016/8/31
 * FIXME
 * Todo  解压完成后   补丁或完整文件路径
 */
public class UnZipFileModel {

    private String DirectoryPath;
    private String patchFilePath = "" ;

    private boolean hasFile = false;


    public UnZipFileModel(String directoryPath) {
        DirectoryPath = directoryPath;
        initFiles();
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public String getPatchFilePath() {
        return patchFilePath;
    }

    private void initFiles(){
        File file=new File(this.DirectoryPath);
        File[] tempList = file.listFiles();
        DebugLog.i("Tag","该目录下对象个数："+tempList.length);
        if (tempList[0].isFile()){
            if (tempList[0].length()>0){
                this.patchFilePath = tempList[0].getAbsolutePath();
                this.hasFile = true;
            }
        }

        for (File aTempList : tempList) {
            if (aTempList.isFile()) {
                DebugLog.i("Tag","文     件：" + aTempList.getAbsolutePath());
            }
            if (aTempList.isDirectory()) {
                DebugLog.i("Tag","文件夹：" + aTempList);
            }
        }
    }
}
