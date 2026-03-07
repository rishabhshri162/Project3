package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.DriverDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Driver model
 * 
 * @author Rishabh
 */
public class DriverModelHibImp implements DriverModelInt {

	public long add(DriverDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;
		long pk = 0;

		DriverDTO existDto = findByDriverCode(dto.getDriverCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Driver code already exists");
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

			throw new ApplicationException("Exception in Driver add " + e.getMessage());

		} finally {
			session.close();
		}

		return pk;
	}

	public void update(DriverDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		DriverDTO existDto = findByDriverCode(dto.getDriverCode());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Driver code already exists");
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

			throw new ApplicationException("Exception in Driver update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	public void delete(DriverDTO dto) throws ApplicationException {

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

			throw new ApplicationException("Exception in Driver delete " + e.getMessage());

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
			Criteria criteria = session.createCriteria(DriverDTO.class);

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Driver list");
		} finally {
			session.close();
		}

		return list;
	}

	public List search(DriverDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(DriverDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(DriverDTO.class);

			if (dto != null) {

				if (dto.getId() > 0) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getDriverCode() != null && dto.getDriverCode().length() > 0) {

					criteria.add(Restrictions.eq("driverCode", dto.getDriverCode()));
				}

				if (dto.getDriverName() != null && dto.getDriverName().length() > 0) {

					criteria.add(Restrictions.like("driverName", dto.getDriverName() + "%"));
				}

				if (dto.getLicenseNumber() != null && dto.getLicenseNumber().length() > 0) {

					criteria.add(Restrictions.like("licenseNumber", dto.getLicenseNumber() + "%"));
				}

				if (dto.getContactNumber() != null && dto.getContactNumber().length() > 0) {

					criteria.add(Restrictions.like("contactNumber", dto.getContactNumber() + "%"));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Driver search");
		} finally {
			session.close();
		}

		return list;
	}

	public DriverDTO findByPK(long pk) throws ApplicationException {

		Session session = null;

		try {
			session = HibDataSource.getSession();
			return (DriverDTO) session.get(DriverDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in finding Driver by PK");

		} finally {
			session.close();
		}
	}

	public DriverDTO findByDriverCode(String driverCode) throws ApplicationException {

		Session session = null;
		DriverDTO dto = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(DriverDTO.class);

			criteria.add(Restrictions.eq("driverCode", driverCode));

			List list = criteria.list();

			if (list.size() > 0) {
				dto = (DriverDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in find Driver by driver code");

		} finally {
			session.close();
		}

		return dto;
	}
}