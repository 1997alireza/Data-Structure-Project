package com.searchProject;

/**
 * Created by AliReza on 16/12/2016.
 */
public class Tools {
    public static boolean isBigger(String s1, String s2){
        char [] c1 = s1.toCharArray(), c2 = s2.toCharArray();
        for(int i = 0; i < c1.length && i < c2.length; i++){
            if(c1[i] < c2[i])
                return false;
            if(c1[i] > c2[i])
                return true;
        }

        if(c1.length < c2.length)
            return false;

        return true;
    }
}
