/**
 * 
 * @author 
 *
 */

package cs.ualberta.conditionlog;

import java.util.ArrayList;

public class PhotoList {
	private String name = "";
	private ArrayList<String> filenames = null;
	
	PhotoList(String name) {
		this.setName(name);
		filenames = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addPhoto(String filename) {
		filenames.add(filename);
	}
	
	public void setFilenames(ArrayList<String> filenames) {
		this.filenames = filenames;
	}
	
}