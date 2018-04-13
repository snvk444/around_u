package materialdesign.snvk.com.materialdesign.model;

import java.util.ArrayList;

import materialdesign.snvk.com.materialdesign.R;

/**
 * Created by Venkata on 3/16/2018.
 */

public class Landscape {
    

    public int getImageID() {

        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    private int imageID;

    public static ArrayList<Landscape> getData(){

        ArrayList<Landscape> datalist = new ArrayList<>();
        int[] images = getImages();
        for(int i = 0; i<images.length; i++) {
            Landscape landscape = new Landscape();
            landscape.setImageID(images[i]);
            datalist.add(landscape);
        }
        return datalist;
    }

    private static int[] getImages() {
        int[] images = {R.drawable.gas, R.drawable.movie, R.drawable.parks, R.drawable.gas};
        return images;
    }
}
