package main;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import algorithm.ES;
import algorithm.PSO;
import algorithm.PSOES;
import gui.GUIRobotics;
import util.Graph;
import util.Path;
import util.Point;

public class Main {
	public static void main(String[] args) throws IOException {
		int count = 0;
		boolean flag = false;
		// while (count < 20) {

		long time = System.currentTimeMillis();
		String FILE_URL = "exp2/moes_test_old_30.txt";
		int numR = 10;
		// String FILE_URL = "exp1/moes_test23_Child200_Elite80.txt";

		// String FILE_URL = "exp1/moes_test23_numR" + numR + ".txt";
		File file = new File(FILE_URL);
		String numberTeString = "8";
		// Tao moi truong
		GUIRobotics gui = new GUIRobotics(600, 100, 10);
		gui.generateEnvironment("old_input/obstacle_" + numberTeString + ".txt");

		// Doc du lieu dau vao
		Graph graph = new Graph("old_input/obstacle_" + numberTeString + ".txt");
		LinkedList<Point> pointsToVisit = readPointData("old_input/input_" + numberTeString + ".txt");

			ArrayList<Point> resultDistance = new ArrayList<Point>();
			ArrayList<Point> resultSafety = new ArrayList<Point>();
		 	ArrayList<Point> resultSmooth = new ArrayList<Point>();

		 ArrayList<Point> resultPareto = new ArrayList<Point>();

		ES es = new ES(numR, pointsToVisit.get(0), pointsToVisit.get(1), graph, numberTeString);
		try {
			es.run();
			resultDistance.add(pointsToVisit.get(0));
			for (int j = 0; j < es.resultDistance.size(); j++) {
				resultDistance.add(es.resultDistance.get(j));
			}
			resultDistance.add(pointsToVisit.get(1));
			gui.canvas.drawLines(resultDistance, pointsToVisit, Color.GREEN);

			 resultSafety.add(pointsToVisit.get(0));
			 for (int j = 0; j < es.resultSafety.size(); j++) {
			 resultSafety.add(es.resultSafety.get(j));
			 }
			 resultSafety.add(pointsToVisit.get(1));
			 gui.canvas.drawLines(resultSafety, pointsToVisit, Color.RED);

			 resultSmooth.add(pointsToVisit.get(0));
			 for (int j = 0; j < es.resultSmooth.size(); j++) {
			 resultSmooth.add(es.resultSmooth.get(j));
			 }
			 resultSmooth.add(pointsToVisit.get(1));
			 gui.canvas.drawLines(resultSmooth, pointsToVisit, Color.BLACK);

			 for (int i = 0; i < es.resultPareto.size(); i++) {
//			 ArrayList<Point> resultPareto = new ArrayList<Point>();
//
			 resultPareto.add(pointsToVisit.get(0));
			 for (int j = 0; j < es.resultPareto.get(i).size(); j++) {
			 resultPareto.add(es.resultPareto.get(i).get(j));
			 }
			 resultPareto.add(pointsToVisit.get(1));
			 gui.canvas.drawLines(resultPareto, pointsToVisit, Color.ORANGE);
			 }

		}

		catch (Exception e) {
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}

		 ArrayList<Point> result = new ArrayList<Point>();
	
//		 PSO pso = new PSO(numR, pointsToVisit.get(0), pointsToVisit.get(1), graph);
//		 try {
//		 pso.run();
//		 result.add(pointsToVisit.get(0));
//		 for (int j = 0; j < pso.result.size(); j++) {
//		 result.add(pso.result.get(j));
//		 }
//		 
//		 result.add(pointsToVisit.get(1));
//		 gui.canvas.drawLines(result, pointsToVisit, Color.BLUE);
//		 } catch (Exception e) {
//		 System.out.println("Something went wrong!");
//		 e.printStackTrace();
//		 }

		 result = new ArrayList<Point>();
		 PSOES psoes = new PSOES(numR, pointsToVisit.get(0), pointsToVisit.get(1),
		 graph);
		 try {

		 psoes.run();
		 result.add(pointsToVisit.get(0));
		 for (int j = 0; j < psoes.result.size(); j++) {
		 result.add(psoes.result.get(j));
		 }
		 result.add(pointsToVisit.get(1));

		 gui.canvas.drawLines(result, pointsToVisit, Color.PINK);
		 } catch (Exception e) {
		 System.out.println("Something went wrong!");
		 e.printStackTrace();
		 }
//		 PSO pso = new PSO(10, pointsToVisit.get(0), pointsToVisit.get(1), graph);
//		 try {
//
//		 pso.run();
//		 result.add(pointsToVisit.get(0));
//		 for (int j = 0; j < pso.result.size(); j++) {
//		 result.add(pso.result.get(j));
//		 }
//		 result.add(pointsToVisit.get(1));
//
//		 gui.canvas.drawLines(result, pointsToVisit, Color.blue);
//		 } catch (Exception e) {
//		 System.out.println("Something went wrong!");
//		 e.printStackTrace();
//		 }

		time = System.currentTimeMillis() - time;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Path path : es.paretoFront) {
				if (path.points[0] != null) {
					flag = true;
					bw.write("Path :" + " " + (double) Math.round(path.distance * 10000) / 10000
							+ " "
							+ (double) Math.round(path.pathSafety(graph) * 10000) / 10000 + " "
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
		// System.out.println("result" + result);
//		System.out.println("Done!")
	}

	// }

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
