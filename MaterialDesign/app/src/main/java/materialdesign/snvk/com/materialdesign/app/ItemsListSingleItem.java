package materialdesign.snvk.com.materialdesign.app;

/**
 * Created by Venkata on 4/5/2018.
 */

class ItemsListSingleItem {
    private String title,thumbnailURL;
    /**
     * Just for the sake of internal reference so that we can identify the item.
     */
    long id;

    /**
     *
     * @param id
     * @param title
     * @param thumbnailURL
     */
    public ItemsListSingleItem(long id, String title, String thumbnailURL) {
        this.id = id;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
    }

    public String getTitle() {
        return title;
    }

    public long getID() {
        return id;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
