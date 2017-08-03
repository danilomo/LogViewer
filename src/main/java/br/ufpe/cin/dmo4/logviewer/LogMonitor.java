package br.ufpe.cin.dmo4.logviewer;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Danilo Oliveira
 */
public class LogMonitor {

    private final String file;
    private Process process;
    private Thread thread;
    private Scanner scanner;
    private ValuesProvider provider;
    private LineParser parser;

    public LogMonitor(String file, String expression) {
        this.file = file;
        this.parser = LineParser.createParser(expression);
    }

    public void startMonitoring() {
        try {
            thread = createControlThread();
            process = createAndStartProcess();

            thread.start();

        } catch (IOException ex) {
            Logger.getLogger(LogMonitor.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    private Thread createControlThread() {
        Runnable r = () -> {
            while (process.isAlive()) {
                if (process.isAlive()) {
                    String str = null;

                    try {
                        str = scanner.nextLine().trim();
                        
                        double val = parser.parse(str);
                        
                        System.out.println(">>" + val);

                        provideValue(val);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                }
            }

        };

        return new Thread(r);
    }

    public void setChart(DynamicChart chart) {
        provider = new ValuesProvider();
        chart.registerAtProvider(provider);
    }

    private void provideValue(double val) {
        if (provider != null) {
            provider.newValue(val);
        } else {
            System.out.println(val);
        }
    }

    private Process createAndStartProcess() throws IOException {
        ProcessBuilder builder = new ProcessBuilder();

        builder.command("tail", "-f", file);

        Process p = builder.start();
        
        System.out.println(p);
        System.out.println(p.isAlive());
        System.out.println(file);

        scanner = new Scanner(p.getInputStream());

        return p;
    }

}
