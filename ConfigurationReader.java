import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class ConfigurationReader {
    private File file;
    private BufferedReader reader;

    public ConfigurationReader(String filename) throws FileNotFoundException {
        file = new File(filename);
        reader = new BufferedReader(new FileReader(file));
    }

    public boolean hasNextLine() throws IOException {
        boolean hasLine = false;
        reader.mark(1024);
        if(reader.readLine() != null) {
            hasLine = true;
            reader.reset();
        }
        return hasLine;
    }

    public String nextLine() throws IOException {
        return reader.readLine();
    }

    public boolean lineIsComment(String line) {
        return (line.charAt(0) == '#' ? true : false);

    }

    public boolean lineIsEmpty(String line) {
        return (line.length() == 0 ? true : false);
    }

    public boolean lineIsValidForParse(String line) {
        return (!lineIsEmpty(line) && !lineIsComment(line) ? true : false);
    }


    public void closeBufferReader() throws IOException {
        reader.close();
    }
}