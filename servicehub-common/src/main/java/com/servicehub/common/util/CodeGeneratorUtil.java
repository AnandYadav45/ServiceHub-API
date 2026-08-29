package com.servicehub.common.util;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class CodeGeneratorUtil {
    private CodeGeneratorUtil(){}

    public static String generate(String prefix){
        long timestampPart = Instant.now().toEpochMilli();
        int randomPart = ThreadLocalRandom.current().nextInt(100, 999);
        return String.format("%s-%06d%d", prefix,timestampPart,randomPart);
    }
}
