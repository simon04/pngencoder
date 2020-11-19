package com.pngencoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;

public class PngEncoder {
    public static int DEFAULT_COMPRESSION_LEVEL = Deflater.BEST_COMPRESSION;

    private final BufferedImage bufferedImage;
    private final int compressionLevel;
    private final boolean multiThreadedCompressionEnabled;
    private final PngEncoderSrgbRenderingIntent srgbRenderingIntent;
    private final PngEncoderPhysicalPixelDimensions physicalPixelDimensions;

    private PngEncoder(
            BufferedImage bufferedImage,
            int compressionLevel,
            boolean multiThreadedCompressionEnabled,
            PngEncoderSrgbRenderingIntent srgbRenderingIntent,
            PngEncoderPhysicalPixelDimensions physicalPixelDimensions) {
        this.bufferedImage = bufferedImage;
        this.compressionLevel = PngEncoderVerificationUtil.verifyCompressionLevel(compressionLevel);
        this.multiThreadedCompressionEnabled = multiThreadedCompressionEnabled;
        this.srgbRenderingIntent = srgbRenderingIntent;
        this.physicalPixelDimensions = physicalPixelDimensions;
    }

    public PngEncoder() {
        this(
                null,
                DEFAULT_COMPRESSION_LEVEL,
                true,
                null,
                null);
    }

    public PngEncoder withBufferedImage(BufferedImage bufferedImage) {
        return new PngEncoder(bufferedImage, compressionLevel, multiThreadedCompressionEnabled, srgbRenderingIntent, physicalPixelDimensions);
    }

    public PngEncoder withCompressionLevel(int compressionLevel) {
        return new PngEncoder(bufferedImage, compressionLevel, multiThreadedCompressionEnabled, srgbRenderingIntent, physicalPixelDimensions);
    }

    public PngEncoder withMultiThreadedCompressionEnabled(boolean multiThreadedCompressionEnabled) {
        return new PngEncoder(bufferedImage, compressionLevel, multiThreadedCompressionEnabled, srgbRenderingIntent, physicalPixelDimensions);
    }

    public PngEncoder withSrgbRenderingIntent(PngEncoderSrgbRenderingIntent srgbRenderingIntent) {
        return new PngEncoder(bufferedImage, compressionLevel, multiThreadedCompressionEnabled, srgbRenderingIntent, physicalPixelDimensions);
    }

    public PngEncoder withPhysicalPixelDimensions(PngEncoderPhysicalPixelDimensions physicalPixelDimensions) {
        return new PngEncoder(bufferedImage, compressionLevel, multiThreadedCompressionEnabled, srgbRenderingIntent, physicalPixelDimensions);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public boolean isMultiThreadedCompressionEnabled() {
        return multiThreadedCompressionEnabled;
    }

    public PngEncoderSrgbRenderingIntent getSrgbRenderingIntent() {
        return srgbRenderingIntent;
    }

    public int toStream(OutputStream outputStream) {
        try {
            return PngEncoderLogic.encode(bufferedImage, outputStream, compressionLevel, multiThreadedCompressionEnabled, srgbRenderingIntent, physicalPixelDimensions);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public int toFile(Path filePath) {
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            return toStream(outputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public int toFile(File file) {
        return toFile(file.toPath());
    }

    public int toFile(String fileName) {
        return toFile(Paths.get(fileName));
    }

    public byte[] toBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(64 * 1024);
        toStream(outputStream);
        return outputStream.toByteArray();
    }
}
