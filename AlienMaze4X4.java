import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AlienMaze {

	public static void main(String[] args) {
		AlienMaze aMaze = new AlienMaze(2);
		aMaze.runProgram();
	}

	int totalCell;
	int dimension;
	HashMap<String, Coordinate> aHM = new HashMap<String, Coordinate>();

	public AlienMaze(int n) {
		dimension = n;
		totalCell = (n * n) * (n * n);
	}

	public void runProgram() {
		int cellCount = 0;
		// Make every cell a set
		while (cellCount != totalCell) {
			// Randomly generate Coordinate object
			// check hashset, if not then increment counts
			Coordinate randomCoord = new Coordinate(dimension);
			// if it's already in the hashmap, dont do anything
			if (!aHM.containsKey(randomCoord.getString())) {
				randomCoord.makeSet();
				aHM.put(randomCoord.getString(), randomCoord);
				cellCount++;
			}
		}
		for (Map.Entry<String, Coordinate> ans : aHM.entrySet()) {
			// For each one, check all four corners
			Coordinate currentCoord = ans.getValue();
			// Checking the negative and positive X Axis
			for (int i = -1; i < 2; i = i + 2) {
				// Check if cell is in the world
				String lookUpKey = currentCoord.getTAxis() + ","
						+ currentCoord.getZAxis() + ","
						+ currentCoord.getYAxis() + ","
						+ (Integer.parseInt(currentCoord.getXAxis()) + i);

				System.out.println(currentCoord.getString()
						+ " looking horizontal " + lookUpKey);

				if (aHM.containsKey(lookUpKey)) {
					int byteWallToKnock1 = (i == -1) ? -2 : -1;
					int byteWallToKnock2 = (byteWallToKnock1 == -2) ? -1 : -2;
					union(currentCoord, aHM.get(lookUpKey), byteWallToKnock1,
							byteWallToKnock2);
					System.out.println("Unioning: " + currentCoord.getString()
							+ ", " + aHM.get(lookUpKey).getString());
				}
			}

			// Checking for the negative and positive Y Axis
			for (int i = -1; i < 2; i += 2) {
				String lookUpKey = currentCoord.getTAxis() + ","
						+ currentCoord.getZAxis() + ","
						+ (Integer.parseInt(currentCoord.getYAxis()) + i) + ","
						+ currentCoord.getXAxis();

				System.out.println(currentCoord.getString()
						+ " looking vertically " + lookUpKey);

				if (aHM.containsKey(lookUpKey)) {
					System.out.println("Unioning: " + currentCoord.getString()
							+ ", " + aHM.get(lookUpKey).getString());
					int byteWallToKnock1 = (i == -1) ? -8 : -4;
					int byteWallToKnock2 = (byteWallToKnock1 == -8) ? -4 : -8;
					union(currentCoord, aHM.get(lookUpKey), byteWallToKnock1,
							byteWallToKnock2);
				}
			}

			for (int i = -1; i < 2; i += 2) {
				String lookUpKey = currentCoord.getTAxis() + ","
						+ (Integer.parseInt(currentCoord.getZAxis()) + i) + ","
						+ currentCoord.getYAxis() + ","
						+ currentCoord.getXAxis();
				System.out.println(currentCoord.getString()
						+ " looking up/down " + lookUpKey);

				if (aHM.containsKey(lookUpKey)) {
					System.out.println("Unioning: " + currentCoord.getString()
							+ ", " + aHM.get(lookUpKey).getString());
					int byteWallToKnock1 = (i == -1) ? -32 : -16;
					int byteWallToKnock2 = (byteWallToKnock1 == -32) ? -16
							: -32;
					union(currentCoord, aHM.get(lookUpKey), byteWallToKnock1,
							byteWallToKnock2);
				}
			}

			for (int i = -1; i < 2; i += 2) {
				String lookUpKey = (Integer.parseInt(currentCoord.getTAxis()) + i)
						+ ","
						+ currentCoord.getZAxis()
						+ ","
						+ currentCoord.getYAxis()
						+ ","
						+ currentCoord.getXAxis();
				System.out.println(currentCoord.getString() + " looking time "
						+ lookUpKey);

				if (aHM.containsKey(lookUpKey)) {
					System.out.println("Unioning: " + currentCoord.getString()
							+ ", " + aHM.get(lookUpKey).getString());
					int byteWallToKnock1 = (i == -1) ? -128 : -64;
					int byteWallToKnock2 = (byteWallToKnock1 == -128) ? -64
							: -128;
					union(currentCoord, aHM.get(lookUpKey), byteWallToKnock1,
							byteWallToKnock2);
				}
			}
		}

		for (Map.Entry<String, Coordinate> ans : aHM.entrySet()) {
			String s1 = String.format("%8s",
					Integer.toBinaryString(ans.getValue().wallGrid & 0xFF)
							.replace(' ', '0'));
			System.out.println(ans.getValue().getString() + ", " + s1);
		}

		System.out.println("\n\n");
		byte[] ans = new byte[totalCell];

		for (int t = 0; t < dimension; t++) {
			for (int z = 0; z < dimension; z++) {
				for (int y = 0; y < dimension; y++) {
					for (int x = 0; x < dimension; x++) {
						String s1 = String.format(
								"%8s",
								Integer.toBinaryString(
										aHM.get("" + t + "," + z + "," + y
												+ "," + x).wallGrid & 0xFF)
										.replace(' ', '0'));
						System.out.println("" + t + "," + z + "," + y + "," + x
								+ ", " + s1);
					}
				}
			}
		}
	}

	public void union(Coordinate coord1, Coordinate coord2, int numWallKnock1,
			int numWall2) {
		Coordinate parent1 = findSet(coord1);
		Coordinate parent2 = findSet(coord2);

		if (parent1.getString().equals(parent2.getString())) {
			return;
		}

		if (parent1.rank >= parent2.rank) {
			parent1.rank = (parent1.rank == parent2.rank) ? parent1.rank + 1
					: parent1.rank;
			parent2.parent = parent1;
			coord1.wallGrid += numWallKnock1;
			coord2.wallGrid += numWall2;
			System.out.println("Wall Knocked Down");
			String s1 = String.format(
					"%8s",
					Integer.toBinaryString(coord1.wallGrid & 0xFF).replace(' ',
							'0'));
			String s2 = String.format(
					"%8s",
					Integer.toBinaryString(coord2.wallGrid & 0xFF).replace(' ',
							'0'));
			System.out.println(coord1.getString() + ", " + s1);
			System.out.println(coord2.getString() + ", " + s2);
		} else {
			parent1.parent = parent2;
			coord1.wallGrid += numWallKnock1;
			coord2.wallGrid += numWall2;
			System.out.println("Wall Knocked Down");
			String s1 = String.format(
					"%8s",
					Integer.toBinaryString(coord1.wallGrid & 0xFF).replace(' ',
							'0'));
			String s2 = String.format(
					"%8s",
					Integer.toBinaryString(coord2.wallGrid & 0xFF).replace(' ',
							'0'));
			System.out.println(coord1.getString() + ", " + s1);
			System.out.println(coord2.getString() + ", " + s2);
		}
	}

	public Coordinate findSet(Coordinate pCoordinate) {
		Coordinate parent = pCoordinate.parent;
		if (parent == pCoordinate) {
			return parent;
		}
		pCoordinate.parent = findSet(pCoordinate.parent);
		return pCoordinate.parent;
	}

	class Coordinate {
		int xAxis;
		int yAxis;
		int zAxis;
		int tAxis;
		int rank;
		byte wallGrid;
		Coordinate parent;

		/**
		 * Generate a Coordinate with random x and y axis within the dimension
		 * 
		 * @param pDimension
		 *            - if n = 2, then x and y can only go up to 0,1
		 */
		public Coordinate(int pDimension) {
			Random randomGenerator = new Random();
			xAxis = randomGenerator.nextInt(100) % pDimension;
			yAxis = randomGenerator.nextInt(99) % pDimension;
			zAxis = randomGenerator.nextInt(98) % pDimension;
			tAxis = randomGenerator.nextInt(97) % pDimension;
			rank = 0;
			wallGrid = (byte) 11111111;
		}

		public byte getByte() {
			return wallGrid;
		}

		public String getXAxis() {
			return "" + xAxis;
		}

		public String getYAxis() {
			return "" + yAxis;
		}

		public String getZAxis() {
			return "" + zAxis;
		}

		public String getTAxis() {
			return "" + tAxis;
		}

		public void makeSet() {
			parent = this;
		}

		public String getString() {
			return ("" + tAxis + "," + zAxis + "," + yAxis + "," + xAxis);
		}
	}
}
