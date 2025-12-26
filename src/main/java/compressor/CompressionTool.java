package src.main.java.compressor;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

/**
 * Main compression tool class.
 */
public class CompressionTool {

    private final PrintStream out;
    private final PrintStream err;

    public CompressionTool(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    public CompressionTool() {
        this(System.out, System.err);
    }

    public static void main(String[] args) {
        new CompressionTool().run(args);
    }

    public void run(String[] args) {
        if(args.length == 0) {
            err.println("Please specify a file path.");
            return;
        }

        Path path = Path.of(args[0]);
        try {
            FileValidator.validate(path);
            if(isCompressed(path)){
                new Decoder(path).runDecoder();
            } else{
                out.println("Compressing file: " + path);
                new Encoder(path).runEncoder();
                out.println("File compressed successfully.");
            }

        } catch (IllegalArgumentException e) {
            err.println(e.getMessage());
        } catch (IOException e) {
            err.println("Error reading file: " + e.getMessage());
        }
    }

    private boolean isCompressed(Path path) {
        return path.toString().endsWith(".cx");
    }
}