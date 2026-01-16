package in.co.rays.project_3.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.project_3.dto.HostelRoomDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.HostelRoomModelHibImp;
import in.co.rays.project_3.model.HostelRoomModelInt;
// import in.co.rays.project_3.model.HostelRoomModelJDBCImpl;

public class HostelRoomModelTest {
	public static HostelRoomModelInt model = new HostelRoomModelHibImp();



    public static void main(String[] args) throws Exception {

//        addTest();
        // updateTest();
        // deleteTest();
        // findByPkTest();
        // findByRoomNumberTest();
        // searchTest();
        // listTest();
    }

    /* ================= Add ================= */

    public static void addTest() throws Exception {

        HostelRoomDTO dto = new HostelRoomDTO();
        dto.setRoomNumber("101");
        dto.setRoomType("Single");
        dto.setCapacity("1");
        dto.setRent("5000");
        dto.setStatus("Available");

        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        long pk = model.add(dto);
        System.out.println("HostelRoom Added with PK = " + pk);
    }

    /* ================= Update ================= */

    public static void updateTest()
            throws ApplicationException, DuplicateRecordException {

        HostelRoomDTO dto = new HostelRoomDTO();
        dto.setId(1L);
        dto.setRoomNumber("H-101");
        dto.setRoomType("Double");
        dto.setCapacity("2");
        dto.setRent("8000");
        dto.setStatus("Occupied");

        dto.setCreatedBy("admin");
        dto.setModifiedBy("admin");
        dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
        dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.update(dto);
        System.out.println("HostelRoom Updated");
    }

    /* ================= Delete ================= */

    public static void deleteTest() throws ApplicationException {

        HostelRoomDTO dto = new HostelRoomDTO();
        dto.setId(1L);

        model.delete(dto);
        System.out.println("HostelRoom Deleted");
    }

    /* ================= Find By PK ================= */

    public static void findByPkTest() throws ApplicationException {

        HostelRoomDTO dto = model.findByPK(1L);
        if (dto != null) {
            System.out.println(
                dto.getId() + "\t" +
                dto.getRoomNumber() + "\t" +
                dto.getRoomType() + "\t" +
                dto.getCapacity() + "\t" +
                dto.getRent() + "\t" +
                dto.getStatus()
            );
        }
    }

    /* ================= Find By Room Number ================= */

    public static void findByRoomNumberTest() throws ApplicationException {

        HostelRoomDTO dto = model.findByRoomNumber("H-101");
        if (dto != null) {
            System.out.println(
                dto.getId() + "\t" +
                dto.getRoomNumber() + "\t" +
                dto.getRoomType() + "\t" +
                dto.getStatus()
            );
        }
    }

    /* ================= Search ================= */

    public static void searchTest() throws ApplicationException {

        HostelRoomDTO dto = new HostelRoomDTO();
        dto.setStatus("Available");

        List list = model.search(dto, 0, 0);
        Iterator it = list.iterator();

        while (it.hasNext()) {
            HostelRoomDTO room = (HostelRoomDTO) it.next();
            System.out.println(
                room.getId() + "\t" +
                room.getRoomNumber() + "\t" +
                room.getRoomType() + "\t" +
                room.getStatus()
            );
        }
    }

    /* ================= List ================= */

    public static void listTest() throws ApplicationException {

        List list = model.list(0, 0);
        Iterator it = list.iterator();

        while (it.hasNext()) {
            HostelRoomDTO dto = (HostelRoomDTO) it.next();
            System.out.println(
                dto.getId() + "\t" +
                dto.getRoomNumber() + "\t" +
                dto.getRoomType() + "\t" +
                dto.getStatus()
            );
        }
    }
}
