package implementation.helpers;

import common.serializable.*;

public class LocationHelper {

	public static Location randomLocation(int min, int max) {
		
		Location location = new Location();
		
		location.setX(random(min, max));
		location.setY(random(min, max));
		
		return location;
	}

	private static int random(int min, int max) {
		
		return (int) (min + Math.random() * (max - min));
	}
}
