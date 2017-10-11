package com.tohier.cartercoin.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.widget.GridView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Superman on 2016/9/9.
 */
public class Tools {


    private static List<Long> times = new ArrayList<Long>();

    public static String getRandomCount(int max, int min){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s+"";
    }


    /**
     * 返回顶部
     * @param gridView
     */
    public static void scrollToListviewTop(final GridView gridView)
    {
        gridView.smoothScrollToPosition(0);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (gridView.getFirstVisiblePosition() > 0)
                {
                    gridView.setSelection(0);
                }
            }
        }, 100);
    }

    public static boolean isPhonticName(String str) {
        char[] chars=str.toCharArray();
        boolean isPhontic = false;
        for(int i = 0; i < chars.length; i++) {
            isPhontic = (chars[i] >= 'a' && chars[i] <= 'z') || (chars[i] >= 'A' && chars[i] <= 'Z');
            if (!isPhontic) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回顶部
     * @param listView
     */
    public static void scrollToListviewTop(final ListView listView)
    {
        listView.smoothScrollToPosition(0);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (listView.getFirstVisiblePosition() > 0)
                {
                    listView.setSelection(0);
                }
            }
        }, 100);
    }



    public static void shuangji(final GridView gridView){
        times.add(SystemClock.uptimeMillis());
        if (times.size() == 2) {
            //已经完成了一次双击，list可以清空了
            if (times.get(times.size()-1)-times.get(0) < 500) {
                times.clear();
                scrollToListviewTop(gridView);

            } else {
                //这种情况下，第一次点击的时间已经没有用处了，第二次就是“第一次”
                times.remove(0);
            }
        }
    }

    public static void shuangji(final ListView listView){
        times.add(SystemClock.uptimeMillis());
        if (times.size() == 2) {
            //已经完成了一次双击，list可以清空了
            if (times.get(times.size()-1)-times.get(0) < 500) {
                times.clear();
                scrollToListviewTop(listView);

            } else {
                //这种情况下，第一次点击的时间已经没有用处了，第二次就是“第一次”
                times.remove(0);
            }
        }
    }


    public static String formatTime(int s){
        int s1=s%60;
        int m=s/60;
        int m1=m%60;
        int h=m/60;
        return (h>10?(h+""):("0"+h))+":"+(m1>10?(m1+""):("0"+m1))+":"+(s1>10?(s1+""):("0"+s1));
    }

    /*
    * 将时间转换为时间戳
    */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }


    /*
* 将时间戳转换为时间
*/
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt*1000);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 获取当前的网络状态 ：false没有网络,true 有网络
     *
     * @param context
     * @return
     */
    public static boolean getAPNType(Context context) {
        boolean isLink = false;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return isLink;
        }

        return true;
    }


    /**
     * 获取本机手机号
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)  context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }

}
