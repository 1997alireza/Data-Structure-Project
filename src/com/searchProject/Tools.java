package com.searchProject;

public class Tools {
    public static boolean isBigger(String s1, String s2){
        char [] c1 = s1.toCharArray(), c2 = s2.toCharArray();
        for(int i = 0; i < c1.length && i < c2.length; i++){
            if(c1[i] < c2[i])
                return false;
            if(c1[i] > c2[i])
                return true;
        }

        return c1.length >= c2.length;
    }

    public static int max (int ... a){
        if(a == null || a.length == 0)
            return 0;
        int max = a[0];
        for(int i = 1; i < a.length; i++)
            max = (a[i] > max) ? a[i] : max;
        return max;
    }
}
