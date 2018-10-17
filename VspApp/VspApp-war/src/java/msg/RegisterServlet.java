package msg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/Register"})
public class RegisterServlet extends HttpServlet {

    @EJB
    private CollectionBean collection;

    @Resource(lookup = "jms/TopicFactory")
    private TopicConnectionFactory factory;

    @Resource(lookup = "jms/DemoTopic")
    private Topic topic;

    private TopicConnection con;
    private TopicSession session;
    private TopicPublisher publisher;

    private String error;

    @Override
    public void init() throws ServletException {
        if (factory != null) {
            try {
                con = factory.createTopicConnection();
                session = con.createTopicSession(true, 0);
                publisher = session.createPublisher(topic);
            } catch (JMSException ex) {
                error = "<p style='color:red;'>RegisterServlet Init Error: " + ex.getMessage() + "</p>";
            }
        }
    }

    @Override
    public void destroy() {
        if (con != null) {
            try {
                con.close();
            } catch (JMSException ex) {
                System.out.println("*******************\nRegisterServlet Destroy Error: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String htmlList = "";
        if (request.getParameter("list") != null) {
            htmlList = getHtmlList();
        } else {
            String login = request.getParameter("loginName");

            if (login == null || login.trim().equals("")) {
                System.out.println("Login is empty!!!");
                response.sendRedirect("index.html");
                return;
            } else if (factory == null) {
                System.out.println("Factory is null!!!");
                response.sendRedirect("index.html");
                return;
            } else if (topic == null) {
                System.out.println("Topic is null!!!");
                response.sendRedirect("index.html");
                return;
            }

            try {
                String msg = "";
                if (request.getParameter("login") != null) {
                    msg = "login:" + login;
                } else if (request.getParameter("logout") != null) {
                    msg = "logout:" + login;
                }
                if (!msg.isEmpty()) {
                    TextMessage message = session.createTextMessage(msg);
                    publisher.publish(message);
                }
            } catch (JMSException ex) {
                error = "<p style='color:red;'>RegisterServlet LogIn Error: " + ex.getMessage() + "</p>";
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>LogAppGenerated</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div>");
            out.println("<h1>Log Application</h1>");
            out.println("<br/>");
            out.println(getForm());
            if (!htmlList.isEmpty()) {
                out.println(htmlList);
            }
            if (error != null && !error.isEmpty()) {
                out.println(error);
                error = "";
            }
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String getHtmlList() {
        Set<String> items = collection.getList();
        StringBuilder sb = new StringBuilder();
        sb.append("<p>\n").append("<ol>\n");
        items.forEach((item) -> {
            sb.append("<li>").append(item).append("</li>\n");
        });
        sb.append("</ol>\n").append("</p>\n");
        return sb.toString();
    }

    private String getForm() {
        StringBuilder sb = new StringBuilder();

        sb.append("<form action='Register' method='GET'>")
                .append("<p>")
                .append("Login: <input type='text' name='loginName' size='20' />")
                .append("</p>")
                .append("<br/>")
                .append("<p>")
                .append("<input type='submit' name='login' value='Login' />")
                .append("<input type='submit' name='logout' value='Logout' />")
                .append("<input type='submit' name='list' value='List' />")
                .append("</p>")
                .append("</form>");

        return sb.toString();
    }
}
