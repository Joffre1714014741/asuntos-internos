/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.auxiliares;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

/**
 *
 * @author jpverdezoto
 */
public class Recurso extends Resource implements java.io.Serializable {

    private String path = "";
    private final HashMap<String, String> headers;
    private final byte[] bytes;
    
    public Recurso(byte[] bytes) {
        this.bytes = bytes;
        this.headers = new HashMap<>();
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override
    public String getRequestPath() {
        return path;
    }

    public void setRequestPath(String path) {
        this.path = path;
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return headers;
    }

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        return false;
    }
}