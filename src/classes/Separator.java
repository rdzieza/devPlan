package classes;

public class Separator implements Item{
	private String date;
	
	public Separator(String date) {
		super();
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public boolean isHeaderType() {
		return true;
	}
	
	
}
