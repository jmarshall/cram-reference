package uk.ac.ebi.ena.cram.checksum;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.tomcat.dbcp.dbcp.DelegatingConnection;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.OracleConnection;
import oracle.sql.CLOB;
import uk.ac.ebi.ena.cram.rest.ChecksumService;

public class ChecksumServlet extends HttpServlet{
    
    private static final long serialVersionUID = 1L;
    public static final String ETA_JNDI_JDBC = "jdbc/etareader_eta";
    static Logger logger = Logger.getLogger(ChecksumService.class);
    public static final String INITIAL_JNDI_CONTEXT = "java:comp/env";

    /**
     * handle rest get
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        resp.setContentType("text/plain");
        String uriString = req.getRequestURI();
        String [] uriSplitString = uriString.split("/");
        String method = null;
        String checksum = null;
        Connection connection = null;
        
        if (uriSplitString.length == 5) {
            method = uriSplitString[3];
            checksum = uriSplitString[4];
        } else if (uriSplitString.length == 4){
            // test used by nagios
            if ("test".equals(uriSplitString[3])) {
                connection = getConnectionFromContext(ETA_JNDI_JDBC);
                if (connection != null) {
                    resp.getOutputStream().print("OK");
                    return;
                } else {
                    resp.sendError(404);
                    return;
                }
            }
        }
  
        StringBuffer buffer = new StringBuffer();
        
        if (method.equals("md5")) {
            buffer.append("select md5 as md5Checksum, ");
            buffer.append("sha1 as sha1Checksum, ");
            buffer.append("seq as sequence ");
            buffer.append("from ");
            buffer.append("eta.reference ");
            buffer.append("where ");
            buffer.append("md5 = ?");
        } else if (method.equals("sha1")) {
            buffer.append("select md5 as md5Checksum, ");
            buffer.append("sha1 as sha1Checksum, ");
            buffer.append("seq as sequence ");
            buffer.append("from ");
            buffer.append("eta.reference ");
            buffer.append("where ");
            buffer.append("sha1 = ?");
        }
        
        PreparedStatement statement = null;
        
        try {
            if (buffer.length() > 0) {
                connection = getConnectionFromContext(ETA_JNDI_JDBC);
            statement = connection.prepareStatement(buffer
                    .toString());
            statement.setString(1, checksum);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                Object clobObject = resultSet.getObject(3);
                Clob clob = (oracle.sql.CLOB)clobObject;
                if (clob.length() <= Integer.MAX_VALUE)
                    resp.setContentLength((int)clob.length());
                Reader char_stream = clob.getCharacterStream();
                int c;
                while ((c = char_stream.read()) != -1) {
                    resp.getOutputStream().print((char)c);
                }
            } else {
                resp.sendError(404);
            }
            resultSet.close();
            } else {
                resp.sendError(404);
            }
            
            
        } catch (Exception e) {
            logger.error("Exception in ChecksumServlet", e);
            resp.sendError(404);
        } finally {
            closeConnection(statement,connection);
        }
    }
    
    public static Connection getConnectionFromContext(String contextName) {
        Connection connection = null;
        OracleConnection oracleConnection = null;

        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(INITIAL_JNDI_CONTEXT);
            logger.info("Getting a connection to Datasource : " + contextName);

            DataSource ds = (DataSource) envCtx.lookup(contextName);
            connection = ds.getConnection();
                
        } catch (NamingException ne) {
            logger.error("getExonerateConnection", ne);
        } catch (SQLException se) {
            logger.error("getExonerateConnection", se);
        }
        return connection;
    }
    
    public static void closeConnection (Statement statement, Connection connection) {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            logger.error("error closeConnection",e);
        }
    }

}
