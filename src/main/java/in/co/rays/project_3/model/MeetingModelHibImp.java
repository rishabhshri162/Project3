package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.MeetingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Meeting model
 * 
 * @author Rishabh
 */
public class MeetingModelHibImp implements MeetingModelInt {

    public long add(MeetingDTO dto) 
            throws ApplicationException, DuplicateRecordException {

        Session session = null;
        Transaction tx = null;
        long pk = 0;

        MeetingDTO existDto = findByMeetingCode(dto.getMeetingCode());
        if (existDto != null) {
            throw new DuplicateRecordException("Meeting code already exists");
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

            throw new ApplicationException(
                    "Exception in Meeting add " + e.getMessage());

        } finally {
            session.close();
        }

        return pk;
    }


    public void update(MeetingDTO dto) 
            throws ApplicationException, DuplicateRecordException {

        Session session = null;
        Transaction tx = null;

        MeetingDTO existDto = findByMeetingCode(dto.getMeetingCode());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Meeting code already exists");
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

            throw new ApplicationException(
                    "Exception in Meeting update " + e.getMessage());

        } finally {
            session.close();
        }
    }


    public void delete(MeetingDTO dto) 
            throws ApplicationException {

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

            throw new ApplicationException(
                    "Exception in Meeting delete " + e.getMessage());

        } finally {
            session.close();
        }
    }


    public List list() throws ApplicationException {
        return list(0, 0);
    }


    public List list(int pageNo, int pageSize) 
            throws ApplicationException {

        Session session = null;
        List list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(MeetingDTO.class);

            if (pageSize > 0) {
                criteria.setFirstResult((pageNo - 1) * pageSize);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();

        } catch (HibernateException e) {
            throw new ApplicationException(
                    "Exception in Meeting list");
        } finally {
            session.close();
        }

        return list;
    }


    public List search(MeetingDTO dto) 
            throws ApplicationException {
        return search(dto, 0, 0);
    }


    public List search(MeetingDTO dto, int pageNo, int pageSize) 
            throws ApplicationException {

        Session session = null;
        List list = null;

        try {

            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(MeetingDTO.class);

            if (dto != null) {

                if (dto.getId() > 0) {
                    criteria.add(
                        Restrictions.eq("id", dto.getId()));
                }

                if (dto.getMeetingCode() != null 
                        && dto.getMeetingCode().length() > 0) {

                    criteria.add(
                        Restrictions.eq("meetingCode",
                                dto.getMeetingCode()));
                }

                if (dto.getMeetingTitle() != null 
                        && dto.getMeetingTitle().length() > 0) {

                    criteria.add(
                        Restrictions.like("meetingTitle",
                                dto.getMeetingTitle() + "%"));
                }

                if (dto.getMeetingLocation() != null 
                        && dto.getMeetingLocation().length() > 0) {

                    criteria.add(
                        Restrictions.like("meetingLocation",
                                dto.getMeetingLocation() + "%"));
                }

                if (dto.getMeetingStatus() != null 
                        && dto.getMeetingStatus().length() > 0) {

                    criteria.add(
                        Restrictions.like("meetingStatus",
                                dto.getMeetingStatus() + "%"));
                }
            }

            if (pageSize > 0) {
                criteria.setFirstResult((pageNo - 1) * pageSize);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();

        } catch (HibernateException e) {
            throw new ApplicationException(
                    "Exception in Meeting search");
        } finally {
            session.close();
        }

        return list;
    }


    public MeetingDTO findByPK(long pk) 
            throws ApplicationException {

        Session session = null;

        try {
            session = HibDataSource.getSession();
            return (MeetingDTO) session.get(
                    MeetingDTO.class, pk);

        } catch (HibernateException e) {

            throw new ApplicationException(
                    "Exception in finding Meeting by PK");

        } finally {
            session.close();
        }
    }


    public MeetingDTO findByMeetingCode(String meetingCode) 
            throws ApplicationException {

        Session session = null;
        MeetingDTO dto = null;

        try {

            session = HibDataSource.getSession();
            Criteria criteria = 
                    session.createCriteria(MeetingDTO.class);

            criteria.add(
                Restrictions.eq("meetingCode", meetingCode));

            List list = criteria.list();

            if (list.size() > 0) {
                dto = (MeetingDTO) list.get(0);
            }

        } catch (HibernateException e) {

            throw new ApplicationException(
                "Exception in find Meeting by meeting code");

        } finally {
            session.close();
        }

        return dto;
    }
}