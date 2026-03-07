package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.TrainTicketDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of TrainTicket model
 * 
 * @author Rishabh
 */
public class TrainTicketModelHibImp implements TrainTicketModelInt {


    public long add(TrainTicketDTO dto) throws ApplicationException, DuplicateRecordException {

        Session session = null;
        Transaction tx = null;
        long pk = 0;

        TrainTicketDTO existDto = findByTrainNumber(dto.getTrainNumber());
        if (existDto != null) {
            throw new DuplicateRecordException("Train number already exists");
        }

        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();

            session.save(dto);
            pk = dto.getId();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new ApplicationException("Exception in TrainTicket add " + e.getMessage());
        } finally {
            session.close();
        }
        return pk;
    }


    public void update(TrainTicketDTO dto) throws ApplicationException, DuplicateRecordException {

        Session session = null;
        Transaction tx = null;

        TrainTicketDTO existDto = findByTrainNumber(dto.getTrainNumber());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Train number already exists");
        }

        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();

            session.update(dto);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new ApplicationException("Exception in TrainTicket update " + e.getMessage());
        } finally {
            session.close();
        }
    }


    public void delete(TrainTicketDTO dto) throws ApplicationException {

        Session session = null;
        Transaction tx = null;

        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();

            session.delete(dto);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new ApplicationException("Exception in TrainTicket delete " + e.getMessage());
        } finally {
            session.close();
        }
    }


    public List list() throws ApplicationException {
        return list(0, 0);
    }

    public List list(int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        List list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(TrainTicketDTO.class);

            if (pageSize > 0) {
                pageNo = ((pageNo - 1) * pageSize) + 1;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in TrainTicket list");
        } finally {
            session.close();
        }
        return list;
    }


    public List search(TrainTicketDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List search(TrainTicketDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        List list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(TrainTicketDTO.class);

            if (dto != null) {

                if (dto.getId() > 0) {
                    criteria.add(Restrictions.eq("id", dto.getId()));
                }

                if (dto.getTrainNumber() != null && dto.getTrainNumber().length() > 0) {
                    criteria.add(Restrictions.eq("trainNumber", dto.getTrainNumber()));
                }

                if (dto.getPassengerName() != null && dto.getPassengerName().length() > 0) {
                    criteria.add(Restrictions.like("passengerName", dto.getPassengerName() + "%"));
                }

                if (dto.getSourceStation() != null && dto.getSourceStation().length() > 0) {
                    criteria.add(Restrictions.like("sourceStation", dto.getSourceStation() + "%"));
                }
                
                if (dto.getTicketClass() != null && dto.getTicketClass().length() > 0) {
                    criteria.add(Restrictions.like("ticketClass", dto.getTicketClass() + "%"));
                }

                if (dto.getDestinationStation() != null && dto.getDestinationStation().length() > 0) {
                    criteria.add(Restrictions.like("destinationStation", dto.getDestinationStation() + "%"));
                }
            }

            if (pageSize > 0) {
                criteria.setFirstResult((pageNo - 1) * pageSize);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in TrainTicket search");
        } finally {
            session.close();
        }
        return list;
    }


    public TrainTicketDTO findByPK(long pk) throws ApplicationException {

        Session session = null;

        try {
            session = HibDataSource.getSession();
            return (TrainTicketDTO) session.get(TrainTicketDTO.class, pk);
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in finding TrainTicket by PK");
        } finally {
            session.close();
        }
    }


    public TrainTicketDTO findByTrainNumber(String trainNumber) throws ApplicationException {

        Session session = null;
        TrainTicketDTO dto = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(TrainTicketDTO.class);
            criteria.add(Restrictions.eq("trainNumber", trainNumber));

            List list = criteria.list();
            if (list.size() > 0) {
                dto = (TrainTicketDTO) list.get(0);
            }

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in find TrainTicket by train number");
        } finally {
            session.close();
        }
        return dto;
    }
}
