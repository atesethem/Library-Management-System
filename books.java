import java.time.LocalDateTime;

public class books extends booksandmembers{
    public int id;
    public int member;
    public String type;
    public String returndate;
    public String memorydate;
    public boolean extendboolean;
    public boolean isreturned;
    public boolean librarymode;

    public books(String type){
        this.id = numberofbooks;
        this.type=type;
        this.member = 0;
        this.returndate = "0";
        this.memorydate = "0";
        this.extendboolean = false;
        this.isreturned = true;
        this.librarymode = false;
    }
    public void setReturndate(String thedate){
        returndate=thedate;
    }
    public void setExtendboolean(Boolean theboolean){
        extendboolean = theboolean;
    }



}
