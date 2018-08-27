package mx.qsistemas.infracciones.DataManagement.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Developed by ingmtz on 10/31/16.
 */

public class GetSaveInfra implements Serializable{

    //INFRACCION--------------------------------------------------------------------------------------------Doc identificador
    private String infraid, infraFolio, infradir_calle, infradir_entre, infradir_y, infradir_colonia, ano, identificacion_num, tarjeta_circ, motivacion,
            salariosminimos, importe, puntossancion, linea_uno, linea_dos, linea_tres, fecha_uno, fecha_dos, fecha_tres, importe_uno, importe_dos,
            importe_tres, id_persona_ayun, fechahora;

    private int marca, submarca, tipo, color, identificacion, tipo_doc, expedido_en, updated;

    private String marcatext, submarcatext, tipotext, colortext, identificaciontext, tipo_doctext, expedido_entext, taza_salario;

    private int isRemision, isAusente, isUpdate, isSync;

    private byte[] imagen_1, imagen_2;

    //

    private String submarca_otra, infractor_nombre, infractor_paterno, infractor_materno, infractor_rfc, infractor_calle, infractor_exterior,
            infractor_interior, infractor_colonia, tipolic_num;

    private int  disposicion, se_retiene, infractor_entidad, infractor_delmun, tipolic_tipo, tipolic_expedida;

    private String disposicion_text, se_retienetext, infractor_entidadtext, infractor_delmuntext, tipolic_tipotext, tipolic_expedidatext;

    private ArrayList<ArtFraccion> articulos;

    private int idDbDir, idDBPersona, idVehiculo;

    private String oficialcaptura, oficialnum;

    private boolean idDBDirPersona, isOnline;

    private double latitude, longitude;



    public int getMarca() {
        return marca;
    }

    public void setMarca(int marca) {
        this.marca = marca;
    }

    public String getSubmarca_otra() {
        return submarca_otra;
    }

