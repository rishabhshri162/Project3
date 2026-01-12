package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.HostelRoomDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

/**
 * JDBC implementation of HostelRoom model
 * 
 * @author Rishabh
 */
public class HostelRoomModelJDBCImpl implements HostelRoomModelInt {

    private static Logger log = Logger.getLogger(HostelRoomModelJDBCImpl.class);

    /* ================= Next PK ================= */

    public long nextPK() throws DatabaseException {
        long pk = 0;
        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps =
                con.prepareStatement("select max(ID) from ST_HOSTEL");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pk = rs.getLong(1);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            log.error("Database Exception", e);
            throw new DatabaseException("Exception in getting PK");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return pk + 1;
    }

    /* ================= Add ================= */

    public long add(HostelRoomDTO dto)
            throws ApplicationException, DuplicateRecordException {

        long pk = 0;
        Connection con = null;

        HostelRoomDTO existDto = findByRoomNumber(dto.getRoomNumber());
        if (existDto != null) {
            throw new DuplicateRecordException("Room already exists");
        }

        try {
            pk = nextPK();
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "insert into ST_HOSTEL " +
                "(ID, ROOM_NUMBER, ROOM_TYPE, CAPACITY, RENT, STATUS, " +
                "CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME) " +
                "values (?,?,?,?,?,?,?,?,?,?)");

            ps.setLong(1, pk);
            ps.setString(2, dto.getRoomNumber());
            ps.setString(3, dto.getRoomType());
            ps.setString(4, dto.getCapacity());
            ps.setString(5, dto.getRent());
            ps.setString(6, dto.getStatus());
            ps.setString(7, dto.getCreatedBy());
            ps.setString(8, dto.getModifiedBy());
            ps.setTimestamp(9, dto.getCreatedDatetime());
            ps.setTimestamp(10, dto.getModifiedDatetime());

            ps.executeUpdate();
            ps.close();
            con.commit();

        } catch (Exception e) {
            log.error("Database Exception", e);
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Rollback exception");
            }
            throw new ApplicationException("Exception in add HostelRoom");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return pk;
    }

    /* ================= Update ================= */

    public void update(HostelRoomDTO dto)
            throws ApplicationException, DuplicateRecordException {

        Connection con = null;

        HostelRoomDTO existDto = findByRoomNumber(dto.getRoomNumber());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Room number already exists");
        }

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "update ST_HOSTEL set ROOM_NUMBER=?, ROOM_TYPE=?, CAPACITY=?, " +
                "RENT=?, STATUS=?, CREATED_BY=?, MODIFIED_BY=?, " +
                "CREATED_DATETIME=?, MODIFIED_DATETIME=? where ID=?");

            ps.setString(1, dto.getRoomNumber());
            ps.setString(2, dto.getRoomType());
            ps.setString(3, dto.getCapacity());
            ps.setString(4, dto.getRent());
            ps.setString(5, dto.getStatus());
            ps.setString(6, dto.getCreatedBy());
            ps.setString(7, dto.getModifiedBy());
            ps.setTimestamp(8, dto.getCreatedDatetime());
            ps.setTimestamp(9, dto.getModifiedDatetime());
            ps.setLong(10, dto.getId());

            ps.executeUpdate();
            ps.close();
            con.commit();

        } catch (Exception e) {
            log.error("Database Exception", e);
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Rollback exception");
            }
            throw new ApplicationException("Exception in update HostelRoom");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }

    /* ================= Delete ================= */

    public void delete(HostelRoomDTO dto) throws ApplicationException {

        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps =
                con.prepareStatement("delete from ST_HOSTEL where ID=?");
            ps.setLong(1, dto.getId());
            ps.executeUpdate();

            ps.close();
            con.commit();

        } catch (Exception e) {
            log.error("Database Exception", e);
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Rollback exception");
            }
            throw new ApplicationException("Exception in delete HostelRoom");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }

    /* ================= Find By PK ================= */

    public HostelRoomDTO findByPK(long pk) throws ApplicationException {

        HostelRoomDTO dto = null;
        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps =
                con.prepareStatement("select * from ST_HOSTEL where ID=?");
            ps.setLong(1, pk);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dto = new HostelRoomDTO();
                dto.setId(rs.getLong(1));
                dto.setRoomNumber(rs.getString(2));
                dto.setRoomType(rs.getString(3));
                dto.setCapacity(rs.getString(4));
                dto.setRent(rs.getString(5));
                dto.setStatus(rs.getString(6));
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in find HostelRoom by PK");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return dto;
    }

    /* ================= Find By Room Number ================= */

    public HostelRoomDTO findByRoomNumber(String roomNumber)
            throws ApplicationException {

        HostelRoomDTO dto = null;
        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps =
                con.prepareStatement("select * from ST_HOSTEL where ROOM_NUMBER=?");
            ps.setString(1, roomNumber);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dto = new HostelRoomDTO();
                dto.setId(rs.getLong(1));
                dto.setRoomNumber(rs.getString(2));
                dto.setRoomType(rs.getString(3));
                dto.setCapacity(rs.getString(4));
                dto.setRent(rs.getString(5));
                dto.setStatus(rs.getString(6));
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in find HostelRoom by room number");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return dto;
    }

    /* ================= List ================= */

    public List list() throws ApplicationException {
        return list(0, 0);
    }

    public List list(int pageNo, int pageSize) throws ApplicationException {

        ArrayList<HostelRoomDTO> list = new ArrayList<>();
        Connection con = null;

        StringBuffer sql = new StringBuffer("select * from ST_HOSTEL");

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);
        }

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HostelRoomDTO dto = new HostelRoomDTO();
                dto.setId(rs.getLong(1));
                dto.setRoomNumber(rs.getString(2));
                dto.setRoomType(rs.getString(3));
                dto.setCapacity(rs.getString(4));
                dto.setRent(rs.getString(5));
                dto.setStatus(rs.getString(6));
                list.add(dto);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in HostelRoom list");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return list;
    }

    /* ================= Search ================= */

    public List search(HostelRoomDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List search(HostelRoomDTO dto, int pageNo, int pageSize)
            throws ApplicationException {

        ArrayList<HostelRoomDTO> list = new ArrayList<>();
        Connection con = null;

        StringBuffer sql = new StringBuffer("select * from ST_HOSTEL where 1=1");

        if (dto != null) {

            if (dto.getRoomNumber() != null && dto.getRoomNumber().length() > 0) {
                sql.append(" and ROOM_NUMBER = '" + dto.getRoomNumber() + "'");
            }
            if (dto.getRoomType() != null && dto.getRoomType().length() > 0) {
                sql.append(" and ROOM_TYPE like '" + dto.getRoomType() + "%'");
            }
            if (dto.getStatus() != null && dto.getStatus().length() > 0) {
                sql.append(" and STATUS like '" + dto.getStatus() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);
        }

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HostelRoomDTO dto1 = new HostelRoomDTO();
                dto1.setId(rs.getLong(1));
                dto1.setRoomNumber(rs.getString(2));
                dto1.setRoomType(rs.getString(3));
                dto1.setCapacity(rs.getString(4));
                dto1.setRent(rs.getString(5));
                dto1.setStatus(rs.getString(6));
                list.add(dto1);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in HostelRoom search");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return list;
    }
}
