/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.acciones.service;

import java.io.IOException;

/**
 *
 * @author 2104481
 */
public interface AccionesServices {

    /**
     *
     * @param tipo
     * @param nombreAccion
     * @return
     * @throws IOException
     */
    public String getAcciones(String tipo, String nombreAccion) throws IOException;

    /**
     *
     * @param palabraClave
     * @return
     * @throws IOException
     */
    public String buscarAcciones(String palabraClave) throws IOException;
}
