package controller.restaurant;

import dbconnection.DbConnection;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.order.Order;
import models.user.Restaurant;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@WebServlet(name = "Restaurants Landing", value = "/restaurant-landing")
public class restaurantLanding extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // Gets the restaurant from the db
        // TODO: Change with retrieving from session
        Document to_find = new Document("_id", new ObjectId("6645179c3418530bd4e7e158"));
        Document found = DbConnection.findOne(
                "restaurants",
                to_find
        );
        Restaurant resto = new Restaurant(found);

        // Makes a hashmap with list of all status and a list of all orders
        HashMap<String, ArrayList<Order>> order_hash = new HashMap<String, ArrayList<Order>>();
        for (Order ord : resto.getListOrders()) {
            if (order_hash.get(ord.getStatus()) != null) {
                order_hash.get(ord.getStatus()).add(ord);
            } else {
                ArrayList<Order> new_stat = new ArrayList<Order>();
                new_stat.add(ord);
                order_hash.put(ord.getStatus(), new_stat);
            }
        }

        // Sets attributes for the view
        req.setAttribute("user", resto);
        req.setAttribute("items_to_scroll", order_hash);
        System.out.println(order_hash);

        RequestDispatcher dispatcher = req
                .getRequestDispatcher("/views/homepages/restaurant_orders_pending.jsp");

        dispatcher.forward(req, resp);

    }

    public void destroy() {
    }
}