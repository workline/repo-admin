package repo.admin.service;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import loggee.api.Logged;

@Logged
@Stateless
public class MessagingService {
    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:/queue/repoEvent")
    private Queue queue;

    public void sendChangeEvent(Long repoItemId, String attributeName, Object oldValue, Object newValue) {
        try {
            System.err.println("sendChangeEvent");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);

            TextMessage message = session.createTextMessage("Attribute {" + attributeName + "} of repo item {" + repoItemId + "} has been changed from "
                    + oldValue + " to " + newValue);
            producer.send(message);
        } catch (JMSException e) {
            throw new RuntimeException("Couldn't send text message!", e);
        }

    }
}
