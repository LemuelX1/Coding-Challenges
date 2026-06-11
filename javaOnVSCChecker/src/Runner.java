import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import processing.core.PApplet;
import processing.core.PVector;

public class Runner extends PApplet {
    float[][] points;
    int[][] connections;
    PVector[] points2D;
    float d = 2.5f;
    final int NUM_POINTS = 306;
    final int NUM_CONNECTIONS = 2500;
    float ax = -0.4f;
    float ay = 0.5f;

    public void draw() {
        background(0);
        float angleX = ax;
        float angleY = ay;
        for (int k = 0; k < NUM_POINTS; k++) {
            float scale = 0.08f;
            float x = points[k][0] * scale;
            float y = points[k][1] * scale;
            float z = points[k][2] * scale;

            // Y rotation
            float x1 = x * cos(angleY) - z * sin(angleY);
            float z1 = x * sin(angleY) + z * cos(angleY);
            float y1 = y;

            // X rotation
            float x2 = x1;
            float y2 = y1 * cos(angleX) - z1 * sin(angleX);
            float z2 = y1 * sin(angleX) + z1 * cos(angleX);

            float finalZ = z2 + 8.0f;
            float xProj = (x2 * d) / finalZ;
            float yProj = (y2 * d) / finalZ;
            float screenX = width / 2f + xProj * 220;
            float screenY = height / 2f - yProj * 220 + 100;
            points2D[k] = new PVector(screenX, screenY);
        }
        for (int j = 0; j < connections.length; j++) {
            int first = connections[j][0];
            int second = connections[j][1];
            if (first != 0 || second != 0) {
                PVector v1 = points2D[first];
                PVector v2 = points2D[second];
                line(v1.x, v1.y, v2.x, v2.y);
            }
        }
        ay += (mouseX - pmouseX) * 0.01f;
        ax += (mouseY - pmouseY) * 0.01f;
    }

    public void setup() {
        background(0);
        stroke(67,241,0);
        points = new float[NUM_POINTS][3];
        connections = new int[NUM_CONNECTIONS][2];
        points2D = new PVector[NUM_POINTS];
        int pointRow = 0;
        int edgeRow = 0;

        try {
            Scanner s1 = new Scanner(new File("C:\\Users\\Lemuel Xu\\Desktop\\VS Code\\javaOnVSCChecker\\triples.txt"));
            while (s1.hasNextLine()) {
                String line = s1.nextLine().trim();
                if (line.startsWith("v ")) {
                    String[] parts = line.split("\\s+");
                    points[pointRow][0] = Float.parseFloat(parts[1]);
                    points[pointRow][1] = Float.parseFloat(parts[2]);
                    points[pointRow][2] = Float.parseFloat(parts[3]);
                    pointRow++;
                }
            }
            s1.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        try {
            Scanner s2 = new Scanner(new File("C:\\Users\\Lemuel Xu\\Desktop\\VS Code\\javaOnVSCChecker\\connections.txt"));
            while (s2.hasNextLine()) {
                String line = s2.nextLine().trim();
                if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    int[] verts = new int[parts.length - 1];
                    for (int i = 1; i < parts.length; i++) {
                        String token = parts[i];
                        verts[i - 1] = Integer.parseInt(token.split("/")[0]) - 1;
                    }
                    for (int i = 0; i < verts.length; i++) {
                        int a = verts[i];
                        int b = verts[(i + 1) % verts.length];
                        if (edgeRow < connections.length) {
                            connections[edgeRow][0] = a;
                            connections[edgeRow][1] = b;
                            edgeRow++;
                        }
                    }   
                }
            }
            s2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Vertices loaded: " + pointRow);
        System.out.println("Edges loaded: " + edgeRow);
    }

    public void settings() {
        size(600, 600);
    }

    public static void main(String[] args) {
        PApplet.main("Runner");
    }
}
