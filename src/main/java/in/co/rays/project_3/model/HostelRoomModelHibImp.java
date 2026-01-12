package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.HostelRoomDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of HostelRoom model
 * 
 * @author Rishabh
 */
public class HostelRoomModelHibImp implements HostelRoomModelInt{

    /* ================= Add ================= */

    public long add(HostelRoomDTO dto)
            throws ApplicationException, DuplicateRecordException {

        Session session = null;
        Transaction tx = null;
        long pk = 0;

        HostelRoomDTO existDto = findByRoomNumber(dto.getRoomNumber());
        if (existDto != null) {
            throw new DuplicateRecordException("Room already exists");
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
            throw new ApplicationException("Exception in HostelRoom add " + e.getMessage());
        } finally {
            session.close();
        }
        return pk;
    }

    /* ================= Update ================= */

    public void update(HostelRoomDTO dto)
            throws ApplicationException, DuplicateRecordException {

        Session session = null;
        Transaction tx = null;

        HostelRoomDTO existDto = findByRoomNumber(dto.getRoomNumber());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Room number already exists");
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
            throw new ApplicationException("Exception in HostelRoom update " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /* ================= Delete ================= */

    public void delete(HostelRoomDTO dto) throws ApplicationException {

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
            throw new ApplicationException("Exception in HostelRoom delete " + e.getMessage());
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
            Criteria criteria = session.createCriteria(HostelRoomDTO.class);

            if (pageSize > 0) {
                pageNo = ((pageNo - 1) * pageSize) + 1;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in HostelRoom list");
        } finally {
            session.close();
        }
        return list;
    }

    /* ================= Search ================= */

    public List search(HostelRoomDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List search(HostelRoomDTO dto, int pageNo, int pageSize)
            throws ApplicationException {

        Session session = null;
        List list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(HostelRoomDTO.class);

            if (dto != null) {

                if (dto.getId() > 0) {
                    criteria.add(Restrictions.eq("id", dto.getId()));
                }

                if (dto.getRoomNumber() != null && dto.getRoomNumber().length() > 0) {
                    criteria.add(Restrictions.eq("roomNumber", dto.getRoomNumber()));
                }

                if (dto.getRoomType() != null && dto.getRoomType().length() > 0) {
                    criteria.add(Restrictions.like("roomType", dto.getRoomType() + "%"));
                }

                if (dto.getStatus() != null && dto.getStatus().length() > 0) {
                    criteria.add(Restrictions.like("status", dto.getStatus() + "%"));
                }
            }

            if (pageSize > 0) {
                criteria.setFirstResult((pageNo - 1) * pageSize);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in HostelRoom search");
        } finally {
            session.close();
        }
        return list;
    }

    /* ================= Find By PK ================= */

    public HostelRoomDTO findByPK(long pk) throws ApplicationException {

        Session session = null;

        try {
            session = HibDataSource.getSession();
            return (HostelRoomDTO) session.get(HostelRoomDTO.class, pk);
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in finding HostelRoom by PK");
        } finally {
            session.close();
        }
    }

    /* ================= Find By Room Number ================= */

    public HostelRoomDTO findByRoomNumber(String roomNumber)
            throws ApplicationException {

        Session session = null;
        HostelRoomDTO dto = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(HostelRoomDTO.class);
            criteria.add(Restrictions.eq("roomNumber", roomNumber));

            List list = criteria.list();
            if (list.size() > 0) {
                dto = (HostelRoomDTO) list.get(0);
            }

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in find HostelRoom by room number");
        } finally {
            session.close();
        }
        return dto;
    }
}