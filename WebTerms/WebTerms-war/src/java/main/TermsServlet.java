package main;

import data.DataBeanLocal;
import data.Definition;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jpa.Tbldefinitions;
import jpa.Tblterms;
import jpa.TbltermsFacadeLocal;

@WebServlet(name = "TermsServlet", urlPatterns = {"/Terms"})
public class TermsServlet extends HttpServlet {

    @EJB
    private DataBeanLocal dataBean;
    
    @EJB
    private TbltermsFacadeLocal termBean;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String term = request.getParameter("term");
        String definition = "";
        boolean prevDisabled = true;
        boolean nextDisabled = true;

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (request.getParameter("delete") != null) {
                if (dataBean.delete(term)) {
                    term = "";
                    definition = "";
                    getServletContext().setAttribute("listItems", null);
                    getServletContext().setAttribute("currentIndex", 0);
                } else {
                    definition = "Problem to delete term. Try later.";
                }
            } else {
                List<Tbldefinitions> items = null;
                int currentIndex = 0;

                if (request.getParameter("find") != null) {
                    Tblterms data = termBean.findByTerm(term);
                    definition = "Definition not found. Enter new definition and press Add.";
                    if (data != null && !data.getTbldefinitionsList().isEmpty()) {
                        items = data.getTbldefinitionsList();
                        getServletContext().setAttribute("listItems", items);
                        getServletContext().setAttribute("currentIndex", currentIndex);
                        definition = items.get(currentIndex).getDefinition();
                    }
                } else if (request.getParameter("next") != null) {
                    items = (List<Tbldefinitions>) getServletContext().getAttribute("listItems");
                    currentIndex = (int) getServletContext().getAttribute("currentIndex");
                    if (items != null && items.size() > 1 && currentIndex < items.size() - 1) {
                        currentIndex++;
                        getServletContext().setAttribute("currentIndex", currentIndex);
                        definition = items.get(currentIndex).getDefinition();
                    }
                } else if (request.getParameter("prev") != null) {
                    items = (List<Tbldefinitions>) getServletContext().getAttribute("listItems");
                    currentIndex = (int) getServletContext().getAttribute("currentIndex");
                    if (items != null && items.size() > 1 && currentIndex > 0) {
                        currentIndex--;
                        getServletContext().setAttribute("currentIndex", currentIndex);
                        definition = items.get(currentIndex).getDefinition();
                    }
                } else if (request.getParameter("add") != null) {
                    definition = request.getParameter("def");
                    if (definition != null && !definition.trim().isEmpty()) {
                        if (dataBean.add(term, definition)) {
                            //items = dataBean.getTermDefinitions(term);
                            currentIndex = items.size() - 1;
                            getServletContext().setAttribute("listItems", items);
                            getServletContext().setAttribute("currentIndex", currentIndex);
                            definition = items.get(currentIndex).getDefinition();
                        } else {
                            definition = "Problem to add new definition. Try later.";
                        }
                    } else {
                        definition = "Definition can't be empty. Enter definition and press Add.";
                    }
                } else if (request.getParameter("update") != null) {
                    definition = request.getParameter("def");
                    currentIndex = (int) getServletContext().getAttribute("currentIndex");
                    items = (List<Tbldefinitions>) getServletContext().getAttribute("listItems");
                    int definitionId = items.get(currentIndex).getId();
                    if (definition != null && !definition.trim().isEmpty()) {
                        if (dataBean.updateDefinition(new Definition(definitionId, definition))) {
                            //items = dataBean.getTermDefinitions(term);
                            getServletContext().setAttribute("listItems", items);
                        } else {
                            definition = "Problem to update definition. Try later.";
                        }
                    } else {
                        definition = "Definition can't be empty. Enter definition and press Update.";
                    }
                }

                prevDisabled = disablePrevButton(currentIndex, items);
                nextDisabled = disableNextButton(currentIndex, items);
            }

            out.println(getPage(getForm(term, definition, prevDisabled, nextDisabled)));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String getPage(String form) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<title>JHelp Terms</title>")
                .append("</head>")
                .append("<body>")
                .append("<h1>JHelp Web</h1>")
                .append("<br/>")
                .append(form)
                .append("</body>")
                .append("</html>");
        return sb.toString();
    }
    
    private boolean disablePrevButton(int index, List<?> list) {
        return list == null || list.size() <= 1 || (list.size() > 1 && index == 0);
    }

    private boolean disableNextButton(int index, List<?> list) {
        return list == null || list.size() <= 1 || (list.size() > 1 && index == list.size() - 1);
    }

    private String getForm(String term, String definition, boolean prevDisabled, boolean nextDisabled) {
        StringBuilder sb = new StringBuilder();
        sb.append("<form action='Terms' method='POST'>")
                .append("Term: <input type='text' name='term' required value='")
                .append(term)
                .append("'/> &nbsp;<input type='submit' name='find' value='Find'/>")
                .append("<br/><br/>")
                .append("<textarea name='def' rows='10' cols='60'>")
                .append(definition)
                .append("</textarea>")
                .append("<br/>")
                .append("<input type='submit' name='prev' value='Prev' ");
        if (prevDisabled) {
            sb.append("disabled");
        }
        sb.append("/>&nbsp;")
                .append("<input type='submit' name='next' value='Next' ");
        if (nextDisabled) {
            sb.append("disabled");
        }
        sb.append("/>")
                .append("<br/><br/>")
                .append("<input type='submit' name='add' value='Add'/>&nbsp;")
                .append("<input type='submit' name='update' value='Update'/>&nbsp;")
                .append("<input type='submit' name='delete' value='Delete'/>")
                .append("</form>");
        return sb.toString();
    }
}
