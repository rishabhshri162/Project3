package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.DriverDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface DriverModelInt {

	public long add(DriverDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(DriverDTO dto) throws ApplicationException;

	public void update(DriverDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(DriverDTO dto) throws ApplicationException;

	public List search(DriverDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public DriverDTO findByPK(long pk) throws ApplicationException;

	public DriverDTO findByDriverCode(String driverCode) throws ApplicationException;
}