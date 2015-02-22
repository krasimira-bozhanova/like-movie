package bg.unisofia.fmi.ai.data;

import com.j256.ormlite.field.DatabaseField;

public class Genre {
    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String name;

    public Genre() {
    }

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
