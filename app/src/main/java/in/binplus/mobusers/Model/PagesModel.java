package in.binplus.mobusers.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 17,January,2020
 */
public class PagesModel {

    String id,title,content;
    public PagesModel() {
    }

    public PagesModel(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
