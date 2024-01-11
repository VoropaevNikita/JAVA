package Task3;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class BytesSum {
    private static int sum(ByteBuffer bb) {
        int sum = 0;
        while (bb.hasRemaining()) {
            if ((sum & 1) != 0)
                sum = (sum >> 1) + 0x8000;
            else
                sum >>= 1;
            sum += bb.get() & 0xff;
            sum &= 0xffff;
        }
        return sum;
    }

    private static void sum(File f) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(f);
                FileChannel fc = fis.getChannel()) {
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            int sum = sum(bb);
            String s = Integer.toString(sum);
            System.out.println(s + "\t" + sz + " байт \t" + f);
        }
    }

    public static void main(String[] args) {
        String path = "src/Task3/file.txt";
        File f = new File(path);
        try {
            sum(f);
        } catch (IOException e) {
            System.err.println(f + ": " + e);
        }
    }
}
