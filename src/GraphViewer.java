import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class GraphViewer
{

    private static Integer port = 7772;
    private static String name = "localhost";
    private Process pr;
    private DataOutputStream out;
    private int width;
    private int height;
    private Boolean dynamic;

    public GraphViewer(int width, int height, Boolean dynamic) throws IOException {
        // Start Graph Viewer
        Runtime rt = Runtime.getRuntime();
        pr = rt.exec("java -jar GraphViewerController.jar --port " + port.toString());

        InetAddress address = InetAddress.getByName(name);
        Socket socket = new Socket(address, port);
        out = new DataOutputStream(socket.getOutputStream());

        // Initialize Graph Viewer
        this.width = width;
        this.height = height;
        this.dynamic = dynamic;
        this.initialize(width, height, dynamic);
    }

    public void initialize(int width, int height, Boolean dynamic) throws IOException {
        String message = "newGraph " + width + " " + height + " " + dynamic.toString() + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void createWindow(int width, int height) throws IOException {
        String message = "createWindow " + width + " " + height + "\n" ;
        out.write(message.getBytes());
        out.flush();
    }

    public void closeWindow() throws IOException {
        String message = "closeWindow\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void addNode(int id) throws IOException {
        if(!dynamic) {
            System.err.println("This graph is not dynamic, so you must use GraphViewer::addNode(int id, int x, int y) instead.\n" +
                    "The node " + id + " will be ignored");
            return;
        }

        String message = "addNode1 " + id + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void addNode(int id, int x, int y) throws IOException {
        if(dynamic) {
            System.err.println("This graph is dynamic,so the provided x and y values for the node with id " + id + " will be ignored\n");
            return;
        }

        String message = "addNode3 " + id + " " + x + " " + y +"\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void addEdge(int id, int v1, int v2, int edgeType) throws IOException {
        String message = "addEdge " + id + " " + v1 + " " + v2 + " " + edgeType + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setEdgeLabel(int k, String label) throws IOException {
        String message = "setEdgeLabel " + k + " " + label + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setVertexLabel(int k, String label) throws IOException {
        String message = "setVertexLabel " + k + " " + label + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void defineEdgeColor(String color) throws IOException {
        String message = "defineEdgeColor " + color + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void removeNode(int id) throws IOException {
        String message = "removeNode " + id + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void removeEdge(int id) throws IOException {
        String message = "removeEdge " + id + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setEdgeColor(int k, String color) throws IOException {
        String message = "setEdgeColor " + k + " " + color + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void defineEdgeDashed(Boolean dashed) throws IOException {
        String message = "defineEdgeDashed " + dashed.toString() + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setEdgeDashed(int k, Boolean dashed) throws IOException {
        String message = "setEdgeDashed " + k + " " + dashed.toString() + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void defineEdgeCurved(Boolean curved) throws IOException {
        String message = "defineEdgeCurved " + curved.toString() + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setEdgeThickness(int k, int thickness) throws IOException {
        String message = "setEdgeThickness " + k + " " + thickness + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void defineVertexColor(String color) throws IOException {
        String message = "defineVertexColor " + color + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setVertexColor(int k, String color) throws IOException {
        String message = "setVertexColor " + k + " " + color + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void defineVertexSize(int size) throws IOException {
        String message = "defineVertexSize " + size + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setVertexSize(int k, int size) throws IOException {
        String message = "setVertexSize " + k + " " + size + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setEdgeWeight(int id, int weight) throws IOException {
        String message = "setEdgeWeight " + id + " " + weight + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void setEdgeFlow(int id, int flow) throws IOException {
        String message = "setEdgeFlow " + id + " " + flow + "\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void rearrange() throws IOException {
        String message = "rearrange\n";
        out.write(message.getBytes());
        out.flush();
    }

    public void close() throws IOException {
        out.close();
        pr.destroy();
    }
}

