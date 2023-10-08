/*
   Copyright 2009-2022 PrimeTek.

   Licensed under PrimeFaces Commercial License, Version 1.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   Licensed under PrimeFaces Commercial License, Version 1.0 (the "License");

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.primefaces.mirage.view;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class ChronolineView {

    private List<Event> events;
    private List<String> events2;

    @PostConstruct
    public void init() {
        events = new ArrayList<>();
        events.add(new Event("Información de la Solicitud", "Paso 1", "pi pi-shopping-cart", "#9C27B0", "El ciudadano ingresará el número de predio del proyecto y seleccionará el tipo de permiso/certificado deseado."));
        events.add(new Event("Datos Generales del Proyecto", "Paso 2", "pi pi-cog", "#673AB7", "Se completará un formulario con la información básica del proyecto y se adjuntará al documentación necesaria."));
        events.add(new Event("Formulario Declarativo", "Paso 3", "pi pi-envelope", "#FF9800", "Se registrará la información de la solicitud en un checklist"));
        events.add(new Event("Registro de la Solicitud", "Paso 4", "pi pi-check", "#607D8B", "Se remitirá la solicitud mediante el Sistema Declarativo del AT"));

        events2 = new ArrayList<>();
        events2.add("2021");
        events2.add("2021");
        events2.add("2022");
        events2.add("2023");
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<String> getEvents2() {
        return events2;
    }

    public static class Event {
        String status;
        String date;
        String icon;
        String color;
        String image;
        String info;

        public Event() {
        }

        public Event(String status, String date, String icon, String color) {
            this.status = status;
            this.date = date;
            this.icon = icon;
            this.color = color;
        }

        public Event(String status, String date, String icon, String color, String info) {
            this.status = status;
            this.date = date;
            this.icon = icon;
            this.color = color;
            this.info = info;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
        
        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
