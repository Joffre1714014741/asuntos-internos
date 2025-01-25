/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import ec.bomberosquito.ai.auxiliares.AuxiliarReporte;
import ec.bomberosquito.ai.auxiliares.DocumentoPDF;
import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.entidades.Eventos;
import ec.bomberosquito.ai.entidades.Personas;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.CasosFacade;
import ec.bomberosquito.ai.facades.EventosFacade;
import ec.bomberosquito.ai.facades.PersonasFacade;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jpverdezoto
 */
@Named(value = "atencionCasosBean")
@ViewScoped
public class atencionCasosBean implements Serializable {

    @Inject
    private SeguridadBean seguridadBean;

    private String pathpdfSolicitud;
    private List<Casos> listaCasos = new ArrayList<>();
    private Casos caso;

    private Eventos evento;
    private File solicitudArchivo;
    private List<Personas> listaPersonas;
    private String selectedOption;
    private List<String> options;

    private Personas involucra;

    @EJB
    private PersonasFacade ejbPersonas;

    @EJB
    private CasosFacade ejbCasos;
    @EJB
    private EventosFacade ejbEventos;

    @PostConstruct
    public void init() {
        listaCasos.clear();
        cargarCasos();
        listaPersonas = buscarInvolucrados();
    }

    public List<Personas> buscarInvolucrados() {
        List<Personas> per = new ArrayList<>();
        HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "o.tipo=:'INVOLUCRADO'");
        paremetros.put("caso", caso);
        try {
            per = ejbPersonas.encontrarParametros(paremetros);
        } catch (ConsultarException e) {
        }
        return per;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public List<String> getOptions() {
        return options;
    }

    public void submit() {
        // Lógica para manejar la opción seleccionada
        System.out.println("Opción seleccionada: " + selectedOption);
    }

    public void cargarCasos() {
        System.out.println("usuario " + seguridadBean.getUsuarioLogeado());
        HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "(o.estado in ('ASIGNADO','PENDIENTE REVISION', 'APROBADO') ) and o.responsable.nombreusuario=:userid");
//        paremetros.put(";where", "(o.estado=:estado or o.estado=:estado1) and o.responsable.nombreusuario=:userid");
//        paremetros.put("estado", "ASIGNADO");
//        paremetros.put("estado1", "PENDIENTE REVISION");
        paremetros.put("userid", seguridadBean.getUsuarioLogeado());

