package com.zhaopeng.demo.provider;

import com.zhaopeng.demo.api.StoreService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhaopeng on 2016/11/15.
 */
public class StoreServiceImpl implements StoreService {
    @Override
    public String getAllStore(String input) {
        System.out.println("被调用了 ！");
        String result="provider is invoking";
        return result;
    }

    public static  void main(String args[]){

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
           System.out.println(cal.getTime());

    }
}
