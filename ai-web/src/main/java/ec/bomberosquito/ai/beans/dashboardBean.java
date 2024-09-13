/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.CasosFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author jpverdezoto
 */
@Named(value = "dashboardBean")
@ViewScoped
public class dashboardBean implements Serializable {

    private DonutChartModel donutModel;
    private List<Casos> listaCasos;

    @EJB
    private CasosFacade ejbCasos;

    public dashboardBean() {

    }

    @PostConstruct
    public void init() {
        createDonutModel();
        ultimosCasos();
    }

    public void ultimosCasos() {
        listaCasos = new LinkedList<>();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put(";where", "o.id is not null");
        parametros.put(";orden", "o.id desc ");
        parametros.put(";inicial", 0);
        parametros.put(";final", 7);
        try {
            listaCasos = ejbCasos.encontrarParametros(parametros);
        } catch (ConsultarException ex) {
            Logger.getLogger(dashboardBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int traerNumeroProcesosxEstado(int caso) {
        String tipoestado;
        switch (caso) {
            case 1: // CREADO
                tipoestado = " in ('CREADO')";
                break;
            case 2: // ASIGNADO
                tipoestado = " in ('ASIGNADO')";
                break;
            case 3: // PENDIENTE REVISION
                tipoestado = " in ('PENDIENTE REVISION')";
                break;
            case 4: // APROBADO
                tipoestado = " not in('APROBADO')";
                break;
            default:
                return 0;
        }

        Map parametros = new HashMap();
        parametros.put(";where", " o.estado " + tipoestado);
        try {
            return ejbCasos.contar(parametros);
        } catch (ConsultarException ex) {
            Logger.getLogger(dashboardBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public void createDonutModel() {
        setDonutModel(new DonutChartModel());
        ChartData data = new ChartData();

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(traerNumeroProcesosxEstado(1));
        values.add(traerNumeroProcesosxEstado(2));
        values.add(traerNumeroProcesosxEstado(3));
        values.add(traerNumeroProcesosxEstado(4));
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(115,182,200)");
        bgColors.add("rgb(240,197,110)");
        bgColors.add("rgb(154,186,127)");
        bgColors.add("rgb(174,130,166)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("CREADOS");
        labels.add("ASIGNADOS");
        labels.add("PENDIENTE REVISION");
        labels.add("APROBADO");
        data.setLabels(labels);

        getDonutModel().setData(data);
    }

    public CasosFacade getEjbCasos() {
        return ejbCasos;
    }

    public void setEjbCasos(CasosFacade ejbCasos) {
        this.ejbCasos = ejbCasos;
    }

    /**
     * @return the donutModel
     */
    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    /**
     * @param donutModel the donutModel to set
     */
    public void setDonutModel(DonutChartModel donutModel) {
        this.donutModel = donutModel;
    }

    /**
     * @return the listaCasos
     */
    public List<Casos> getListaCasos() {
        return listaCasos;
    }

    /**
     * @param listaCasos the listaCasos to set
     */
    public void setListaCasos(List<Casos> listaCasos) {
        this.listaCasos = listaCasos;
    }

}
