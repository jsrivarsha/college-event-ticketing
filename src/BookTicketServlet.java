package com.college;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/bookTicket")
public class BookTicketServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int eventId = Integer.parseInt(request.getParameter("eventId"));
        String name = request.getParameter("name");
        String rollNo = request.getParameter("rollNo");
        String email = request.getParameter("email");

        Connection conn = DBConnection.getConnection();

        try {
            // 1. Decrease seat count dynamically (prevents overbooking)
            String updateQuery = "UPDATE events SET available_seats = available_seats - 1 WHERE event_id = ? AND available_seats > 0";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, eventId);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                // 2. Generate unique Ticket Code
                String ticketCode = "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                // 3. Insert record into database
                String insertQuery = "INSERT INTO bookings (ticket_code, event_id, student_name, student_roll_no, student_email) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, ticketCode);
                insertStmt.setInt(2, eventId);
                insertStmt.setString(3, name);
                insertStmt.setString(4, rollNo);
                insertStmt.setString(5, email);
                insertStmt.executeUpdate();

                // 4. Output response to user
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
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Error processing your booking. Please check database logs.</h2>");
        }
    }
}