package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import algorithm.PSO;
import gui.GUIRobotics;
import util.Graph;
import util.Path;
import util.Point;

public class Main {
	public static void main(String[] args) throws IOException {
		int count = 0;
		boolean flag = false;
		while (count < 1) {
			final int numR = 10;
			long time = System.currentTimeMillis();
			String FILE_URL = "exp2/mopso_test27.txt";

			// String FILE_URL = "exp2/psoes_test23_numR" + numR + ".txt";
			File file = new File(FILE_URL);
			String numberTeString = "27";
			// Tao moi truong
			 GUIRobotics gui = new GUIRobotics(800, 100, 10);
			 gui.generateEnvironment("input/obstacle_" + numberTeString + ".txt");

			// Doc du lieu dau vao
			Graph graph = new Graph("input/obstacle_" + numberTeString + ".txt");
			LinkedList<Point> pointsToVisit = readPointData("input/input_" + numberTeString + ".txt");

			ArrayList<Point> result = new ArrayList<Point>();
			PSO pso = new PSO(numR, pointsToVisit.get(0), pointsToVisit.get(1), graph);
			try {

				pso.run();
				result.add(pointsToVisit.get(0));
				for (int j = 0; j < pso.result.size(); j++) {
					result.add(pso.result.get(j));
				}
				result.add(pointsToVisit.get(1));

				 gui.canvas.drawLines(result, pointsToVisit);
			} catch (Exception e) {
				System.out.println("Something went wrong!");
				e.printStackTrace();
			}

			time = System.currentTimeMillis() - time;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				for (Path path : pso.NaParticles) {
					if (path.points[0] != null) {
						flag = true;
						bw.write("Path :" + "  " + (double) Math.round(path.distance * 10000) / 10000 + " "
								+ (double) Math.round(path.pathSafety(graph) * 10000) / 10000 + "  "
								+ (double) Math.round(path.pathSmooth() * 10000) / 10000 + "\n");
					}
				}
				if (flag == true)
					count++;
				flag = false;
				bw.write("Total execution time: " + (time) + "\n");
				bw.write("----------------------\n");
				bw.close();
			} catch (Exception e) {
				System.out.println("Something went wrong!");
				e.printStackTrace();
			}
			System.out.println("Time:\t" + time + " ms");
			System.out.println("Done!");

		}

	}

	public static LinkedList<Point> readPointData(String filename) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(filename));
		LinkedList<Point> pointsToVisit = new LinkedList<Point>();
		double x = scan.nextDouble();
		while (x != -1) {
			double y = scan.nextDouble();
			pointsToVisit.addLast(new Point(x, y));
			x = scan.nextDouble();
		}
		scan.close();

		return pointsToVisit;
	}
}
