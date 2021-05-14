package com.qrlo.qrloservicecore.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-09
 */
@Service
public class QrCodeService {
    private static final Map<EncodeHintType,?> DEFAULT_HINT = Map.of(EncodeHintType.CHARACTER_SET, "UTF-8");
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private final MultiFormatWriter multiFormatWriter;

    public QrCodeService(MultiFormatWriter multiFormatWriter) {
        this.multiFormatWriter = multiFormatWriter;
    }

    public Mono<byte[]> generateQrCode(String content) {
        return generateQrCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Mono<byte[]> generateQrCode(String content, int width, int height) {
        return Mono.create(sink -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, DEFAULT_HINT);
                MatrixToImageWriter.writeToStream(bitMatrix, MediaType.IMAGE_PNG.getSubtype(), baos, new MatrixToImageConfig());
                sink.success(baos.toByteArray());
            } catch (IOException | WriterException e) {
                sink.error(e);
            }
        });
    }
}
