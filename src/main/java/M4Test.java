import org.json.JSONObject;
import org.json.XML;

import java.util.List;
import java.util.stream.Collectors;

public class M4Test {
    public static void main(String[] args) {
        JSONObject obj = XML.toJSONObject("<Books><book><title>AAA</title><author>ASmith</author></book><book><title>BBB</title><author>BSmith</author></book></Books>");
        // Do some transformation on each node
        System.out.println(obj.toString());
        System.out.println("-------------------------------");
        obj.toStream().forEach(node -> {
            // do some transformation, possibly based on the path of the node
            System.out.println(node);
        });
        System.out.println("-------------------------------");
        // Extract values for key "title"
        List<String> titles = obj.toStream()
                .filter(node -> node.has("title"))
                .map(node -> node.getString("title"))
                .collect(Collectors.toList());

        for(String str: titles){
            System.out.println(str);
        }

        System.out.println("--------------------------------");

        // Filter nodes based on certain properties and do some transformation
        obj.toStream().filter(node -> node.optString("author").equals("ASmith"))
                .forEach(node -> {
                    // do some transformation
                    System.out.println(node);
                });

    }
}