package com.lexinsmart.xushun.lexinibeacon.utils.ibeacon;

import com.lexinsmart.xushun.lexinibeacon.model.Coordinate;
import com.lexinsmart.xushun.lexinibeacon.model.Round;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xushun on 2017/6/20.
 */


public class Calculate {
    /**
     * 求三角形质心算法
     *
     * @param r1
     *            Round对象
     * @param r2
     * @param r3
     * @return Coordinate对象
     */
    public static Coordinate triCentroid(Round r1, Round r2, Round r3) {

		/* 有效交叉点1 */
        Coordinate p1 = new Coordinate();
		/* 有效交叉点2 */
        Coordinate p2 = new Coordinate();
		/* 有效交叉点3 */
        Coordinate p3 = new Coordinate();

		/* 三点质心坐标 */
        Coordinate centroid = new Coordinate();

		/* r1,r2交点 */
        List<Coordinate> intersections1 = intersection(r1.getX(), r1.getY(), r1.getR(), r2.getX(), r2.getY(),
                r2.getR());

        if (intersections1 != null && !intersections1.isEmpty()) {
            System.out.println("r1,r2交点");
            if (intersections1.size() == 1) {
                p1 = intersections1.get(0);
                System.out.println("r1,r2相切");
                System.out.println("r1,r2 相信：" + p1.toString());

            } else {
                if (Math.pow(intersections1.get(0).getX() - r3.getX(), 2)
                        + Math.pow(intersections1.get(0).getY() - r3.getY(), 2) <= Math
                        .pow(intersections1.get(1).getX() - r3.getX(), 2)
                        + Math.pow(intersections1.get(1).getY() - r3.getY(), 2)) {
                    p1 = intersections1.get(0);
                } else {
                    p1 = intersections1.get(1);

                }
                System.out.println("r1,r2 相信：" + p1.toString());

            }
        } else {// 没有交点定位错误
            System.out.println("r1 r2 没有交点");
            p1.setX((r1.getX() + r2.getX()) / 2);
            p1.setY((r1.getY() + r2.getY()) / 2);
            // return null;
        }

		/* r1,r3交点 */
        List<Coordinate> intersections2 = intersection(r1.getX(), r1.getY(), r1.getR(), r3.getX(), r3.getY(),
                r3.getR());

        if (intersections2 != null && !intersections2.isEmpty()) {
            System.out.println("r1,r3交点");
            if (intersections2.size() == 1) {
                p2 = intersections2.get(0);
                System.out.println("r1,r3相切");
                System.out.println("r1,r3 相信：" + p2.toString());

            } else {
                if (Math.pow(intersections2.get(0).getX() - r2.getX(), 2)
                        + Math.pow(intersections2.get(0).getY() - r2.getY(), 2)
                        <=
                        Math.pow(intersections2.get(1).getX() - r2.getX(), 2)
                                + Math.pow(intersections2.get(1).getY() - r2.getY(), 2)) {
                    p2 = intersections2.get(0);
                } else {
                    p2 = intersections2.get(1);

                }
                System.out.println("r1,r3相信：" + p2.toString());

            }

        } else {// 没有交点定位错误
            System.out.println("r1  r3 没有交点");
            p2.setX((r1.getX() + r3.getX()) / 2);
            p2.setY((r1.getY() + r3.getY()) / 2);
            // return null;
        }

		/* r3,r2交点 */
        List<Coordinate> intersections3 = intersection(r2.getX(), r2.getY(), r2.getR(), r3.getX(), r3.getY(),
                r3.getR());

        if (intersections3 != null && !intersections3.isEmpty()) {
            System.out.println("r2,r3交点");
            if (intersections3.size() == 1) {
                p3 = intersections3.get(0);
                System.out.println("r2,r3相切");
                System.out.println("r2,r3 相信：" + p3.toString());

            } else {
                if (Math.pow(intersections3.get(0).getX() - r1.getX(), 2)
                        + Math.pow(intersections3.get(0).getY() - r1.getY(), 2)
                        <=
                        Math.pow(intersections3.get(1).getX() - r1.getX(), 2)
                                + Math.pow(intersections3.get(1).getY() - r1.getY(), 2)) {
                    p3 = intersections3.get(0);
                } else {
                    p3 = intersections3.get(1);

                }
                System.out.println("r2, r3相信：" + p3.toString());

            }

        } else {// 没有交点定位错误
            System.out.println("r2  r3 没有交点");
            p3.setX((r2.getX() + r3.getX()) / 2);
            p3.setY((r2.getY() + r3.getY()) / 2);

            // return null;
        }

		/* 质心 */
        centroid.setX((p1.getX() + p2.getX() + p3.getX()) / 3);
        centroid.setY((p1.getY() + p2.getY() + p3.getY()) / 3);

        return centroid;
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
            return null;
        } else if (x1 == x2 && y1 == y2) {// 同心圆
            return null;
        } else if (y1 == y2 && x1 != x2) {
            double a = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2)) / (2 * x2 - 2 * x1);
            if (d == Math.abs(r1 - r2) || d == r1 + r2) {// 只有一个交点时
                coor = new Coordinate();
                coor.setY(a);
                coor.setY(y1);
                points.add(coor);
            } else {// 两个交点
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
