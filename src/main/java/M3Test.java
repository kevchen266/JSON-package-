import org.json.JSONObject;
import org.json.XML;

import java.io.*;

public class M3Test {
    public static void main(String[] args) throws IOException {
        interface MyFunction {
            String apply(String a);
        }
        // implementation of the functional interface using lambda
        MyFunction addPrefix = (a) -> "foo" + a;

        String xmlString = "<?xml version=\"1.0\"?>\n" +
                "<company>\n" +
                "  <employee>\n" +
                "    <name>John Doe</name>\n" +
                "    <department>HR</department>\n" +
                "  </employee>\n" +
                "</company>";

        JSONObject jobj = XML.toJSONObject(new StringReader(xmlString), addPrefix::apply);

        // Write the JSONObject to a file
        try (FileWriter file = new FileWriter("reversedKeysOutput.json")) {
            file.write(jobj.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}