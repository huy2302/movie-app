
package com.example.moviefilmapp.Domain;

import java.util.List;

public class Genres {

//    @SerializedName("id")
//    @Expose
//    private Integer id;
//    @SerializedName("name")
//    @Expose
//    private String name;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
    private List<GenreItems> genreItems;

    public List<GenreItems> getGenreItems() {
        return genreItems;
    }

    public void setGenreItems(List<GenreItems> genreItems) {
        this.genreItems = genreItems;
    }
}
