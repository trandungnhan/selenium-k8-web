package test_data;

import com.google.gson.Gson;
import test_data.computer.ComputerData;

import java.util.Arrays;

public class TestGSON {
    
    // Convert from JSON to Java Object
    
    // Convert from Java object to GSON

    public static void main(String[] args) {
//        exploreGsonFeature();
     testDataBuilder();
        

    }

    private static void testDataBuilder() {
//        String fileLocation = "/src/test/java/test_data/computer/CheapComputerDataList.json";
        String fileLocation = "/src/test/java/test_data/computer/CheapComputerData.json";
//        ComputerData[] computerData = DataObjectBuilder.buildDataObjectFrom(fileLocation, ComputerData[].class);
        ComputerData computerData = DataObjectBuilder.buildDataObjectFrom(fileLocation, ComputerData.class);
        System.out.println(computerData);
    }

    private static void exploreGsonFeature() {
        String JSONString = "  {\n" +
                "    \"processorType\": \"Fast\",\n" +
                "    \"ram\": \"8 GB\",\n" +
                "    \"hdd\": \"320 GB\",\n" +
                "    \"software\": \"Image Viever\"\n" +
                "  }";
        Gson gson = new Gson();
        ComputerData computerData = gson.fromJson(JSONString, ComputerData.class);
        System.out.println(computerData);

        System.out.println(gson.toJson(computerData));
    }
}