        try {
            listaCasos = ejbCasos.encontrarParametros(paremetros);
        } catch (ConsultarException e) {
        }
    }

    public void inicioCaso() {
        // Editar caso
        caso.setEstado("EN REVISIÓN");
        ejbCasos.edit(caso);
        //fin editar caso
        // crear tracking
        evento.setFechahora(new Date());
        evento.setEstado("EN REVISIÓN");
        evento.setAccionrealizada("Director Ingresa Caso");
        evento.setCaso(caso);
        evento.setAccionante(seguridadBean.getUserLogueado().getTipo());

        ejbEventos.create(evento);
        // finizaiza tracking
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso editado"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-casos");
        PrimeFaces.current().executeScript("PF('dialogoGestionCaso').hide()");

    }

    public void generarPdf(Casos casoParametro) {
        caso = casoParametro;
        try {
            DocumentoPDF pdf = new DocumentoPDF("", "", 1, true);
            List<AuxiliarReporte> columnas = new LinkedList<>();
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "DIRECCION DE ASUNTOS INTERNOS", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "INFORME DE INVESTIGACIÓN", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "DAJ-UI-CBDMQ-2024-0" + caso.getId().toString() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            //I. DATOS GENERALES
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "I. DATOS GENERALES \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "Funcionario(s) Involucrado(s) \n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            pdf.agregarTabla(null, columnas, 1, 100, new float[]{Float.valueOf(100)}, false, Font.HELVETICA);

            columnas = new LinkedList<>();
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "NOMBRES: ", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getNombreinvolucrado(), 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "APELLIDOS: ", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getApellidosinvloucrado(), 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "FECHA DEL INCIDENTE: ", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getFechadeincidente().toString(), 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "FECHA INFORMADA: ", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getFechadeasignacion().toString() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));

            pdf.agregarTabla(null, columnas, 1, 100, new float[]{Float.valueOf(50), Float.valueOf(50)}, false, Font.HELVETICA);

            columnas = new LinkedList<>();
            //ANTECEDENTES
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "II. ANTECEDENTES \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getAntecedentes() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "III. CALIFICACIÓN \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getCalificacion() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "IV. BASE LEGAL \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Constitución de la República del Ecuador
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "CONSTITUCIÓN DE LA REPÚBLICA DEL ECUADOR \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 76
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 76.- En todo proceso en el que se determinen derechos y obligaciones de cualquier orden, se asegurará el derecho al debido proceso que incluirá las siguientes garantías básicas:", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "1. Corresponde a toda autoridad administrativa o judicial, garantizar el cumplimiento de las normas y los derechos de las partes.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "2. Se presumirá la inocencia de toda persona, y será tratada como tal, mientras no se declare su responsabilidad mediante resolución firme o sentencia ejecutoriada.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 54
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 54.- Las personas o entidades que presten servicios públicos o que produzcan o comercialicen bienes de consumo, serán responsables civil y penalmente por la deficiente prestación del servicio, por la calidad defectuosa del producto, o cuando sus condiciones no estén de acuerdo con la publicidad efectuada o con la descripción que incorpore.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 83
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 83.- Son deberes y responsabilidades de las ecuatorianas y los ecuatorianos, sin perjuicio de otros previstos en la Constitución y la ley:", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "(…) 11. Asumir las funciones públicas como un servicio a la colectividad y rendir cuentas a la sociedad y a la autoridad, de acuerdo con la ley.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "12. Ejercer la profesión u oficio con sujeción a la ética (…)", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 226
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 226. - Las instituciones del Estado, sus organismos, dependencias, las servidoras o servidores públicos y las personas que actúen en virtud de una potestad estatal ejercerán solamente las competencias y facultades que les sean atribuidas en la Constitución y la ley. Tendrán el deber de coordinar acciones para el cumplimiento de sus fines y hacer efectivo el goce y ejercicio de los derechos reconocidos en la Constitución.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 227
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 227.- La administración pública constituye un servicio a la colectividad que se rige por los principios de eficacia, eficiencia, calidad, jerarquía, desconcentración, descentralización, coordinación, participación, planificación, transparencia y evaluación.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 233
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 233.- Ninguna servidora ni servidor público estará exento de responsabilidades por los actos realizados en el ejercicio de sus funciones, o por sus omisiones, y serán responsables administrativa, civil y penalmente por el manejo y administración de fondos, bienes o recursos públicos. \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Ley Orgánica de Servicio Público
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "LEY ORGANICA DE SERVICIO PUBLICO, LOSEP \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 22
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 22.- Deberes de las o los servidores públicos. - Son deberes de las y los servidores públicos:", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "e) Velar por la economía y recursos del Estado y por la conservación de los documentos, útiles, equipos, muebles y bienes en general confiados a su guarda, administración o utilización de conformidad con la ley y las normas secundarias;", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 42
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 42.- De las faltas disciplinarias. - Se considera faltas disciplinarias aquellas acciones u omisiones de las servidoras o servidores públicos que contravengan las disposiciones del ordenamiento jurídico vigente en la República y esta ley, en lo atinente a derechos y prohibiciones constitucionales o legales.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Serán sancionadas por la autoridad nominadora o su delegado. Para efectos de la aplicación de esta ley, las faltas se clasifican en leves y graves.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "a.- Faltas leves. - Son aquellas acciones u omisiones realizadas por descuidos o desconocimientos leves, siempre que no alteren o perjudiquen gravemente el normal desarrollo y desenvolvimiento del servicio público.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
// Artículo 43
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 43.- Sanciones disciplinarias. – Las sanciones disciplinarias por orden de gravedad son las siguientes:", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "a) Amonestación verbal;", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "b) Amonestación escrita;", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "c) Sanción pecuniaria administrativa;", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "d) Suspensión temporal sin goce de remuneración; y,", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "e) Destitución.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "La amonestación escrita se impondrá cuando la servidora o servidor haya recibido, durante un mismo mes calendario, dos o más amonestaciones verbales.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "La sanción pecuniaria administrativa o multa no excederá el monto del diez por ciento de la remuneración, y se impondrá por reincidencia en faltas leves en el cumplimiento de sus sus deberes. En caso de reincidencia, la servidora o servidor será destituido con sujeción a la ley. Las sanciones se impondrán de acuerdo a la gravedad de las faltas. \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
//CÓDIGO ORGÁNICO INTEGRAL PENAL, COIP
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "CÓDIGO ORGÁNICO INTEGRAL PENAL, COIP \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "SECCIÓN SEGUNDA", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "Delitos culposos de tránsito", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 380.- Daños materiales. - La persona que como consecuencia de un accidente de tránsito cause daños materiales cuyo costo de reparación sea mayor a dos salarios y no exceda de seis salarios básicos unificados del trabajador en general, será sancionada con multa de dos salarios básicos unificados del trabajador en general y reducción de seis puntos en su licencia de conducir, sin perjuicio de la responsabilidad civil para con terceros a que queda sujeta por causa de la infracción… \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
//CÓDIGO ORGÁNICO ADMINISTRATIVO (COA)          
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "CÓDIGO ORGÁNICO ADMINISTRATIVO (COA) \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 89.- Actividad de las Administraciones Públicas. Las actuaciones administrativas son:", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "1. Acto administrativo", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "2. Acto de simple administración", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "3. Contrato administrativo", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "4. Hecho administrativo", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "5. Acto normativo de carácter administrativo", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Las administraciones públicas pueden, excepcionalmente, emplear instrumentos de derecho privado, para el ejercicio de sus competencias.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 120.- Acto de simple administración. Acto de simple administración es toda declaración unilateral de voluntad, interna o entre órganos de la administración, efectuada en ejercicio de la función administrativa que produce efectos jurídicos individuales y de forma indirecta.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 123.- Alcance del dictamen o informe. El dictamen o informe se referirá a los aspectos objeto de la consulta o del requerimiento; a las materias objeto de la competencia del órgano emisor y a los aspectos que incumben a la profesión, arte u oficio, de los servidores públicos que lo suscriben... \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
//CODIGO CIVIL         
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "CODIGO CIVIL \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 30.- Se llama FUERZA MAYOR o caso fortuito, el imprevisto a que no es posible resistir, como un naufragio, un terremoto, el apresamiento de enemigos, los actos de autoridad ejercidos por un funcionario público, etc. \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            //REGLAMENTO ADMINISTRATIVO Y CONTROL DE BIENES DEL SECTOR PÚBLICO
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "REGLAMENTO ADMINISTRATIVO Y CONTROL DE BIENES DEL SECTOR PÚBLICO \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 20.- Usuario Final. - Será el responsable del cuidado, buen uso, custodia y conservación de los bienes e inventarios a él asignados para el desempeño de sus funciones y los que por delegación expresa se agreguen a su cuidado, conforme a las disposiciones legales y reglamentarias correspondientes.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Art. 49.- Daño, pérdida o destrucción de bienes e inventarios.- La máxima autoridad, o su delegado sustentado en los respectivos informes técnicos y demás documentos administrativos y/o judiciales, dispondrá la reposición de bienes nuevos de similares o superiores características; o, el pago al valor actual de mercado, al Usuario Final; terceros que de cualquier manera tengan acceso al bien cuando realicen acciones de mantenimiento o reparación por requerimiento de la institución; o, a la persona causante de la afectación al bien siempre y cuando se compruebe su identidad. Si sobre la base del informe técnico y demás documentos administrativos señalados en el inciso anterior se desprende que el bien ha sufrido daños que no afecten el normal funcionamiento, estando en posesión del Usuario Final, será responsabilidad del mismo:", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "a)Cubrir los valores que se desprendan por la reparación o restitución del bien afectado.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "b)Efectuar personalmente las gestiones necesarias para obtener del o los terceros que hayan afectado al bien, la reparación o restitución parcial del mismo.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "c) Gestionar con la Unidad Administrativa de Bienes e Inventarios el mantenimiento o reparación a través de terceros que hayan sido contratados por la Institución para efectuar tales labores, en cuyo caso cubrirá los valores resultantes de la reparación o mantenimiento por el daño ocasionado al bien.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "La Unidad de Administración de Bienes e Inventarios gestionará con las unidades pertinentes, a fin de realizar la verificación y recepción del bien a conformidad.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "Sin perjuicio de lo dispuesto en las letras a), b) y c) de este artículo, será responsabilidad de los Usuarios Finales el cuidado, buen uso, custodia y conservación de los bienes e inventarios asignados, así como cualquier afectación que recaiga sobre aquellos. En los casos de pérdida o desaparición de los bienes por hurto, robo, abigeato, fuerza mayor o caso fortuito se estará a lo previsto en los artículos 146 y 150 de este Reglamento, según corresponda. \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            //PROCEDIMIENTO PARA LA INDEMNIZACIÓN DEL SEGURO DE LOS VEHICULOS INSTITUCIONALES
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "PROCEDIMIENTO PARA LA INDEMNIZACIÓN DEL SEGURO DE LOS VEHICULOS INSTITUCIONALES \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "VII. PROCEDIMIENTO…No. 3 Notificar el siniestro ocurrido mediante un Informe del Siniestro dirigido a su jefe inmediato en máximo 24 horas con respaldo fotográfico. (Servidor(a) responsable del vehículo). \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            //POLÍTICAS DE OPERACIÓN
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "POLÍTICAS DE OPERACIÓN \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "1. La notificación del siniestro deberá ser efectuada, a través del sistema, en un plazo máximo de 72 horas ocurrido el mismo.", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, "2. El informe al jefe inmediato deberá ser notificado mediante los gestores documentales y deberán tener el respaldo fotográfico de los daños causados que tiene el vehículo. \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            //V. ACTIVIDADES REALIZADAS
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "V. ACTIVIDADES REALIZADAS \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getActividadesrealizadas() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            //VI. RESUMEN DEL CASO
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "VI. RESUMEN DEL CASO \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getRelaciondehechos() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            //VII. CONCLUSIONES
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "VII. CONCLUSIONES \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getConclusiones() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            //VIII. RECOMENDACIÓN
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "VIII. RECOMENDACIÓN \n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, caso.getRecomendaciones() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
//FIRMAS
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 12, true, "Elaborado por: \n\n\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 11, true, caso.getResponsable().getPersona().getNombres(), 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 11, true, caso.getResponsable().getPersona().getApellidos(), 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 11, true, caso.getResponsable().getPersona().getCedula(), 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            pdf.agregarTabla(null, columnas, 1, 100, new float[]{Float.valueOf(100)}, false, Font.HELVETICA);

            pdf.traerRecurso();
            solicitudArchivo = pdf.traerArchivo();

            mostrarPDF(solicitudArchivo.getAbsolutePath(), "test1.pdf");
            PrimeFaces.current().executeScript("PF('manageDialogpdf').show()");

        } catch (IOException | DocumentException | ConsultarException ex) {
            Logger.getLogger(atencionCasosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPDF(String pathparametro, String nombreArchivo) throws IOException, ConsultarException {
        File file = new File(pathparametro);
        String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("visor");
        String pathpdfEliminar = crearFicheroTemp(nombreArchivo, Files.readAllBytes(file.toPath()), realPath);
        System.out.println("nombreArchivo " + nombreArchivo);
        pathpdfSolicitud = "./visor/ordentrabajo/" + nombreArchivo;

    }

    public String crearFicheroTemp(String id, byte[] archivo, String pathTemp) throws IOException, ConsultarException {

        File folder = new File(pathTemp + "/ordentrabajo");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File fichero = new File(folder.getAbsolutePath() + "/" + id);
        fichero.createNewFile();

        try ( OutputStream out = new FileOutputStream(fichero.getCanonicalPath())) {
            out.write(archivo);
        }
        return fichero.getCanonicalPath();
    }

    public Boolean validacionInforme() { // si vale
        if (caso.getLugardeincidente() == null || caso.getLugardeincidente().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar lugar del incidente"));
            return false;
        }
        if (caso.getFechadeincidente() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar fecha del incidente"));
            return false;
        }
        if (caso.getFechadeasignacion() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar fecha informada"));
            return false;
        }
        if (caso.getAntecedentes() == null || caso.getAntecedentes().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar los antecedentes"));
            return false;
        }
        if (caso.getCalificacion() == null || caso.getCalificacion().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar la clalificaión"));
            return false;
        }
        if (caso.getActividadesrealizadas() == null || caso.getActividadesrealizadas().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar las actividades que realizaste en el investigación"));
            return false;
        }
        if (caso.getRelaciondehechos() == null || caso.getRelaciondehechos().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar el resumen del caso"));
            return false;
        }
        if (caso.getConclusiones() == null || caso.getConclusiones().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar las conclusiones"));
            return false;
        }
        if (caso.getRecomendaciones() == null || caso.getRecomendaciones().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar la recomendación"));
            return false;
        }
        return true;
    }

    public String enviarInforme() { // si vale
        if (caso.getId() == null) {
            System.out.println("NULO NULO");
            return null;
        }
        if (validacionInforme() == false) {
            return null;
        }
        Eventos tracking = new Eventos();

        tracking.setFechahora(new Date());
        tracking.setEstado("PARA REVISION");
        tracking.setAccionrealizada("Analista envía el informe al Director para su revisión");
        tracking.setCaso(caso);
        tracking.setComentario(caso.getObservaciones());
        tracking.setAccionante(seguridadBean.getUserLogueado().getTipo());

        ejbEventos.create(tracking);

        caso.setEstado("PARA REVISION");
        ejbCasos.edit(caso);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El Informe se ha enviado para su revisión"));
        listaCasos.clear();
        cargarCasos();
        PrimeFaces.current().executeScript("PF('manageDialogenviar').hide()");
        return null;

    }

    public void concluirInforme() {
        caso.setEstado("APROBADO");
        ejbCasos.edit(caso);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El Informe finalizó con éxito"));
    }

    public void grabar() {
        if (caso.getId() == null) {
            return;
        }
        caso.setNombreinvolucrado(involucra.getNombres());
        caso.setApellidosinvloucrado(involucra.getApellidos());
        ejbCasos.edit(caso);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Informe editado con éxito"));
        PrimeFaces.current().executeScript("PF('manageDialogcaso').hide()");
    }

    public List<Casos> getListaCasos() {
        return listaCasos;
    }

    public void setListaCasos(List<Casos> listaCasos) {
        this.listaCasos = listaCasos;
    }

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }

    public File getSolicitudArchivo() {
        return solicitudArchivo;
    }

    public void setSolicitudArchivo(File solicitudArchivo) {
        this.solicitudArchivo = solicitudArchivo;
    }

    public String getPathpdfSolicitud() {
        return pathpdfSolicitud;
    }

    public void setPathpdfSolicitud(String pathpdfSolicitud) {
        this.pathpdfSolicitud = pathpdfSolicitud;
    }

    public SeguridadBean getSeguridadBean() {
        return seguridadBean;
    }

    public void setSeguridadBean(SeguridadBean seguridadBean) {
        this.seguridadBean = seguridadBean;
    }

    public Personas getInvolucra() {
        return involucra;
    }

    public void setInvolucra(Personas involucra) {
        this.involucra = involucra;
    }

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

}
