package com.nishchalarora.datavis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.NotFoundException;

public class BusinessDao {
	private static final String SQL_GET_ALL_BUSINESSES = "SELECT * FROM Business";
	private static final String SQL_GET_BUSINESS = "SELECT * FROM Business WHERE id = ?";
	private static final String SQL_INSERT_BUSINESS = "INSERT INTO Business (name, address) VALUES (?, ?)";
	private static final String SQL_UPDATE_BUSINESS = "UPDATE Business SET name = ?, address = ? WHERE id = ?";
	private static final String SQL_DELETE_BUSINESS = "DELETE FROM Business WHERE id = ?";

	private static final Logger logger = Logger.getLogger(BusinessDao.class.getName());

	public List<Business> getBusinesses() throws Exception {
		List<Business> businessList = new ArrayList<Business>();

		Connection con;
		Statement stmt;
		ResultSet rs;

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(Constants.JNDI_DATA_SOURCE);
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_GET_ALL_BUSINESSES);

			while (rs.next()) {
				businessList.add(retrieveBusinessFromResultSet(rs));
			}

			con.close();
			stmt.close();
			rs.close();
		} catch (SQLException sqle) {
			logger.log(Level.SEVERE, sqle.getMessage(), sqle);
			throw sqle;
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, ne.getMessage(), ne);
			throw ne;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}

		return businessList;
	}

	public Business insertBusiness(Business business) throws Exception {
		Connection con;
		PreparedStatement stmt;
		ResultSet rs;
		Business insertedBusiness = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(Constants.JNDI_DATA_SOURCE);
			con = ds.getConnection();
			stmt = con.prepareStatement(SQL_INSERT_BUSINESS);

			stmt.setString(1, business.getName());
			stmt.setString(2, business.getAddress());

			stmt.executeUpdate();
			logger.log(Level.INFO, "Row added");
			stmt.close();

			stmt = con.prepareStatement(SQL_GET_BUSINESS);
			stmt.setInt(1, business.getId());
			rs = stmt.executeQuery();
			if (rs.next()) {
				insertedBusiness = retrieveBusinessFromResultSet(rs);
			}

			con.close();
			stmt.close();
			rs.close();
		} catch (SQLException sqle) {
			logger.log(Level.SEVERE, sqle.getMessage(), sqle);
			throw sqle;
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, ne.getMessage(), ne);
			throw ne;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}

		return insertedBusiness;
	}

	public Business updateBusiness(int id, Business business) throws Exception {
		Connection con;
		PreparedStatement stmt;
		ResultSet rs;
		Business updatedBusiness = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(Constants.JNDI_DATA_SOURCE);
			con = ds.getConnection();
			
			stmt = con.prepareStatement(SQL_UPDATE_BUSINESS);
			stmt.setString(1, business.getName());
			stmt.setString(2, business.getAddress());
			stmt.setInt(3, id);

			int updated = stmt.executeUpdate();
			stmt.close();

			if (updated == 0) {
				throw new NotFoundException("Record with id '" + id + "' not found");
			} else {
				logger.log(Level.INFO, "Row updated");
			}

			stmt = con.prepareStatement(SQL_GET_BUSINESS);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				updatedBusiness = retrieveBusinessFromResultSet(rs);
			}

			con.close();
			stmt.close();
			rs.close();
		} catch (SQLException sqle) {
			logger.log(Level.SEVERE, sqle.getMessage(), sqle);
			throw sqle;
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, ne.getMessage(), ne);
			throw ne;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}

		return updatedBusiness;
	}

	public void deleteBusiness(int id) throws Exception {
		Connection con;
		PreparedStatement stmt;

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(Constants.JNDI_DATA_SOURCE);
			con = ds.getConnection();
			stmt = con.prepareStatement(SQL_DELETE_BUSINESS);

			stmt.setInt(1, id);
			int deleted = stmt.executeUpdate();

			if (deleted == 0) {
				throw new NotFoundException("Record with id '" + id + "' not found");
			} else {
				logger.log(Level.INFO, "Row deleted");
			}

			con.close();
			stmt.close();
		} catch (SQLException sqle) {
			logger.log(Level.SEVERE, sqle.getMessage(), sqle);
			throw sqle;
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, ne.getMessage(), ne);
			throw ne;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
	}

	private Business retrieveBusinessFromResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String address = rs.getString("address");
		return (new Business(id, name, address));
	}
}
