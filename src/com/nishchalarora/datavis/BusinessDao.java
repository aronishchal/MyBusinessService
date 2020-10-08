package com.nishchalarora.datavis;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.HttpsURLConnection;
import javax.sql.DataSource;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class BusinessDao {
	private static final String SQL_GET_ALL_BUSINESSES = "SELECT * FROM Business";
	private static final String SQL_GET_BUSINESS = "SELECT * FROM Business WHERE id = ?";
	private static final String SQL_INSERT_BUSINESS = "INSERT INTO Business (name, address, lat, lon) VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE_BUSINESS = "UPDATE Business SET name = ?, address = ?, lat = ?, lon = ? WHERE id = ?";
	private static final String SQL_DELETE_BUSINESS = "DELETE FROM Business WHERE id = ?";

	private static Properties properties = new Properties();
	private static String geocodingApiKey = null;

	private static final Logger logger = Logger.getLogger(BusinessDao.class.getName());

	public BusinessDao() {
		try {
			properties.load(BusinessDao.class.getClassLoader().getResourceAsStream("mybusinessservice.properties"));
			geocodingApiKey = properties.getProperty("geocodingapikey");
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	private static String getResponseData(HttpsURLConnection connection) throws Exception {
		InputStream is = connection.getInputStream();
		byte[] bytes = IOUtils.toByteArray(is);
		return new String(bytes, "UTF-8");
	}

	private static HttpsURLConnection createConnection(URL url) throws Exception {
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		return connection;
	}

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

	public Business getBusiness(int id) throws Exception {
		Business business = null;

		Connection con;
		PreparedStatement stmt;
		ResultSet rs;

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(Constants.JNDI_DATA_SOURCE);
			con = ds.getConnection();
			stmt = con.prepareStatement(SQL_GET_BUSINESS);

			stmt.setInt(1, id);
			rs = stmt.executeQuery();

			if (rs.next()) {
				business = retrieveBusinessFromResultSet(rs);
			} else {
				throw new NotFoundException("Record with id '" + id + "' not found");
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

		return business;
	}

	public void insertBusiness(Business business) throws Exception {
		Connection con;
		PreparedStatement stmt;

		try {
			Double lat = null;
			Double lon = null;
			
			JSONObject geocoded = getGeocodedAddressInfo(business.getAddress());
			if (geocoded != null) {
				lat = geocoded.getDouble("lat");
				lon = geocoded.getDouble("lon");
			} else {
				throw new NotFoundException("Address not found");
			}

			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(Constants.JNDI_DATA_SOURCE);
			con = ds.getConnection();
			stmt = con.prepareStatement(SQL_INSERT_BUSINESS);
			stmt.setString(1, business.getName());
			stmt.setString(2, business.getAddress());
			stmt.setDouble(3, lat);
			stmt.setDouble(4, lon);
			stmt.executeUpdate();

			logger.log(Level.INFO, "Row added");

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

	public void updateBusiness(int id, Business business) throws Exception {
		Connection con;
		PreparedStatement stmt;

		try {
			Double lat = null;
			Double lon = null;
			
			JSONObject geocoded = getGeocodedAddressInfo(business.getAddress());
			if (geocoded != null) {
				lat = geocoded.getDouble("lat");
				lon = geocoded.getDouble("lon");
			} else {
				throw new NotFoundException("Address not found");
			}
			
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(Constants.JNDI_DATA_SOURCE);
			con = ds.getConnection();

			stmt = con.prepareStatement(SQL_UPDATE_BUSINESS);
			stmt.setString(1, business.getName());
			stmt.setString(2, business.getAddress());
			stmt.setDouble(3, lat);
			stmt.setDouble(4, lon);
			stmt.setInt(5, id);

			int updated = stmt.executeUpdate();
			stmt.close();

			if (updated == 0) {
				throw new NotFoundException("Record with id '" + id + "' not found");
			} else {
				logger.log(Level.INFO, "Row updated");
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

	private JSONObject getGeocodedAddressInfo(String address) throws Exception {
		JSONObject geocoded = null;
		
		URL url = new URL("https://us1.locationiq.com/v1/search.php?key=" + geocodingApiKey + "&q="
				+ address + "&format=json");
		HttpsURLConnection connection = createConnection(url);
		connection.connect();

		if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
			String responseData = getResponseData(connection);
			JSONArray arr = new JSONArray(responseData);
			if (arr.get(0) != null) {
				geocoded = (JSONObject) arr.get(0);
			}
		} else {
			logger.log(Level.SEVERE, connection.getResponseMessage(), connection.getErrorStream());
			throw new InternalServerErrorException(connection.getResponseMessage());
		}
		
		return geocoded;
	}
	
	private Business retrieveBusinessFromResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String address = rs.getString("address");
		double lat = rs.getDouble("lat");
		double lon = rs.getDouble("lon");
		return (new Business(id, lat, lon, name, address));
	}
}
