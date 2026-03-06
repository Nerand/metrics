package org.example.performancedemo.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Locale;
import java.util.Random;

public class IoBenchmark {

    private static final int[] SIZES = {10_000, 50_000, 100_000};
    private static final int RECORD_SIZE = 20; // int + double + long

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);

        File dir = new File("io-bench-data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (int count : SIZES) {
            System.out.println("\n===== RECORDS: " + count + " =====");

            File rafFile = new File(dir, "raf_" + count + ".bin");
            File channelFile = new File(dir, "channel_" + count + ".bin");
            File mappedFile = new File(dir, "mapped_" + count + ".bin");

            long rafWrite = measure(() -> writeWithRandomAccessFile(rafFile, count));
            long channelWrite = measure(() -> writeWithFileChannel(channelFile, count));
            long mappedWrite = measure(() -> writeWithMappedBuffer(mappedFile, count));

            long rafSeqRead = measure(() -> sequentialReadWithRandomAccessFile(rafFile, count));
            long channelSeqRead = measure(() -> sequentialReadWithFileChannel(channelFile, count));
            long mappedSeqRead = measure(() -> sequentialReadWithMappedBuffer(mappedFile, count));

            long rafRandomRead = measure(() -> randomReadWithRandomAccessFile(rafFile, count, 1_000));
            long channelRandomRead = measure(() -> randomReadWithFileChannel(channelFile, count, 1_000));
            long mappedRandomRead = measure(() -> randomReadWithMappedBuffer(mappedFile, count, 1_000));

            long rafMem = usedMemoryBytes(() -> writeWithRandomAccessFile(new File(dir, "tmp_raf.bin"), count));
            long channelMem = usedMemoryBytes(() -> writeWithFileChannel(new File(dir, "tmp_channel.bin"), count));
            long mappedMem = usedMemoryBytes(() -> writeWithMappedBuffer(new File(dir, "tmp_mapped.bin"), count));

            System.out.println("WRITE:");
            System.out.println("RandomAccessFile: " + rafWrite + " ms");
            System.out.println("FileChannel     : " + channelWrite + " ms");
            System.out.println("MappedByteBuffer: " + mappedWrite + " ms");

            System.out.println("SEQUENTIAL READ:");
            System.out.println("RandomAccessFile: " + rafSeqRead + " ms");
            System.out.println("FileChannel     : " + channelSeqRead + " ms");
            System.out.println("MappedByteBuffer: " + mappedSeqRead + " ms");

            System.out.println("RANDOM READ (1000 reads):");
            System.out.println("RandomAccessFile: " + rafRandomRead + " ms");
            System.out.println("FileChannel     : " + channelRandomRead + " ms");
            System.out.println("MappedByteBuffer: " + mappedRandomRead + " ms");

            System.out.println("APPROX MEMORY DELTA:");
            System.out.println("RandomAccessFile: " + rafMem + " bytes");
            System.out.println("FileChannel     : " + channelMem + " bytes");
            System.out.println("MappedByteBuffer: " + mappedMem + " bytes");

            demonstrateOffsetWrite(rafFile);
        }
    }

    private static void writeWithRandomAccessFile(File file, int count) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);

            for (int i = 0; i < count; i++) {
                raf.writeInt(i);
                raf.writeDouble(i * 1.5);
                raf.writeLong(System.currentTimeMillis() + i);
            }
        }
    }

    private static void writeWithFileChannel(File file, int count) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel channel = raf.getChannel()) {

            raf.setLength(0);
            ByteBuffer buffer = ByteBuffer.allocate(8192);

            for (int i = 0; i < count; i++) {
                if (buffer.remaining() < RECORD_SIZE) {
                    buffer.flip();
                    channel.write(buffer);
                    buffer.clear();
                }

                buffer.putInt(i);
                buffer.putDouble(i * 1.5);
                buffer.putLong(System.currentTimeMillis() + i);
            }

            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }

            channel.force(true);
        }
    }

    private static void writeWithMappedBuffer(File file, int count) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel channel = raf.getChannel()) {

            long fileSize = (long) count * RECORD_SIZE;
            raf.setLength(fileSize);

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

            for (int i = 0; i < count; i++) {
                buffer.putInt(i);
                buffer.putDouble(i * 1.5);
                buffer.putLong(System.currentTimeMillis() + i);
            }

            buffer.force();
        }
    }

    private static void sequentialReadWithRandomAccessFile(File file, int count) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            for (int i = 0; i < count; i++) {
                raf.readInt();
                raf.readDouble();
                raf.readLong();
            }
        }
    }

    private static void sequentialReadWithFileChannel(File file, int count) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel channel = raf.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(8192);

            while (channel.read(buffer) > 0) {
                buffer.flip();

                while (buffer.remaining() >= RECORD_SIZE) {
                    buffer.getInt();
                    buffer.getDouble();
                    buffer.getLong();
                }

                buffer.compact();
            }
        }
    }

    private static void sequentialReadWithMappedBuffer(File file, int count) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel channel = raf.getChannel()) {

            long fileSize = (long) count * RECORD_SIZE;
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            for (int i = 0; i < count; i++) {
                buffer.getInt();
                buffer.getDouble();
                buffer.getLong();
            }
        }
    }

    private static void randomReadWithRandomAccessFile(File file, int count, int reads) throws IOException {
        Random random = new Random(42);

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            for (int i = 0; i < reads; i++) {
                int index = random.nextInt(count);
                raf.seek((long) index * RECORD_SIZE);
                raf.readInt();
                raf.readDouble();
                raf.readLong();
            }
        }
    }

    private static void randomReadWithFileChannel(File file, int count, int reads) throws IOException {
        Random random = new Random(42);

        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel channel = raf.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(RECORD_SIZE);

            for (int i = 0; i < reads; i++) {
                int index = random.nextInt(count);
                buffer.clear();
                channel.read(buffer, (long) index * RECORD_SIZE);
                buffer.flip();
                buffer.getInt();
                buffer.getDouble();
                buffer.getLong();
            }
        }
    }

    private static void randomReadWithMappedBuffer(File file, int count, int reads) throws IOException {
        Random random = new Random(42);

        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel channel = raf.getChannel()) {

            long fileSize = (long) count * RECORD_SIZE;
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            for (int i = 0; i < reads; i++) {
                int index = random.nextInt(count);
                int pos = index * RECORD_SIZE;

                buffer.position(pos);
                buffer.getInt();
                buffer.getDouble();
                buffer.getLong();
            }
        }
    }

    private static void demonstrateOffsetWrite(File file) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.seek(RECORD_SIZE); // вторая запись
            raf.writeInt(999999);
            raf.writeDouble(999.999);
            raf.writeLong(123456789L);
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(RECORD_SIZE);
            int id = raf.readInt();
            double value = raf.readDouble();
            long ts = raf.readLong();

            System.out.println("OFFSET WRITE CHECK (RandomAccessFile, 2nd record): "
                    + "id=" + id + ", value=" + value + ", ts=" + ts);
        }
    }

    private static long measure(IoRunnable action) throws Exception {
        long start = System.nanoTime();
        action.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000;
    }

    private static long usedMemoryBytes(IoRunnable action) throws Exception {
        System.gc();
        Thread.sleep(100);

        Runtime runtime = Runtime.getRuntime();
        long before = runtime.totalMemory() - runtime.freeMemory();

        action.run();

        System.gc();
        Thread.sleep(100);

        long after = runtime.totalMemory() - runtime.freeMemory();
        return Math.max(after - before, 0);
    }

    @FunctionalInterface
    interface IoRunnable {
        void run() throws Exception;
    }
}