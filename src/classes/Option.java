package classes;

/**
 * 
 * @author Robert Dzieża
 * 
 *         Model class, representing single option.
 * 
 */
public class Option {
	public String title;
	public int icon;

	public Option(String title, int icon) {
		super();
		this.title = title;
		this.icon = icon;
	}

	public Option(int icon, String title) {
		super();
		this.title = title;
		this.icon = icon;
	}

}
