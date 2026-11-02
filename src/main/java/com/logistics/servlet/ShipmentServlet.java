
package com.logistics.servlet;

import com.google.gson.Gson;
import com.logistics.model.Shipment;
import com.logistics.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/shipments")
public class ShipmentServlet extends HttpServlet {

    private Gson gson = new Gson();

    // Get all shipments
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Shipment> shipments = new ArrayList<>();

        String sql = "SELECT * FROM shipments";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Shipment shipment = new Shipment();

                shipment.setShipmentId(rs.getInt("shipment_id"));
                shipment.setSource(rs.getString("source"));
                shipment.setDestination(rs.getString("destination"));
                shipment.setWeight(rs.getDouble("weight"));
                shipment.setShipmentDate(rs.getDate("shipment_date"));
                shipment.setStatus(rs.getString("status"));

                shipments.add(shipment);
            }

            PrintWriter out = response.getWriter();
            out.print(gson.toJson(shipments));
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    // Add a new shipment
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

           com.google.gson.JsonObject json =
    gson.fromJson(request.getReader(), com.google.gson.JsonObject.class);

Shipment shipment = new Shipment();

shipment.setSource(json.get("source").getAsString());
shipment.setDestination(json.get("destination").getAsString());
shipment.setWeight(json.get("weight").getAsDouble());

shipment.setShipmentDate(
    Date.valueOf(json.get("shipmentDate").getAsString())
);

            String sql = "INSERT INTO shipments " +
                    "(source, destination, weight, shipment_date, status) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, shipment.getSource());
                ps.setString(2, shipment.getDestination());
                ps.setDouble(3, shipment.getWeight());
                ps.setDate(4, shipment.getShipmentDate());
                ps.setString(5, "Pending");

                ps.executeUpdate();
            }

            response.setStatus(HttpServletResponse.SC_CREATED);

            PrintWriter out = response.getWriter();
            out.print("{\"message\":\"Shipment added successfully\"}");
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
