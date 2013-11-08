/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package piscke.business.batch;

import piscke.business.dao.FuncionarioDAO;
import piscke.business.vo.Funcionario;
import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 *
 * @author leandro.piscke
 */
@Dependent
@Named("DemitirTodosWriter")
public class DemitirTodosWriter extends AbstractItemWriter{
 
    @EJB
    FuncionarioDAO funcionarioDAO;

    @Override
    public void writeItems(List<Object> items) throws Exception {
        for (Object obj : items){
            Funcionario funcionario = (Funcionario) obj;

            funcionarioDAO.merge(funcionario);
        }
    }
}
