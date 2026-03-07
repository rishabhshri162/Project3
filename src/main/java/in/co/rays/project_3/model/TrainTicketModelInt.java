package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.TrainTicketDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface TrainTicketModelInt {

    public long add(TrainTicketDTO dto) 
            throws ApplicationException, DuplicateRecordException;

    public void delete(TrainTicketDTO dto) 
            throws ApplicationException;

    public void update(TrainTicketDTO dto) 
            throws ApplicationException, DuplicateRecordException;

    public List list() 
            throws ApplicationException;

    public List list(int pageNo, int pageSize) 
            throws ApplicationException;

    public List search(TrainTicketDTO dto) 
            throws ApplicationException;

    public List search(TrainTicketDTO dto, int pageNo, int pageSize) 
            throws ApplicationException;

    public TrainTicketDTO findByPK(long pk) 
            throws ApplicationException;

    public TrainTicketDTO findByTrainNumber(String trainNumber) 
            throws ApplicationException;
}
