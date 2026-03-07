package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.TrainTicketDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

/**
 * JDBC implementation of TrainTicket model
 * 
 * @author Rishabh
 */
public class TrainTicketModelJDBCImpl implements TrainTicketModelInt {

    private static Logger log = Logger.getLogger(TrainTicketModelJDBCImpl.class);


    public long nextPK() throws DatabaseException {
        long pk = 0;
        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps =
                con.prepareStatement("select max(ID) from ST_TRAIN_TICKET");
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


    public long add(TrainTicketDTO dto)
            throws ApplicationException, DuplicateRecordException {

        long pk = 0;
        Connection con = null;

        TrainTicketDTO existDto = findByTrainNumber(dto.getTrainNumber());
        if (existDto != null) {
            throw new DuplicateRecordException("Train number already exists");
        }

        try {
            pk = nextPK();
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "insert into ST_TRAIN_TICKET " +
                "(ID, PASSENGER_NAME, TRAIN_NUMBER, TRAIN_NAME, SOURCE_STATION, DESTINATION_STATION, " +
                "JOURNEY_DATE, SEAT_NUMBER, TICKET_CLASS, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ps.setLong(1, pk);
            ps.setString(2, dto.getPassengerName());
            ps.setString(3, dto.getTrainNumber());
            ps.setString(4, dto.getTrainName());
            ps.setString(5, dto.getSourceStation());
            ps.setString(6, dto.getDestinationStation());
            ps.setString(7, dto.getJourneyDate());
            ps.setString(8, dto.getSeatNumber());
            ps.setString(9, dto.getTicketClass());
            ps.setString(10, dto.getCreatedBy());
            ps.setString(11, dto.getModifiedBy());
            ps.setTimestamp(12, dto.getCreatedDatetime());
            ps.setTimestamp(13, dto.getModifiedDatetime());

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
            throw new ApplicationException("Exception in add TrainTicket");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return pk;
    }


    public void update(TrainTicketDTO dto)
            throws ApplicationException, DuplicateRecordException {

        Connection con = null;

        TrainTicketDTO existDto = findByTrainNumber(dto.getTrainNumber());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Train number already exists");
        }

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "update ST_TRAIN_TICKET set PASSENGER_NAME=?, TRAIN_NUMBER=?, TRAIN_NAME=?, " +
                "SOURCE_STATION=?, DESTINATION_STATION=?, JOURNEY_DATE=?, SEAT_NUMBER=?, TICKET_CLASS=?, " +
                "CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? where ID=?");

            ps.setString(1, dto.getPassengerName());
            ps.setString(2, dto.getTrainNumber());
            ps.setString(3, dto.getTrainName());
            ps.setString(4, dto.getSourceStation());
            ps.setString(5, dto.getDestinationStation());
            ps.setString(6, dto.getJourneyDate());
            ps.setString(7, dto.getSeatNumber());
            ps.setString(8, dto.getTicketClass());
            ps.setString(9, dto.getCreatedBy());
            ps.setString(10, dto.getModifiedBy());
            ps.setTimestamp(11, dto.getCreatedDatetime());
            ps.setTimestamp(12, dto.getModifiedDatetime());
            ps.setLong(13, dto.getId());

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
            throw new ApplicationException("Exception in update TrainTicket");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }


    public void delete(TrainTicketDTO dto) throws ApplicationException {

        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps =
                con.prepareStatement("delete from ST_TRAIN_TICKET where ID=?");
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
            throw new ApplicationException("Exception in delete TrainTicket");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }


    public TrainTicketDTO findByPK(long pk) throws ApplicationException {

        TrainTicketDTO dto = null;
        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps =
                con.prepareStatement("select * from ST_TRAIN_TICKET where ID=?");
            ps.setLong(1, pk);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dto = new TrainTicketDTO();
                dto.setId(rs.getLong(1));
                dto.setPassengerName(rs.getString(2));
                dto.setTrainNumber(rs.getString(3));
                dto.setTrainName(rs.getString(4));
                dto.setSourceStation(rs.getString(5));
                dto.setDestinationStation(rs.getString(6));
                dto.setJourneyDate(rs.getString(7));
                dto.setSeatNumber(rs.getString(8));
                dto.setTicketClass(rs.getString(9));
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in find TrainTicket by PK");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return dto;
    }


    public TrainTicketDTO findByTrainNumber(String trainNumber)
            throws ApplicationException {

        TrainTicketDTO dto = null;
        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps =
                con.prepareStatement("select * from ST_TRAIN_TICKET where TRAIN_NUMBER=?");
            ps.setString(1, trainNumber);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dto = new TrainTicketDTO();
                dto.setId(rs.getLong(1));
                dto.setPassengerName(rs.getString(2));
                dto.setTrainNumber(rs.getString(3));
                dto.setTrainName(rs.getString(4));
                dto.setSourceStation(rs.getString(5));
                dto.setDestinationStation(rs.getString(6));
                dto.setJourneyDate(rs.getString(7));
                dto.setSeatNumber(rs.getString(8));
                dto.setTicketClass(rs.getString(9));
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in find TrainTicket by train number");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return dto;
    }


    public List list() throws ApplicationException {
        return list(0, 0);
    }

    public List list(int pageNo, int pageSize) throws ApplicationException {

        ArrayList<TrainTicketDTO> list = new ArrayList<>();
        Connection con = null;

        StringBuffer sql = new StringBuffer("select * from ST_TRAIN_TICKET");

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);
        }

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TrainTicketDTO dto = new TrainTicketDTO();
                dto.setId(rs.getLong(1));
                dto.setPassengerName(rs.getString(2));
                dto.setTrainNumber(rs.getString(3));
                dto.setTrainName(rs.getString(4));
                dto.setSourceStation(rs.getString(5));
                dto.setDestinationStation(rs.getString(6));
                dto.setJourneyDate(rs.getString(7));
                dto.setSeatNumber(rs.getString(8));
                dto.setTicketClass(rs.getString(9));
                list.add(dto);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in TrainTicket list");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return list;
    }


    public List search(TrainTicketDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List search(TrainTicketDTO dto, int pageNo, int pageSize)
            throws ApplicationException {

        ArrayList<TrainTicketDTO> list = new ArrayList<>();
        Connection con = null;

        StringBuffer sql = new StringBuffer("select * from ST_TRAIN_TICKET where 1=1");

        if (dto != null) {

            if (dto.getTrainNumber() != null && dto.getTrainNumber().length() > 0) {
                sql.append(" and TRAIN_NUMBER = '" + dto.getTrainNumber() + "'");
            }
            if (dto.getPassengerName() != null && dto.getPassengerName().length() > 0) {
                sql.append(" and PASSENGER_NAME like '" + dto.getPassengerName() + "%'");
            }
            if (dto.getSourceStation() != null && dto.getSourceStation().length() > 0) {
                sql.append(" and SOURCE_STATION like '" + dto.getSourceStation() + "%'");
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
                TrainTicketDTO dto1 = new TrainTicketDTO();
                dto1.setId(rs.getLong(1));
                dto1.setPassengerName(rs.getString(2));
                dto1.setTrainNumber(rs.getString(3));
                dto1.setTrainName(rs.getString(4));
                dto1.setSourceStation(rs.getString(5));
                dto1.setDestinationStation(rs.getString(6));
                dto1.setJourneyDate(rs.getString(7));
                dto1.setSeatNumber(rs.getString(8));
                dto1.setTicketClass(rs.getString(9));
                list.add(dto1);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in TrainTicket search");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return list;
    }
}
