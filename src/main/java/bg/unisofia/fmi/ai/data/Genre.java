package bg.unisofia.fmi.ai.data;

import com.j256.ormlite.field.DatabaseField;

public class Genre {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField(unique = true)
    private String name;

    public Genre() {
    }

    public Genre(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Genre other = (Genre) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
