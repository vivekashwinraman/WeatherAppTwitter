package com.twitter.challenge.utils;

import java.util.List;

/**
 * Created by edk763 on 9/16/17.
 */

public class StandardDevCalculator {
    public static float calculate(List<Float> data) {
        return (float)Math.sqrt(getVariance(data));
    }

        private static float getMean(List<Float> data) {
            float sum = 0.0f;
            for(float a : data)
                sum += a;
            return sum/data.size();
        }

        private static float getVariance(List<Float> data)
        {
            float mean = getMean( data);
            float temp = 0f;
            for(float a : data)
                temp += (a - mean)*( a - mean);
            return temp/(data.size()-1);
        }

}
