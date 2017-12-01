package com.example.gurvindersingh.worker_part.Pojo_Clases;

/**
 * Created by Gurvinder Singh on 10/5/2016.
 */
public class Services_Data {
    String Id;
    String Name;
    String Images;

    public Services_Data(String id, String name, String images) {
        Id = id;
        Name = name;
        Images = images;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getImages() {
        return Images;
    }
}
