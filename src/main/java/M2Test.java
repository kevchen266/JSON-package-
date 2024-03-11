import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.XML;
import java.io.StringReader;

public class M2Test {

    public static void main(String[] args) {
        // XML string
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<contact>\n" +
                "  <nick>Crista</nick>\n" +
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";

        // Attempt to parse XML based on JSONPointer path and print the result
        try {

            JSONObject jobj = XML.toJSONObject(new StringReader(xmlString));
            JSONObject result = (JSONObject) new JSONPointer("/contact/address").queryFrom(jobj);
            System.out.println("Result parsed by path:\n" + result.toString(2));
        } catch (Exception e) {
            System.out.println("Error occurred while parsing XML: " + e.getMessage());
        }

        System.out.println("-----------------------");

        // Attempt to replace the object at the specified path and print the result
        try {
            JSONObject replacement = new JSONObject("{\"street\":\"Ave of the Arts\"}");
            JSONObject jobj = XML.toJSONObject(new StringReader(xmlString));
            // Simplified replacement logic here, actual application may require more complex logic for precise location and replacement
            jobj.getJSONObject("contact").getJSONObject("address").put("street", replacement);
            System.out.println("Result after replacement:\n" + jobj.toString(2));
        } catch (Exception e) {
            System.out.println("Error occurred while replacing the object at the specified path: " + e.getMessage());
        }
    }
}
