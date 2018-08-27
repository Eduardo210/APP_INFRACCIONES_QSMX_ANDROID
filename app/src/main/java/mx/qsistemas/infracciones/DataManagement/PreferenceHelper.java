package mx.qsistemas.infracciones.DataManagement;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Developed by ingmtz on 10/17/16.
 */

public class PreferenceHelper {


    private SharedPreferences preferences;

    public Context context;

    private final String SERVICE_IP = "service_ip";
    private final String USER_IDPERSONAAYUNTAMIENTO = "user_idPersonaAyuntamiento";
    private final String USER_IDPERSONA = "user_idPersona";
    private final String USER_NOMBRE = "user_nombre";
    private final String USER_APATERNO = "user_apaterno";
    private final String USER_AMATERNO = "user_amaterno";
    private final String PREFIX = "prefix";

    private final String HEADER_1 = "header_1";
    private final String HEADER_2 = "header_2";
    private final String HEADER_3 = "header_3";
    private final String HEADER_4 = "header_4";
    private final String HEADER_5 = "header_5";
    private final String HEADER_6 = "header_6";

    private final String FOOTER_1 = "footer_1";
    private final String FOOTER_2 = "footer_2";
    private final String FOOTER_3 = "footer_3";

    private final String DIR_1 = "dir_1";
    private final String DIR_2 = "dir_2";
    private final String DIR_3 = "dir_3";
    private final String DIR_4 = "dir_4";

    private final String MUNICIPIO = "municipio";
    private final String IDPAIS = "idpais";
    private final String IDENTIDAD = "identidad";
    private final String IDMUNICIPIO = "idmunicipio";


    private final String CANCONSULT = "canconsult";
    private final String CANINSERT = "caninsert";
    private final String CANEDIT = "canedit";
    private final String CANPAY = "capay";
    private final String CANSYNC = "cansync";

    private final String HASALL = "hasall";

    private final String LAST_FECHA = "last_fecha";
    private final String IS_SEARCH = "search";

    public PreferenceHelper(Context context){
        preferences= context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putServiceIP(String servip){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SERVICE_IP, servip);
        editor.apply();
    }

    public String getServiceIP(){
        return preferences.getString(SERVICE_IP, null);
    }

