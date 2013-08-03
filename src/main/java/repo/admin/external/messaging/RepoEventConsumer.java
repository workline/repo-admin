package repo.admin.external.messaging;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import loggee.api.Logged;

@Logged
@MessageDriven(mappedName = "RepoEventListener", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/repoEvent") })
public class RepoEventConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            System.err.println("Received message: " + ((TextMessage) message).getText());
        } catch (JMSException e) {
            throw new RuntimeException("Couldn't read text message!", e);
        }
    }

}
