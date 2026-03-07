package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.CateringDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface CateringModelInt {

	public long add(CateringDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(CateringDTO dto) throws ApplicationException;

	public void update(CateringDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(CateringDTO dto) throws ApplicationException;

	public List search(CateringDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public CateringDTO findByPK(long pk) throws ApplicationException;

	public CateringDTO findByVendorName(String vendorName) throws ApplicationException;
}