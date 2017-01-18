package com.hueandme.sfeer.sfeerconfig;

/**
 * Created by Tobi on 18-Jan-17.
 */

public class EmotionSetting extends SfeerSetting {

    public EmotionSetting(){
        colors.put("happy", new int[]{ 50, 50, 0});
        colors.put("comfort", new int[]{ 0, 50, 0});
        colors.put("peaceful", new int[]{ 0, 0, 50});
        colors.put("optimistic", new int[]{ 75, 25, 0});
        colors.put("inspired", new int[]{ 50, 0, 50});
    }

    public int[] getHappy(){
        return colors.get("happy");
    }

    public int[] getComfort(){
        return colors.get("comfort");
    }

    public int[] getPeaceful(){
        return colors.get("peaceful");
    }

    public int[] getOptimistic(){
        return colors.get("optimistic");
    }

    public int[] getInspired(){
        return colors.get("inspired");
    }

}
