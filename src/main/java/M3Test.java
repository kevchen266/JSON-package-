import org.json.JSONObject;
import org.json.XML;

import java.io.*;

public class M3Test {
    public static void main(String[] args) throws IOException {
        interface MyFunction {
            String apply(String a);
        }
        // implementation of the functional interface using lambda
        MyFunction addPrefix = (a) -> "swe262_" + a;

        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "  <nick>Crista </nick>\n"+
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";

        JSONObject jobj = XML.toJSONObject(new StringReader(xmlString), addPrefix::apply);

        FileWriter file = null;
        try {
            file = new FileWriter("output.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        file.write(jobj.toString(4));
        file.close();

    }
}