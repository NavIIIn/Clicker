/*
  This servlet saves/updates the score of users as well as informing
  the client what the previous saved score was.
*/

import java.io.*;
import java.sql.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ScoreKeeper extends HttpServlet {

    // SQL methods
    public String selectStmt(Statement stmt, String name) throws SQLException
    {
	ResultSet rset = stmt.executeQuery("select score from clicks where name='"
					   + name + "'");
	if(!rset.next())
	    return "null";
	else
	    return Integer.toString(rset.getInt("score"));
    }

    public void updateStmt(Statement stmt, String name, String score) throws SQLException
    {
	stmt.executeUpdate("update clicks set score=" + score 
			   + " where name='" + name + "'");
    }

    public void insertStmt(Statement stmt, String name) throws SQLException
    {
	stmt.executeUpdate("insert into clicks values('" + name + "', 0)");
    }

    // core method
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
 
	Connection conn = null;
	Statement stmt = null;
	try {
	    conn = DriverManager.getConnection(
					       "jdbc:mysql://localhost:3306/db?useSSL=false", "root", "");
            // database-URL(hostname, port, default database), username, password
 
	    stmt = conn.createStatement();
 
	    String name = request.getParameter("name");
	    String score = request.getParameter("score");

	    // Either get score for initial login or
	    // save score for update
	    if(score.equals("0"))
		score = selectStmt(stmt, name);
	    else
		updateStmt(stmt, name, score);

	    // If new player insert name
	    if(score.equals("null"))
		insertStmt(stmt, name);

	    
	    out.println(score);
 
	} catch (SQLException ex) {
	    ex.printStackTrace();
	} finally {
	    out.close();  // Close the output writer
	    try {
		if (stmt != null) stmt.close();
		if (conn != null) conn.close();
	    } catch (SQLException ex) {
		ex.printStackTrace();
	    }
	}
    }
}
