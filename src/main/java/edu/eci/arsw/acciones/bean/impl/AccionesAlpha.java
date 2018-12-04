/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.acciones.bean.impl;

import edu.eci.arsw.acciones.model.HttpConnection;
import edu.eci.arsw.acciones.bean.Acciones;
import java.io.IOException;
import org.springframework.stereotype.Service;

/**
 *
 * @author 2104481
 */
@Service("Alpha")
public class AccionesAlpha implements Acciones {

    @Override
    public String getAcciones(String tipo, String nombreAccion) throws IOException {
        return HttpConnection.getUrlData("https://www.alphavantage.co/query?function=" + tipo + "&symbol=" + nombreAccion + "&interval=5min&apikey=Q1QZFVJQ21K7C6XM");
    }

    @Override
    public String buscarAcciones(String palabraClave) throws IOException {
        return HttpConnection.getUrlData("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + palabraClave + "&apikey=5min&apikey=Q1QZFVJQ21K7C6XM");
    }

}
