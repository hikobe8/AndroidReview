package com.palmax.commonutil;

import android.graphics.Bitmap;

/**
 * 直方图算法
 *
 */
public class HistogramSimilarUtil {

	/**
	 * hist[0][]red的直方图，hist[1][]green的直方图，hist[2][]blue的直方图
	 * 
	 * @param img
	 *            要获取直方图的图像
	 * @return 返回r,g,b的三维直方图
	 */
	public static double[][] getHistogram(Bitmap img) {
		int w = img.getWidth();
		int h = img.getHeight();
		int size = w * h;
		double[][] hist = new double[3][256];
		int r, g, b;
		int[] pix = new int[size];
		img.getPixels(pix, 0, w, 0, 0, w, h);
		for (int i = 0; i < size; i++) {
			r = pix[i] >> 16 & 0xff;
			g = pix[i] >> 8 & 0xff;
			b = pix[i] & 0xff;

			hist[0][r]++;
			hist[1][g]++;
			hist[2][b]++;
		}
		for (int j = 0; j < 256; j++) {
			for (int i = 0; i < 3; i++) {
				hist[i][j] = hist[i][j] / (size);
			}
		}
		return hist;
	}

	public static double indentification(double[][] histR, double[][] histD) {
		double p = (double) 0.0;
		for (int i = 0; i < histR.length; i++) {
			for (int j = 0; j < histR[0].length; j++) {
				p += Math.sqrt(histR[i][j] * histD[i][j]);
			}
		}
		return p / 3;
	}
}
