package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.HostelRoomDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface HostelRoomModelInt {

	public long add(HostelRoomDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(HostelRoomDTO dto) throws ApplicationException;

	public void update(HostelRoomDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(HostelRoomDTO dto) throws ApplicationException;

	public List search(HostelRoomDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public HostelRoomDTO findByPK(long pk) throws ApplicationException;

	public HostelRoomDTO findByRoomNumber(String roomNumber) throws ApplicationException;
}
