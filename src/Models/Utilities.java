package Models;

import Models.Shapes.Circle;

import java.awt.*;
import java.util.ArrayList;

public class Utilities {

    public static int getNearest(ArrayList<IShape> drawnCircles, Point point) {

        if(drawnCircles.size() == 0)
            throw new RuntimeException("Display an error message of no nodes");
        int minX = (int)((Circle)drawnCircles.get(0)).getLayoutX();
        int minY = (int)((Circle)drawnCircles.get(0)).getLayoutY();
        int minProp = 0;
        for(int i = 1 ; i < drawnCircles.size() ; i++)
        {
            int x = (int)((Circle)drawnCircles.get(i)).getLayoutX();
            int y = (int)((Circle)drawnCircles.get(i)).getLayoutY();
            if(isNearest(point,minX,minY,x,y))
            {
                minX = x;
                minY = y;
                minProp = i;
            }
        }
        if(contains(point, (Circle)drawnCircles.get(minProp)))
            return minProp;
        return -1;
    }

    private static boolean contains(Point point, Circle circle) {
        int r = (int)circle.getRadius();
        int x = (int)circle.getLayoutX();
        int y = (int)circle.getLayoutY();

        if(point.x > (x-r) && point.x < (x+r))
            if(point.y > (y-r) && point.y < (y+r))
                return true;
        return false;
    }

    private static boolean isNearest(Point point, int minX, int minY, int x, int y) {
        int diffX = Math.abs(x - point.x);
        int diffXMin = Math.abs(minX - point.x);

        if(diffXMin > diffX)
            return true;

        int diffY = Math.abs(y - point.y);
        int diffYMin = Math.abs(minY - point.y);
        if(diffYMin > diffY)
            return true;
        return false;
    }
}
