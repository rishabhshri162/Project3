package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.MeetingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface MeetingModelInt {

	public long add(MeetingDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(MeetingDTO dto) throws ApplicationException;

	public void update(MeetingDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(MeetingDTO dto) throws ApplicationException;

	public List search(MeetingDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public MeetingDTO findByPK(long pk) throws ApplicationException;

	public MeetingDTO findByMeetingCode(String meetingCode) throws ApplicationException;
}