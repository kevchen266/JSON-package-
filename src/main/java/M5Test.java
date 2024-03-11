import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.util.function.Consumer;

public class M5Test {
    public static void main(String[] args) throws IOException {

        //function for tranformation
        interface MyFunction {
            String apply(String a);
        }
        // implementation of the functional interface using lambda
        MyFunction addPrefix = (a) -> "swe262_!" + a;

        //string to read
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "  <nick>Crista </nick>\n"+
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";

        //function for writing
        interface MyWriteFunction {
            void apply(JSONObject a);
        }

        FileWriter file = null;

        try {
            file = new FileWriter("output.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileWriter finalFile = file;
        MyWriteFunction writeToFile = (a) -> {
            try {
                finalFile.write(a.toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        //function for exceptions
        Consumer<Exception> exceptionHandler = new Consumer<Exception>() {
            public void accept(Exception e) {
                e.printStackTrace();
            }
        };

        XML.toJSONObject(new StringReader(xmlString), addPrefix::apply, writeToFile::apply, exceptionHandler);

        for(int i = 0; i < 5; i++) {
            System.out.println("writing to output.json... " + ((i+1) * 20) + "%" );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        file.close();
    }
}