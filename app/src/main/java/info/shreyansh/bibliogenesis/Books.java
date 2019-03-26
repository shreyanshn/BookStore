/**
 * Created by samridhi1581 on 12-09-2016.
 */


package info.shreyansh.bibliogenesis;

public class Books {
    private String name;
    private String author;
    private int thumbnail;
    public Books(){

    }

    public Books(String name, String author, int thumbnail){
        this.name = name;
        this.author = author;
        this.thumbnail = thumbnail;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getAuthor(){

        return author;
    }

    public void setThumbnail(int thumbnail){
        this.thumbnail=thumbnail;
    }

    public int getThumbnail() {
        return thumbnail;
    }


}
