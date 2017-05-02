package com.back.music.dao.back;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.back.music.conf.BackDataSourceConfig;
import com.back.music.entity.back.Manager;

@Repository
public class ManagerDao {

	private BackDataSourceConfig config;

	private void initDatabase() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String createTableSql = getCreateTableSQL();
			conn = getConnection();
			ps = conn.prepareStatement(createTableSql);
			ps.execute();
			conn.commit();
		} catch (IOException e) {
			handleError(null, "Read h2.sql Throw Error : " + e.getMessage());
		} catch (Exception e) {
			handleError(conn, "Init Database Throw Error : " + e.getMessage());
		} finally {
			close(conn, ps, null);
		}
		if (!exists("admin")) {
			addManager("admin", "admin", "admin");
		}
	}

	private String getCreateTableSQL() throws IOException {
		StringBuffer ret = new StringBuffer();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("sql/h2.sql");
		Reader reader = new InputStreamReader(is);
		char[] buffer = new char[256];
		int readCount = -1;
		while (-1 != (readCount = reader.read(buffer))) {
			ret.append(buffer, 0, readCount);
		}
		return ret.toString();
	}

	public boolean addManager(String account, String name, String password) {
		boolean ret = false;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("INSERT INTO MANAGER(ACCOUNT,NAME,PASSWORD) VALUES(?,?,?)");
			ps.setString(1, account);
			ps.setString(2, name);
			ps.setString(3, password);
			ps.execute();
			conn.commit();
			ret = true;
		} catch (Exception e) {
			handleError(conn, "Add Manager Throw Error : " + e.getMessage());
		} finally {
			close(conn, ps, null);
		}
		return ret;
	}

	public Manager login(String account, String password) {
		Manager loginManager = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID,ACCOUNT,NAME FROM MANAGER WHERE ACCOUNT = ? AND PASSWORD = ?");
			ps.setString(1, account);
			ps.setString(2, password);
			rs = ps.executeQuery();
			conn.commit();
			if (rs.next()) {
				loginManager = new Manager();
				loginManager.setId(rs.getInt("ID"));
				loginManager.setAccount(rs.getString("ACCOUNT"));
				loginManager.setName(rs.getString(3));
			}
		} catch (Exception e) {
			handleError(conn, "Add Manager Throw Error : " + e.getMessage());
		} finally {
			close(conn, ps, rs);
		}
		return loginManager;
	}

	public boolean alterPassword(int id, String password) {
		int affectRows = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("UPDATE MANAGER SET PASSWORD = ? WHERE ID = ?");
			ps.setString(1, password);
			ps.setInt(2, id);
			affectRows = ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			handleError(conn, "Add Manager Throw Error : " + e.getMessage());
		} finally {
			close(conn, ps, null);
		}
		return 0 != affectRows;
	}

	public boolean exists(String account) {
		boolean ret = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ACCOUNT FROM MANAGER WHERE ACCOUNT = ?");
			ps.setString(1, account);
			rs = ps.executeQuery();
			conn.commit();
			ret = rs.next();
		} catch (Exception e) {
			handleError(conn, "Exists Error : " + e.getMessage());
		} finally {
			this.close(conn, ps, rs);
		}
		return ret;
	}

	public List<Manager> getAllManager() {
		List<Manager> managers = new ArrayList<Manager>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID,ACCOUNT,NAME FROM MANAGER WHERE ACCOUNT <> 'admin'");
			rs = ps.executeQuery();
			conn.commit();
			while (rs.next()) {
				Manager manager = new Manager();
				manager.setId(rs.getInt(1));
				manager.setAccount(rs.getString(2));
				manager.setName(rs.getString(3));
				managers.add(manager);
			}
		} catch (Exception e) {
			handleError(conn, "Get All Manager Error : " + e.getMessage());
		} finally {
			this.close(conn, ps, rs);
		}
		return managers;
	}

	public boolean removeManager(int id) {
		int affectRows = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("DELETE FROM MANAGER WHERE ID = ?");
			ps.setInt(1, id);
			affectRows = ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			handleError(conn, "Remove Manager Error : " + e.getMessage());
		} finally {
			this.close(conn, ps, null);
		}
		return 0 != affectRows;
	}

	private Connection getConnection() throws Exception {
		Connection conn = null;
		String driver = config.getDriver();
		String url = config.getUrl();
		String username = config.getUsername();
		String password = config.getPassword();
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			throw new Exception("Get Database Connection Error : " + e.getMessage());
		}
		return conn;
	}

	private void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (null != rs)
				rs.close();
			if (ps != null)
				ps.close();
			if (null != conn)
				conn.close();
		} catch (SQLException e) {
			System.out.println("Close Error : " + e.getMessage());
		}
	}

	private void handleError(Connection conn, String errorMessage) {
		if (null != conn)
			try {
				conn.rollback();
			} catch (SQLException e) {
				System.out.println("Connection Rollback Error : " + e.getMessage());
			}
		System.out.println(errorMessage);

	}

	@Autowired
	public void setConfig(BackDataSourceConfig config) {
		this.config = config;
		initDatabase();
	}

}
