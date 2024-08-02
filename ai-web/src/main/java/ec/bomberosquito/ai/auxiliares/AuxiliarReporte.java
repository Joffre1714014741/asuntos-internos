/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.auxiliares;


import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;

/**
 *
 * @author jpverdezoto
 */
public class AuxiliarReporte {
    private String dato;
    private float longitud;
    private int alineacion;
    private int columnas;
    private int tamanio;
    private boolean negrilla;
    private Object valor;
    
    private int columnasO;
    private int fila;
    private boolean color;
    private PdfPTable tabla;
    private Color colorFuente;
    private Color colorBorde;
    private Color colorFondo;
    
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_RIGHT_2 = 1;
    public static final int ALIGN_JUSTIFIED = 3;
    
    public AuxiliarReporte() {
    }

    public AuxiliarReporte(String dato, Object valor) {
        this.dato = dato;
        this.valor = valor;
    }

    public AuxiliarReporte(String dato, int columnas, int alineacion, int tamanio, boolean negrilla, Object valor) {
        this.dato = dato;
        this.columnas = columnas;
        this.alineacion = alineacion;
        this.tamanio = tamanio;
        this.negrilla = negrilla;
        this.valor = valor;
    }


    public AuxiliarReporte(String dato, int columnas, int alineacion, int tamanio, boolean negrilla, int tcolumnas, int fila, boolean color, PdfPTable valor) {
        this.dato = dato;
        this.columnas = columnas;
        this.alineacion = alineacion;
        this.tamanio = tamanio;
        this.negrilla = negrilla;
        this.tabla = valor;
        this.columnasO = tcolumnas;
        this.fila = fila;
        this.color = color;
    }

    public AuxiliarReporte(String dato, int columnas, int alineacion, int tamanio, boolean negrilla, Object valor, int tcolumnas, int fila, Color fuente, Color borde, Color fondo) {
        this.dato = dato;
        this.columnas = columnas;
        this.alineacion = alineacion;
        this.tamanio = tamanio;
        this.negrilla = negrilla;
        this.valor = valor;
        this.columnasO = tcolumnas;
        this.fila = fila;
        this.colorFuente = fuente;
        this.colorBorde = borde;
        this.colorFondo = fondo;
    }

    /**
     * @return the valor
     */
    public Object getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Object valor) {
        this.valor = valor;
    }

    /**
     * @return the dato
     */
    public String getDato() {
        return dato;
    }

    /**
     * @param dato the dato to set
     */
    public void setDato(String dato) {
        this.dato = dato;
    }

    /**
     * @return the longitud
     */
    public float getLongitud() {
        return longitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    /**
     * @return the alineacion
     */
    public int getAlineacion() {
        return alineacion;
    }

    /**
     * @param alineacion the alineacion to set
     */
    public void setAlineacion(int alineacion) {
        this.alineacion = alineacion;
    }

    /**
     * @return the tamanio
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * @param tamanio the tamanio to set
     */
    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    /**
     * @return the negrilla
     */
    public boolean isNegrilla() {
        return negrilla;
    }

    /**
     * @param negrilla the negrilla to set
     */
    public void setNegrilla(boolean negrilla) {
        this.negrilla = negrilla;
    }

    /**
     * @return the columnas
     */
    public int getColumnas() {
        return columnas;
    }

    /**
     * @param columnas the columnas to set
     */
    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getColumnasO() {
        return columnasO;
    }

    public void setColumnasO(int columnasO) {
        this.columnasO = columnasO;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public PdfPTable getTabla() {
        return tabla;
    }

    public void setTabla(PdfPTable tabla) {
        this.tabla = tabla;
    }

    public Color getColorFuente() {
        return colorFuente;
    }

    public void setColorFuente(Color colorFuente) {
        this.colorFuente = colorFuente;
    }

    public Color getColorBorde() {
        return colorBorde;
    }

    public void setColorBorde(Color colorBorde) {
        this.colorBorde = colorBorde;
    }

    public Color getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
    }
    
}