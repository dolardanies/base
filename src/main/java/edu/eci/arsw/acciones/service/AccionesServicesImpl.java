/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.acciones.service;

import edu.eci.arsw.acciones.bean.Acciones;
import edu.eci.arsw.acciones.model.Accion;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import edu.eci.arsw.acciones.persistence.AccionesRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author 2104481
 */
@Service
public class AccionesServicesImpl implements AccionesServices {

    @Autowired
    @Qualifier("Alpha")
    Acciones acciones;

    @Autowired
    AccionesRepository repositorio;

    @Override
    public String getAcciones(String tipo, String nombreAccion) throws IOException {
        String respuesta;
        Accion accionCache = repositorio.findByNombre(nombreAccion);
        if (accionCache == null) {
            System.out.println("No tiene la accion!");
            ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<>();
            respuesta = acciones.getAcciones(tipo, nombreAccion);
            //Se busca en la respuesta que hace el API si hay algun error como los que se muestran a continuacion
            if (respuesta.toLowerCase().contains("\"error message\"")) {
                throw new IOException("Hay dificultades con el API externo.");
            } else if (respuesta.toLowerCase().contains("\"note\"")) {
                throw new IOException("Al parecer esa acción no existe.");
            }
            hm.put(tipo, respuesta);
            repositorio.save(new Accion(nombreAccion, hm));
        } else if (accionCache.getTimeSeries().containsKey(tipo)) {
            System.out.println("Tiene la accion y el tipo!");
            respuesta = accionCache.getTimeSeries().get(tipo);
            //Obtener la fecha acual
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            Date fechaActual = null;
            //Obtener la fecha del ultimo registro almacenado
            JSONObject jsonObj = new JSONObject(respuesta.toLowerCase());
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
            String strFecha = jsonObj.getJSONObject("meta data").getString("3. last refreshed");
            Date fechaCache = null;
            //Parsear los strings antes tomados a fecha
            try {
                fechaCache = formatoDelTexto.parse(strFecha);
                fechaActual = formatoDelTexto.parse(dtf.format(now));
            } catch (ParseException ex) {
                Logger.getLogger(AccionesServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Si la fecha actual es mayor a la fecha del ultimo registro almacenado se actualiza
            if (fechaActual.compareTo(fechaCache) > 0) {
                respuesta = acciones.getAcciones(tipo, nombreAccion);
                accionCache.getTimeSeries().put(tipo, respuesta);
                repositorio.save(accionCache);
            }
        } else {
            System.out.println("Tiene la accion pero no el tipo!");
            respuesta = acciones.getAcciones(tipo, nombreAccion);
            accionCache.getTimeSeries().put(tipo, respuesta);
            repositorio.save(accionCache);
        }
        return respuesta;
    }

    @Override
    public String buscarAcciones(String palabraClave) throws IOException {
        return acciones.buscarAcciones(palabraClave);
    }
}
