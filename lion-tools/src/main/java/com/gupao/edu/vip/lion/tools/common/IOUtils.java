
package com.gupao.edu.vip.lion.tools.common;

import com.gupao.edu.vip.lion.api.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 */
public final class IOUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LOGGER.error("close closeable ex", e);
            }
        }
    }

    public static byte[] compress(byte[] data) {

        Profiler.enter("time cost on [compress]");

        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length / 4);
        DeflaterOutputStream zipOut = new DeflaterOutputStream(out);
        try {
            zipOut.write(data);
            zipOut.finish();
            zipOut.close();
        } catch (IOException e) {
            LOGGER.error("compress ex", e);
            return Constants.EMPTY_BYTES;
        } finally {
            close(zipOut);
            Profiler.release();
        }
        return out.toByteArray();
    }

    public static byte[] decompress(byte[] data) {
        Profiler.enter("time cost on [decompress]");
        InflaterInputStream zipIn = new InflaterInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length * 4);
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = zipIn.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            LOGGER.error("decompress ex", e);
            return Constants.EMPTY_BYTES;
        } finally {
            close(zipIn);
            Profiler.release();
        }
        return out.toByteArray();
    }
}
