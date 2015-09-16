package bottomLevel.formula;

import java.util.ArrayList;

public class Correlation {
	public Correlation() {

	}

	public double spearman(ArrayList<Double> x, ArrayList<Double> y) {
		ArrayList<Double> rankX = new ArrayList<Double>();
		ArrayList<Double> rankY = new ArrayList<Double>();
		for (int i = 0; i < x.size(); ++i) {
			int count = 0;
			for (int j = 0; j < x.size(); ++j) {
				if (x.get(j) > x.get(i)) {
					++count;
				}
			}
			rankX.add((double) (count + 1));
		}
		for (int i = 0; i < y.size(); ++i) {
			int count = 0;
			for (int j = 0; j < y.size(); ++j) {
				if (y.get(j) > y.get(i)) {
					++count;
				}
			}
			rankY.add((double) (count + 1));
		}
		return pearson(rankX, rankY);
	}

	public double pearson(ArrayList<Double> x, ArrayList<Double> y) {
		double r = 0;
		double avgX = 0;
		double avgY = 0;
		double sX = 0;
		double sY = 0;
		double denominator = 0;
		double member = 0;
		if (x.size() != y.size())
			return -1;
		for (Double xi : x) {
			avgX += xi;
		}
		avgX = avgX / x.size();
		for (Double yi : y) {
			avgY += yi;
		}
		avgY = avgY / y.size();
		for (Double xi : x) {
			sX += (xi - avgX) * (xi - avgX);
		}
		for (Double yi : y) {
			sY += (yi - avgY) * (yi - avgY);
		}
		denominator = Math.sqrt(sX * sY);
		for (int i = 0; i < x.size(); ++i) {
			member += (x.get(i) - avgX) * (y.get(i) - avgY);
		}
		if (denominator == 0)
			return -1;
		r = member / denominator;
		return r;
	}
}