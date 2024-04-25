import java.io.*;

public class Readerandwriter {
    public BufferedReader reader;
    public static BufferedWriter writer;

    public Readerandwriter(String fileName, String outputfilename) {
        try {
            reader = new BufferedReader(new FileReader(fileName));
            writer = new BufferedWriter(new FileWriter(outputfilename));
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void filewriter(String writtesomething) throws IOException {
        writer.write(writtesomething);
        writer.newLine();

    }

    public static void fileclose() throws IOException {
        writer.close();
    }

    public void readfile() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.split("\t")[0].trim().equals("addBook")) {
                Functions.AddBook(line);
            }
            if (line.split("\t")[0].trim().equals("addMember")) {
                Functions.AddMember(line);
            }
            if (line.split("\t")[0].trim().equals("borrowBook")) {
                Functions.BorrowingBook(line);
            }
            if (line.split("\t")[0].trim().equals("returnBook")) {
                Functions.ReturningBook(line);
            }
            if (line.split("\t")[0].trim().equals("extendBook")) {
                Functions.Extendbook(line);
            }
            if (line.split("\t")[0].trim().equals("readInLibrary")) {
                Functions.ReadInLibrary(line);
            }
            if (line.split("\t")[0].trim().equals("getTheHistory")) {
                Functions.gettheHistory();
            }


        }
    }
}
