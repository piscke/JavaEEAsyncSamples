/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piscke.business.batch;

import piscke.business.component.RecisaoComponent;
import piscke.business.vo.Funcionario;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 *
 * @author leandro.piscke
 */
@Dependent
@Named("DemitirTodosProcessor")
public class DemitirTodosProcessor implements javax.batch.api.chunk.ItemProcessor {

    @EJB
    RecisaoComponent recisao;
    

    @Override
    public Object processItem(Object obj) throws Exception {
        Funcionario funcionario = (Funcionario) obj;
        
        recisao.calcularDemissao(funcionario);
        
        return funcionario;
    }
}
