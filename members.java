public class members extends booksandmembers {
    public int id;
    public boolean isacademic;
    public int numberofbooksborrowed;
    public members(String isacademic){
        id=getNumberofmembers();
        if(isacademic.equals("A")){
            this.isacademic=true;
        }
        else{
            this.isacademic = false;
        }
        numberofbooksborrowed = 0;
    }

}
