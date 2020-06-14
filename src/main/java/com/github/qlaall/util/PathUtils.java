package com.github.qlaall.util;

import com.github.qlaall.entity.PathNodeEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PathUtils {
    /**
     * [/game/a]->[/game,/game/a]
     * @param path
     * @return
     */
    public static List<PathNodeEntity> split(String path){
        String[] split = StringUtils.split(path, "/");
        String lastString="/";
        List<PathNodeEntity> r=new ArrayList<>(split.length);
        for (int i = 0; i < split.length; i++) {
            String thisS=split[i];
           PathNodeEntity pne=new PathNodeEntity();
           pne.setParentPath(lastString);
           pne.setName(thisS);
           pne.setFullPathName(lastString+thisS);
           r.add(pne);

           lastString+=thisS+"/";
        }
        return r;
    }

}
