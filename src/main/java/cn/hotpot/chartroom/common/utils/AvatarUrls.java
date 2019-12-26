package cn.hotpot.chartroom.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
public class AvatarUrls {
    private static List<String> list = new ArrayList<>();

    static {
        list.add("/img/avatar/Member001.jpg");
        list.add("/img/avatar/Member002.jpg");
        list.add("/img/avatar/Member003.jpg");
        list.add("/img/avatar/Member004.jpg");
        list.add("/img/avatar/Member005.jpg");
        list.add("/img/avatar/Member006.jpg");
        list.add("/img/avatar/Member007.jpg");
        list.add("/img/avatar/Member008.jpg");
        list.add("/img/avatar/Member009.jpg");
    }

    public static String getAny() {
        int i = new Random().nextInt(list.size());
        return "/cr/" + list.get(i);
    }
}
