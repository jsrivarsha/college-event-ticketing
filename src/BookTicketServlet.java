package com.college;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/bookTicket")
public class BookTicketServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int eventId = Integer.parseInt(request.getParameter("eventId"));
        String name = request.getParameter("name");
        String rollNo = request.getParameter("rollNo");
        String email = request.getParameter("email");

        String updateQuery = "UPDATE events SET available_seats = available_seats - 1 WHERE event_id = ? AND available_seats > 0";
        String insertQuery = "INSERT INTO bookings (ticket_code, event_id, student_name, student_roll_no, student_email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            updateStmt.setInt(1, eventId);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                String ticketCode = "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                insertStmt.setString(1, ticketCode);
                insertStmt.setInt(2, eventId);
                insertStmt.setString(3, name);
                insertStmt.setString(4, rollNo);
                insertStmt.setString(5, email);
                insertStmt.executeUpdate();

                out.println("<html><head><title>Booking Confirmed</title></head><body style='font-family:Arial; text-align:center; padding-top:50px;'>");
                out.println("<h2 style='color:green;'>Booking Successful!</h2>");
                out.println("<p>Thank you, <b>" + name + "</b>!</p>");
                out.println("<p>Your Unique Ticket Code: <b>" + ticketCode + "</b></p>");
                out.println("<p>Confirmation sent to: <b>" + email + "</b></p>");
                out.println("<br><a href='index.html'>Book Another Seat</a>");
                out.println("</body></html>");
            } else {
                out.println("<html><body style='font-family:Arial; text-align:center; padding-top:50px;'>");
                out.println("<h2 style='color:red;'>Booking Failed: No available seats for this event!</h2>");
                out.println("<a href='index.html'>Try Again</a>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Error processing your booking. Please check database logs.</h2>");
        }
    }
}