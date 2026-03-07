package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.CateringDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class CateringModelHibImp implements CateringModelInt {

	public long add(CateringDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;
		long pk = 0;

		CateringDTO existDto = findByVendorName(dto.getVendorName());
		if (existDto != null) {
			throw new DuplicateRecordException("Vendor already exists");
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

			throw new ApplicationException("Exception in Catering add " + e.getMessage());

		} finally {
			session.close();
		}

		return pk;
	}

	public void update(CateringDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		CateringDTO existDto = findByVendorName(dto.getVendorName());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Vendor already exists");
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

			throw new ApplicationException("Exception in Catering update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	public void delete(CateringDTO dto) throws ApplicationException {

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

			throw new ApplicationException("Exception in Catering delete " + e.getMessage());

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
			Criteria criteria = session.createCriteria(CateringDTO.class);

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Catering list");

		} finally {
			session.close();
		}

		return list;
	}

	public List search(CateringDTO dto) throws ApplicationException {

		return search(dto, 0, 0);
	}

	public List search(CateringDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CateringDTO.class);

			if (dto != null) {

				if (dto.getId() > 0) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getVendorName() != null && dto.getVendorName().length() > 0) {

					criteria.add(Restrictions.like("vendorName", dto.getVendorName() + "%"));
				}

				if (dto.getMenuType() != null && dto.getMenuType().length() > 0) {

					criteria.add(Restrictions.like("menuType", dto.getMenuType() + "%"));
				}

				if (dto.getCost() != null && dto.getCost().length() > 0) {

					criteria.add(Restrictions.like("cost", dto.getCost() + "%"));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Catering search");

		} finally {
			session.close();
		}

		return list;
	}

	public CateringDTO findByPK(long pk) throws ApplicationException {

		Session session = null;

		try {

			session = HibDataSource.getSession();
			return (CateringDTO) session.get(CateringDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in finding Catering by PK");

		} finally {
			session.close();
		}
	}

	public CateringDTO findByVendorName(String vendorName) throws ApplicationException {

		Session session = null;
		CateringDTO dto = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CateringDTO.class);

			criteria.add(Restrictions.eq("vendorName", vendorName));

			List list = criteria.list();

			if (list.size() > 0) {
				dto = (CateringDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in find Catering by Vendor Name");

		} finally {
			session.close();
		}

		return dto;
	}
}