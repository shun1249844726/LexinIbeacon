package com.lexinsmart.xushun.lexinibeacon.utils.ibeacon;

import com.lexinsmart.xushun.lexinibeacon.model.Coordinate;
import com.lexinsmart.xushun.lexinibeacon.model.Round;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xushun on 2017/6/16.
 */

public class MyCalculate {
    public static Coordinate triCentroid(Round r1, Round r2, Round r3) {

		/* 有效交叉点1 */
        Coordinate p1 = null;
		/* 有效交叉点2 */
        Coordinate p2 = null;
		/* 有效交叉点3 */
        Coordinate p3 = null;

		/* 三点质心坐标 */
        Coordinate centroid = new Coordinate();

		/* r2,r3交点 */
        List<Coordinate> intersections3 = intersection(r2.getX(), r2.getY(), r2.getR(), r3.getX(), r3.getY(),
                r3.getR());
        if (intersections3 != null && !intersections3.isEmpty()) {
            System.out.println("r2,r3交点");
            for (int i = 0; i < intersections3.size(); i++) {
                System.out.println(intersections3.get(i).getX() + "\t" + intersections3.get(i).getY());
            }
            if (intersections3.size() == 1) {
                p3 = intersections3.get(0);
            } else if (intersections3.size() == 2) {

                double d1 = Math.pow(intersections3.get(0).getX() - r1.getX(), 2)
                        + Math.pow(intersections3.get(0).getY() - r1.getY(), 2);

                double d2 = Math.pow(intersections3.get(1).getX() - r1.getX(), 2)
                        + Math.pow(intersections3.get(1).getY() - r1.getY(), 2);

                if (d1 > d2) {
                    p3 = intersections3.get(0);
                    System.out.println("P3:d2");

                } else {
                    p3 = intersections3.get(1);
                    System.out.println("P3:d1");

                }
            }
            centroid.setX((p3.getX() + r1.getX())/2);
            centroid.setY((p3.getY()+ r1.getY())/2);

            return centroid;

        } else {// 没有交点定位错误
            // return null;

            centroid.setX((r2.getX() +r1.getX())/2);
            centroid.setY((r2.getY()+ r1.getX())/2);
            return centroid;
        }
    }

    /**
     * 求两个圆的交点
     *
     * @param x1
     *            圆心1横坐标
     * @param y1
     *            圆心1纵坐标
     * @param r1
     *            圆心1半径
     * @param x2
     *            圆心2横坐标
     * @param y2
     *            圆心2纵坐标
     * @param r2
     *            圆心2半径
     * @return 返回两个圆的交点坐标对象列表
     */
    public static List<Coordinate> intersection(double x1, double y1, double r1, double x2, double y2, double r2) {

        double d = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));// 两圆心距离

        if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) < (r1 + r2)) {// 两圆相交

        }

        List<Coordinate> points = new ArrayList<Coordinate>();// 交点坐标列表

        Coordinate coor;

        if (d > r1 + r2 || d < Math.abs(r1 - r2)) {// 相离或内含
            System.out.println("相离或内含");
            return null;
        } else if (x1 == x2 && y1 == y2) {// 同心圆
            System.out.println("同心圆");

            return null;
        } else if (y1 == y2 && x1 != x2) {
            double a = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2)) / (2 * x2 - 2 * x1);
            if (d == Math.abs(r1 - r2) || d == r1 + r2) {// 只有一个交点时
                System.out.println("只有一个交点时");

                coor = new Coordinate();
                coor.setY(a);
                coor.setY(y1);
                points.add(coor);
            } else {// 两个交点
                System.out.println("两个交点");

                double t = r1 * r1 - (a - x1) * (a - x1);
                coor = new Coordinate();
                coor.setX(a);
                coor.setY(y1 + Math.sqrt(t));
                points.add(coor);
                coor = new Coordinate();
                coor.setX(a);
                coor.setY(y1 - Math.sqrt(t));
                points.add(coor);
            }
        } else if (y1 != y2) {
            double k, disp;
            k = (2 * x1 - 2 * x2) / (2 * y2 - 2 * y1);
            disp = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2) - (y1 * y1 - y2 * y2)) / (2 * y2 - 2 * y1);// 直线偏移量
            double a, b, c;
            a = (k * k + 1);
            b = (2 * (disp - y1) * k - 2 * x1);
            c = (disp - y1) * (disp - y1) - r1 * r1 + x1 * x1;
            double disc;
            disc = b * b - 4 * a * c;// 一元二次方程判别式
            if (d == Math.abs(r1 - r2) || d == r1 + r2) {
                coor = new Coordinate();
                coor.setX((-b) / (2 * a));
                coor.setY(k * coor.getX() + disp);
                points.add(coor);
            } else {
                coor = new Coordinate();
                coor.setX(((-b) + Math.sqrt(disc)) / (2 * a));
                coor.setY(k * coor.getX() + disp);
                points.add(coor);
                coor = new Coordinate();
                coor.setX(((-b) - Math.sqrt(disc)) / (2 * a));
                coor.setY(k * coor.getX() + disp);
                points.add(coor);
            }
        }
        return points;
    }
}
