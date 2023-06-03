package com.example.artvswar.util;

import org.springframework.stereotype.Component;

@Component
public class RatioHelper {

    public double getTransformedRatio(double defaultRatio) {
        double transformedRatio = 0;
        if (defaultRatio < 0.75) {
            transformedRatio = 0.5;
        } else if (defaultRatio >= 0.75 && defaultRatio < 1.25) {
            transformedRatio = 1;
        } else if (defaultRatio >= 1.25 && defaultRatio < 1.75) {
            transformedRatio = 1.5;
        } else if (defaultRatio >= 1.75) {
            transformedRatio = 2;
        }
        return transformedRatio;
    }
}
