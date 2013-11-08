/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piscke.business.batch;

import piscke.business.dao.FuncionarioDAO;
import piscke.business.vo.Funcionario;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Properties;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author leandro.piscke
 */
@Dependent
@Named("DemitirTodosReader")
public class DemitirTodosReader extends AbstractItemReader {

    private Iterator<Funcionario> funcionariosAtivos;
    private Properties partParams;

    @Inject
    JobContext jobCtx;

    @EJB
    FuncionarioDAO funcionarioDAO;

    @Override
    public void open(Serializable ckpt) throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long execID = jobCtx.getExecutionId();
        partParams = jobOperator.getParameters(execID);
        /* Get the range of items to work on in this partition */
        int firstItem = ((Long) partParams.get("firstItem")).intValue();
        int numItems = ((Long) partParams.get("numItems")).intValue();

        funcionariosAtivos = funcionarioDAO.buscaPaginado(firstItem, numItems).iterator();
    }

    @Override
    public Object readItem() throws Exception {
        if (funcionariosAtivos.hasNext()) {
            return funcionariosAtivos.next();
        } else {
            return null;
        }
    }
}
