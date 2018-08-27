package mx.qsistemas.infracciones.DataManagement.Models;

import java.io.Serializable;

/**
 * Developed by ingmtz on 10/31/16.
 */

public class ArtFraccion implements Serializable {

    private String articulo, fraccion, descripcion, salarios, puntos, id, idarticulo;

    private int isSearch;

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getFraccion() {
        return fraccion;
    }

    public void setFraccion(String fraccion) {
        this.fraccion = fraccion;
    }

    public String getSalarios() {
        return salarios;
    }

    public void setSalarios(String salarios) {
        this.salarios = salarios;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdarticulo() {
        return idarticulo;
    }

    public void setIdarticulo(String idarticulo) {
        this.idarticulo = idarticulo;
    }

    public int getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(int isSearch) {
        this.isSearch = isSearch;
    }
}
