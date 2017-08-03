package br.ufpe.cin.dmo4.logviewer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Danilo Oliveira
 */
public class LogMain {

    public static void main(String[] args) throws IOException, JSONException {
        if(args.length != 2){
            System.err.println("Usage: java -jar logviewer.jar <config.json> <logfile>");
            System.exit(1);
        }
        
        String config = readFile(args[0]);

        openLogWindow(config, args[1]);
    }

    private static String readFile(String arg) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arg)), StandardCharsets.UTF_8);
    }

    private static void openLogWindow(String config, String fileName) throws JSONException {
        JSONObject json = new JSONObject(config);

        JSONArray yRange = json.getJSONArray("yRange");
        float yLower = (float) yRange.getDouble(0);
        float yUpper = (float) yRange.getDouble(1);
        String label = json.getString("label");
        String expression = json.getString("expression");

        final ChartFrame cf = new ChartFrame(yLower, yUpper, label);
        
        java.awt.EventQueue.invokeLater(() -> {
            cf.setVisible(true);
        });
        
        LogMonitor lm = new LogMonitor(fileName, expression);
        
        lm.setChart(cf.getChart());
        
        lm.startMonitoring();

    }

}