    public void setSubmarca_otra(String submarca_otra) {
        this.submarca_otra = submarca_otra;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getIdentificacion_num() {
        return identificacion_num;
    }

    public void setIdentificacion_num(String identificacion_num) {
        this.identificacion_num = identificacion_num;
    }

    public String getTarjeta_circ() {
        return tarjeta_circ;
    }

    public void setTarjeta_circ(String tarjeta_circ) {
        this.tarjeta_circ = tarjeta_circ;
    }

    public String getInfradir_calle() {
        return infradir_calle;
    }

    public void setInfradir_calle(String infradir_calle) {
        this.infradir_calle = infradir_calle;
    }

    public String getInfradir_entre() {
        return infradir_entre;
    }

    public void setInfradir_entre(String infradir_entre) {
        this.infradir_entre = infradir_entre;
    }

    public String getInfradir_y() {
        return infradir_y;
    }

    public void setInfradir_y(String infradir_y) {
        this.infradir_y = infradir_y;
    }

    public String getInfradir_colonia() {
        return infradir_colonia;
    }

    public void setInfradir_colonia(String infradir_colonia) {
        this.infradir_colonia = infradir_colonia;
    }

    public String getInfractor_nombre() {
        return infractor_nombre;
    }

    public void setInfractor_nombre(String infractor_nombre) {
        this.infractor_nombre = infractor_nombre;
    }

    public String getInfractor_paterno() {
        return infractor_paterno;
    }

    public void setInfractor_paterno(String infractor_paterno) {
        this.infractor_paterno = infractor_paterno;
    }

    public String getInfractor_materno() {
        return infractor_materno;
    }

    public void setInfractor_materno(String infractor_materno) {
        this.infractor_materno = infractor_materno;
    }

    public String getInfractor_rfc() {
        return infractor_rfc;
    }

    public void setInfractor_rfc(String infractor_rfc) {
        this.infractor_rfc = infractor_rfc;
    }

    public String getInfractor_calle() {
        return infractor_calle;
    }

    public void setInfractor_calle(String infractor_calle) {
        this.infractor_calle = infractor_calle;
    }

    public String getInfractor_exterior() {
        return infractor_exterior;
    }

    public void setInfractor_exterior(String infractor_exterior) {
        this.infractor_exterior = infractor_exterior;
    }

    public String getInfractor_interior() {
        return infractor_interior;
    }

    public void setInfractor_interior(String infractor_interior) {
        this.infractor_interior = infractor_interior;
    }

    public String getInfractor_colonia() {
        return infractor_colonia;
    }

    public void setInfractor_colonia(String infractor_colonia) {
        this.infractor_colonia = infractor_colonia;
    }

    public String getTipolic_num() {
        return tipolic_num;
    }

    public void setTipolic_num(String tipolic_num) {
        this.tipolic_num = tipolic_num;
    }

    public byte[] getImagen_2() {
        return imagen_2;
    }

    public void setImagen_2(byte[] imagen_2) {
        this.imagen_2 = imagen_2;
    }

    public byte[] getImagen_1() {
        return imagen_1;
    }

    public void setImagen_1(byte[] imagen_1) {
        this.imagen_1 = imagen_1;
    }

    public int getSubmarca() {
        return submarca;
    }

    public void setSubmarca(int submarca) {
        this.submarca = submarca;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    public int getExpedido_en() {
        return expedido_en;
    }

    public void setExpedido_en(int expedido_en) {
        this.expedido_en = expedido_en;
    }

    public int getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(int tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public int getDisposicion() {
        return disposicion;
    }

    public void setDisposicion(int disposicion) {
        this.disposicion = disposicion;
    }

    public int getSe_retiene() {
        return se_retiene;
    }

    public void setSe_retiene(int se_retiene) {
        this.se_retiene = se_retiene;
    }

    public int getInfractor_entidad() {
        return infractor_entidad;
    }

    public void setInfractor_entidad(int infractor_entidad) {
        this.infractor_entidad = infractor_entidad;
    }

    public int getInfractor_delmun() {
        return infractor_delmun;
    }

    public void setInfractor_delmun(int infractor_delmun) {
        this.infractor_delmun = infractor_delmun;
    }

    public int getTipolic_tipo() {
        return tipolic_tipo;
    }

    public void setTipolic_tipo(int tipolic_tipo) {
        this.tipolic_tipo = tipolic_tipo;
    }

    public int getTipolic_expedida() {
        return tipolic_expedida;
    }

    public void setTipolic_expedida(int tipolic_expedida) {
        this.tipolic_expedida = tipolic_expedida;
    }

    public ArrayList<ArtFraccion> getArticulos() {
        return articulos;
    }

    public void setArticulos(ArrayList<ArtFraccion> articulos) {
        this.articulos = articulos;
    }

    public String getSalariosminimos() {
        return salariosminimos;
    }

    public void setSalariosminimos(String salariosminimos) {
        this.salariosminimos = salariosminimos;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getPuntossancion() {
        return puntossancion;
    }

    public void setPuntossancion(String puntossancion) {
        this.puntossancion = puntossancion;
    }

    public int getIsRemision() {
        return isRemision;
    }

    public void setIsRemision(int isRemision) {
        this.isRemision = isRemision;
    }

    public int getIsAusente() {
        return isAusente;
    }

    public void setIsAusente(int isAusente) {
        this.isAusente = isAusente;
    }

    public String getMotivacion() {
        return motivacion;
    }

    public void setMotivacion(String motivacion) {
        this.motivacion = motivacion;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getInfraid() {
        return infraid;
    }

    public void setInfraid(String infraid) {
        this.infraid = infraid;
    }

    public String getLinea_uno() {
        return linea_uno;
    }

    public void setLinea_uno(String linea_uno) {
        this.linea_uno = linea_uno;
    }

    public String getLinea_dos() {
        return linea_dos;
    }

    public void setLinea_dos(String linea_dos) {
        this.linea_dos = linea_dos;
    }

    public String getLinea_tres() {
        return linea_tres;
    }

    public void setLinea_tres(String linea_tres) {
        this.linea_tres = linea_tres;
    }

    public String getFecha_uno() {
        return fecha_uno;
    }

    public void setFecha_uno(String fecha_uno) {
        this.fecha_uno = fecha_uno;
    }

    public String getFecha_dos() {
        return fecha_dos;
    }

    public void setFecha_dos(String fecha_dos) {
        this.fecha_dos = fecha_dos;
    }

    public String getFecha_tres() {
        return fecha_tres;
    }

    public void setFecha_tres(String fecha_tres) {
        this.fecha_tres = fecha_tres;
    }

    public String getImporte_uno() {
        return importe_uno;
    }

    public void setImporte_uno(String importe_uno) {
        this.importe_uno = importe_uno;
    }

    public String getImporte_dos() {
        return importe_dos;
    }

    public void setImporte_dos(String importe_dos) {
        this.importe_dos = importe_dos;
    }

    public String getImporte_tres() {
        return importe_tres;
    }

    public void setImporte_tres(String importe_tres) {
        this.importe_tres = importe_tres;
    }

    public String getId_persona_ayun() {
        return id_persona_ayun;
    }

    public void setId_persona_ayun(String id_persona_ayun) {
        this.id_persona_ayun = id_persona_ayun;
    }

    public String getInfraFolio() {
        return infraFolio;
    }

    public void setInfraFolio(String infraFolio) {
        this.infraFolio = infraFolio;
    }

    public String getMarcatext() {
        return marcatext;
    }

    public void setMarcatext(String marcatext) {
        this.marcatext = marcatext;
    }

    public String getSubmarcatext() {
        return submarcatext;
    }

    public void setSubmarcatext(String submarcatext) {
        this.submarcatext = submarcatext;
    }

    public String getTipotext() {
        return tipotext;
    }

    public void setTipotext(String tipotext) {
        this.tipotext = tipotext;
    }

    public String getColortext() {
        return colortext;
    }

    public void setColortext(String colortext) {
        this.colortext = colortext;
    }

    public String getIdentificaciontext() {
        return identificaciontext;
    }

    public void setIdentificaciontext(String identificaciontext) {
        this.identificaciontext = identificaciontext;
    }

    public String getExpedido_entext() {
        return expedido_entext;
    }

    public void setExpedido_entext(String expedido_entext) {
        this.expedido_entext = expedido_entext;
    }

    public String getTipo_doctext() {
        return tipo_doctext;
    }

    public void setTipo_doctext(String tipo_doctext) {
        this.tipo_doctext = tipo_doctext;
    }

    public String getDisposicion_text() {
        return disposicion_text;
    }

    public void setDisposicion_text(String disposicion_text) {
        this.disposicion_text = disposicion_text;
    }

    public String getSe_retienetext() {
        return se_retienetext;
    }

    public void setSe_retienetext(String se_retienetext) {
        this.se_retienetext = se_retienetext;
    }

    public String getInfractor_entidadtext() {
        return infractor_entidadtext;
    }

    public void setInfractor_entidadtext(String infractor_entidadtext) {
        this.infractor_entidadtext = infractor_entidadtext;
    }

    public String getInfractor_delmuntext() {
        return infractor_delmuntext;
    }

    public void setInfractor_delmuntext(String infractor_delmuntext) {
        this.infractor_delmuntext = infractor_delmuntext;
    }

    public String getTipolic_tipotext() {
        return tipolic_tipotext;
    }

    public void setTipolic_tipotext(String tipolic_tipotext) {
        this.tipolic_tipotext = tipolic_tipotext;
    }

    public String getTipolic_expedidatext() {
        return tipolic_expedidatext;
    }

    public void setTipolic_expedidatext(String tipolic_expedidatext) {
        this.tipolic_expedidatext = tipolic_expedidatext;
    }

    public String getTaza_salario() {
        return taza_salario;
    }

    public void setTaza_salario(String taza_salario) {
        this.taza_salario = taza_salario;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    public int getIdDbDir() {
        return idDbDir;
    }

    public void setIdDbDir(int idDbDir) {
        this.idDbDir = idDbDir;
    }

    public int getIdDBPersona() {
        return idDBPersona;
    }

    public void setIdDBPersona(int idDBPersona) {
        this.idDBPersona = idDBPersona;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public boolean isIdDBDirPersona() {
        return idDBDirPersona;
    }

    public void setIdDBDirPersona(boolean idDBDirPersona) {
        this.idDBDirPersona = idDBDirPersona;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getOficialcaptura() {
        return oficialcaptura;
    }

    public void setOficialcaptura(String oficialcaptura) {
        this.oficialcaptura = oficialcaptura;
    }

    public String getOficialnum() {
        return oficialnum;
    }

    public void setOficialnum(String oficialnum) {
        this.oficialnum = oficialnum;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
