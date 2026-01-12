package in.co.rays.project_3.dto;


public class HostelRoomDTO extends BaseDTO {


	private static final long serialVersionUID = 1L;

	private String roomNumber;
	private String roomType;
	private String capacity;
	private String rent;
	private String status;


	public String getRoomNumber() {
	        return roomNumber;
	    }

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getRent() {
		return rent;
	}

	public void setRent(String rent) {
		this.rent = rent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String getKey() {
		return String.valueOf(getId());
	}

	@Override
	public String getValue() {
		return roomNumber;
	}
}
