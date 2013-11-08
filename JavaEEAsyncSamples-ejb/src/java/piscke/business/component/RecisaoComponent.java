/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piscke.business.component;

import piscke.business.dao.FuncionarioDAO;
import piscke.business.vo.Funcionario;
import java.util.Properties;
import java.util.concurrent.Future;
import javax.annotation.Resource;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 *
 * @author leandro.piscke
 */
@Stateless
@LocalBean
public class RecisaoComponent {

    @Inject
    FuncionarioDAO funcionarioDAO;

    public void calcularDemissao(Funcionario funcionario) throws InterruptedException {
        if (funcionario != null) {
            funcionario.setAtivo(false);
            System.out.println(String.format("calculando: %s - %s", funcionario.getNome(), funcionario.getId()));

            Thread.sleep(10000);
        }
    }

    public void demitir(long funcionarioId) throws InterruptedException {
        System.out.println("----------Demitir síncrono----------");

        Funcionario funcionario = funcionarioDAO.buscaPorId(funcionarioId);
        calcularDemissao(funcionario);
        funcionarioDAO.persistir(funcionario);
    }

    @Resource
    private SessionContext sessionContext;

    @Asynchronous
    public Future<String> demitirAssincrono(long funcionarioId) {
        System.out.println("----------Demitir assíncrono----------");
        
        String status = "demitido";
        try {
            Funcionario funcioario = funcionarioDAO.buscaPorId(funcionarioId);

            calcularDemissao(funcioario);
            if (!sessionContext.wasCancelCalled()) {
                funcionarioDAO.persistir(funcioario);
            } else {
                funcionarioDAO.cancelar();
                status = "cancelado";
            }

        } catch (InterruptedException ex) {
        }
        
        return new AsyncResult(status);
    }

    @Resource(mappedName = "jms/demitir")
    private Queue demitir;
    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;

    public void enviarMensagemParaDemitir(Long funcionarioId) {
        System.out.println("----------Demitir via mensagem----------");
        
        context.createProducer().send(demitir, funcionarioId.toString());
    }

    public long disparaProcessoDemitirTodos() throws Exception {
        System.out.println("----------Demissão em massa via batch processing----------");

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        return jobOperator.start("DemitirTodosFuncionarios", new Properties());
    }
}
