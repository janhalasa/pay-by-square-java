package io.github.janhalasa.paybysquare.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.github.janhalasa.paybysquare.model.PayBySquareDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class PayBySquareGenerator {

    private final PayBySquareSerializer serializer = new PayBySquareSerializer();
    private final PayBySquareEncoder encoder = new PayBySquareEncoder();

    public String generateString(PayBySquareDocument document) throws IOException {
        String serialized = serializer.serialize(document);
        // Debug
        // System.out.println("Serialized TSV: " + serialized.replace("\t", "[TAB]"));
        return encoder.encode(serialized);
    }

    public byte[] generateQrCode(PayBySquareDocument document, int size) throws IOException, WriterException {
        String code = generateString(document);
        return generateQrCodeFromCode(code, size);
    }

    public byte[] generateQrCodeFromCode(String code, int size) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // Content is Base32, so ASCII, but UTF-8 is safe default
        hints.put(EncodeHintType.MARGIN, 0); // Optional: Minimal margin

        BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, size, size, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
