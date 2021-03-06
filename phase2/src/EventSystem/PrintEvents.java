package EventSystem;

import java.io.*;
import java.awt.Desktop;

/**
 * This class prints a user's list of registered events.
 *
 * @author Fred, Nithilan
 */
public class PrintEvents {
    private String filepath;

    /**
     * Constructor that sets the user's filepath, which is saves a file temporarily before printing to a browser.
     */
    public PrintEvents(){ this.filepath = System.getProperty("user.dir");}

    /**
     * This method prepares the html and converts the list of events to a table.
     *
     * @param doc   The list of events from a user.
     */
    public void print(StringBuilder doc) {

        try {
            String html = "<html><head>" + "<style>\n" +
                    "table {\n" +
                    "  font-family: arial, sans-serif;\n" +
                    "  border-collapse: collapse;\n" +
                    "  width: 100%;\n" +
                    "}\n" +
                    "\n" +
                    "td, th {\n" +
                    "  border: 1px solid #dddddd;\n" +
                    "  text-align: left;\n" +
                    "  padding: 8px;\n" +
                    "}\n" +
                    "\n" +
                    "tr:nth-child(even) {\n" +
                    "  background-color: #dddddd;\n" +
                    "}\n" +
                    "</style>\n" + "<title>Events List</title></head>" +
                    "<body>\n" +
                    "\n" +
                    "<h2 style=\"text-align: center;\">Conference Schedule</h2>\n" +
                    "\n" +
                    "<table>\n" +
                    "  <tr style=\"background: black; color: white;\">\n" +
                    "    <th>Event Name</th>\n" +
                    "    <th>Start time</th>\n" +
                    "    <th>Duration</th>\n" +
                    "    <th>Room</th>\n" +
                    "    <th>Total Capacity</th>\n" +
                    "    <th>Spots taken</th>\n" +
                    "    <th>Speakers</th>\n" +
                    "  </tr>\n" + doc +
                    "</table>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";
            WriteToFile(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteToFile(String fileContents) throws IOException {
        String tempFile = filepath + File.separator + "Events.html";
        File file = new File(tempFile);

        if (file.exists()) {
            try {
                File newFileName = new File(filepath + File.separator + "backup_" + "Events.html");
                file.renameTo(newFileName);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
        Writer writer = new OutputStreamWriter(outputStream);
        writer.write(fileContents);
        writer.close();

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(file.toURI());
        }
    }


}