package mx.qsistemas.infracciones.DataManagement.Models;

/**
 * Developed by ingmtz on 10/31/16.
 */

public class Catalogs {
    private String id, value, union, dataone, datatwo, datathree;

    @Override
    public String toString() {
        return value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public String getDataone() {
        return dataone;
    }

    public void setDataone(String dataone) {
        this.dataone = dataone;
    }

    public String getDatatwo() {
        return datatwo;
    }

    public void setDatatwo(String datatwo) {
        this.datatwo = datatwo;
    }

    public String getDatathree() {
        return datathree;
    }

    public void setDatathree(String datathree) {
        this.datathree = datathree;
    }
}
