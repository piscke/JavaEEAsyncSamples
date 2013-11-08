/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package piscke.business.batch;

import java.io.Serializable;

/**
 *
 * @author leandro.piscke
 */
public class DemitirTodosCheckpoint implements Serializable{
    private long ultimoIdProcessado = 0;
    public void setUltimoIdProcessado(long ultimoId) { ultimoIdProcessado = ultimoId; }
    public long getUltimoIdProcessado() { return ultimoIdProcessado; }
}
