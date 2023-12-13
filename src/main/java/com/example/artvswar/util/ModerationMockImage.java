package com.example.artvswar.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ModerationMockImage {
    public static final String PENDING_URL = "https://res.cloudinary.com/dq415fvzp/image/upload/"
            + "v1700416227/art-app/service/moderation_images/pending.jpg";
    public static final String REJECTED_URL = "https://res.cloudinary.com/dq415fvzp/image/upload"
            + "/v1700416248/art-app/service/moderation_images/rejected.jpg";

    public static final double IMAGE_WIDTH = 600;
    public static final double IMAGE_HEIGHT = 600;
}
