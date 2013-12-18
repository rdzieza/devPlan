package classes;

public class Event implements Item{
	private long id;
	private String subject;
	private String startHours;
	private String endHour;
	private Long timestamp;
	private String room;
	private String type;
	
	

	public Event(long id, String subject, String startHours, String endHour,
			Long timestamp, String room, String type) {
		super();
		this.id = id;
		this.subject = subject;
		this.startHours = startHours;
		this.endHour = endHour;
		this.timestamp = timestamp;
		this.room = room;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStartHours() {
		return startHours;
	}

	public void setStartHours(String startHours) {
		this.startHours = startHours;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean isHeaderType() {
		return false;
	}

}
