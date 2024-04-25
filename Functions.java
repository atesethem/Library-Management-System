import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Functions {
    public static List<books> booksList = new ArrayList<>();
    public static List<books> PList = new ArrayList<>();
    public static List<books> HList = new ArrayList<>();
    public static List<members> membersList = new ArrayList<>();
    public static List<members> SList = new ArrayList<>();
    public static List<members> AList = new ArrayList<>();
    public static List<String> booksnumbers = new ArrayList<>();
    public static List<String> membersnumbers = new ArrayList<>();
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void AddBook(String theline) throws IOException {
        // This method adds a new book to the library system by creating a books object from the given input line, adding it to the booksList, and categorizing it as printed or handwritten.
        // It also updates the booksnumbers list
        String[] parts = theline.split("\t");
        books thebook = new books(parts[1]);
        booksList.add(thebook);
        if(thebook.type.equals("P")){
            PList.add(thebook);
            Readerandwriter.filewriter("Created new book: Printed [id: " + thebook.id + "]");
        }
        else{
            HList.add(thebook);
            Readerandwriter.filewriter("Created new book: Handwritten [id: " + thebook.id + "]");
        }

        booksnumbers.add(String.valueOf(booksandmembers.numberofbooks));
        booksandmembers.numberofbooks++;
    }
    public static void AddMember(String theline) throws IOException {
        // The method parses the input line, creates a new member object, and adds it to the appropriate member list.
        // Print the type of member created along with their id
        // Update the count of the number of members
        String[] parts = theline.split("\t");
        members themember = new members(parts[1]);
        membersList.add(themember);
        if(!themember.isacademic){
            SList.add(themember);
            Readerandwriter.filewriter("Created new member: Student [id: " + themember.id + "]");
        }
        else{
            AList.add(themember);
            Readerandwriter.filewriter("Created new member: Academic [id: " + themember.id + "]");
        }
        membersnumbers.add(String.valueOf(booksandmembers.numberofmembers));
        booksandmembers.numberofmembers++;
    }

    public static void BorrowingBook(String theline) throws IOException {
        //This method extracts the relevant information from the input line, including the book id, member id, and borrowing date.
        // It then checks if both the book id and member id are valid and present in the respective lists.
        // If so, it proceeds with the borrowing process
        try {
            String[] parts = theline.split("\t");
            int bookid = Integer.parseInt(parts[1]);
            int memberid = Integer.parseInt(parts[2]);
            String thedate = parts[3];
            LocalDate deadline = null;
            if (booksnumbers.contains(String.valueOf(bookid)) && membersnumbers.contains(String.valueOf(memberid))) {
                outerLoop: for (books thebook : booksList) {
                    if (thebook.id == bookid) {
                        if (thebook.type.equals("P")) {
                            if (thebook.isreturned) {
                                for (members themember : membersList) {
                                    if (themember.id == memberid) {
                                        if (themember.numberofbooksborrowed > 1 && !themember.isacademic) {
                                            Readerandwriter.filewriter("You have exceeded the borrowing limit!");
                                            break outerLoop;
                                        } else if (themember.numberofbooksborrowed > 3 && themember.isacademic) {
                                            Readerandwriter.filewriter("You have exceeded the borrowing limit!");
                                            break outerLoop;
                                        } else {
                                            themember.numberofbooksborrowed++;
                                        }
                                        if (themember.isacademic) {
                                            deadline = LocalDate.parse(thedate, formatter).plusDays(14);
                                        } else {
                                            deadline = LocalDate.parse(thedate, formatter).plusDays(7);
                                        }
                                    }
                                }
                                thebook.member = memberid;
                                thebook.isreturned = false;
                                thebook.returndate = deadline.format(formatter);
                                thebook.memorydate = parts[3];
                                Readerandwriter.filewriter("The book [" + bookid + "] was  borrowed by member [" + memberid + "] at " + thedate);
                                break;
                            } else {
                                Readerandwriter.filewriter("The book hasn't returned");
                                break;
                            }
                        } else {
                            Readerandwriter.filewriter("You can't borrow this book.");
                            break;
                        }

                }
                }
            }
            else{
                Readerandwriter.filewriter("The book id or the member id doesn't matched.");
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("You entered missing data.");
        }
    }
    public static void ReturningBook(String theline) throws IOException {
        // This part of the function class shows us certain stages and its controls when giving the book to the library
        try {
            String[] parts = theline.split("\t");
            int bookid = Integer.parseInt(parts[1]);
            int memberid = Integer.parseInt(parts[2]);
            String lateday = parts[3];
            if (booksnumbers.contains(String.valueOf(bookid)) && membersnumbers.contains(String.valueOf(memberid))) {
                for (books thebook : booksList) {
                    if(thebook.id==bookid && !thebook.isreturned ){
                        for(members themember : membersList){
                            if(themember.id == memberid){
                                themember.numberofbooksborrowed--;
                            }
                        }
                        thebook.isreturned = true;
                        LocalDate borrowingtime = LocalDate.parse(thebook.returndate,formatter);
                        LocalDate returningtime = LocalDate.parse(lateday,formatter);
                        long timeinterval = ChronoUnit.DAYS.between(borrowingtime, returningtime);
                        if(timeinterval<=0 || thebook.librarymode){
                            int fee = 0;
                            Readerandwriter.filewriter("The book [" + bookid + "] was  returned by member [" + memberid + "] at " + lateday + " fee: " + 0);
                            thebook.librarymode=false;
                            break;
                        }
                        else{
                            int fee = (int) timeinterval;
                            Readerandwriter.filewriter("The book [" + bookid + "] was  returned by member [" + memberid + "] at " + lateday+ " fee: " + fee);
                            break;
                        }

                }
                }
            }
            else{
                Readerandwriter.filewriter("The book id or the member id doesn't matched.");
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("You entered missing data.");
        }
    }
    public static void Extendbook(String theline) throws IOException {
        //This method is created to provide to extend the deadline of the book returning. This happens under certain conditions and controls, which you can see below
        try {
            String[] parts = theline.split("\t");
            int bookid = Integer.parseInt(parts[1]);
            int memberid = Integer.parseInt(parts[2]);
            String lateday = parts[3];
            LocalDate deadline = LocalDate.parse(lateday, formatter).plusDays(7);
            if (booksnumbers.contains(String.valueOf(bookid)) && membersnumbers.contains(String.valueOf(memberid))) {
                for (books thebook : booksList) {
                    if (thebook.id == bookid && !thebook.isreturned && !thebook.extendboolean) {
                        if (LocalDate.parse(thebook.memorydate, formatter).isBefore(LocalDate.parse(lateday, formatter))) {
                            thebook.extendboolean = true;
                            thebook.returndate = deadline.format(formatter);
                            Readerandwriter.filewriter("The deadline of book [" + bookid + "] was extended by member [" + memberid +"] at " + lateday);
                            Readerandwriter.filewriter("New deadline of book [" + thebook.id + "] " + "is " + thebook.returndate);
                            break;
                        }
                        else{
                            Readerandwriter.filewriter("The deadline has passed");
                            break;
                        }
                    }
                    else{
                        Readerandwriter.filewriter("You cannot extend the deadline!");
                        break;
                    }
                }
            }
            else{
                Readerandwriter.filewriter("The book id or the member id doesn't matched.");
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("You entered missing data.");
        }
    }

        public static void ReadInLibrary(String theline) throws IOException{
        //This section is different from the borrowing section. Here I created a variable called "library mode" for the book constructor
            // The first difference is that I did not set any time limit here.
            // The second difference is that academics can access handwritten books here
        try {
            String[] parts = theline.split("\t");
            int bookid = Integer.parseInt(parts[1]);
            int memberid = Integer.parseInt(parts[2]);
            String thedate = parts[3];
            if (booksnumbers.contains(String.valueOf(bookid)) && membersnumbers.contains(String.valueOf(memberid))) {
                outerLoop:for (books thebook : booksList) {
                    if (thebook.id == bookid) {
                        for(members themember:membersList) {
                            if(memberid == themember.id){
                                if (!themember.isacademic && thebook.type.equals("H")) {
                                    Readerandwriter.filewriter("Students can not read handwritten books!");
                                    break outerLoop;
                                }
                        }
                        }
                        if (thebook.isreturned) {
                            thebook.member = memberid;
                            thebook.isreturned = false;
                            thebook.librarymode = true;
                            thebook.returndate=thedate;
                            thebook.memorydate = parts[3];
                            Readerandwriter.filewriter("The book [" + bookid + "] was read in library by member [" + memberid + "] at " + thedate);
                            break;
                        } else {
                            Readerandwriter.filewriter("You can not read this book!");
                            break;
                        }
                    }
                }
            }
            else{
                Readerandwriter.filewriter("The book id or the member id doesn't matched.");
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("You entered missing data.");
        }


    }
    public static void gettheHistory() throws IOException {
        //Here, certain commands are given to create the appropriate output for the desired output. Their outputs and certain previously unused lists were used
        Readerandwriter.filewriter("History of library:" + "\n");
        Readerandwriter.filewriter("Number of students: " + SList.size());
        for(members thestudent:SList){
            Readerandwriter.filewriter("Student [id: " + thestudent.id + "]");
        }
        Readerandwriter.filewriter("\n");
        Readerandwriter.filewriter("Number of academics: " + AList.size());
        for(members theacademics:AList){
            Readerandwriter.filewriter("Academic [id: " + theacademics.id + "]");
        }
        Readerandwriter.filewriter("\n");
        Readerandwriter.filewriter("Number of printed books: " + PList.size());
        for(books printedbook:PList){
            Readerandwriter.filewriter("Printed [id: " + printedbook.id + "]");
        }
        Readerandwriter.filewriter("\n");
        Readerandwriter.filewriter("Number of handwritten books: " + HList.size());
        for(books handwrittenbook:HList){
            Readerandwriter.filewriter("Handwritten [id: " + handwrittenbook.id + "]");
        }
        Readerandwriter.filewriter("\n");
        int borrowedbooks = 0;
        int librarybooks =0;
        for(books thebook : booksList){
            if(!thebook.isreturned && !thebook.librarymode){
                borrowedbooks++;
            }
        }
        Readerandwriter.filewriter("Number of borrowed books: " + borrowedbooks);
        for(books thebook : booksList){
            if(!thebook.isreturned && !thebook.librarymode){
                Readerandwriter.filewriter("The book [" + thebook.id + "]" + " was borrowed by member [" + thebook.member + "] at " + thebook.memorydate);
            }
        }
        Readerandwriter.filewriter("\n");
        for(books thebook : booksList){
            if(!thebook.isreturned && thebook.librarymode){
                librarybooks++;
            }
        }
        Readerandwriter.filewriter("Number of books read in library: " + librarybooks);
        for(books thebook : booksList){
            if(!thebook.isreturned && thebook.librarymode){
                Readerandwriter.filewriter("The book [" + thebook.id + "]" + " was read in library by member [" + thebook.member + "] at " + thebook.memorydate);
            }
        }
    }
}