    public void putPrefix(String prefix){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFIX, prefix);
        editor.apply();
    }

    public String getPrefix(){
        return preferences.getString(PREFIX, null);
    }

    public void putIdPersonaAyuntamiento(String idpersonaayuntamiento){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_IDPERSONAAYUNTAMIENTO, idpersonaayuntamiento);
        editor.apply();
    }

    public String getIdPersonaAyuntamiento(){
        return preferences.getString(USER_IDPERSONAAYUNTAMIENTO, null);
    }

    public void putIdPersona(String idpersonaayuntamiento){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_IDPERSONA, idpersonaayuntamiento);
        editor.apply();
    }

    public String getIdPersona(){
        return preferences.getString(USER_IDPERSONA, null);
    }

    public void putNombre(String nombre){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NOMBRE, nombre);
        editor.apply();
    }

    public String getNombre(){
        return preferences.getString(USER_NOMBRE, null);
    }

    public void putAPaterno(String apaterno){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_APATERNO, apaterno);
        editor.apply();
    }

    public String getAPaterno(){
        return preferences.getString(USER_APATERNO, null);
    }

    public void putAMaterno(String amaterno){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_AMATERNO, amaterno);
        editor.apply();
    }

    public String getAMaterno(){
        return preferences.getString(USER_AMATERNO, null);
    }

    public void logout(){
        putIdPersonaAyuntamiento(null);
        putNombre(null);
        putAPaterno(null);
        putAMaterno(null);
        putCanConsult(null);
        putCanInsert(null);
        putCanEdit(null);
        putCanPay(null);
        putCanSync(null);
        putHasAll(null);
    }


    //PRINTER

    public void putHeader1(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HEADER_1, header);
        editor.apply();
    }

    public String getHeader1(){
        return preferences.getString(HEADER_1, null);
    }

    public void putHeader2(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HEADER_2, header);
        editor.apply();
    }

    public String getHeader2(){
        return preferences.getString(HEADER_2, null);
    }

    public void putHeader3(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HEADER_3, header);
        editor.apply();
    }

    public String getHeader3(){
        return preferences.getString(HEADER_3, null);
    }

    public void putHeader4(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HEADER_4, header);
        editor.apply();
    }

    public String getHeader4(){
        return preferences.getString(HEADER_4, null);
    }

    public void putHeader5(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HEADER_5, header);
        editor.apply();
    }

    public String getHeader5(){
        return preferences.getString(HEADER_5, null);
    }

    public void putHeader6(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HEADER_6, header);
        editor.apply();
    }

    public String getHeader6(){
        return preferences.getString(HEADER_6, null);
    }

    public void putFooter1(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FOOTER_1, header);
        editor.apply();
    }

    public String getFooter1(){
        return preferences.getString(FOOTER_1, null);
    }

    public void putFooter2(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FOOTER_2, header);
        editor.apply();
    }

    public String getFooter2(){
        return preferences.getString(FOOTER_2, null);
    }

    public void putFooter3(String header){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FOOTER_3, header);
        editor.apply();
    }

    public String getFooter3(){
        return preferences.getString(FOOTER_3, null);
    }

    public void putCanConsult(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CANCONSULT, s);
        editor.apply();
    }

    public String getCanConsult(){
        return preferences.getString(CANCONSULT, null);
    }

    public void putCanEdit(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CANEDIT, s);
        editor.apply();
    }

    public String getCanEdit(){
        return preferences.getString(CANEDIT, null);
    }

    public void putCanInsert(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CANINSERT, s);
        editor.apply();
    }

    public String getCanInsert(){
        return preferences.getString(CANINSERT, null);
    }

    public void putCanPay(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CANPAY, s);
        editor.apply();
    }

    public String getCanPay(){
        return preferences.getString(CANPAY, null);
    }

    public void putCanSync(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CANSYNC, s);
        editor.apply();
    }

    public String getCanSync(){
        return preferences.getString(CANSYNC, null);
    }

    public void putHasAll(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HASALL, s);
        editor.apply();
    }

    public String getHasAll(){
        return preferences.getString(HASALL, null);
    }

    public void putLastFecha(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LAST_FECHA, s);
        editor.apply();
    }

    public String getLastFecha(){
        return preferences.getString(LAST_FECHA, null);
    }

    public void putDirOne(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DIR_1, s);
        editor.apply();
    }

    public String getDirOne(){
        return preferences.getString(DIR_1, null);
    }

    public void putDirTwo(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DIR_2, s);
        editor.apply();
    }

    public String getDirTwo(){
        return preferences.getString(DIR_2, null);
    }

    public void putDirThree(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DIR_3, s);
        editor.apply();
    }

    public String getDirThree(){
        return preferences.getString(DIR_3, null);
    }

    public void putDirFour(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DIR_4, s);
        editor.apply();
    }

    public String getDirFour(){
        return preferences.getString(DIR_4, null);
    }

    public void putMunicipio(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MUNICIPIO, s);
        editor.apply();
    }

    public String getMunicipio(){
        return preferences.getString(MUNICIPIO, null);
    }

    public void putIDPais(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IDPAIS, s);
        editor.apply();
    }

    public String getIDPais(){
        return preferences.getString(IDPAIS, null);
    }

    public void putIDEntidad(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IDENTIDAD, s);
        editor.apply();
    }

    public String getIDEntidad(){
        return preferences.getString(IDENTIDAD, null);
    }

    public void putIDMunicipio(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IDMUNICIPIO, s);
        editor.apply();
    }

    public String getIDMunicipio(){
        return preferences.getString(IDMUNICIPIO, null);
    }


    public void putIsSearch(String s) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IS_SEARCH, s);
        editor.apply();
    }

    public String getIsSearch(){
        return preferences.getString(IS_SEARCH, null);
    }


}
