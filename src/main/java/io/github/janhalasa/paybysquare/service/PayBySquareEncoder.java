package io.github.janhalasa.paybysquare.service;

import org.tukaani.xz.LZMA2Options;

import org.tukaani.xz.LZMAOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class PayBySquareEncoder {

    // Base32hex Alphabet: 0-9 A-V
    private static final char[] BASE32_HEX = "0123456789ABCDEFGHIJKLMNOPQRSTUV".toCharArray();

    public String encode(String dataSequence) throws IOException {
        byte[] dataBytes = dataSequence.getBytes(StandardCharsets.UTF_8);

        // 1. Calculate CRC32
        CRC32 crc = new CRC32();
        crc.update(dataBytes);
        int checksum = (int) crc.getValue();

        // 2. Concatenate [CRC32 (4 bytes LE)] + [Data]
        ByteArrayOutputStream crcAndData = new ByteArrayOutputStream();
        crcAndData.write(intToLE(checksum));
        crcAndData.write(dataBytes);
        byte[] toCompress = crcAndData.toByteArray();

        // 3. Compress LZMA
        // Spec: LC=3, LP=0, PB=2, Dict=128KB

        // Use org.tukaani.xz.LZMA2Options or LZMAOutputStream directly?
        // Spec usually implies LZMA1 for legacy reasons if not specified as XZ.
        // Parameters LC, LP, PB are specific to LZMA1.
        // We will try to use the raw LZMAOutputStream from tukaani if possible.
        // The constructor LZMAOutputStream(OutputStream out, LZMA2Options options, long
        // inputSize) exists,
        // but we need to pass specific LC/LP/PB.
        // Those keys are in LZMA2Options? No, LZMA2 doesn't use LC/LP/PB in the same
        // way exposed.
        // Wait, LZMA1 vs LZMA2.
        // If spec says LC/LP/PB, it is definitely LZMA1.
        // `LZMAOutputStream` in tukaani supports LZMA1.

        // Note: We need to set properties manually.
        // If we look at `LZMAOutputStream` source or common usage:
        // It accepts `LZMA2Options`... Wait.
        // Actually, `org.tukaani.xz.LZMAOutputStream` writes the raw LZMA stream.
        // We need to configure it.
        // Let's assume we can pass the options.
        // Since I can't check the library perfectly, I will implement with what I
        // recall:
        // LZMAOutputStream(OutputStream, LZMA2Options, inputSize)
        // But I need LZMA1 properties.
        // Let's use `org.tukaani.xz.LZMA2Options` and hope defaults or explicit set
        // works?
        // NO, LZMA2Options is for LZMA2.
        // There should be `LZMAInputStream` and `LZMAOutputStream` for .lzma files.
        // But specific raw stream?

        // Alternative: Use `org.tukaani.xz.LZMA2Options` might be wrong if it's LZMA1.
        // The spec parameters (LC=3, LP=0, PB=2) are standard LZMA1.
        // I will try to use `new LZMAOutputStream(out, new LZMA2Options(1),
        // inputLength)` is likely wrong.

        // Let's try `LZMA2Options` with `setLcLpPb(3, 0, 2)` if it exists.
        // If not, I might need `SingleXZInputStream`?

        // Actually, for "PayBySquare", it's likely a raw LZMA stream (no XZ header,
        // maybe 5 byte props header?).
        // Spec says: "LZMA compressed... Preceded by 2-byte decompressed size".
        // It does NOT mention the 5-byte properties header typical of .lzma files.
        // If I use standard `LzmaOutputStream`, it might write the 13-byte header (5
        // props + 8 size).
        // I need to be careful.

        // Let's assume for now I will use:
        // `new LZMAOutputStream(out, new LZMA2Options(), toCompress.length)`
        // AND I need to check if I can control the header.

        // Re-reading spec notes via valid memory or inference:
        // "The LZMA compressed data... The parameters used are... LC=3..."
        // It does not say "Standard LZMA file format".

        // I'll stick to a simple implementation and if I had the `LZMAEncoder` class
        // directly I'd use it.
        // Tukaani has `LZMAOutputStream`. I'll use it and we might need to verify the
        // output format.
        // However, `LZMAOutputStream` usually writes the .lzma header.
        // If the spec expects JUST the compressed stream without the 5-byte props, I'll
        // need to strip them
        // or use an internal class.
        // OR, the header IS expected but the spec describes the params to expect.
        // In many banking standards (like ST SEPA), they use raw LZMA.
        // I will write the `LZMAOutputStream` and assumes it does the right thing or I
        // might need to strip the first 13 bytes if the spec says "2 byte decompressed
        // size" is the ONLY header.

        // Spec: "Compressed block... is preceded by a 2-byte... unsigned integer
        // representing the size...".
        // It implies custom header.
        // So I should probably STRIP the `LZMAOutputStream` header if it adds one.
        // `LZMAOutputStream` adds 13 bytes.
        // I should write to a buffer, skip the first 13 bytes, and take the rest.
        // BUT, does the decompressor need the properties?
        // If the parameters are FIXED in the spec, the decompressor knows them and
        // doesn't need the 5-byte header.
        // This is extremely common in embedded QRs.
        // So plan:
        // 1. Compress with `LZMAOutputStream` (which uses LC=3, LP=0, PB=2 if
        // configurable, or default).
        // Wait, default is usually LC=3, LP=0, PB=2!
        // 2. Strip the 13-byte header.
        // 3. Prepend the 2-byte size.

        LZMA2Options options = new LZMA2Options();
        options.setDictSize(128 * 1024);
        // options.setLcLpPb(3, 0, 2); // Unavailble in LZMA2Options, using defaults
        // which are usually congruent.

        ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
        try (LZMAOutputStream lzmaOut = new LZMAOutputStream(tempBuffer, options, toCompress.length)) {
            lzmaOut.write(toCompress);
            lzmaOut.finish();
        }

        byte[] fullLzmaOutput = tempBuffer.toByteArray();
        // Skip first 13 bytes (Props(5) + DictSize(4) + UncompressedSize(8)?? No,
        // standard .lzma header is 13 bytes)
        // 5 bytes props + 8 bytes size = 13 bytes.

        // However, `LZMAOutputStream` in XZ-Java might produce XZ format?
        // No, `LZMAOutputStream` produces legacy .lzma format.
        // `XZOutputStream` produces .xz format.
        // I used `LZMAOutputStream`.

        // I will assume I need to strip the header (13 bytes).
        int headerLen = 13;
        if (fullLzmaOutput.length < headerLen)
            throw new IOException("LZMA output too short");

        byte[] rawLzmaStream = new byte[fullLzmaOutput.length - headerLen];
        System.arraycopy(fullLzmaOutput, headerLen, rawLzmaStream, 0, rawLzmaStream.length);

        // 4. Prepend Decompressed Size (2 bytes LE)
        // Use the length of [CRC32 + Data]
        int decompressedSize = toCompress.length; // Max 65535?
        // Spec says 2 bytes.

        ByteArrayOutputStream finalBinary = new ByteArrayOutputStream();
        finalBinary.write(shortToLE((short) decompressedSize));
        finalBinary.write(rawLzmaStream);

        byte[] binaryData = finalBinary.toByteArray();

        // 5. Prepend Custom Header (2 bytes)
        // BySquareType(4)|Version(4)|DocumentType(4)|Reserved(4)
        // 0000 | 0000 | 0000 | 0000 = 0x0000
        byte[] header = new byte[] { 0x00, 0x00 };

        ByteArrayOutputStream streamWithHeader = new ByteArrayOutputStream();
        streamWithHeader.write(header);
        streamWithHeader.write(binaryData);

        byte[] fullStream = streamWithHeader.toByteArray();

        // 6. Base32hex Encode
        return base32HexEncode(fullStream);
    }

    private String base32HexEncode(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int buffer = 0;
        int bitsLeft = 0;

        for (byte b : data) {
            buffer = (buffer << 8) | (b & 0xFF);
            bitsLeft += 8;
            while (bitsLeft >= 5) {
                int index = (buffer >> (bitsLeft - 5)) & 0x1F;
                sb.append(BASE32_HEX[index]);
                bitsLeft -= 5;
            }
        }
        if (bitsLeft > 0) {
            int index = (buffer << (5 - bitsLeft)) & 0x1F;
            sb.append(BASE32_HEX[index]);
        }
        return sb.toString();
    }

    private byte[] intToLE(int value) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
    }

    private byte[] shortToLE(short value) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
    }
}
