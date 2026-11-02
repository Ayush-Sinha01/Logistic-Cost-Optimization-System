
package com.logistics.servlet;

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

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/api/optimize")
public class OptimizationServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String shipmentId = request.getParameter("shipmentId");

        if (shipmentId == null || shipmentId.trim().isEmpty()) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Shipment ID is required"
            );
            return;
        }

        String sql =
            "SELECT r.route_id, r.source, r.destination, " +
            "r.distance, r.transport_mode, " +
            "c.fuel_cost_per_km, c.driver_charges, c.other_charges, " +
            "(r.distance * c.fuel_cost_per_km + " +
            "c.driver_charges + c.other_charges) AS total_cost " +
            "FROM routes r " +
            "JOIN transportation_cost c ON r.route_id = c.route_id " +
            "JOIN shipments s ON s.source = r.source " +
            "AND s.destination = r.destination " +
            "WHERE s.shipment_id = ? " +
            "ORDER BY total_cost ASC";

        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(shipmentId));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Map<String, Object> route = new HashMap<>();

                    route.put("routeId", rs.getInt("route_id"));
                    route.put("source", rs.getString("source"));
                    route.put("destination", rs.getString("destination"));
                    route.put("distance", rs.getDouble("distance"));
                    route.put("transportMode", rs.getString("transport_mode"));
                    route.put("totalCost", rs.getDouble("total_cost"));

                    results.add(route);
                }
            }

            PrintWriter out = response.getWriter();
            out.print(gson.toJson(results));
            out.flush();

        } catch (Exception e) {
            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            e.printStackTrace();
        }
    }
}
