package controller.consumer;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.ui_util.ItemBoxUi;
import models.user.Consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@WebServlet(name = "Consumer Landing", value = "/customer-landing")
public class consumerLanding extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // Check if login
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("accountType") != "customer") {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        Consumer user = (Consumer) session.getAttribute("user");

        HashMap<String, ArrayList<ItemBoxUi>> resto_hash = user.get_restaurant_options();
        HashMap<String, ArrayList<ItemBoxUi>> orders_scroll = user.get_recent_orders();

        // Sets attributes for the view
        req.setAttribute("orders_to_scroll", orders_scroll);
        req.setAttribute("tags_to_scroll", resto_hash);

        RequestDispatcher dispatcher = req
                .getRequestDispatcher("/views/homepages/consumer_dashboard.jsp");

        dispatcher.forward(req, resp);

    }

    public void destroy() {
    }
}
