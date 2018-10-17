package msg;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "loginBean"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/DemoTopic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/DemoTopic"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class LoginMessageBean implements MessageListener {

    @EJB
    private CollectionBean bean;

    public LoginMessageBean() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            String msg = ((TextMessage) message).getText();
            if (msg.startsWith("login")) {
                String login = msg.split(":")[1];
                bean.add(login);
            }
        } catch (JMSException ex) {
            System.out.println("Login Bean Error: " + ex.getMessage());
        }
    }
}
