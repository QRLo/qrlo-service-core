package com.qrlo.qrloservicecore.config;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-09
 */
@Configuration
public class ZXingConfig {
    @Bean
    public MultiFormatWriter multiFormatWriter() {
        return new MultiFormatWriter();
    }
}
