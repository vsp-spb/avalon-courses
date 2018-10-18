package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jpa.Tbldefinitions;
import jpa.TbldefinitionsFacadeLocal;
import jpa.Tblterms;
import jpa.TbltermsFacadeLocal;

@WebServlet(name = "TermsServlet", urlPatterns = {"/Terms"})
public class TermsServlet extends HttpServlet {

    @EJB
    private TbltermsFacadeLocal termBean;

    @EJB
    private TbldefinitionsFacadeLocal defBean;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            FormData data = null;
            if (request.getParameter("find") != null) {
                data = processFind(request);
            } else if (request.getParameter("next") != null) {
                data = processNext(request);
            } else if (request.getParameter("prev") != null) {
                data = processPrev(request);
            } else if (request.getParameter("add") != null) {
                data = processAdd(request);
            } else if (request.getParameter("update") != null) {
                data = processUpdate(request);
            } else if (request.getParameter("delete") != null) {
                data = processDelete(request);
            }

            if (data != null) {
                out.println(getPage(getForm(data)));
            }
        }
    }

    private FormData processFind(HttpServletRequest request) {
        String term = request.getParameter("term");
        String definition = "Definition not found. Enter new definition and press Add.";
        boolean prevDisabled = true;
        boolean nextDisabled = true;

        Tblterms termItem = termBean.findByTerm(term);
        if (termItem != null && !termItem.getTbldefinitionsList().isEmpty()) {
            List<Tbldefinitions> items = termItem.getTbldefinitionsList();
            int currentIndex = 0;
            getServletContext().setAttribute("listItems", items);
            getServletContext().setAttribute("currentIndex", currentIndex);

            definition = items.get(currentIndex).getDefinition();
            prevDisabled = disablePrevButton(currentIndex, items);
            nextDisabled = disableNextButton(currentIndex, items);
        }

        FormData data = new FormData(term, definition, prevDisabled, nextDisabled);

        return data;
    }

    private FormData processNext(HttpServletRequest request) {
        String term = request.getParameter("term");
        String definition = "";

        List<Tbldefinitions> items = (List<Tbldefinitions>) getServletContext().getAttribute("listItems");
        int currentIndex = (int) getServletContext().getAttribute("currentIndex");
        if (items != null && items.size() > 1 && currentIndex < items.size() - 1) {
            currentIndex++;
            getServletContext().setAttribute("currentIndex", currentIndex);
            definition = items.get(currentIndex).getDefinition();
        }

        boolean prevDisabled = disablePrevButton(currentIndex, items);
        boolean nextDisabled = disableNextButton(currentIndex, items);

        FormData data = new FormData(term, definition, prevDisabled, nextDisabled);

        return data;
    }

    private FormData processPrev(HttpServletRequest request) {
        String term = request.getParameter("term");
        String definition = "";

        List<Tbldefinitions> items = (List<Tbldefinitions>) getServletContext().getAttribute("listItems");
        int currentIndex = (int) getServletContext().getAttribute("currentIndex");
        if (items != null && items.size() > 1 && currentIndex > 0) {
            currentIndex--;
            getServletContext().setAttribute("currentIndex", currentIndex);
            definition = items.get(currentIndex).getDefinition();
        }

        boolean prevDisabled = disablePrevButton(currentIndex, items);
        boolean nextDisabled = disableNextButton(currentIndex, items);

        FormData data = new FormData(term, definition, prevDisabled, nextDisabled);

        return data;
    }

    private FormData processAdd(HttpServletRequest request) {
        String term = request.getParameter("term");
        String definition = request.getParameter("def");

        boolean prevDisabled = true;
        boolean nextDisabled = true;

        if (definition != null && !definition.trim().isEmpty()) {
            Tbldefinitions newDefinition = new Tbldefinitions();
            newDefinition.setDefinition(definition);

            List<Tbldefinitions> items;

            Tblterms data = termBean.findByTerm(term);
            boolean callCreate = false;
            if (data != null) {
                items = data.getTbldefinitionsList();
            } else {
                callCreate = true;
                data = new Tblterms();
                data.setTerm(term);
                items = new ArrayList<>();
            }

            newDefinition.setTermId(data);
            items.add(newDefinition);
            data.setTbldefinitionsList(items);

            if (callCreate) {
                termBean.create(data);
            } else {
                termBean.edit(data);
            }

            int currentIndex = items.size() - 1;
            getServletContext().setAttribute("listItems", items);
            getServletContext().setAttribute("currentIndex", currentIndex);
            definition = items.get(currentIndex).getDefinition();

            prevDisabled = disablePrevButton(currentIndex, items);
            nextDisabled = disableNextButton(currentIndex, items);
        } else {
            definition = "Definition can't be empty. Enter definition and press Add.";
        }

        FormData data = new FormData(term, definition, prevDisabled, nextDisabled);

        return data;
    }

    private FormData processUpdate(HttpServletRequest request) {
        String term = request.getParameter("term");
        String definition = request.getParameter("def");
        int currentIndex = (int) getServletContext().getAttribute("currentIndex");
        List<Tbldefinitions> items = (List<Tbldefinitions>) getServletContext().getAttribute("listItems");
        Tbldefinitions item = items.get(currentIndex);
        if (definition != null && !definition.trim().isEmpty()) {
            item.setDefinition(definition);
            defBean.edit(item);
            Tblterms data = termBean.findByTerm(term);
            getServletContext().setAttribute("listItems", data.getTbldefinitionsList());
        } else {
            definition = "Definition can't be empty. Enter definition and press Update.";
        }

        boolean prevDisabled = disablePrevButton(currentIndex, items);
        boolean nextDisabled = disableNextButton(currentIndex, items);

        FormData data = new FormData(term, definition, prevDisabled, nextDisabled);

        return data;
    }

    private FormData processDelete(HttpServletRequest request) {
        String term = request.getParameter("term");
        termBean.deleteByTerm(term);
        term = "";
        String definition = "";
        getServletContext().setAttribute("listItems", null);
        getServletContext().setAttribute("currentIndex", 0);

        return new FormData(term, definition, true, true);
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

    private String getForm(FormData data) {
        StringBuilder sb = new StringBuilder();
        sb.append("<form action='Terms' method='POST'>")
                .append("Term: <input type='text' name='term' required value='")
                .append(data.getTerm())
                .append("'/> &nbsp;<input type='submit' name='find' value='Find'/>")
                .append("<br/><br/>")
                .append("<textarea name='def' rows='10' cols='60'>")
                .append(data.getDefinition())
                .append("</textarea>")
                .append("<br/>")
                .append("<input type='submit' name='prev' value='Prev' ");
        if (data.isPrevDisabled()) {
            sb.append("disabled");
        }
        sb.append("/>&nbsp;")
                .append("<input type='submit' name='next' value='Next' ");
        if (data.isNextDisabled()) {
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
