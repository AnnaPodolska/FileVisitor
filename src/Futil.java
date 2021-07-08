import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    private static String str;
    private static Path startingDir;

    private static void readAndWriteFromFile(String infile, String outfile){
        try {

            FileChannel fcin = new FileInputStream(infile).getChannel();
            FileChannel fcout = new FileOutputStream(outfile, true).getChannel();
            ByteBuffer buff = ByteBuffer.allocate((int)fcin.size());
            fcin.read(buff);

            Charset inCharset = Charset.forName("Cp1250");
            Charset outCharset = Charset.forName("UTF-8");

            buff.flip();
            CharBuffer cbuff = inCharset.decode(buff);
            buff = outCharset.encode(cbuff);
            fcout.write(buff);

            fcin.close();
            fcout.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void processDir(String dirName, String resultFileName) {
        startingDir = Paths.get(dirName);
        try {
            FileWriter txtFile = new FileWriter(resultFileName);
        } catch (IOException e){
            e.printStackTrace();
        }

        FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){

                if (file.toString().endsWith(".txt")) {
                    readAndWriteFromFile(file.toString(), resultFileName);
                    System.out.println("Txt File: " + file + ", size: " + attrs.size());

                }
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(startingDir, visitor);
        } catch (IOException x){
            x.printStackTrace();
        }
    }
}

