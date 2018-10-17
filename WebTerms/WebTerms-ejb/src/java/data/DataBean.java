package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class DataBean implements DataBeanLocal {

    @EJB
    private ConnectionBeanLocal connectionBean;

    @Override
    public List<Definition> getTermDefinitions(String term) throws SQLException {
        List<Definition> data = null;
        PreparedStatement ps = null;
        try {
            Connection con = connectionBean.getConnection();
            String SQL = "select def.id, definition from tbldefinitions as def\n"
                    + " inner join tblterms as tm on def.term_id = tm.id\n"
                    + " where tm.TERM = ?";
            ps = con.prepareStatement(SQL);
            ps.setString(1, term);
            try (ResultSet rs = ps.executeQuery()) {
                data = new ArrayList<>();
                while (rs.next()) {
                    data.add(new Definition(rs.getInt("id"), rs.getString("definition")));
                }
            }
        } catch (SQLException e) {
            System.out.println("DataBean.getTermDefinitions -> " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return data;
    }

    private int getNextId(String tableName) throws SQLException {
        int id = 1;
        PreparedStatement ps = null;
        try {
            Connection con = connectionBean.getConnection();
            String SQL = "select max(id) from " + tableName;
            ps = con.prepareStatement(SQL);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt(1) + 1;
                }
            }
        } catch (SQLException e) {
            System.out.println("DataBean.getNextId -> " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return id;
    }

    @Override
    public boolean add(String term, String definition) throws SQLException {
        boolean wasAdded = false;
        PreparedStatement ps = null;
        try {
            Connection con = connectionBean.getConnection();
            String SQL = "select id from tblterms where term = ?";
            ps = con.prepareStatement(SQL);
            ps.setString(1, term);
            try (ResultSet rs = ps.executeQuery()) {
                int id;
                if (rs.next()) {
                    id = rs.getInt("id");
                } else {
                    id = getNextId("tblterms");
                    SQL = "insert into tblterms values(?, ?)";
                    ps = con.prepareStatement(SQL);
                    ps.setInt(1, id);
                    ps.setString(2, term);
                    ps.execute();
                }
                int defId = getNextId("tbldefinitions");
                SQL = "insert into tbldefinitions values(?, ?, ?)";
                ps = con.prepareStatement(SQL);
                ps.setInt(1, defId);
                ps.setString(2, definition);
                ps.setInt(3, id);
                ps.execute();
                wasAdded = true;
            }
        } catch (SQLException e) {
            System.out.println("DataBean.add -> " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return wasAdded;
    }

    @Override
    public boolean updateDefinition(Definition definition) throws SQLException {
        boolean wasUpdated = false;
        PreparedStatement ps = null;
        try {
            Connection con = connectionBean.getConnection();
            String SQL = "update tbldefinitions set definition = ? where id = ?";
            ps = con.prepareStatement(SQL);
            ps.setString(1, definition.getDefinition());
            ps.setInt(2, definition.getId());
            ps.execute();
            wasUpdated = true;
        } catch (SQLException e) {
            System.out.println("DataBean.update -> " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return wasUpdated;
    }

    @Override
    public boolean delete(String term) throws SQLException {
        boolean wasDeleted = false;
        PreparedStatement ps = null;
        try {
            Connection con = connectionBean.getConnection();
            String SQL = "select id from tblterms where term = ?";
            ps = con.prepareStatement(SQL);
            ps.setString(1, term);
            try (ResultSet rs = ps.executeQuery()) {
                int id;
                if (rs.next()) {
                    id = rs.getInt("id");
                    SQL = "delete from tblterms where id = ?";
                    ps = con.prepareStatement(SQL);
                    ps.setInt(1, id);
                    ps.execute();
                    wasDeleted = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("DataBean.delete -> " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return wasDeleted;
    }
}
