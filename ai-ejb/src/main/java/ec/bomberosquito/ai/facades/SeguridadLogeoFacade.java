/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.facades;

import ec.bomberosquito.ai.converters.Codificador;
import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.auxiliares.AuxiliarUsuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jpverdezoto
 */
@Stateless // Describe a laa clase como un bean sin estado
public class SeguridadLogeoFacade {
    @EJB
    private UsuariosFacade ejbUsuarios;
    private List<Usuarios> listaUsuario;
     
    
    public Usuarios logear(String usuario,String password) throws ConsultarException{
        listaUsuario = ejbUsuarios.findAll();
        Usuarios user = new Usuarios();
        AuxiliarUsuario au = new AuxiliarUsuario();
        for(Usuarios u : listaUsuario){
            Codificador c = new Codificador();
                    if ((u.getContrasena().equals(c.getEncoded(password, "MD5"))) && (u.getNombreusuario().equals(usuario))){
                        user = u;
                        System.out.println("si encontro");
                        break;
                    }
                    user = null;
        }
        return user;
    } 
    


    public UsuariosFacade getEjbUsuarios() {
        return ejbUsuarios;
    }

    public void setEjbUsuarios(UsuariosFacade ejbUsuarios) {
        this.ejbUsuarios = ejbUsuarios;
    }
    
    
}
