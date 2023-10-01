package com.example.artvswar.util;

import org.springframework.stereotype.Component;

@Component
public class RatioHelper {

    public double getTransformedRatio(double defaultRatio) {
        double transformedRatio = 0;
        if (defaultRatio < 0.625) {
            transformedRatio = 0.5;
        } else if (defaultRatio >= 0.625 && defaultRatio < 0.875) {
            transformedRatio = 0.75;
        } else if (defaultRatio >= 0.875 && defaultRatio < 1.125) {
            transformedRatio = 1;
        } else if (defaultRatio >= 1.125 && defaultRatio < 1.375) {
            transformedRatio = 1.25;
        } else if (defaultRatio >= 1.375 && defaultRatio < 1.625) {
            transformedRatio = 1.5;
        }
        else if (defaultRatio >= 1.625 && defaultRatio < 1.875) {
            transformedRatio = 1.75;
        }
        else if(defaultRatio >= 1.875) {
            transformedRatio = 2.0;
        }
        return transformedRatio;
    }
}
