/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piscke.business.consumer;

import piscke.business.component.RecisaoComponent;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author leandro.piscke
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/demitir")
})
public class RecisaoConsumer implements MessageListener {

    public RecisaoConsumer() {
    }

    @EJB
    RecisaoComponent recisao;

    @Override
    public void onMessage(Message message) {
        TextMessage tmsg = (TextMessage) message;

        try {
            System.out.println("Mensagem recebida");

            long funcionarioId = Long.parseLong(tmsg.getText());
            recisao.demitir(funcionarioId);
            
            System.out.println("Mensagem processada");

        } catch (Exception ex) {
            System.out.println("Erro ao processar mensagem");
        }
    }
}
