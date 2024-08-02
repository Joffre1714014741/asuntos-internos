/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.auxiliares;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
/**
 *
 * @author jpverdezoto
 */

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class DocumentoPDF {

    private final File temp;
    private final Document documento;
    private byte[] archivo;

    public DocumentoPDF(String titulo, List<String> titulos, String usuario) throws IOException, DocumentException {
        temp = File.createTempFile("" + Calendar.getInstance().getTimeInMillis(), ".pdf");
        // left rigth top bottom
        documento = new Document(PageSize.A4, 60, 60, 120, 30);
        PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(temp));
//        writer.setPageEvent(new headerFooterVertical(titulos, usuario, titulo));//para vertical
        writer.setPageEvent(new headerFooter(titulo));//para vertical
        documento.open();
    }

    public DocumentoPDF(String titulo, String usuario) throws IOException, DocumentException {
        temp = File.createTempFile("" + Calendar.getInstance().getTimeInMillis(), ".pdf");
        documento = new Document(PageSize.A4.rotate());
        // left rigth top bottom
        documento.setMargins(45, 20, 80, 30);
//        documento = new Document(PageSize.A4, 15, 15, 120, 30);
        PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(temp));
        writer.setPageEvent(new headerFooterRotate(usuario, titulo));
        documento.open();
    }

    public DocumentoPDF(String titulo, String usuario, int orientacion) throws IOException, DocumentException {
        temp = File.createTempFile("" + Calendar.getInstance().getTimeInMillis(), ".pdf");
        System.out.println("archivo tempral  " + temp.getAbsolutePath());
        if (orientacion == 0) {
            documento = new Document(PageSize.A4.rotate());
        } else {
            documento = new Document(PageSize.A4);
        }
        // left rigth top bottom
        documento.setMargins(45, 20, 80, 30);
//        documento = new Document(PageSize.A4, 15, 15, 120, 30);
        PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(temp));
        writer.setPageEvent(new headerFooterRotate(usuario, titulo));
        documento.open();
    }

    public DocumentoPDF(String titulo, String usuario, int orientacion, Boolean plantilla) throws IOException, DocumentException {
        temp = File.createTempFile("" + Calendar.getInstance().getTimeInMillis(), ".pdf");
        System.out.println("archivo tempral  " + temp.getAbsolutePath());
        if (orientacion == 0) {
            documento = new Document(PageSize.A4.rotate());
        } else {
            documento = new Document(PageSize.A4);
        }
        // left rigth top bottom
//        documento = new Document(PageSize.A4, 15, 15, 120, 30);
        PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(temp));
        if (plantilla) {
            writer.setPageEvent(new headerFooterPlantilla(usuario, titulo));
            documento.setMargins(45, 20, 80, 30);

        } else {
            writer.setPageEvent(new headerFooterRotate(usuario, titulo));
            documento.setMargins(70, 70, 80, 30);

        }
        documento.open();
    }

    public File traerArchivo() {
        return temp;
    }

    public void eliminarTemporal() {
        temp.delete();
    }

    public Recurso traerRecurso() throws IOException, DocumentException {
        documento.close();
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        return new Recurso(Files.readAllBytes(Paths.get(temp.getAbsolutePath())));
//        return new Recurso(ec, temp.getName(), Files.readAllBytes(Paths.get(temp.getAbsolutePath())));
    }

    private static PdfPCell createImageAnexoCell(String directorio) throws DocumentException, IOException {

        Image img = Image.getInstance(directorio);
        // img.scaleToFit(80, 80);
        img.scalePercent(20f);
        //img.scalePercent(100);
        //img.setRotationDegrees(45f);
        //img.setAlignment(Element.ALIGN_CENTER);
        //img.setAbsolutePosition(150f, 150f);
        PdfPCell cell = new PdfPCell(img);
        return cell;
    }

    private static PdfPCell createImageAnexoCellPlantilla(String directorio, Color borde) throws DocumentException, IOException {

        Image img = Image.getInstance(directorio);
        // img.scaleToFit(80, 80);
        img.scalePercent(20f);
        //img.scalePercent(100);
        //img.setRotationDegrees(45f);
        //img.setAbsolutePosition(150f, 150f);
        PdfPCell cell = new PdfPCell(img);
        cell.setBackgroundColor(Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(borde);

        return cell;
    }

    public void agregarLinea() {
        try {
            DottedLineSeparator separator = new DottedLineSeparator();
            separator.setPercentage(59500f / 523f);
            Chunk linebreak = new Chunk(separator);
            documento.add(linebreak);
        } catch (DocumentException ex) {
            Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static PdfPCell createImageAnexoCellFicha(Image directorio) throws DocumentException, IOException {

        Image image = Image.getInstance(directorio);
        float originalWidth = image.getWidth();
        float originalHeight = image.getHeight();
        float maxHeight = 162f;
        float maxWidth = 262f; // Nuevo valor máximo de ancho
        float heightScaleFactor = maxHeight / originalHeight;
        float widthScaleFactor = maxWidth / originalWidth; // Factor de escala para el ancho

// Determinar el factor de escala final basado en la altura y el ancho
        float scaleFactor = Math.min(heightScaleFactor, widthScaleFactor);

        image.scaleAbsoluteWidth(originalWidth * scaleFactor);
        image.scaleAbsoluteHeight(originalHeight * scaleFactor); // Usar el factor de escala correcto aquí
        image.setAlignment(Image.ALIGN_CENTER);

        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);

        return cell;
    }

    private PdfPCell encabezadoIzquierda(String texto, int fontsize, boolean borde) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, new Font(Font.TIMES_ROMAN, fontsize, Font.BOLD)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (!borde) {
            celda.setBorderColor(Color.WHITE);
        }
        celda.setBorder(0);
        celda.setBorderWidthTop(0.5f);
        celda.setBorderWidthLeft(0.5f);
        celda.setBorderWidthRight(0.5f);
        celda.setBorderWidthBottom(0.5f);

        return celda;
    }

    private PdfPCell celda1(String texto, int alinear, boolean bold, int columnas, int fontSize, Color fuenteAux, Color borde, Color fondo, int tipoLetra) {

        Font fuente = new Font(tipoLetra, fontSize, (bold ? Font.BOLD : Font.NORMAL));
        fuente.setColor(fuenteAux);
        PdfPCell celda = new PdfPCell(new Phrase(columnas, texto, fuente));
        celda.setHorizontalAlignment(alinear);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBackgroundColor(fondo);
        celda.setBorderColor(borde);
        float altoCelda = fontSize + fuente.getCalculatedLeading(1.5f);
        float paddingVertical = (altoCelda - fontSize) / 2;
        celda.setPaddingTop(paddingVertical);
        celda.setPaddingBottom(paddingVertical);

        celda.setBackgroundColor(fondo);
        celda.setBorderColor(borde);
//        celda.setBorder(0);
//        celda.setBorderWidthTop(0.5f);
//        celda.setBorderWidthLeft(0.5f);
//        celda.setBorderWidthRight(0.5f);
//        celda.setBorderWidthBottom(0.5f);

        return celda;
    }

    private PdfPCell celda(String texto, int alinear, boolean bold, int columnas, int fontSize, Color fuenteAux, Color borde, Color fondo, int tipoLetra) {

        Font fuente = new Font(tipoLetra, fontSize, (bold ? Font.BOLD : Font.NORMAL));
        fuente.setColor(fuenteAux);
        PdfPCell celda = new PdfPCell(new Phrase(columnas, texto, fuente));
        celda.setHorizontalAlignment(alinear);
        celda.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
        celda.setBorderColor(borde);
        if (fondo != null) {
            celda.setBackgroundColor(fondo);
        }
//        celda.setBorder(0);
//        celda.setBorderWidthTop(0.5f);
//        celda.setBorderWidthLeft(0.5f);
//        celda.setBorderWidthRight(0.5f);
//        celda.setBorderWidthBottom(0.5f);

        return celda;
    }

    public void agregaParrafo(String texto) {
        Paragraph linea = new Paragraph(texto, new Font(Font.COURIER, 6, Font.NORMAL));
        linea.setAlignment(Element.ALIGN_JUSTIFIED);
        try {
            documento.add(linea);
        } catch (DocumentException ex) {
            Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PdfPTable retornarTabla(List<AuxiliarReporte> titulos, List<AuxiliarReporte> valores, float[] tamanio, int porcentaje, String tituloTabla, boolean borde, int tipoLetra) throws DocumentException, IOException {
        int filasTitulo = 1;
        if (titulos == null) {
            titulos = new LinkedList<>();
        }
        if (valores == null) {
            return null;
        }
        if (valores.isEmpty()) {
            return null;
        }
        if (titulos.isEmpty()) {
            filasTitulo = 0;
        } else {

        }
        float[] anchoColumnas = tamanio;
        PdfPTable tablaInterna = new PdfPTable(anchoColumnas);
        tablaInterna.setWidthPercentage(porcentaje);
        tablaInterna.setHorizontalAlignment(Element.ALIGN_LEFT);
        for (AuxiliarReporte v : titulos) {
            PdfPCell celdaFinal = encabezadoIzquierda((String) v.getValor(), v.getTamanio(), borde);
            if (v.getColumnasO() != 0 && v.getFila() == 0) {
                celdaFinal.setColspan(v.getColumnasO());
                tablaInterna.addCell(celdaFinal);
            } else if (v.getColumnasO() == 0 && v.getFila() == 0) {
                tablaInterna.addCell(celdaFinal);
            } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                celdaFinal.setRowspan(v.getFila());
                tablaInterna.addCell(celdaFinal);
            }
        }

        for (AuxiliarReporte v : titulos) {
            tablaInterna.addCell(encabezadoIzquierda(String.valueOf(v.getValor()), v.getTamanio(), borde));
        }
        for (AuxiliarReporte v : valores) {
            switch (v.getDato()) {
                case "String":

                    if (v.getColumnasO() != 0 && v.getFila() == 0) {
                        PdfPCell celdaFinal = new PdfPCell(celda((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));
                        // Indicamos cuantas columnas ocupa la celda
                        celdaFinal.setColspan(v.getColumnasO());
                        tablaInterna.addCell(celdaFinal);
                    } else if (v.getColumnasO() == 0 && v.getFila() == 0) {

                        tablaInterna.addCell(celda((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));

                    } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                        PdfPCell celdaFinal = new PdfPCell(celda((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));
                        celdaFinal.setRowspan(v.getFila());
                        tablaInterna.addCell(celdaFinal);
                    }

                    break;
                case "String1":

                    if (v.getColumnasO() != 0 && v.getFila() == 0) {
                        PdfPCell celdaFinal = new PdfPCell(celda1((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));
                        // Indicamos cuantas columnas ocupa la celda
                        celdaFinal.setColspan(v.getColumnasO());
                        tablaInterna.addCell(celdaFinal);
                    } else if (v.getColumnasO() == 0 && v.getFila() == 0) {

                        tablaInterna.addCell(celda1((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));

                    } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                        PdfPCell celdaFinal = new PdfPCell(celda1((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));
                        celdaFinal.setRowspan(v.getFila());
                        tablaInterna.addCell(celdaFinal);
                    }

                    break;

                case "Table":
                    if (v.getColumnasO() != 0 && v.getFila() == 0) {
                        PdfPCell celdaFinal = new PdfPCell(v.getTabla());
                        // Indicamos cuantas columnas ocupa la celda
                        celdaFinal.setColspan(v.getColumnasO());
                        tablaInterna.addCell(celdaFinal);
                    } else if (v.getColumnasO() == 0 && v.getFila() == 0) {
                        tablaInterna.addCell(v.getTabla());

                    } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                        PdfPCell celdaFinal = new PdfPCell(v.getTabla());
                        celdaFinal.setRowspan(v.getFila());
                        tablaInterna.addCell(celdaFinal);
                    }
                    //tablaInterna.addCell(v.getValor1());
                    break;

            }
        }
        return tablaInterna;
    }

    public void agregarTabla(List<AuxiliarReporte> titulos, List<AuxiliarReporte> valores, int tamanio, int porcentaje, float[] tabla, boolean borde, int tipoLetra) {
        int filasTitulo = 1;
        try {
            if (titulos == null) {
                titulos = new LinkedList<>();
            }
            if (valores == null) {
                return;
            }

            if (titulos.isEmpty()) {
                filasTitulo = 0;
            } else {

            }
            float[] anchoColumnas = tabla;
            PdfPTable tablaInterna = new PdfPTable(anchoColumnas);

            tablaInterna.setWidthPercentage(porcentaje);
            tablaInterna.setHorizontalAlignment(Element.ALIGN_CENTER);

            for (AuxiliarReporte v : titulos) {
                PdfPCell celdaFinal = encabezadoIzquierda((String) v.getValor(), v.getTamanio(), borde);
                if (v.getColumnasO() != 0 && v.getFila() == 0) {
                    celdaFinal.setColspan(v.getColumnasO());
                    tablaInterna.addCell(celdaFinal);
                } else if (v.getColumnasO() == 0 && v.getFila() == 0) {
                    tablaInterna.addCell(celdaFinal);
                } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                    celdaFinal.setRowspan(v.getFila());
                    tablaInterna.addCell(celdaFinal);
                }
            }

            for (AuxiliarReporte v : valores) {
                switch (v.getDato()) {
                    case "String":

                        if (v.getColumnasO() != 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(celda((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));
                            // Indicamos cuantas columnas ocupa la celda
                            celdaFinal.setColspan(v.getColumnasO());
                            tablaInterna.addCell(celdaFinal);
                        } else if (v.getColumnasO() == 0 && v.getFila() == 0) {

                            tablaInterna.addCell(celda((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));

                        } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                            PdfPCell celdaFinal = new PdfPCell(celda((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));
                            celdaFinal.setRowspan(v.getFila());
                            tablaInterna.addCell(celdaFinal);
                        }

                        break;
                    case "String1":

                        if (v.getColumnasO() != 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(celda1((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));
                            // Indicamos cuantas columnas ocupa la celda
                            celdaFinal.setColspan(v.getColumnasO());
                            tablaInterna.addCell(celdaFinal);
                        } else if (v.getColumnasO() == 0 && v.getFila() == 0) {

                            tablaInterna.addCell(celda1((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));

                        } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                            PdfPCell celdaFinal = new PdfPCell(celda1((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), v.getColorFuente(), v.getColorBorde(), v.getColorFondo(), tipoLetra));
                            celdaFinal.setRowspan(v.getFila());
                            tablaInterna.addCell(celdaFinal);
                        }

                        break;
                    case "Table":
                        tablaInterna.addCell(v.getTabla());
                        break;
                    case "image":
                        if (v.getColumnasO() != 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCell((String) v.getValor()));

                            // Indicamos cuantas columnas ocupa la celda
                            celdaFinal.setColspan(v.getColumnasO());
                            tablaInterna.addCell(celdaFinal);
                        } else if (v.getColumnasO() == 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCell((String) v.getValor()));
                            tablaInterna.addCell(celdaFinal);

                        } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCell((String) v.getValor()));
                            celdaFinal.setRowspan(v.getFila());
                            celdaFinal.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celdaFinal.setPadding(5f);
                            //celdaFinal.setVerticalAlignment(Element.ALIGN_CENTER);
                            tablaInterna.addCell(celdaFinal);
                        }

                        break;
                    case "imagePlantilla":
                        if (v.getColumnasO() != 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCellPlantilla((String) v.getValor(), v.getColorBorde()));

                            // Indicamos cuantas columnas ocupa la celda
                            celdaFinal.setColspan(v.getColumnasO());
                            tablaInterna.addCell(celdaFinal);
                        } else if (v.getColumnasO() == 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCellPlantilla((String) v.getValor(), v.getColorBorde()));
                            tablaInterna.addCell(celdaFinal);

                        } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCellPlantilla((String) v.getValor(), v.getColorBorde()));
                            celdaFinal.setRowspan(v.getFila());
                            celdaFinal.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celdaFinal.setPadding(5f);
                            //celdaFinal.setVerticalAlignment(Element.ALIGN_CENTER);
                            tablaInterna.addCell(celdaFinal);
                        }

                        break;
                    case "imagen":
                        if (v.getColumnasO() != 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCellFicha((Image) v.getValor()));

                            // Indicamos cuantas columnas ocupa la celda
                            celdaFinal.setColspan(v.getColumnasO());
                            tablaInterna.addCell(celdaFinal);
                        } else if (v.getColumnasO() == 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCellFicha((Image) v.getValor()));
                            tablaInterna.addCell(celdaFinal);

                        } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                            PdfPCell celdaFinal = new PdfPCell(createImageAnexoCellFicha((Image) v.getValor()));
                            celdaFinal.setRowspan(v.getFila());
                            celdaFinal.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celdaFinal.setPadding(5f);
                            //celdaFinal.setVerticalAlignment(Element.ALIGN_CENTER);
                            tablaInterna.addCell(celdaFinal);
                        }

                        break;
                    case "HTMLI":
                        HTMLWorker htmlWorker = new HTMLWorker(documento);
                        //htmlWorker.text((String) v.getValor());
                        htmlWorker.parse(new StringReader((String) v.getValor()));
                        break;
                    case "Cabecera":

                        if (v.getColumnasO() != 0 && v.getFila() == 0) {
                            PdfPCell celdaFinal = new PdfPCell(encabezadoTablaTest((String) v.getValor(), v.getTamanio()));
                            // Indicamos cuantas columnas ocupa la celda
                            celdaFinal.setColspan(v.getColumnasO());
//                            tablaInterna.setHeaderRows(2);
                            tablaInterna.addCell(celdaFinal);
                        } else if (v.getColumnasO() == 0 && v.getFila() == 0) {

                            tablaInterna.addCell(encabezadoTabla((String) v.getValor(), v.getTamanio()));

                        } else if (v.getColumnasO() == 0 && v.getFila() != 0) {
                            PdfPCell celdaFinal = new PdfPCell(encabezadoTabla((String) v.getValor(), v.getTamanio()));
                            celdaFinal.setRowspan(v.getFila());
                            tablaInterna.addCell(celdaFinal);
                        }

                        break;

                }

            }
            documento.add(tablaInterna);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PdfPCell encabezadoTablaTest(String texto, int tamanio) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, new Font(Font.COURIER, tamanio, Font.BOLD)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        celda.setBorderWidthTop(1);
        celda.setBorderWidthLeft(0);
        celda.setBorderWidthRight(0);
        celda.setBorderWidthBottom(1);
        return celda;
    }

    public void agregarTabla(List<String> titulos, List<AuxiliarReporte> valores, int tamanio, int porcentaje, String tituloTabla, int alineacion) {
        int filasTitulo = 1;
        try {
            if (titulos == null) {
                titulos = new LinkedList<>();
            }
            if (valores == null) {
                return;
            }
            if (valores.isEmpty()) {
                return;
            }
            if (titulos.isEmpty()) {
                filasTitulo = 0;
            } else {

            }
            PdfPTable tablaInterna = new PdfPTable(tamanio);
            tablaInterna.setWidthPercentage(porcentaje);
            tablaInterna.setHorizontalAlignment(alineacion);

            if (!((tituloTabla == null) || (tituloTabla.isEmpty()))) {
                filasTitulo++;
                tablaInterna.setHeaderRows(2);
                tablaInterna.addCell(encabezadoTabla(tituloTabla, titulos.size()));
                documento.add(tablaInterna);

            } else {
                tablaInterna.setHeaderRows(filasTitulo);
            }

            for (String v : titulos) {
                tablaInterna.addCell(encabezado(v, alineacion, false, tamanio, 8, false));
            }
            for (AuxiliarReporte v : valores) {
                switch (v.getDato()) {
                    case "String":
                        tablaInterna.addCell(celda((String) v.getValor(), alineacion, false, 6, 8));
                        break;
                    case "Double":
                        if (v.getValor() == null) {
                            tablaInterna.addCell(celda((""), alineacion, false, 6, 8));
                        } else {
                            DecimalFormat df = new DecimalFormat("###,##0.00");
                            double valor = (v.getValor() == null ? 0.0 : (double) v.getValor());
                            tablaInterna.addCell(celda(df.format(valor), Element.ALIGN_RIGHT, false, 6, 8));
                        }
                        break;
                    case "Date":
                        if (v.getValor() == null) {
                            tablaInterna.addCell(celda((""), alineacion, false, 6, 8));
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            tablaInterna.addCell(celda((sdf.format(v.getValor())), alineacion, false, 6, 8));
                        }
                        break;
                }
            }

            documento.add(tablaInterna);
        } catch (DocumentException ex) {
            Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarTablaReporte(
            List<AuxiliarReporte> titulos,
            List<AuxiliarReporte> valores,
            int tamanio,
            int porcentaje,
            String tituloTabla) {

        int filasTitulo = 1;
        try {
            if (titulos == null) {
                titulos = new LinkedList<>();
            }
            if (valores == null) {
                return;
            }
            if (valores.isEmpty()) {
                return;
            }
            if (titulos.isEmpty()) {
                filasTitulo = 0;
            }

            float[] anchoColumnas = new float[tamanio];
            int i = 0;
            for (AuxiliarReporte v : titulos) {
                anchoColumnas[i++] = v.getColumnas();
            }
            PdfPTable tablaInterna = new PdfPTable(anchoColumnas);
//            tablaInterna.setTotalWidth(documento.getPageSize().getWidth() - 80);
//            tablaInterna.setLockedWidth(true);
            tablaInterna.setWidthPercentage(porcentaje);
            tablaInterna.setHorizontalAlignment(Element.ALIGN_LEFT);

            if (!((tituloTabla == null) || (tituloTabla.isEmpty()))) {
                filasTitulo++;
                tablaInterna.setHeaderRows(2);
                tablaInterna.addCell(encabezadoTabla(tituloTabla, titulos.size()));
                documento.add(tablaInterna);
            } else {
                tablaInterna.setHeaderRows(filasTitulo);
            }

            for (AuxiliarReporte v : titulos) {
                tablaInterna.addCell(encabezado((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio(), true));
            }
            for (AuxiliarReporte v : valores) {
                switch (v.getDato()) {
                    case "String":
                        if (v.getValor() == null) {
                            tablaInterna.addCell(celda("", v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio()));
                        } else {
                            tablaInterna.addCell(celda((String) v.getValor(), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio()));
                        }
                        break;
                    case "Double":
                        if (v.getValor() == null) {
                            tablaInterna.addCell(celda("", Element.ALIGN_LEFT, false, 6, 8));
                        } else {
                            DecimalFormat df = new DecimalFormat("###,##0.00");
                            double valor = (v.getValor() == null ? 0.0 : (double) v.getValor());
                            tablaInterna.addCell(celda(df.format(valor), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio()));
                        }
                        break;
                    case "Date":
                        if (v.getValor() == null) {
                            tablaInterna.addCell(celda("", Element.ALIGN_LEFT, false, 6, 8));
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            tablaInterna.addCell(celda(sdf.format(v.getValor()), v.getAlineacion(), v.isNegrilla(), v.getColumnas(), v.getTamanio()));
                        }
                        break;
                }
            }

            documento.add(tablaInterna);
        } catch (DocumentException ex) {
            Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PdfPCell encabezado(String texto, int alinear, boolean bold, int columnas, int fontSize, boolean cerrado) {
        PdfPCell celda = new PdfPCell(new Phrase(columnas, texto, new Font(Font.TIMES_ROMAN, fontSize, (bold ? Font.BOLDITALIC : Font.NORMAL))));
        celda.setHorizontalAlignment(alinear);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(0);
        if (cerrado) {
            celda.setBorderWidthTop(1);
            celda.setBorderWidthBottom(1);
        }
        return celda;
    }

    private PdfPCell encabezadoTabla(String texto, int columnas) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, new Font(Font.TIMES_ROMAN, 8, Font.BOLDITALIC)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setColspan(columnas);
        celda.setBorder(0);
        celda.setBorderWidthTop(1);
        celda.setBorderWidthLeft(1);
        celda.setBorderWidthRight(1);
        celda.setBorderWidthBottom(0);
        return celda;
    }

    /**
     *
     * @param texto Texto que irá en la celda
     * @param alineacion ¿Qué alineación tendrá? ALIGN_LEFT[0], ALIGN_CENTER[1],
     * ALIGN_RIGHT[2]
     * @param bold ¿Negrita? Sí, No
     * @param columnas
     * @param fontSize Tamaño de la fuente
     * @return
     */
    public PdfPCell celda(String texto, int alineacion, boolean bold, int columnas, int fontSize) {
        PdfPCell celda = new PdfPCell(new Phrase(columnas, texto, new Font(Font.TIMES_ROMAN, fontSize, (bold ? Font.BOLD : Font.NORMAL))));
        celda.setHorizontalAlignment(alineacion);
        celda.setVerticalAlignment(Element.ALIGN_TOP);
        celda.setBorder(0);
        return celda;
    }

    public void agregaParrafo(String texto, int alineacion, int tamanio, boolean bold) {
        Paragraph linea = new Paragraph(texto, new Font(Font.TIMES_ROMAN, tamanio, (bold ? Font.BOLD : Font.NORMAL)));
        linea.setAlignment(alineacion);
        try {
            documento.add(linea);
        } catch (DocumentException ex) {
            Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the archivo
     */
    public byte[] getArchivo() {
        return archivo;
    }

    /**
     * @param archivo the archivo to set
     */
    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    /**
     * Clase para imprimir encabezado y pie de página para página A4 Verticales
     * sólo con el logo
     */
    class headerFooter extends PdfPageEventHelper {

        PdfTemplate t;
        Image total;
        String titulo;

        public headerFooter(String titulo) {
            this.titulo = titulo;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(0, 0);
            try {
                total = Image.getInstance(t);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            try {
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{90, 240, 90});
                table.setTotalWidth(420);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(51);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                Image centro = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/logocbq.png"));
                centro.scaleAbsolute(70f, 70f);

                PdfPCell cell = new PdfPCell(new Phrase(""));
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(centro);
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("\n" + titulo, new Font(Font.TIMES_ROMAN, 12, Font.BOLD)));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(0);
                table.addCell(cell);

                table.writeSelectedRows(0, -1, 80, 803, writer.getDirectContent());
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        Element.ALIGN_CENTER,
                        new Phrase(String.format("%d", writer.getPageNumber()), new Font(Font.TIMES_ROMAN, 7, Font.BOLDITALIC)),
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 10, 0);
            } catch (BadElementException | IOException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.TIMES_ROMAN, 7, Font.BOLDITALIC)),
                    2, 4, 0);
        }
    }

    /**
     * Clase para imprimir encabezado y pie de página para página A4 Verticales
     */
    class headerFooterVertical extends PdfPageEventHelper {

        PdfTemplate t;
        Image total;
        List<String> titulos;
        String usuario;
        String titulo;

        public headerFooterVertical(List<String> titulos, String usuario, String titulo) {
            this.titulos = titulos;
            this.usuario = usuario;
            this.titulo = titulo;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(0, 0);
            try {
                total = Image.getInstance(t);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            try {
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{90, 240, 90});
                table.setTotalWidth(420);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(51);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                //Image izq = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/logocbq.png"));
//                Image der = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/escuela.png"));
                //  izq.scaleAbsolute(45f, 45f);
//                der.scaleAbsolute(40f, 40f);

                PdfPCell cell = new PdfPCell();
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("CUERPO DE BOMBEROS \nDEL DISTRITO METROPOLITANO DE QUITO\n"
                        + titulo, new Font(Font.TIMES_ROMAN, 8, Font.BOLDITALIC)));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                cell = new PdfPCell(new Phrase("USUARIO : " + usuario + "\n FECHA : " + sdf.format(new Date()), new Font(Font.TIMES_ROMAN, 7, Font.ITALIC)));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);

                table.writeSelectedRows(0, -1, 80, 803, writer.getDirectContent());
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        Element.ALIGN_CENTER,
                        new Phrase(String.format("%d", writer.getPageNumber()), new Font(Font.TIMES_ROMAN, 7, Font.BOLDITALIC)),
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 10, 0);
            } catch (BadElementException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.TIMES_ROMAN, 7, Font.BOLDITALIC)),
                    2, 4, 0);
        }
    }

    /**
     * Clase para imprimir encabezado y pie de página para página A4
     * Horizontales
     */
    class headerFooterRotate extends PdfPageEventHelper {

        PdfTemplate t;
        Image total;
        String usuario;
        String titulo;

        public headerFooterRotate(String usuario, String titulo) {
            this.usuario = usuario;
            this.titulo = titulo;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(0, 0);
            try {
                total = Image.getInstance(t);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            try {
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{50, 530, 205});
                table.setTotalWidth(800);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(20);
//                table.getDefaultCell().setBorder(Rectangle.BOTTOM);
//                Image izq = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/logocbq.png"));
//                Image der = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/escuela.png"));
                // izq.scaleAbsolute(70f, 70f);
//                izq.scaleAbsolute(45f, 22f);

                PdfPCell cell = new PdfPCell();
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(titulo, new Font(Font.TIMES_ROMAN, 10, Font.BOLD)));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table.addCell(cell);

//                cell = new PdfPCell(der);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                cell = new PdfPCell(new Phrase("USUARIO : " + usuario + "\n FECHA : " + sdf.format(new Date()), new Font(Font.TIMES_ROMAN, 6, Font.BOLD)));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                table.writeSelectedRows(0, -1, 30, 580, writer.getDirectContent());
//                table.writeSelectedRows(0, -1, 30, 803, writer.getDirectContent());
//                for (String s:titulos){
//                     document.add(new Paragraph(s,new Font(Font.TIMES_ROMAN, 8, Font.BOLD)));
//                }
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        Element.ALIGN_CENTER,
                        new Phrase(String.format("%d", writer.getPageNumber()), new Font(Font.TIMES_ROMAN, 6, Font.BOLD)),
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 10, 0);
            } catch (BadElementException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.TIMES_ROMAN, 6, Font.BOLD)),
                    2, 4, 0);
        }
    }

    class headerFooterPlantilla extends PdfPageEventHelper {

        PdfTemplate t;
        Image total;
        String usuario;
        String titulo;

        public headerFooterPlantilla(String usuario, String titulo) {
            this.usuario = usuario;
            this.titulo = titulo;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(0, 0);
            try {
                total = Image.getInstance(t);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            try {
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{50, 530, 205});
                table.setTotalWidth(800);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(20);
//                table.getDefaultCell().setBorder(Rectangle.BOTTOM);
//                Image izq = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/logocbq.png"));
//                Image der = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/escuela.png"));
                // izq.scaleAbsolute(70f, 70f);
//                izq.scaleAbsolute(45f, 22f);

                PdfPCell cell = new PdfPCell();
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(titulo, new Font(Font.TIMES_ROMAN, 10, Font.BOLD)));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table.addCell(cell);

//                cell = new PdfPCell(der);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                cell = new PdfPCell(new Phrase("USUARIO : " + usuario + "\n FECHA : " + sdf.format(new Date()), new Font(Font.TIMES_ROMAN, 6, Font.BOLD)));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                table.writeSelectedRows(0, -1, 30, 580, writer.getDirectContent());
//                table.writeSelectedRows(0, -1, 30, 803, writer.getDirectContent());
//                for (String s:titulos){
//                     document.add(new Paragraph(s,new Font(Font.TIMES_ROMAN, 8, Font.BOLD)));
//                }
                ColumnText.showTextAligned(
                        writer.getDirectContent(),
                        Element.ALIGN_CENTER,
                        new Phrase(String.format("%d", writer.getPageNumber()), new Font(Font.TIMES_ROMAN, 6, Font.BOLD)),
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 10, 0);

                Image header = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/cabecera_02.png"));
                Image footer = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/footer_02.png"));
                Image background = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/imagenes/back_03.png"));

                float height = document.getPageSize().getHeight();
                float width = document.getPageSize().getWidth();

                writer.getDirectContent().addImage(header, width, 0f, 0f, width / 6f, 0f, height - (width / 6f));
                writer.getDirectContentUnder().addImage(background, width, 0f, 0f, height, 0f, 0f);
                writer.getDirectContent().addImage(footer, width, 0f, 0f, width / 19.65f, 0f, 0f);
            } catch (BadElementException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DocumentoPDF.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.TIMES_ROMAN, 6, Font.BOLD)),
                    2, 4, 0);
        }
    }
}


