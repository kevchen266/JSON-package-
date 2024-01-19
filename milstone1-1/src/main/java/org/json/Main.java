package org.json;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Please provide an XML file path as the first argument.");
                return;
            }

            String xmlFilePath = args[0];
            String xml = new String(Files.readAllBytes(Paths.get(xmlFilePath)));
            JSONObject json = XML.toJSONObject(xml);

            // Task 1: Write JSON object to disk
            writeJsonToFile(json, "Task1_output.json");

            // Task 2: Extract sub-object and write to disk
            if (args.length > 1) {
                String jsonPointerPath = args[1];
                try {
                    JSONPointer jsonPointer = new JSONPointer(jsonPointerPath);
                    Object result = jsonPointer.queryFrom(json);

                    if (result != null) {
                        if (result instanceof JSONObject) {
                            JSONObject subObject = (JSONObject) result;
                            writeJsonToFile(subObject, "Task2_sub-object_output.json");
                        } else {
                            // Handle cases where the result is not a JSONObject
                            System.out.println("The sub-object found is not a JSONObject.");
                        }
                    } else {
                        System.out.println("No sub-object found for the provided JSONPointer.");
                    }
                } catch (Exception e) {
                    System.out.println("Error processing JSONPointer: " + e.getMessage());
                }
            }


            // Task 3: Check for a key path
            if (args.length > 2) {
                String keyPath = args[2];
                try {
                    if (json.query(keyPath) != null) {
                        writeJsonToFile(json, "Task3_keypath_output.json");
                    }
                } catch (Exception e) {
                    System.out.println("The provided key path does not exist in the JSON object: " + keyPath);
                }
            }


            // Task 4: Add prefix "swe262_" to all keys
            JSONObject prefixedJson = addPrefixToKeys(json, "swe262_");
            writeJsonToFile(prefixedJson, "Task4_prefixed_output.json");

            // Task 5: Replace a sub-object
            if (args.length > 3) {
                String replacePath = args[2];
                String jsonString = args[3];
                System.out.println("JSON String: " + jsonString); // Debug print

                try {
                    JSONObject replacement = new JSONObject(jsonString); // Assuming args[4] is a JSON string
                    replaceSubObject(json, replacePath, replacement);
                    writeJsonToFile(json, "Task5_replaced_output.json");
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing errors
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeJsonToFile(JSONObject json, String fileName) throws IOException {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json.toString(4));
        }
    }

    private static JSONObject addPrefixToKeys(JSONObject json, String prefix) {
        JSONObject result = new JSONObject();
        json.keys().forEachRemaining(key -> {
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                value = addPrefixToKeys((JSONObject) value, prefix);
            } else if (value instanceof JSONArray) {
                value = addPrefixToArray((JSONArray) value, prefix);
            }
            result.put(prefix + key, value);
        });
        return result;
    }

    private static JSONArray addPrefixToArray(JSONArray array, String prefix) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            Object item = array.get(i);
            if (item instanceof JSONObject) {
                result.put(addPrefixToKeys((JSONObject) item, prefix));
            } else if (item instanceof JSONArray) {
                result.put(addPrefixToArray((JSONArray) item, prefix));
            } else {
                result.put(item);
            }
        }
        return result;
    }

    private static void replaceSubObject(JSONObject json, String jsonPointer, JSONObject replacement) {
        try {
            String[] keys = jsonPointer.substring(1).split("/");
            JSONObject subObject = json;

            // Navigate to the parent of the target element
            for (int i = 0; i < keys.length - 1; i++) {
                Object currentElement = subObject.get(keys[i]);
                if (currentElement instanceof JSONArray) {
                    JSONArray array = (JSONArray) currentElement;
                    int index = Integer.parseInt(keys[++i]); // Get the next key as an index
                    if (index < array.length()) {
                        subObject = array.getJSONObject(index);
                    } else {
                        throw new JSONException("Index " + index + " out of bounds for array '" + keys[i - 1] + "'");
                    }
                } else if (currentElement instanceof JSONObject) {
                    subObject = (JSONObject) currentElement;
                } else {
                    throw new JSONException("JSONObject['" + keys[i] + "'] not found.");
                }
            }

            // Replace the target element
            String lastKey = keys[keys.length - 1];
            subObject.put(lastKey, replacement);
        } catch (JSONException e) {
            System.out.println("Error during replacement: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid array index: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during processing: " + e.getMessage());
        }
    }

}
