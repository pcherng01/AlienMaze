import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AlienMaze {

	public static void main(String[] args) throws IOException {
		AlienMaze aMaze = new AlienMaze(20);
		aMaze.runProgram();
		//long heapSize = Runtime.getRuntime().totalMemory();
		//System.out.println("heapsize is::" + heapSize);
		//System.out.println("11111111".hashCode());
		//System.out.println(("11" + "111111").hashCode());
		/*
		int anInt = 40414243;
		int fourty = anInt / 1000000;
		int fourtyOne = (anInt % 1000000) / 10000;
		int fourtyTwo = (anInt % 10000) / 100;
		int fourtyThree = (anInt % 100);
		System.out.println(fourty + " " + fourtyOne + " " + fourtyTwo + " "
				+ fourtyThree);*/

		/*
		int i = 45;
		byte ii = 45;
		int intHash = ("" + (i - 1)).hashCode();
		int byteHash = ("" + (ii - 1)).hashCode();
		System.out.println(intHash);
		System.out.println(byteHash);*/

		/*
		String aStr1 = "112";
		String aStr2 = "" + (111 + 1);
		int hash1 = aStr1.hashCode();
		int hash2 = aStr2.hashCode();
		System.out.println(hash1);
		System.out.println(hash2);*/
	}

	int totalCell;
	byte dimension;
	HashMap<Integer, Coordinate> aHM = new HashMap<Integer, Coordinate>();

	public AlienMaze(int n) {
		dimension = (byte) n;
		totalCell = (n * n) * (n * n);
	}

	public void runProgram() throws IOException {
		int cellCount = 0;
		// Make every cell a set
		Coordinate randomCoord;
		Integer hc;
		String aStr;
		for (int t = 0; t < dimension; t++) {
			for (int z = 0; z < dimension; z++) {
				for (int y = 0; y < dimension; y++) {
					for (int x = 0; x < dimension; x++) {
						int numCoord = (t * 1000000) + (z * 10000) + (y * 100)
								+ x;
						randomCoord = new Coordinate(numCoord);
						aStr = (randomCoord.tAxis + "," + (randomCoord.zAxis)
								+ "," + randomCoord.yAxis + "," + randomCoord.xAxis);
						hc = aStr.hashCode();
						if (!aHM.containsKey(hc)) {
							randomCoord.makeSet();
							aHM.put(hc, randomCoord);
							//System.out.println(randomCoord.getString() + "->"
							//		+ hc);
							cellCount++;
						} else {
							System.out.println("error putting: " + aStr);
							System.out.println("hashcode :" + hc
									+ " belongs to " + aHM.get(hc).getString());
						}

					}
				}
			}
		}
		System.out.println((cellCount == totalCell) ? "Yes" : "Nah");
		System.out.println(totalCell - cellCount);

		Coordinate currentCoord;
		String aString;
		int lookUpKey, byteWallToKnock1, byteWallToKnock2;
		for (Map.Entry<Integer, Coordinate> ans : aHM.entrySet()) {
			// For each one, check all four corners
			currentCoord = ans.getValue();
			// Checking the negative and positive X Axis
			for (int i = -1; i < 2; i = i + 2) {
				// Check if cell is in the world
				aString = (currentCoord.tAxis + "," + currentCoord.zAxis + ","
						+ currentCoord.yAxis + "," + ((currentCoord.xAxis) + i));

				lookUpKey = (currentCoord.tAxis + "," + currentCoord.zAxis
						+ "," + currentCoord.yAxis + "," + ((currentCoord.xAxis) + i))
						.hashCode();
				/*
								System.out
										.println(currentCoord.getString()
												+ " looking horizontal " + aString + " ->>"
												+ lookUpKey);*/

				if (aHM.containsKey(lookUpKey)) {
					byteWallToKnock1 = (i == -1) ? -2 : -1;
					byteWallToKnock2 = (byteWallToKnock1 == -2) ? -1 : -2;
					union(currentCoord, aHM.get(lookUpKey), byteWallToKnock1,
							byteWallToKnock2);
					/*
										System.out.println("Unioning: " + currentCoord.getString()
												+ ", " + aHM.get(lookUpKey).getString());*/
				}
			}

			// Checking for the negative and positive Y Axis
			for (int i = -1; i < 2; i += 2) {
				lookUpKey = (currentCoord.tAxis + "," + currentCoord.zAxis
						+ "," + (currentCoord.yAxis + i) + "," + currentCoord.xAxis)
						.hashCode();
				/*
								System.out
										.println(currentCoord.getString()
												+ " looking vertically "
												+ (currentCoord.tAxis + ","
														+ currentCoord.zAxis + ","
														+ (currentCoord.yAxis + i) + "," + currentCoord.xAxis)
												+ ", " + lookUpKey);*/

				if (aHM.containsKey(lookUpKey)) {
					/*
										System.out.println("Unioning: " + currentCoord.getString()
												+ ", " + aHM.get(lookUpKey).getString());*/
					byteWallToKnock1 = (i == -1) ? -8 : -4;
					byteWallToKnock2 = (byteWallToKnock1 == -8) ? -4 : -8;
					union(currentCoord, aHM.get(lookUpKey), byteWallToKnock1,
							byteWallToKnock2);
				}
			}

			for (int i = -1; i < 2; i += 2) {
				lookUpKey = (currentCoord.tAxis + ","
						+ (currentCoord.zAxis + i) + "," + currentCoord.yAxis
						+ "," + currentCoord.xAxis).hashCode();
				/*
								System.out
										.println(currentCoord.getString()
												+ " looking up/down "
												+ (currentCoord.tAxis + ","
														+ (currentCoord.zAxis + i) + ","
														+ currentCoord.yAxis + "," + currentCoord.xAxis)
												+ ", " + lookUpKey);*/

				if (aHM.containsKey(lookUpKey)) {
					/*
										System.out.println("Unioning: " + currentCoord.getString()
												+ ", " + aHM.get(lookUpKey).getString());*/
					byteWallToKnock1 = (i == -1) ? -32 : -16;
					byteWallToKnock2 = (byteWallToKnock1 == -32) ? -16 : -32;
					union(currentCoord, aHM.get(lookUpKey), byteWallToKnock1,
							byteWallToKnock2);
				}
			}

			for (int i = -1; i < 2; i += 2) {
				lookUpKey = ((currentCoord.tAxis + i) + ","
						+ currentCoord.zAxis + "," + currentCoord.yAxis + "," + currentCoord.xAxis)
						.hashCode();
				/*
								System.out
										.println(currentCoord.getString()
												+ " looking time "
												+ ((currentCoord.tAxis + i) + ","
														+ currentCoord.zAxis + ","
														+ currentCoord.yAxis + "," + currentCoord.xAxis)
												+ ", " + lookUpKey);*/

				if (aHM.containsKey(lookUpKey)) {
					/*
										System.out.println("Unioning: " + currentCoord.getString()
												+ ", " + aHM.get(lookUpKey).getString());*/
					byteWallToKnock1 = (i == -1) ? -128 : -64;
					byteWallToKnock2 = (byteWallToKnock1 == -128) ? -64 : -128;
					union(currentCoord, aHM.get(lookUpKey), byteWallToKnock1,
							byteWallToKnock2);
				}
			}
		}
		/*
				for (Map.Entry<Integer, Coordinate> ans : aHM.entrySet()) {
					String s1 = String.format("%8s",
							Integer.toBinaryString(ans.getValue().wallGrid & 0xFF)
									.replace(' ', '0'));
					System.out.println(ans.getValue().getString() + ", " + s1);
				}*/

		System.out.println("\n\n");
		byte[] ans = new byte[totalCell];
		int ansIndex = 0;
		//String s1 = "";
		int aHC;
		String s1;
		for (int t = 0; t < dimension; t++) {
			for (int z = 0; z < dimension; z++) {
				for (int y = 0; y < dimension; y++) {
					for (int x = 0; x < dimension; x++) {

						s1 = String
								.format("%8s",
										Integer.toBinaryString(
												aHM.get(("" + t + "," + z + ","
														+ y + "," + x)
														.hashCode()).wallGrid & 0xFF)
												.replace(' ', '0'));
						//System.out.println("" + t + "," + z + "," + y + "," + x
						//	+ ", " + s1);
						ans[ansIndex] = aHM.get(("" + t + "," + z + "," + y
								+ "," + x).hashCode()).wallGrid;
						ansIndex++;
					}
				}
			}
		}

		/*
				System.out.println("\n\nRunning through the array:");
				for (int i = 0; i < ans.length; i++) {
					System.out.println(ans[i]);
				}*/

		// Write byte array to file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("428Lab2");
			fos.write(ans);
			fos.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
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
			parent1.rank = (byte) ((parent1.rank == parent2.rank) ? parent1.rank + 1
					: parent1.rank);
			parent2.parent = parent1;
			coord1.wallGrid += numWallKnock1;
			coord2.wallGrid += numWall2;
			/*
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
						System.out.println(coord2.getString() + ", " + s2);*/
		} else {
			parent1.parent = parent2;
			coord1.wallGrid += numWallKnock1;
			coord2.wallGrid += numWall2;
			/*
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
						System.out.println(coord2.getString() + ", " + s2);*/
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
		byte xAxis;
		byte yAxis;
		byte zAxis;
		byte tAxis;
		byte rank;
		byte wallGrid;
		Coordinate parent;

		/**
		 * Generate a Coordinate with random x and y axis within the dimension
		 * 
		 * @param pDimension
		 *            - if n = 2, then x and y can only go up to 0,1
		 */
		public Coordinate(int anInt) {

			xAxis = (byte) (anInt % 100);
			yAxis = (byte) ((anInt % 10000) / 100);
			zAxis = (byte) ((anInt % 1000000) / 10000);
			tAxis = (byte) (anInt / 1000000);
			rank = 0;
			wallGrid = (byte) 11111111;
		}

		public void makeSet() {
			parent = this;
		}

		public String getString() {
			return ("" + tAxis + "," + zAxis + "," + yAxis + "," + xAxis);
		}

		public int getHashCode() {

			//int hc = ("" + tAxis + "," + zAxis + "," + yAxis + "," + xAxis)
			//		.hashCode();
			return (tAxis + "," + (zAxis) + "," + yAxis + "," + xAxis)
					.hashCode();
		}
	}
}
