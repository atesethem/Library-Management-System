import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Readerandwriter readeradwriter = new Readerandwriter(args[0],args[1]);
        readeradwriter.readfile();
        readeradwriter.fileclose();


    }
}